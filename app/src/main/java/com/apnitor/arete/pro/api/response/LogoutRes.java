package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoutRes {

    @Expose
    @SerializedName("message")
    private String message;

    public LogoutRes(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
