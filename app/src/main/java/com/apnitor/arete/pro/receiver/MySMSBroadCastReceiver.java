package com.apnitor.arete.pro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MySMSBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)

    {

        Log.e("onReceive", "SMS");
        // Get Bundle object contained in the SMS intent passed in

        Log.e("onReceive", "Intent=" + intent);
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str = "";

        if (bundle != null) {

            // Get the SMS message

            Log.e("onReceive", "bundle=" + bundle.toString());


            Bundle data = intent.getExtras();
            try {
                Object[] pdus = new Object[0];
                if (data != null) {

                    pdus = (Object[]) data.get("pdus"); // the pdus key will contain the newly received SMS
                    Log.e("onReceive", "pdus=" + pdus.length);

                }

            } catch (Exception e) {
                e.printStackTrace();

            }


           /* Object[] pdus = (Object[]) bundle.get("pdus");


            smsm = new SmsMessage[pdus.length];// Here crash with Oreo
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

                sms_str += "\r\nMessage: ";
                sms_str += smsm[i].getMessageBody().toString();
                sms_str+= "\r\n";

                String Sender = smsm[i].getOriginatingAddress();
                //Check here sender is yours
                Intent smsIntent = new Intent("otp");
                smsIntent.putExtra("message",sms_str);

                Log.e("onReceive","CODE="+sms_str);//CODE=
               // Message: Dear Fhfhfhfdh, Your one time password is 6931. Regards CleanAppHouse

                LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

            }*/
        }
    }
}



