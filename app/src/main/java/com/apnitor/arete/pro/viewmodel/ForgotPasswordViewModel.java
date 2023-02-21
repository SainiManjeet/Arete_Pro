package com.apnitor.arete.pro.viewmodel;

import android.app.Application;

import com.apnitor.arete.pro.api.request.ForgotPasswordReq;
import com.apnitor.arete.pro.api.request.NewPasswordReq;
import com.apnitor.arete.pro.api.request.VrifyOtpReq;
import com.apnitor.arete.pro.api.response.ForgotPasswordRes;
import com.apnitor.arete.pro.api.response.UpdatePasswordRes;
import com.apnitor.arete.pro.api.response.VerifyPasswordRes;
import com.apnitor.arete.pro.repository.ProfileRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ForgotPasswordViewModel extends BaseViewModel {

    private ProfileRepository profileRepository;
    private MutableLiveData<UpdatePasswordRes> updatePasswordResLiveData = new MutableLiveData<>();
    private MutableLiveData<VerifyPasswordRes> verifyPasswordResLiveData = new MutableLiveData<>();
    private MutableLiveData<ForgotPasswordRes> forgotPasswordResLiveData = new MutableLiveData<>();

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public LiveData<ForgotPasswordRes> getForgotPasswordLiveData() {
        return forgotPasswordResLiveData;
    }

    public LiveData<UpdatePasswordRes> getNewPasswordResLiveData() {
        return updatePasswordResLiveData;
    }

    public LiveData<VerifyPasswordRes> getVerifyUserResLiveData() {
        return verifyPasswordResLiveData;
    }


    public void newPassword(String newPasswprd, String phone,String email,String otp) {
        NewPasswordReq newPasswordReq = new NewPasswordReq(newPasswprd, phone , email,otp);
        consumeApi(profileRepository.newPassword(newPasswordReq), data -> updatePasswordResLiveData.setValue(data));
    }
    public void forgotPassword(String email,String phone) {
        ForgotPasswordReq forgotPasswordReq = new ForgotPasswordReq(email, phone);
        consumeApi(profileRepository.forgotPassword(forgotPasswordReq), data -> forgotPasswordResLiveData.setValue(data));
    }

    public void verifyUserOnPhone(String userId,String otp) {
        VrifyOtpReq vrifyOtpReq = new VrifyOtpReq(userId,otp);
        consumeApi(profileRepository.verifyOnPhone(vrifyOtpReq), data -> verifyPasswordResLiveData.setValue(data));
    }

    public void verifyUserOnEmail(String userId,String otp) {
        VrifyOtpReq vrifyOtpReq = new VrifyOtpReq(userId,otp);
        consumeApi(profileRepository.verifyOnEmail(vrifyOtpReq), data -> verifyPasswordResLiveData.setValue(data));
    }
}
