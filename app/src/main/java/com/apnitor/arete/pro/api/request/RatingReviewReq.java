package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingReviewReq {

    @Expose
    @SerializedName("authToken")
    private String authToken;

    @Expose
    @SerializedName("providerId")
    private String providerId;

    public RatingReviewReq(String authToken, String providerId) {
        this.authToken = authToken;
        this.providerId = providerId;

    }

}
