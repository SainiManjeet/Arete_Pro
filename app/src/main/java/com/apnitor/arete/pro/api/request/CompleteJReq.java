package com.apnitor.arete.pro.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;

import java.util.ArrayList;
import java.util.List;

public class CompleteJReq implements Parcelable {

    public static final Creator<CreateJobReq> CREATOR = new Creator<CreateJobReq>() {
        @Override
        public CreateJobReq createFromParcel(Parcel in) {
            return new CreateJobReq(in);
        }

        @Override
        public CreateJobReq[] newArray(int size) {
            return new CreateJobReq[size];
        }
    };
    @Expose
    @SerializedName("authToken")
    public String authToken;
    @Expose
    @SerializedName("jobId")
    public String jobId;

    @Expose
    @SerializedName("bedrooms")
    public Integer bedrooms;

    @Expose
    @SerializedName("bathrooms")
    public Integer bathrooms;


    @Expose
    @SerializedName("others")
    public Integer others;

    @Expose
    @SerializedName("sqft")
    public Double sqft;

    @Expose
    @SerializedName("when")
    public String when;

    @Expose
    @SerializedName("time")
    public String time;


    @Expose
    @SerializedName("date")
    public String date;

    @Expose
    @SerializedName("howOften")
    public String howOften;

    @Expose
    @SerializedName("providerId")
    public String providerId;


    @Expose
    @SerializedName("estTime")
    public Double estTime;


    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("priceType")
    public String priceType;

    @Expose
    @SerializedName("specialNotesForProvider")
    public String specialNotesForProvider;

    @Expose
    @SerializedName("cleaningType")
    public String cleaningType;

    @Expose
    @SerializedName("kitchen")
    public Integer kitchen;

    @Expose
    @SerializedName("estPrice")
    public String estPrice;


    @Expose
    @SerializedName("jobType")
    public String jobType;

    @Expose
    @SerializedName("clientPrice")
    public String price;

    @Expose
    @SerializedName("extraCleaning")
    public List<ExtraCleaningRes> extraCleaning;


    @Expose
    @SerializedName("images")
    public ArrayList<ImageUrlReq> images;

    @Expose
    @SerializedName("preWalkNotes")
    public String preWalkNotes;

    @Expose
    @SerializedName("postWalkNotes")
    public String postWalkNotes;

    @Expose
    @SerializedName("preWalkImages")
    public ArrayList<ImageUrlReq> preWalkImages;

    @Expose
    @SerializedName("postWalkImages")
    public ArrayList<ImageUrlReq> postWalkImages;


    public CompleteJReq() {
    }

    public CompleteJReq(String authToken, String jobId, String cleaningType, Integer bedrooms, Integer bathrooms, Integer kitchen, Integer others, Double sqft, String date, String time
            , String howOften, String when, String priceType, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                        ArrayList<ExtraCleaningRes> extraCleaning, Double estTime, String estPrice, String price, String providerId, String description, String jobType, ArrayList<ImageUrlReq> preWalkImages, String preWalkNotes) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.priceType = priceType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.kitchen = kitchen;
        this.others = others;
        this.sqft = sqft;
        this.date = date;
        this.time = time;
        this.howOften = howOften;
        this.when = when;
        this.specialNotesForProvider = specialNotesForProvider;
        this.images = images;
        this.preWalkImages = preWalkImages;
        // this.postWalkImages = postWalkImages;
        this.preWalkNotes = preWalkNotes;
        // this.postWalkNotes = postWalkNotes;

        this.extraCleaning = extraCleaning;
        this.estTime = estTime;
        this.price = price;
        this.providerId = providerId;
        this.description = description;
        this.jobType = jobType;
        this.cleaningType = cleaningType;
        this.estPrice = estPrice;
    }

    // Prewalk Postwalk notes : So that job marked as completed
    public CompleteJReq(String authToken, String jobId, String cleaningType, Integer bedrooms, Integer bathrooms, Integer kitchen, Integer others, Double sqft, String date, String time
            , String howOften, String when, String priceType, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                        ArrayList<ExtraCleaningRes> extraCleaning, Double estTime, String estPrice, String price, String providerId, String description, String jobType, ArrayList<ImageUrlReq> postWalkImages, String postWalkNotes, ArrayList<ImageUrlReq> preWalkImages, String preWalkNotes) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.priceType = priceType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.kitchen = kitchen;
        this.others = others;
        this.sqft = sqft;
        this.date = date;
        this.time = time;
        this.howOften = howOften;
        this.when = when;
        this.specialNotesForProvider = specialNotesForProvider;
        this.images = images;
        this.preWalkImages = preWalkImages;
        this.postWalkImages = postWalkImages;
        this.preWalkNotes = preWalkNotes;
        this.postWalkNotes = postWalkNotes;
        this.extraCleaning = extraCleaning;
        this.estTime = estTime;
        this.price = price;
        this.providerId = providerId;
        this.description = description;
        this.jobType = jobType;
        this.cleaningType = cleaningType;
        this.estPrice = estPrice;
    }

    // For Edit Job Only
    public CompleteJReq(String authToken, String jobId, String cleaningType, Integer bedrooms, Integer bathrooms, Integer kitchen, Integer others, Double sqft, String date, String time
            , String howOften, String when, String priceType, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                        ArrayList<ExtraCleaningRes> extraCleaning, Double estTime, String estPrice, String price, String providerId, String description, String jobType) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.priceType = priceType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.kitchen = kitchen;
        this.others = others;
        this.sqft = sqft;
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


    protected CompleteJReq(Parcel in) {
        authToken = in.readString();
        providerId = in.readString();
        if (in.readByte() == 0) {
            bedrooms = null;
        } else {
            bedrooms = in.readInt();
        }
        if (in.readByte() == 0) {
            bathrooms = null;
        } else {
            bathrooms = in.readInt();
        }
        if (in.readByte() == 0) {
            kitchen = null;
        } else {
            kitchen = in.readInt();
        }
        if (in.readByte() == 0) {
            others = null;
        } else {
            others = in.readInt();
        }
        if (in.readByte() == 0) {
            sqft = null;
        } else {
            sqft = in.readDouble();
        }
        date = in.readString();
        time = in.readString();
        cleaningType = in.readString();
        howOften = in.readString();
        when = in.readString();
        specialNotesForProvider = in.readString();
        images = in.createTypedArrayList(ImageUrlReq.CREATOR);
        extraCleaning = in.createTypedArrayList(ExtraCleaningRes.CREATOR);
        preWalkImages = in.createTypedArrayList(ImageUrlReq.CREATOR);
        postWalkImages = in.createTypedArrayList(ImageUrlReq.CREATOR);
        price = in.readString();
        estTime = in.readDouble();
        estPrice = in.readString();
        description = in.readString();
        jobType = in.readString();
        jobId = in.readString();
        preWalkNotes = in.readString();
        postWalkNotes = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authToken);
        dest.writeString(providerId);
        dest.writeString(jobId);
        if (bedrooms == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bedrooms);
        }
        if (bathrooms == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bathrooms);
        }
        if (kitchen == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(kitchen);
        }
        if (others == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(others);
        }
        if (sqft == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(sqft);
        }
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(cleaningType);
        dest.writeString(howOften);
        dest.writeString(when);
        dest.writeString(specialNotesForProvider);
        dest.writeTypedList(images);
        dest.writeTypedList(preWalkImages);
        dest.writeTypedList(postWalkImages);
        dest.writeTypedList(extraCleaning);
        dest.writeString(price);
        dest.writeDouble(estTime);
        dest.writeString(estPrice);
        dest.writeString(description);
        dest.writeString(jobType);
        dest.writeString(preWalkNotes);
        dest.writeString(postWalkNotes);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}