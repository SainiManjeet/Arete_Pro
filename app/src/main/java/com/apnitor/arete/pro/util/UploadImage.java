package com.apnitor.arete.pro.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.application.SharedPreferenceHelper;
import com.apnitor.arete.pro.viewmodel.ProfileViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UploadImage {

    public String mCurrentPhotoPath;
    private String imgUrl;
    private Activity mContext;
    private ProgressBar mProgressBar;
    private ProfileViewModel viewModel;

    public UploadImage(Activity context) {
        mContext = context;
        viewModel = new ProfileViewModel(context.getApplication());
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String saveImage(Uri muri) {
        File file = null;
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(muri);
            OutputStream out;

            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            File createDir = new File(root + "CleanAppHouse" + File.separator);
            if (!createDir.exists()) {
                createDir.mkdir();
            }
            file = new File(root + "CleanAppHouse" + File.separator + "CleanApp" + System.currentTimeMillis() + ".JPG");
            file.createNewFile();
            out = new FileOutputStream(file);
            byte[] mBytes = getBytes(inputStream);
            out.write(mBytes);
            out.close();
            Log.e("file Path", "" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void imageActivityResult(String path, ProgressBar progressBar) {
        mProgressBar = progressBar;
        CompressImage mImage = new CompressImage(mContext);
        String url = mImage.compressImage(path);
        new UpdateImage().execute(Uri.parse(url));
    }

    private String uploadFIle(Uri fileUri) {

        BasicAWSCredentials credentials = new BasicAWSCredentials(mContext.getResources().getString(R.string.strAccess), mContext.getResources().getString(R.string.strSecret));
        ClientConfiguration config = new ClientConfiguration();
        config.setConnectionTimeout(60 * 1000); // 60 sec
        config.setSocketTimeout(60 * 1000); // 60 sec
        final AmazonS3Client s3Client = new AmazonS3Client(credentials,config);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        // Solution is update the Signer Version.
        clientConfiguration.setSignerOverride("S3SignerType");

        if (!s3Client.doesBucketExist("profile/uploadPhotos")) {
            s3Client.createBucket("profile/uploadPhotos");
        }
        //  Log.e("Bucketys",""+s3Client.listBuckets());
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();
        String path = fileUri.getPath(); // "/mnt/sdcard/FileName.mp3"
        File file = new File(path);
        String[] img = path.split("MyFolder/Images/");

        final String uploadUrl = "profile/uploadPhotos/" + img[1];
// "jsaS3" will be the folder that contains the file
        final TransferObserver uploadObserver =
                transferUtility.upload(uploadUrl, file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed download.
                    Log.e("Bucketys", "uploaded successfully");

                    mProgressBar.setVisibility(View.GONE);
                    String imgUrl = s3Client.getUrl("", "cleanapphouse/" + uploadUrl).toString();

                    SharedPreferenceHelper mHelper = new SharedPreferenceHelper(mContext);
                    mHelper.updateProfilePhoto(imgUrl);
                    viewModel.updateProfile(new UpdateProfileReq(mHelper.getAuthToken(), imgUrl));
                    Log.e("Upload Image url is >>>", "" + imgUrl);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
                Log.e("upload Handeled", "ok" + percentDone);
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
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
            Log.e("upload Handeled", "ok");
        }
        return imgUrl;
    }

    private class UpdateImage extends AsyncTask<Uri, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Uri... params) {
            String url = uploadFIle(params[0]);
            return url;
        }

        @Override
        protected void onPostExecute(String imgUrl) {
        }
    }
// If your upload does not trigger the onStateChanged method inside your
// TransferListener, you can directly check the transfer state as shown here.


    public void createJobImages(String path, ProgressBar progressBar) {
        mProgressBar = progressBar;
        CompressImage mImage = new CompressImage(mContext);
        String url = mImage.compressImage(path);
        new UpdateImage().execute(Uri.parse(url));
    }

}
