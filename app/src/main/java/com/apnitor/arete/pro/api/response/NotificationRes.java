package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationRes {
    @Expose
    @SerializedName("title")
    public String title;
    @Expose
    @SerializedName("message")
    public  String message;
    @Expose
    @SerializedName("createdOn")
    public String date;
    @Expose
    @SerializedName("time")
    public String time;
    @Expose
    @SerializedName("image")
    public String image;
}
