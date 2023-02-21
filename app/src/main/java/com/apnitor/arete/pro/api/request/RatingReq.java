package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingReq {
    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("to")
    public String to;

 /*   @Expose
    @SerializedName("rating")
    public int rating;*/

    @Expose
    @SerializedName("rating")
    public float rating;

    @Expose
    @SerializedName("review")
    public String review;

    @Expose
    @SerializedName("jobId")
    public String jobId;


    @Expose
    @SerializedName("favorite")
    public Boolean favorite;

    public RatingReq(String authToken, String to, float rating, String review, String jobId, Boolean favorite) {
        this.authToken = authToken;
        this.to = to;
        this.rating = rating;
        this.review = review;
        this.jobId = jobId;
        this.favorite = favorite;
    }

}
