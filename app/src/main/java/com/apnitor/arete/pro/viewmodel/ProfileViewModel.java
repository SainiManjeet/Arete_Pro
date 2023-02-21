package com.apnitor.arete.pro.viewmodel;

import android.app.Application;


import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.api.response.UpdateProfileRes;
import com.apnitor.arete.pro.repository.ProfileRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProfileViewModel extends BaseViewModel {

    private ProfileRepository profileRepository;
    private MutableLiveData<UpdateProfileRes> updateProfileResLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public LiveData<UpdateProfileRes> getUpdateProfileResLiveData() {
        return updateProfileResLiveData;
    }

    public void updateProfile(UpdateProfileReq updateProfileReq) {
        consumeApi(
                profileRepository.updateProfile(updateProfileReq),
                data -> updateProfileResLiveData.setValue(data)
        );
    }
}
