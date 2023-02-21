package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;

import java.util.ArrayList;
import java.util.List;

public class CreateJobReqNew  {

    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("providerId")
    public String providerId;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("bedrooms")
    public Integer bedrooms;

    @Expose
    @SerializedName("bathrooms")
    public Integer bathrooms;

    @Expose
    @SerializedName("kitchen")
    public Integer kitchen;

    @Expose
    @SerializedName("others")
    public Integer others;

    @Expose
    @SerializedName("sqft")
    public Double sqft;

  /*  @Expose
    @SerializedName("address")
    public List<GetAddress> address;*/

    @Expose
    @SerializedName("address")
    public GetAddress address;

    @Expose
    @SerializedName("date")
    public String date;

    @Expose
    @SerializedName("time")
    public String time;
    @Expose
    @SerializedName("cleaningType")
    public String cleaningType;

    @Expose
    @SerializedName("howOften")
    public String howOften;

    @Expose
    @SerializedName("when")
    public String when;

    @Expose
    @SerializedName("specialNotesForProvider")
    public String specialNotesForProvider;


    @Expose
    @SerializedName("images")
    public ArrayList<ImageUrlReq> images;
    @Expose
    @SerializedName("extraCleaning")
    public List<ExtraCleaningRes> extraCleaning;

    @Expose
    @SerializedName("clientPrice")
    public String price;

    /*@Expose
    @SerializedName("estTime")
    public String estTime;*/

    @Expose
    @SerializedName("estTime")
    public float estTime;

    @Expose
    @SerializedName("estPrice")
    public String estPrice;

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("jobType")
    public String jobType;

    // Prewalk Notes
 /*   @Expose
    @SerializedName("preWalkNotes")
    public String preWalkNotes;
*/
    public CreateJobReqNew() {
    }


    public CreateJobReqNew(String authToken, String cleaningType, Integer bedrooms, Integer bathrooms, Integer kitchen, Integer others, Double sqft, GetAddress address, String date, String time
            , String howOften, String when, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                           ArrayList<ExtraCleaningRes> extraCleaning, float estTime, String estPrice, String price, String providerId, String description, String jobType) {
            this.authToken = authToken;
            this.bedrooms = bedrooms;
            this.bathrooms = bathrooms;
            this.kitchen = kitchen;
            this.others = others;
        this.sqft = sqft;
        this.address = address;
        this.date = date;
        this.time = time;
        this.howOften = howOften;
        this.when = when;
        this.specialNotesForProvider = specialNotesForProvider;
        this.images = images;
        this.extraCleaning = extraCleaning;
        this.estTime = estTime;
        this.price = price;
        this.providerId = providerId;
        this.description = description;
        this.jobType = jobType;
        this.cleaningType = cleaningType;
        this.estPrice = estPrice;

    }


}
