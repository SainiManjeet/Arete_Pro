package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInReq {


    @Expose
    @SerializedName("password")
    private String password;

    // For Facebook login:
    @Expose
    @SerializedName("fbUserId")
    private String fbUserId;

    @Expose
    @SerializedName("firstName")
    private String firstName;

    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;

    @Expose
    @SerializedName("deviceId")
    private String deviceId;

    @Expose
    @SerializedName("uniqueId")
    private String uniqueId;

    @Expose
    @SerializedName("deviceType")
    private String deviceType;

    @Expose
    @SerializedName("googleId")
    private String googleId;

    @Expose
    @SerializedName("lastName")
    private String lastName;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("userType")
    private String userType;
    @Expose
    @SerializedName("phone")
    private String phone;


    public LogInReq(String email, String password, String deviceId, String deviceType, String uniqueId, String userType, String phone) {
        this(email, password, "", "", "", "", deviceId, uniqueId, "", deviceType, userType, phone);
    }

    public LogInReq(String fbEmail, String fbUserId, String fbFirstName, String fbLastName, String imageUrl, String deviceId, String deviceType, String uniqueId, String userType) {
        this(fbEmail, "", fbUserId, fbFirstName, fbLastName, imageUrl, deviceId, uniqueId, "", deviceType, userType, "");
    }

    public LogInReq(String fbEmail, String fbUserId, String fbFirstName, String fbLastName, String imageUrl, String deviceId, String deviceType, String googleId, String uniqueId, String userType) {
        this(fbEmail, "", "", fbFirstName, fbLastName, imageUrl, deviceId, uniqueId, googleId, deviceType, userType, "");
    }

    /*  public LogInReq(String googleId, String fbFirstName, String fbLastName, String fbEmail) {
          this("", "", googleId, fbFirstName, fbLastName, fbEmail);
      }*/
    public LogInReq(String fbEmail, String password, String fbUserId, String fbFirstName, String fbLastName, String imageUrl, String deviceId, String uniqueId, String googleId, String deviceType, String userType, String phone) {

        this.deviceId = deviceId;
        this.googleId = googleId;
        this.uniqueId = uniqueId;
        this.deviceType = deviceType;
        this.password = password;
        this.fbUserId = fbUserId;
        this.firstName = fbFirstName;
        this.lastName = fbLastName;
        this.email = fbEmail;
        this.imageUrl = imageUrl;
        this.userType = userType;
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
