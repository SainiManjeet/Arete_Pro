package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakeBidReq {



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
    @SerializedName("action")
    public String action;

    public MakeBidReq(String authToken,  String jobId){
        this(authToken,"","",jobId,"");
    }
    public MakeBidReq(String authToken, String jobId,String action){
        this(authToken,"","",jobId,action);
    }
    public MakeBidReq(String authToken, String price, String description,String jobId,String action){
        this.action=action;
        this.price=price;
        this.description=description;
        this.jobId=jobId;
        this.authToken=authToken;
    }

}
