package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Availability {


    @Expose
    @SerializedName("availability")
    public AvailabilityDays availability;


}
