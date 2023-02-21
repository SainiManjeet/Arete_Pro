package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeBidRes {

    @Expose
    @SerializedName("bidId")
    private String bidId;

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public MakeBidRes(String bidId) {
        this.bidId = bidId;
    }
}
