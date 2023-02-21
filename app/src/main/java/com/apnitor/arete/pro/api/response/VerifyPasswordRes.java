package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyPasswordRes {

    @Expose
    @SerializedName("success")
    private Boolean success;

    public VerifyPasswordRes(Boolean success) {
        this.success = success;
    }
}
