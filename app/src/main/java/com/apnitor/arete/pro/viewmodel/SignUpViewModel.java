package com.apnitor.arete.pro.viewmodel;

import android.app.Application;


import com.apnitor.arete.pro.api.request.SignUpReq;
import com.apnitor.arete.pro.api.response.SignUpRes;
import com.apnitor.arete.pro.repository.ProfileRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SignUpViewModel extends BaseViewModel {

    private ProfileRepository profileRepository;
    private MutableLiveData<SignUpRes> signUpResLiveData = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public LiveData<SignUpRes> getSignUpResLiveData() {
        return signUpResLiveData;
    }

    public void signUp(String firstName, String lastName,String email,String phone, String password,String userType) {
        SignUpReq signUpReq = new SignUpReq(firstName, lastName,email, phone,password,userType);
        consumeApi(profileRepository.signUp(signUpReq), data -> signUpResLiveData.setValue(data));
    }
}
