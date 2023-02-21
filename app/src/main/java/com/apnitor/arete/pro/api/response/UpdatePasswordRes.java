package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePasswordRes {

    @Expose
    @SerializedName("success")
    private Boolean success;

    public UpdatePasswordRes(Boolean success) {
        this.success = success;

    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
