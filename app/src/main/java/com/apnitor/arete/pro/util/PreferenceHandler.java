package com.apnitor.arete.pro.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Common PrefrenceConnector class for storing preference values.
 */
public class PreferenceHandler {

    public static final String PREF_NAME = "MAIDPICKER_PREFERENCES";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String PREF_KEY_CREATE_JOB = "PREF_KEY_CREATE_JOB";
    public static final String PREF_TOTAL_BEDROOM = "totalBedroom";
    public static final String PREF_TOTAL_BATHROOM = "totalBathroom";
    public static final String PREF_TOTAL_KITCHEN = "totalKitchen";
    public static final String PREF_TOTAL_OTHER = "totalOther";
    public static final String PREF_TOTAL_SQFT = "totalSqft";
    public static final String PREF_EXTRA_CLEAN = "PREF_EXTRA_CLEAN";

    // Cleaning type
    public static final String PREF_FIRST_FLOOR = "firstFloor";
    public static final String PREF_SECOND_FLOOR = "secondFloor";
    public static final String PREF_FRENCH_WINDOWS = "frenchWindows";
    public static final String PREF_PATIO_DOOR = "patioDoor";
    public static final String PREF_GARDEN_WINDOW = "gardenWindows";
    public static final String PREF_WARDROBE_MIRROR = "wardrobeMirror";
    public static final String PREF_SCREENS = "screens";
    public static final String PREF_SKYLIGHT = "skylight";
    public static final String PREF_STORIES = "stories";

    public static final String PREF_IS_VALID_ZIPCODE = "PREF_IS_VALID_ZIPCODE";
    public static final String PREF_KEY_UPDATE_JOB = "PREF_KEY_UPDATE_JOB";

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void cleraData(Context context) {
        getPreferences(context).edit().clear().commit();
    }
}
