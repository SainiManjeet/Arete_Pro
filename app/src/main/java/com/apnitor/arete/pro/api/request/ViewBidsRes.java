package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.response.GetProviderRes;

import java.util.ArrayList;

public class ViewBidsRes {


    @Expose
    @SerializedName("bids")
    ArrayList<GetProviderRes> bids;


    public ViewBidsRes(ArrayList<GetProviderRes> bids) {
        this.bids = bids;
    }

    public ArrayList<GetProviderRes> getViewBids() {
        return bids;
    }

    public void setJobs(ArrayList<GetProviderRes> bids) {
        this.bids = bids;
    }



}

