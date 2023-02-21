package com.apnitor.arete.pro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.apnitor.arete.pro.dashboard.ClientRequestsFragment;
import com.apnitor.arete.pro.util.RequestCodes;

public class CustomBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "CustomBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, " Action " + action);
        if (RequestCodes.NOTIFICATION_NEW_BID.equals(action)) {
            Log.e(TAG, " Inside Action " + action);
            ClientRequestsFragment.update();
        }
    }
}
