package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateBidReq {

    @Expose
    @SerializedName("price")
    public String price;

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("jobId")
    public String jobId;

    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("bidId")
    public String bidId;



    public UpdateBidReq(String authToken, String price, String description, String jobId,String bidId) {
        this.price = price;
        this.description = description;
        this.jobId = jobId;
        this.authToken = authToken;
        this.bidId = bidId;
    }


}
