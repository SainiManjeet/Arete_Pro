package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetMyRatingRes implements Parcelable {

    public static final Creator<GetMyRatingRes> CREATOR = new Creator<GetMyRatingRes>() {
        @Override
        public GetMyRatingRes createFromParcel(Parcel in) {
            return new GetMyRatingRes(in);
        }

        @Override
        public GetMyRatingRes[] newArray(int size) {
            return new GetMyRatingRes[size];
        }
    };

    @Expose
    @SerializedName("rating")
    public String rating;

    // add

    @Expose
    @SerializedName("clientId")
    public String clientId;
    @Expose
    @SerializedName("comment")
    public String comment;
    @Expose
    @SerializedName("name")
    public  String providerName;
    @Expose
    @SerializedName("imageUrl")
    public String imageUrl;



    /* Added check if it effect somewhere*/
    @Expose
    @SerializedName("favorite")
    public String favorite;


    // Reviews
    @Expose
    @SerializedName("reviews")
   // public ArrayList<ReviewRes> providerReviews;
    public ArrayList<ReviewRes> providerReviews;

    public GetMyRatingRes() {
    }


    public GetMyRatingRes(
            ArrayList<ReviewRes> providerReviews) {
        this.providerReviews = providerReviews;

    }

    protected GetMyRatingRes(Parcel in) {

        rating = in.readString();

        providerReviews = in.createTypedArrayList(ReviewRes.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rating);
        dest.writeTypedList(providerReviews);//
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
