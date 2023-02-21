package com.apnitor.arete.pro.viewmodel;

import android.app.Application;

import com.apnitor.arete.pro.api.request.LogInReq;
import com.apnitor.arete.pro.api.request.LogoutReq;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.api.response.LogoutRes;
import com.apnitor.arete.pro.repository.ProfileRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends BaseViewModel {

    private ProfileRepository profileRepository;
    private MutableLiveData<LogInRes> logInResLiveData = new MutableLiveData<>();
    private MutableLiveData<LogoutRes> logOutResLiveData = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public LiveData<LogInRes> getLogInResLiveData() {
        return logInResLiveData;
    }

    public LiveData<LogoutRes> getLogoutResLiveData() {
        return logOutResLiveData;
    }


    public void login(LogInReq logInReq) {
        consumeApi(profileRepository.logIn(logInReq), data -> logInResLiveData.setValue(data));
    }

    public void logout(LogoutReq logoutReq) {
        consumeApi(profileRepository.logout(logoutReq), data -> logOutResLiveData.setValue(data));
    }
}
