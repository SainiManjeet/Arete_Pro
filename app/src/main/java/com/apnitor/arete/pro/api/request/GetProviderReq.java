package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProviderReq {
    @Expose
    @SerializedName("userId")
    public String userId;
    public GetProviderReq(String userId){
        this.userId=userId;
    }
}
