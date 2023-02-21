package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAddRatingRes implements Parcelable {

    public static final Creator<GetAddRatingRes> CREATOR = new Creator<GetAddRatingRes>() {
        @Override
        public GetAddRatingRes createFromParcel(Parcel in) {
            return new GetAddRatingRes(in);
        }

        @Override
        public GetAddRatingRes[] newArray(int size) {
            return new GetAddRatingRes[size];
        }
    };
    @Expose
    @SerializedName("ratingId")
    public String ratingId;
    @Expose
    @SerializedName("message")
    public String message;

    protected GetAddRatingRes(Parcel in) {
        ratingId = in.readString();
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ratingId);
        dest.writeString(message);
    }
}
