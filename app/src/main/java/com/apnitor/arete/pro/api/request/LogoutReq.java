package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutReq {

    @Expose
    @SerializedName("authToken")
    private String authToken;

    public LogoutReq(String token){
        this.authToken=token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
