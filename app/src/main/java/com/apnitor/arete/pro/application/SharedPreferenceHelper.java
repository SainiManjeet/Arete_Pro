package com.apnitor.arete.pro.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.api.response.LogInRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SharedPreferenceHelper {
    String LOG_TAG = "SharedPreferenceHelper";
    public static final String HOURLY_RATE = "hourlyRate";
    public static final String RADIUS = "radius";
    public static final String ZIPCODE = "zipcode";
    private static final String DELIMITER = ";";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String AUTH_TOKEN = "authToken";
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "username";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String PROFILE_PHOTO_URL = "profilePhotoUrl";
    private static final String PHONE = "phone";
    private static final String COUNTRY_NAME = "countryName";
    private static final String ADDRESS = "address";
    private static final String PROVIDER_ADDRESS = "provideraddress";
    private static final String BIRTHDATE = "birthDate";
    private static final String GENDER = "gender";
    private static final String ABOUT_ME = "AboutME";
    private static final String STATE = "state";
    private static final String CITY = "city";
    private static final String USER_TYPES = "userTypes";
    private static final String SPECIALITY = "speciality";
    private static final String FIREBASE_TOKEN = "firebase_token";
    private static final String API_ERR_MSG = ";";
    private static final String FAV_PROVIDER_ID = "FavProviderId";
    private static final String FAV_PROVIDER_NAME = "FavProviderName";
    private SharedPreferences sharedPreferences;

    private static final String AVAILABILITY = "availability";


    /**
     * Please get the instance of this class using getApplication().getSharedPreferenceHelper().
     */
    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
    }

    public SharedPreferenceHelper(MaidPickerApplication application) {
        sharedPreferences = application.getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
    }

    public void logIn(LogInRes logInRes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(AUTH_TOKEN, logInRes.authToken);
        editor.putString(USER_ID, logInRes.userId);
        Log.e("USER_ID", "" + logInRes.authToken);
        editor.putString(USER_NAME, logInRes.userId);
        editor.putString(FIRST_NAME, logInRes.firstName);
        editor.putString(LAST_NAME, logInRes.lastName);
        editor.putString(EMAIL, logInRes.email);
        Log.e("USER_ID", "" + logInRes.email);
        editor.putString(PROFILE_PHOTO_URL, logInRes.profilePhotoUrl);
        editor.putString(PHONE, logInRes.phone);

        editor.putString(COUNTRY_NAME, logInRes.countryName);
        editor.putString(STATE, logInRes.state);
        editor.putString(CITY, logInRes.city);

        if (logInRes.hourlyRate != null) {
            editor.putFloat(HOURLY_RATE, logInRes.hourlyRate);
        }
        editor.putString(ABOUT_ME, logInRes.aboutMe);
        editor.putString(BIRTHDATE, logInRes.birthdate);
        editor.putString(GENDER, logInRes.gender);
        editor.putString(USER_TYPES, listToString(logInRes.userTypes));
        editor.putString(SPECIALITY, listToString(logInRes.speciality)); //
        if (logInRes.address != null && logInRes.address.size()>0)
            editor.putString(ADDRESS, new Gson().toJson(logInRes.address.get(0)));
        editor.putString(PROVIDER_ADDRESS, new Gson().toJson(logInRes.providerAddress));
        editor.putString(ZIPCODE, logInRes.zipcode);
        editor.putInt(RADIUS, logInRes.radius);

        if (logInRes.availability!=null){
            editor.putString(AVAILABILITY, new Gson().toJson(logInRes.availability));
        }
        //
        editor.apply();
        editor.commit();
    }

    public void updateProfilePhoto(String url) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_PHOTO_URL, url);
        editor.apply();
    }

    public String getProfilePhoto() {
        return sharedPreferences.getString(PROFILE_PHOTO_URL, "");
    }

    public void profileUpdated(UpdateProfileReq updateProfileReq) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(PROFILE_PHOTO_URL, updateProfileReq.imageUrl);
        editor.putString(PHONE, updateProfileReq.phone);
        editor.putString(ABOUT_ME, updateProfileReq.aboutMe);
        editor.putFloat(HOURLY_RATE, updateProfileReq.hourlyRate);
        editor.putString(BIRTHDATE, updateProfileReq.birthDate);
        editor.putString(GENDER, updateProfileReq.gender);
        editor.putString(EMAIL, updateProfileReq.email);
        editor.putInt(RADIUS, updateProfileReq.radius);
        editor.putString(ZIPCODE, updateProfileReq.zipcode);
        editor.putString(SPECIALITY, String.valueOf(updateProfileReq.speciality));
        editor.putString(ADDRESS,  new Gson().toJson(updateProfileReq.address.get(0)));
        editor.putString(PROVIDER_ADDRESS, new Gson().toJson(updateProfileReq.providerAddress));

        editor.putString(AVAILABILITY, new Gson().toJson(updateProfileReq.availability)); //
        editor.apply();
    }



    public void setUserID(String userID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userID);
        editor.apply();
    }

    public String getUserType() {
        return sharedPreferences.getString(USER_TYPES, "");
    }

    public void setUserType(String userType) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TYPES, userType);
        editor.apply();
    }

    public String getSpeciality() {
        return sharedPreferences.getString(SPECIALITY, "");
    }

    // Speciality
    public void setSpeciality(String speciality) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPECIALITY, speciality);
        editor.apply();
    }

    public float getHourlyPrice() {
        return sharedPreferences.getFloat(HOURLY_RATE, 0);
    }

    public void saveFirebaseToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_TOKEN, token);
        editor.apply();
    }

    public String getFirebaseToken() {
        return sharedPreferences.getString(FIREBASE_TOKEN, "");
    }


    /**
     * USE clear()
     * <p>
     * instead of setting empty values
     */


    public void logOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
       /* editor.putBoolean(IS_LOGGED_IN, false);
        editor.putString(AUTH_TOKEN, "");
        editor.putString(USER_ID, "");
        editor.putString(USER_NAME, "");
        editor.putString(FIRST_NAME, "");
        editor.putString(LAST_NAME, "");
        editor.putString(EMAIL, "");
        editor.putString(PROFILE_PHOTO_URL, "");
        editor.putString(PHONE, "");
        editor.putString(COUNTRY_NAME, "");
        editor.putString(STATE, "");
        editor.putString(CITY, "");
        editor.putString(USER_TYPES, "");
        editor.putString(ABOUT_ME, "");
        editor.putString(GENDER, "");
        editor.putString(BIRTHDATE, "");
        editor.apply();*/

        editor.clear();
        editor.commit();

    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }
    //

    public String getFisrstName() {
        return sharedPreferences.getString(FIRST_NAME, "");
    }

    public String getPhone() {
        return sharedPreferences.getString(PHONE, "");
    }

    public String getImage() {
        return sharedPreferences.getString(PROFILE_PHOTO_URL, "");
    }

    public String getLastName() {
        return sharedPreferences.getString(LAST_NAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(USER_ID, "");
    }


    /**
     * TODO
     * <p>
     * Add availability here
     *
     * @return
     */


    public LogInRes getLogInRes() {
        Log.d(LOG_TAG, "PROVIDER_ADDRESS " + sharedPreferences.getString(PROVIDER_ADDRESS, ""));
        Log.d(LOG_TAG, "Address  " + sharedPreferences.getString(ADDRESS, ""));
        Log.d(LOG_TAG, "Availability  " + sharedPreferences.getString(AVAILABILITY, ""));
        return new LogInRes(
                sharedPreferences.getString(AUTH_TOKEN, ""),
                sharedPreferences.getString(USER_ID, ""),
                sharedPreferences.getString(USER_NAME, ""),
                sharedPreferences.getString(FIRST_NAME, ""),
                sharedPreferences.getString(LAST_NAME, ""),
                sharedPreferences.getString(EMAIL, ""),
                sharedPreferences.getString(PROFILE_PHOTO_URL, ""),
                sharedPreferences.getString(PHONE, ""),
                sharedPreferences.getFloat(HOURLY_RATE, 0),
                sharedPreferences.getString(ABOUT_ME, ""),
                sharedPreferences.getString(BIRTHDATE, ""),
                sharedPreferences.getString(GENDER, ""),
                sharedPreferences.getString(COUNTRY_NAME, ""),
                sharedPreferences.getString(STATE, ""),
                sharedPreferences.getString(CITY, ""),
                stringToList(Objects.requireNonNull(sharedPreferences.getString(USER_TYPES, ""))),
                sharedPreferences.getString(ZIPCODE, ""),
                sharedPreferences.getInt(RADIUS, 0),
                stringToList(Objects.requireNonNull(sharedPreferences.getString(SPECIALITY, ""))),
                sharedPreferences.getString(PROVIDER_ADDRESS, ""),
                sharedPreferences.getString(ADDRESS, ""),
                sharedPreferences.getString(AVAILABILITY, "")
        );
    }

    // Favo

    public String
    getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, "");
    }

    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return TextUtils.join(DELIMITER, list);
    }

    private List<String> stringToList(String str) {
        if (str.isEmpty())
            return new ArrayList<>();

        List<String> stringList = Arrays.asList(str.split(DELIMITER));
        if (stringList == null)
            return new ArrayList<>();
        else
            return stringList;
    }
}
