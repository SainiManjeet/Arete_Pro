package com.apnitor.arete.pro.repository;

import android.app.Application;

import com.apnitor.arete.pro.api.ApiService;
import com.apnitor.arete.pro.api.ProfileApi;
import com.apnitor.arete.pro.api.request.ForgotPasswordReq;
import com.apnitor.arete.pro.api.request.LogInReq;
import com.apnitor.arete.pro.api.request.LogoutReq;
import com.apnitor.arete.pro.api.request.NewPasswordReq;
import com.apnitor.arete.pro.api.request.VrifyOtpReq;
import com.apnitor.arete.pro.api.request.SignUpReq;
import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.api.response.BaseRes;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.api.response.ForgotPasswordRes;
import com.apnitor.arete.pro.api.response.LogoutRes;
import com.apnitor.arete.pro.api.response.SignUpRes;
import com.apnitor.arete.pro.api.response.UpdatePasswordRes;
import com.apnitor.arete.pro.api.response.UpdateProfileRes;
import com.apnitor.arete.pro.api.response.VerifyPasswordRes;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ProfileRepository {

    private ProfileApi profileApi;

    public ProfileRepository(Application application) {
        profileApi = ApiService.getProfileApi(application);
    }

    public Single<BaseRes<LogInRes>> logIn(LogInReq logInReq) {
//        return Single.create((SingleOnSubscribe<BaseRes<LogInRes>>) emitter -> {
//
//            LogInRes logInRes = new LogInRes(logInReq.getUserName(), logInReq.getPassword());
//            emitter.onSuccess(new BaseRes<>(true, logInRes, 0, "", ""));
//        })
//                .delay(2, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io());
//        // TDO: 01/12/18 Please use the following code when api are working.
        return profileApi.logIn(logInReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<LogoutRes>> logout(LogoutReq logoutReq) {
        return profileApi.logout(logoutReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<SignUpRes>> signUp(SignUpReq signUpReq) {
        return profileApi.signUp(signUpReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<ForgotPasswordRes>> forgotPassword(ForgotPasswordReq forgotPasswordReq) {
        return profileApi.forgotPassword(forgotPasswordReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<VerifyPasswordRes>> verifyOnPhone(VrifyOtpReq vrifyOtpReq) {
        return profileApi.verifyOnPhone(vrifyOtpReq)
                .subscribeOn(Schedulers.io());
    }
    public Single<BaseRes<VerifyPasswordRes>> verifyOnEmail(VrifyOtpReq vrifyOtpReq) {
        return profileApi.verifyOnEmail(vrifyOtpReq)
                .subscribeOn(Schedulers.io());
    }
    public Single<BaseRes<UpdatePasswordRes>> newPassword(NewPasswordReq newPasswordReq) {
        return profileApi.updatePassword(newPasswordReq)
                .subscribeOn(Schedulers.io());
    }



    public Single<BaseRes<UpdateProfileRes>> updateProfile(UpdateProfileReq updateProfileReq) {
//        return Single.create((SingleOnSubscribe<BaseRes<UpdateProfileRes>>) emitter ->
//                emitter.onSuccess(new BaseRes<>(true, new UpdateProfileRes(), 0, "", "")))
//                .delay(2, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io());
        return profileApi.updateProfile(updateProfileReq)
                .subscribeOn(Schedulers.io());
    }
}
