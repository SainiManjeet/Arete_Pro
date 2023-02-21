package com.apnitor.arete.pro.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.profile.ProfileActivity;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import androidx.core.app.NotificationCompat;
import io.reactivex.annotations.NonNull;

public class UIUtility {

    public static String getEditTextValue(EditText editText) {
        if (editText != null && editText.getText() != null) {
            return editText.getText().toString();
        }
        return "";
    }

    public static boolean isNotValidEmail(CharSequence target) {
        return TextUtils.isEmpty(target) || !Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isNotValidPhone(CharSequence target) {
        return TextUtils.isEmpty(target) || !Patterns.PHONE.matcher(target).matches()||target.length()>15||target.length()<10;
    }

    public static boolean isValidPassword(String s) {
       String strPattern = "((?=.*[a-z])(?=.*[A-Z])(?=.*[$&+,:;=\\\\?@#|/'<>.^*()%!-]).{8,25})";
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                strPattern);

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }
    public static boolean checkSpcChar(String str){
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");

        if (regex.matcher(str).find()) {
           return true;
        }else {
            return false;
        }
    }
    public static boolean checkCaps(String str){
        Pattern pattern = Pattern.compile("(?=.*[A-Z])");
        Matcher matcher = pattern.matcher(str);
       if(matcher.find()) {
           return true;
        }else{
           return false;
       }
    }
    public static boolean checkSmall(String str) {
        Pattern pattern = Pattern.compile("(?=.*[a-z])");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            return true;
        }else{
            return false;
        }
    }


    public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static void showDialogWithSingleButton(Context context, String message, String buttonText) {
        try {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                    .content(message)
                    .positiveText(buttonText)
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            if(context instanceof ProfileActivity){
                                ((ProfileActivity)context).MandatoryFieldsValidations();
                            }
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void sendNotificationJobAlert(Context context,String messageBody) {

        PendingIntent pendingIntent;
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = context.getString(R.string.default_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.mipmap.ic_notifaction_new)
                        .setContentTitle(context.getString(R.string.notify_new_job))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri).setColor(context.getResources().getColor(R.color.colorAccent))
                        .setContentIntent(pendingIntent);

        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel;
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public static void hideKeyboard(Context context,EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
