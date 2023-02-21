package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityReq /*implements Parcelable */ {

    @Expose
    @SerializedName("availType")
    public String type;

    @Expose
    @SerializedName("availabilityDays")
    ArrayList<AvailabilityElements> availabilityDays;
    @Expose
    @SerializedName("nonavailabilityDays")
    ArrayList<String> nonavailabilityDays;

    public AvailabilityReq() {
    }

    public String getType() {
        return type;
    }

    //  String nonavailabilityDays[];

    public void setType(String type) {
        this.type = type;
    }


    public ArrayList<AvailabilityElements> getAvailabilityDays() {
        return availabilityDays;
    }

    public void setAvailabilityDays(ArrayList<AvailabilityElements> availabilityDays) {
        this.availabilityDays = availabilityDays;
    }

    public ArrayList<String> getNonavailabilityDays() {
        return nonavailabilityDays;
    }

    public void setNonavailabilityDays(ArrayList<String> nonavailabilityDays) {
        this.nonavailabilityDays = nonavailabilityDays;
    }


}
