package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateJobReq {
    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("images")
    public ArrayList<ImageUrlReq> images;

    @Expose
    @SerializedName("preWalkImages")
    public ArrayList<ImageUrlReq> preWalkImages;

    @Expose
    @SerializedName("postWalkImages")
    public ArrayList<ImageUrlReq> postWalkImages;

    @Expose
    @SerializedName("jobId")
    public String jobId;

    public UpdateJobReq() {
    }

    // JOB IMAGES
    public UpdateJobReq(String authToken, ArrayList<ImageUrlReq> images, String jobId) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.images = images;
    }

    // PRE WALK IMAGES
    public UpdateJobReq(String authToken, String jobId, ArrayList<ImageUrlReq> preWalkImages) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.preWalkImages = preWalkImages;
    }

    // POST WALK IMAGES
    public UpdateJobReq(ArrayList<ImageUrlReq> postWalkImages, String jobId, String authToken) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.postWalkImages = postWalkImages;
    }

}
