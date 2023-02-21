package com.apnitor.arete.pro.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.apnitor.arete.pro.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!getPrefHelper().getUserType().isEmpty() && getPrefHelper().isLoggedIn()) {
            finishStartActivity(LoginActivity.class);
        }
    }

    public void onClientClicked(View view) {
        getPrefHelper().setUserType("client");
        //
        Bundle mBundle = new Bundle();
        mBundle.putString("userType", "client");
        Log.e("TYPE=", "onClientClicked*=" + getPrefHelper().getUserType());
        finishStartActivity(LoginActivity.class, mBundle);
    }

    public void onProviderClicked(View view) {
        getPrefHelper().setUserType("serviceProvider");
        //
        Bundle mBundle = new Bundle();
        mBundle.putString("userType", "serviceProvider");
        finishStartActivity(LoginActivity.class, mBundle);

    }

    @Override
    public void onBackPressed() {
        Log.e("onBackPressed=", "onBackPressed");
        moveTaskToBack(true);
        System.exit(1);
    }


}
