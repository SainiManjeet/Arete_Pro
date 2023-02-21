package com.apnitor.arete.pro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.apnitor.arete.pro.provider.HomeProviderActivity;
import com.google.gson.Gson;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.CreateJobNewReq;
import com.apnitor.arete.pro.api.request.CreateJobReq;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.api.request.UpdateJobReq;

import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleObserver;

public class UploadImageService extends Service implements LifecycleObserver {

    TransferUtility transferUtility;
    List<TransferObserver> observers;
    JobSpecViewModel mJobSpecViewModel;
    String jobId, authToken;
    int count = 0;
    ArrayList<ImageUrlReq> mImageList;
    CreateJobNewReq mJobReq;
    AmazonS3Client s3Client;
    String LOG_TAG = "UploadImageService";
    private String NOTIFICATION_CHANNEL_ID = "CreateJob";
    private int NOTIFICATION_ID = 111;

    public UploadImageService() {
    }

    public List<TransferObserver> getObservers() {
        return observers;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mJobSpecViewModel = new JobSpecViewModel(getApplication());
        jobId = intent.getStringExtra("jobId");
        authToken = intent.getStringExtra("authToken");
        mJobReq = intent.getParcelableExtra("createJob");
        if (authToken==null){
            authToken=mJobReq.authToken;
        }
        Log.d(LOG_TAG,"jobId is "+jobId);
        Log.d(LOG_TAG,"authToken is "+authToken);
        Log.d(LOG_TAG,"mJobReq is "+mJobReq.toString());
        startInForeground();
        new UpdateImage().execute(mJobReq.images);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startInForeground() {
        Intent notificationIntent = new Intent(this, HomeProviderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(UploadImageService.this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Arete Pro")
                .setContentText("Your job image is uploading...")
                .setTicker("")
                .setContentIntent(pendingIntent);
        Notification notification = builder.build();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Arete Pro", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Your job image is uploading...");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, notification);
    }

    private void imagesObservers(ArrayList<ImageUrlReq> imageUrlReqs) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(getResources().getString(R.string.strAccess), getResources().getString(R.string.strSecret));
        ClientConfiguration config = new ClientConfiguration();
        config.setConnectionTimeout(60 * 1000); // 60 sec
        config.setSocketTimeout(60 * 1000); // 60 sec
        s3Client = new AmazonS3Client(credentials,config);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        // Solution is update the Signer Version.
        clientConfiguration.setSignerOverride("S3SignerType");
        if (!s3Client.doesBucketExist("jobs/jobPictures")) {
            s3Client.createBucket("jobs/jobPictures");
        }
        transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();
        observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);
        for (ImageUrlReq urlReq : imageUrlReqs) {
            Log.d(LOG_TAG, "Image PATH  is " + urlReq.imageUrl);
            String path = urlReq.imageUrl; // "/mnt/sdcard/FileName.mp3"
            if (!path.contains("http")) {
                File file = new File(path);
                String[] img = path.split("/");
                final String uploadUrl = "jobs/jobPictures/" + img[img.length - 1];
                // "jsaS3" will be the folder that contains the file
                final TransferObserver uploadObserver =
                        transferUtility.upload(uploadUrl, file);
                observers.add(uploadObserver);
            }
        }
        uploadFIle(observers);
    }

    private String uploadFIle(List<TransferObserver> observers) {
        count = 0;
        mImageList = new ArrayList<>();
        for (TransferObserver observer : observers) {

            observer.setTransferListener(new TransferListener() {

                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (TransferState.COMPLETED == state) {
                        // Handle a completed download.
                        String imgPath = observer.getAbsoluteFilePath();
                        String[] paths = imgPath.split("/");
                        String uploadUrl = paths[paths.length - 1];
                        count++;
                        String imgUrl = s3Client.getUrl("", "cleanapphouse/jobs/jobPictures/" + uploadUrl).toString();
                        ImageUrlReq req = new ImageUrlReq();
                        req.imageUrl = imgUrl;
                        mImageList.add(req);
                        Log.e("check image Size=="+mImageList.size(),mImageList.toString()+"+="+mJobReq.images.size());
                        if (mImageList.size() >= mJobReq.images.size()) {
                            mJobReq.images = mImageList;
                            // saveLastJob(mJobReq);
                            mJobSpecViewModel.updateJob(new UpdateJobReq(authToken, mImageList, jobId));
                            stopForeground(true);
                            stopSelf();
                        }
                        Log.d("Upload Image url is >>>", observer.getAbsoluteFilePath() + "," + observer.getBucket());
                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
//                    int percentDone = (int) percentDonef;
                    //Log.e("upload Handeled", "ok" + percentDone);
                }

                @Override
                public void onError(int id, Exception ex) {
                    // Handle errors
                    ex.getStackTrace();
                    Log.e("error Handeled", "ok" + ex.getMessage());
                }
            });

            // If your upload does not trigger the onStateChanged method inside your
            // TransferListener, you can directly check the transfer state as shown here.
            if (TransferState.COMPLETED == observer.getState()) {
                // Handle a completed upload.
                Log.e("<<<upload Handeled>>>", "ok");
            }
        }
        return "";
    }

    void saveLastJob(CreateJobReq mJobReq) {
        String createJobReqJson = new Gson().toJson(mJobReq);
        PreferenceHandler.writeString(this, PreferenceHandler.PREF_KEY_CREATE_JOB, createJobReqJson);
    }

    private class UpdateImage extends AsyncTask<ArrayList<ImageUrlReq>, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(ArrayList<ImageUrlReq>... params) {
            imagesObservers(params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String imgUrl) {

        }
    }
}
