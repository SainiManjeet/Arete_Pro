package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewPasswordReq {

    @Expose
    @SerializedName("password")
    private String newPassword;

    @Expose
    @SerializedName("otp")
    private String otp;


    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("phone")
    private String phone;


    public NewPasswordReq(String newPassword, String phone, String email, String otp) {
        this.newPassword = newPassword;
        this.email = email;
        this.phone = phone;
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
