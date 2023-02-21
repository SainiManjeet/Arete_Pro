package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetJobReq {
    @Expose
    @SerializedName("jobNumber")
    private String id;
    @Expose
    @SerializedName("authToken")
    private String authToken;
    @Expose
    @SerializedName("type")
    private String type;
    public GetJobReq(String id,String authToken, String type) {
        this.id=id;
        this.authToken=authToken;
        this.type=type;
    }

    // Get Bid
    public GetJobReq(String authToken) {
        this.authToken=authToken;
    }


}
