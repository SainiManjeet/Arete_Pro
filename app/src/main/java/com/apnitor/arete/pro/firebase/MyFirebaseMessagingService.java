package com.apnitor.arete.pro.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.provider.FindJobsProviderFragment;
import com.apnitor.arete.pro.provider.HomeProviderActivity;
import com.apnitor.arete.pro.provider.MyBidsProviderActivity;
import com.apnitor.arete.pro.provider.MyJobsProviderFragment;
import com.apnitor.arete.pro.provider.PastJobsProviderActivity;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.RequestCodes;

import androidx.core.app.NotificationCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String JOB_ID = "JOB_ID";
    private static final String JOB_TYPE = "JOB_TYPE";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

            try {
                String jobType = remoteMessage.getData().get("type");
                String message = remoteMessage.getData().get("message");
                if (message != null) {
                    Log.d(TAG, "Message " + message);
                    Log.e(TAG, "jobType " + jobType);
                    if (jobType == null) {
                        jobType = "";
                    }

                    // NEW JOB CREATED
                    if (jobType.equalsIgnoreCase(RequestCodes.JOB_CREATED)) {
                        sendNewJobAlertBroadCast(message, jobType);
                        sendNotificationJobAlert(message, HomeProviderActivity.class);
                        return;
                    }
                    // NEW BID PLACED
                    else if (jobType.equalsIgnoreCase(RequestCodes.BID_PLACED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_new_bid), HomeActivity.class);
                        return;
                    }
                    // JOB STARTED
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_STARTED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_start_job), HomeActivity.class);
                        return;
                    }
                    // JOB COMPLETED
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_COMPLETED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_completed_job), HomeActivity.class);
                        return;
                    }
                    //
                    // JOB OFFER
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_OFFER)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendJobOfferedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_offer), HomeProviderActivity.class);
                        return;
                    }
                    // JOB ACCEPTED
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_ACCEPTED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_accepted), HomeActivity.class);
                        return;
                    }  // JOB ASSIGNED
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_ASSIGNED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendJobAssignedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_assigned), HomeProviderActivity.class);
                        return;
                    }   // JOB PAID
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_PAID)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_paid), PastJobsProviderActivity.class);
                        return;
                    }   // JOB CANCELLED
                   /* else if (jobType.equalsIgnoreCase(RequestCodes.JOB_CANCELLED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_cancelled), HomeProviderActivity.class);
                        return;
                    }*/
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_CANCELLED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendJobAssignedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_cancelled), HomeProviderActivity.class);
                        return;
                    }

                    // JOB UPDATED
                    else if (jobType.equalsIgnoreCase(RequestCodes.JOB_UPDATED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        //sendNotification(message, jobType, getString(R.string.notify_job_updated), HomeProviderActivity.class);
                        sendNotification(message, jobType, getString(R.string.notify_job_updated), MyBidsProviderActivity.class);
                        return;

                    }
                    // JOB REJECTEDsds
                   /* else if (jobType.equalsIgnoreCase(RequestCodes.JOB_REJECTED)) {
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_rejected));
                        return;
                    }*/
                   // INTEREST SENT
                    // TODO type is need to confirm from backend
                    else if (jobType.equalsIgnoreCase(RequestCodes.BID_INTERESTED)) {

                        Log.e(TAG, "insideIntreset " + jobType);
                        String jobId = remoteMessage.getData().get("jobId");
                        sendBidPlacedBroadCast(jobId, jobType);
                        sendNotification(message, jobType, getString(R.string.notify_job_interested), HomeActivity.class);
                        return;
                    }


                    if (PreferenceHandler.readString(this, "userTypes", "").equalsIgnoreCase("serviceProvider")) {
                        sendNotification(message, jobType, getString(R.string.app_name), HomeProviderActivity.class);
                    } else {
                        sendNotification(message, jobType, getString(R.string.app_name), HomeActivity.class);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }


    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

       /* SharedPreferenceHelper mPreferenceHelper=new SharedPreferenceHelper(getApplicationContext());
        mPreferenceHelper.saveFirebaseToken(token);*/
    }


    <T> void sendNotification(String messageBody, String type, String title, Class<T> tClass) {

        PendingIntent pendingIntent;
        Intent intent = new Intent(this, tClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_notifaction)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri).setColor(getResources().getColor(R.color.colorAccent))
                        .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (type.equalsIgnoreCase("bidPlaced")) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
            } else {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel;
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (type.equalsIgnoreCase("bidPlaced")) {
                channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_LOW);
            } else {
                channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
            }
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNewJobAlertBroadCast(String messageBody, String type) {
        try {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(FindJobsProviderFragment.BROADCAST_ACTION);
            broadCastIntent.putExtra("messageBody", messageBody);
            broadCastIntent.putExtra("type", type);
            sendBroadcast(broadCastIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendJobAssignedBroadCast(String messageBody, String type) {
        try {
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(MyJobsProviderFragment.BROADCAST_ACTION);
            broadCastIntent.putExtra("messageBody", messageBody);
            broadCastIntent.putExtra("type", type);
            sendBroadcast(broadCastIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    protected void sendBidPlacedBroadCast(String jobId, String type) {
        try {
            Intent intent2 = new Intent();
            intent2.setAction(RequestCodes.NOTIFICATION_NEW_BID);
            intent2.putExtra("jobId", jobId);
            intent2.putExtra("type", type);
            sendBroadcast(intent2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void sendJobOfferedBroadCast(String jobId, String type) {
        try {
            Intent intent2 = new Intent();
            intent2.setAction(MyJobsProviderFragment.BROADCAST_ACTION);
            intent2.putExtra("jobId", jobId);
            intent2.putExtra("type", type);
            sendBroadcast(intent2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    <T> void sendNotificationJobAlert(String messageBody, Class<T> tClass) {

        PendingIntent pendingIntent;
        Intent intent = new Intent(this, tClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_notifaction)
                        .setContentTitle(getString(R.string.notify_new_bid))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri).setColor(getResources().getColor(R.color.colorAccent))
                        .setContentIntent(pendingIntent);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}