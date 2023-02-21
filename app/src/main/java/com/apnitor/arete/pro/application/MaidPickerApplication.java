package com.apnitor.arete.pro.application;


import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
/*import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;*/

public class MaidPickerApplication extends Application {

    // Singletons
    private SharedPreferenceHelper sharedPreferenceHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        initSingletonsForApp();
    }

    private void initSingletonsForApp() {
        sharedPreferenceHelper = new SharedPreferenceHelper(this);
    }

    public SharedPreferenceHelper getSharedPreferenceHelper() {
        return sharedPreferenceHelper;
    }
}
