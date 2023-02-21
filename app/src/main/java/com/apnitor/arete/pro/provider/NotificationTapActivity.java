package com.apnitor.arete.pro.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.profile.BaseActivity;

public class NotificationTapActivity extends BaseActivity {
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        String userTypes = getUserTypes();

        if (userTypes.equals("serviceProvider") || userTypes.equals("Service Provider")) {
            startActivity(HomeProviderActivity.class);
        } else {
            startActivity(HomeActivity.class);
        }

    }

    private String getUserTypes() {
        return mPreferences.getString("userTypes", "");
    }
}
