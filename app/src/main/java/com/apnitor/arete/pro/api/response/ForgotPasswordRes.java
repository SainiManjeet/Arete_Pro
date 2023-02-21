package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRes {

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("otp")
    private String otp;

    @Expose
    @SerializedName("message")
    private String message;

    public ForgotPasswordRes(String email, String otp ) {
        this.email = email;
        this.otp=otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
