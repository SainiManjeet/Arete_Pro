package com.apnitor.arete.pro.api;

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
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileApi {

    @POST("login")
    Single<BaseRes<LogInRes>> logIn(@Body LogInReq logInReq);

    @POST("signUp")
    Single<BaseRes<SignUpRes>> signUp(@Body SignUpReq signUpReq);

    @POST("forgotPassword")
    Single<BaseRes<ForgotPasswordRes>> forgotPassword(@Body ForgotPasswordReq forgotPasswordReq);

    @POST("resetPassword")
    Single<BaseRes<UpdatePasswordRes>> updatePassword(@Body NewPasswordReq newPasswordReq);

    @POST("verifyPhoneOnSignup")
    Single<BaseRes<VerifyPasswordRes>> verifyOnPhone(@Body VrifyOtpReq vrifyOtpReq);

    @POST("verifyEmailOnProfile")
    Single<BaseRes<VerifyPasswordRes>> verifyOnEmail(@Body VrifyOtpReq vrifyOtpReq);

    @POST("logout")
    Single<BaseRes<LogoutRes>> logout(@Body LogoutReq logoutReq);

    @POST("updateProfile")
    Single<BaseRes<UpdateProfileRes>> updateProfile(@Body UpdateProfileReq updateProfileReq);
}
