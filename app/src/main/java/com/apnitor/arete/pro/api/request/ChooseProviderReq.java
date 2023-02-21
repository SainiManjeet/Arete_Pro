package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChooseProviderReq {

    @Expose
    @SerializedName("authToken")
    private String authToken;

    public ChooseProviderReq(String authToken) {

        this.authToken=authToken;

    }

}
