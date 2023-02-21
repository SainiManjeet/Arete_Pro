package com.apnitor.arete.pro.util;

import android.content.Context;

import com.apnitor.arete.pro.R;

public class Validation {

    public static boolean isValidatedCleaningType(Context context, String field) {
        if (field.isEmpty()) {
            UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_cleaning), "Ok");
            return false;
        }
        return true;
    }

    public static boolean isValidatedAddress(Context context, String cleaning, String address) {
        if (isValidatedCleaningType(context, cleaning)) {
            if (address.isEmpty()) {
                UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_address), "Ok");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidatedRoomDetails(Context context, String cleaning, String address, String roomDetails) {
        if (isValidatedAddress(context, cleaning, address) == true) {
            if (roomDetails.isEmpty()) {
                UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_room_details), "Ok");
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidatedWhen(Context context, String cleaning, String address, String roomDetails, String when) {
        if (isValidatedRoomDetails(context, cleaning, address, roomDetails)) {
            if (when.isEmpty()) {
                UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_when), "Ok");
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidatedHowOften(Context context, String cleaning, String address, String roomDetails,String when, String howOften) {
        if (isValidatedWhen(context, cleaning, address, roomDetails,when)) {
            if (howOften.isEmpty()) {
                UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_how_often), "Ok");
                return false;
            }else {
                return true;
            }
        }
        return false;
    }
    public static boolean isValidatedHowOften(Context context, String field) {
        if (field.isEmpty()) {
            UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_how_often), "Ok");
            return false;
        }
        return true;
    }

    public static boolean isValidatedCleaningPrice(Context context, String field) {
        if (field.isEmpty()) {
            UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_cleaning_price), "Ok");
            return false;
        }
        return true;


    }
    public static boolean isValidatedString(Context context, String field,String message) {
        if (field.isEmpty()) {
            UIUtility.showDialogWithSingleButton(context, message, "Ok");
            return false;
        }
        return true;
    }
    public static boolean isValidatedInteger(Context context, Integer field,String message) {
        if (field==0) {
            UIUtility.showDialogWithSingleButton(context, message, "Ok");
            return false;
        }
        return true;
    }
    public static boolean isValidatedDouble(Context context, Double field,String message) {
        if (field==0.0) {
            UIUtility.showDialogWithSingleButton(context, message, "Ok");
            return false;
        }
        return true;
    }

    //
    public static boolean isValidatedCompleteJob(Context context, String prewalkNote) {
        if (prewalkNote.isEmpty()){
            UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_pre_walk_note), "Ok");
            return false;
        }
        return true;
    }

    public static boolean isValidatedPostWalkJob(Context context, String prewalkNote) {
        if (prewalkNote.isEmpty()){
            UIUtility.showDialogWithSingleButton(context, context.getString(R.string.validation_post_walk_note), "Ok");
            return false;
        }
        return true;
    }
}
