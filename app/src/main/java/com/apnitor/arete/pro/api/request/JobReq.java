package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class JobReq {
    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("type")
    public String type;

    public JobReq(String authToken, String type) {
        this.authToken = authToken;
        this.type = type;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
