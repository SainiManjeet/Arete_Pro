package com.apnitor.arete.pro.profile;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.LogInReq;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.provider.HomeProviderActivity;

import com.apnitor.arete.pro.util.HouseConstants;
import com.apnitor.arete.pro.viewmodel.LoginViewModel;

import java.security.MessageDigest;
import java.util.List;


public class LoginActivity extends BaseActivity {

    private LoginViewModel loginViewModel;

    public static void printHashKey(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d("AppLog", "key:" + hashKey + "=");
            }
        } catch (Exception e) {
            Log.d("AppLog", "error:", e);
        }
    }

    public void login(LogInReq logInReq) {
        loginViewModel.login(logInReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        printHashKey(LoginActivity.this);
        getPrefHelper().setUserType("serviceProvider");

        if (getPrefHelper().isLoggedIn()) {
            if (getPrefHelper().getUserType().equals("serviceProvider")) {
                if (getPrefHelper().getHourlyPrice() != 0) {
                    finishStartActivity(HomeProviderActivity.class);
                } else {
                    finishStartActivity(ProfileActivity.class);
                }
            } else {
                finishStartActivity(HomeActivity.class);
            }
        }

        loginViewModel = getViewModel(LoginViewModel.class);

        loginViewModel.getLogInResLiveData().observe(this, logInRes -> {
            //  showSnackBar(R.id.my_nav_host_fragment,"You are successfully logged in.");
            Toast.makeText(this, "You are successfully logged in.", Toast.LENGTH_SHORT).show();
            loginSuccessfully(logInRes);
        });
    }

    public void loginSuccessfully(LogInRes logInRes) {

        getPrefHelper().logIn(logInRes);

        List<String> mUserTypes = logInRes.userTypes;

        if (mUserTypes.get(0).equals("serviceProvider") && getPrefHelper().getUserType().equalsIgnoreCase("serviceProvider")) {
            if (getPrefHelper().getHourlyPrice() != 0) {
                Bundle bundle = new Bundle();
                bundle.putInt(HouseConstants.NOTIFICATION_COUNT, logInRes.notificationCount);
                finishStartActivity(HomeProviderActivity.class, bundle);
            } else {
                finishStartActivity(ProfileActivity.class);
            }
        } else {

            Bundle bundle = new Bundle();
            bundle.putInt(HouseConstants.NOTIFICATION_COUNT, logInRes.notificationCount);
            finishStartActivity(HomeActivity.class, bundle);
        }
    }


}
