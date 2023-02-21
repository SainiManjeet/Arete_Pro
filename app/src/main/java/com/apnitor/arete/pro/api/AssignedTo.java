package com.apnitor.arete.pro.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignedTo implements Parcelable {
    public static final Creator<AssignedTo> CREATOR = new Creator<AssignedTo>() {
        @Override
        public AssignedTo createFromParcel(Parcel in) {
            return new AssignedTo(in);
        }

        @Override
        public AssignedTo[] newArray(int size) {
            return new AssignedTo[size];
        }
    };
    @Expose
    @SerializedName("providerId")
    public String providerId;

    @Expose
    @SerializedName("providerName")
    public String providerName;

    @Expose
    @SerializedName("providerImageUrl")
    public String providerImg;

    @Expose
    @SerializedName("bookedPrice")
    public String bookedPrice;


    public AssignedTo() {

    }

    protected AssignedTo(Parcel in) {
        providerId = in.readString();
        providerName = in.readString();
        providerImg = in.readString();
        bookedPrice = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerId);
        dest.writeString(providerName);
        dest.writeString(providerImg);
        dest.writeString(bookedPrice);


    }
}
