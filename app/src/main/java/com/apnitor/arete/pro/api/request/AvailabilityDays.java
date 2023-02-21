package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AvailabilityDays {

    @Expose
    @SerializedName("availabilityDays")
    public ArrayList<AvailabilityElements> availabilityDays;
    @Expose
        @SerializedName("nonavailabilityDays")
    public ArrayList<String> nonavailabilityDays;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Expose
    @SerializedName("availType")
    public String type;

    public ArrayList<String> getNonavailabilityDays() {
        return nonavailabilityDays;
    }

    public void setNonavailabilityDays(ArrayList<String> nonavailabilityDays) {
        this.nonavailabilityDays = nonavailabilityDays;
    }

    public ArrayList<AvailabilityElements> getAvailabilityDays() {
        return availabilityDays;
    }

    public void setAvailabilityDays(ArrayList<AvailabilityElements> availabilityDays) {
        this.availabilityDays = availabilityDays;
    }


}
