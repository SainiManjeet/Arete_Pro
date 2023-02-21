package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientJobReq {

    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("type")
    public String type;

    public ClientJobReq() {
    }

    public ClientJobReq(String authToken, String type) {
        this.authToken = authToken;
        this.type = type;
    }
}
