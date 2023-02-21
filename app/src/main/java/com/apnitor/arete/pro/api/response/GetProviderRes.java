package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;

import java.util.ArrayList;

public class GetProviderRes implements Parcelable {

    public static final Creator<GetProviderRes> CREATOR = new Creator<GetProviderRes>() {
        @Override
        public GetProviderRes createFromParcel(Parcel in) {
            return new GetProviderRes(in);
        }

        @Override
        public GetProviderRes[] newArray(int size) {
            return new GetProviderRes[size];
        }
    };
    @Expose
    @SerializedName("id")
    public String providerId;
    @Expose
    @SerializedName("name")
    public String providerName;
    @Expose
    @SerializedName("image")
    public String providerImage;

    @SerializedName("totalHours")
    public String totalHours;

    @Expose
    @SerializedName("noOfJobs")
    public String jobDone;
    @Expose
    @SerializedName("rating")
    public String rating;
    @Expose
    @SerializedName("noOfRating")
    public String providerRating;
    @Expose
    @SerializedName("price")
    public String providerPrice;
    @Expose
    @SerializedName("bidPrice")
    public String bidPrice;
    @Expose
    @SerializedName("birthDate")
    public String birthDate;
    @Expose
    @SerializedName("gender")
    public String gender;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("Address")
    public GetAddress address;

    @Expose
    @SerializedName("aboutme")
    public String aboutMe;

    @Expose
    @SerializedName("providers")
    public ArrayList<GetProviderRes> providerRes;

    // Reviews
    @Expose
    @SerializedName("reviews")
    public ArrayList<ReviewRes> providerReviews;

    // Description
    @Expose
    @SerializedName("bidDescription")
    public String bidDescription;


    public GetProviderRes() {
    }


    public GetProviderRes(
            ArrayList<ReviewRes> providerReviews) {
        this.providerReviews = providerReviews;

    }

    protected GetProviderRes(Parcel in) {
        providerId = in.readString();
        providerName = in.readString();
        providerImage = in.readString();
        totalHours = in.readString();
        jobDone = in.readString();
        providerRating = in.readString();
        rating = in.readString();
        //providerReviews = in.readString();
        providerPrice = in.readString();
        bidPrice = in.readString();
        birthDate = in.readString();
        gender = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readParcelable(GetAddress.class.getClassLoader());
        aboutMe = in.readString();
        bidDescription = in.readString();

        /*clientId = in.readString();
        comment = in.readString();*/
        providerRes = in.createTypedArrayList(GetProviderRes.CREATOR);
        providerReviews = in.createTypedArrayList(ReviewRes.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerId);
        dest.writeString(providerName);
        dest.writeString(providerImage);
        dest.writeString(totalHours);
        dest.writeString(jobDone);
        dest.writeString(providerRating);
        dest.writeString(rating);
        // dest.writeString(providerReviews);
        dest.writeString(providerPrice);
        dest.writeString(bidPrice);
        dest.writeString(birthDate);
        dest.writeString(gender);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeParcelable(address, flags);
        dest.writeString(aboutMe);
        dest.writeString(bidDescription);


        /*dest.writeString(clientId);
        dest.writeString(comment);*/
        dest.writeTypedList(providerRes);
        dest.writeTypedList(providerReviews);//
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
