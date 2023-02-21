package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VrifyOtpReq {

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("phone")
    private String phone;

    @Expose
    @SerializedName("otp")
    private String otp;

    @Expose
    @SerializedName("userId")
    private String userId;


    public VrifyOtpReq(String email) {
        this(email,"","");
    }
    public VrifyOtpReq(String userId, String otp) {
        this("",userId,otp);
    }

    public VrifyOtpReq(String email, String userId, String otp) {
        this.email = email;
        this.otp=otp;
        this.userId=userId;
    }
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
