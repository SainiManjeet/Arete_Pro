package com.apnitor.arete.pro.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetAddress implements Parcelable {
    public GetAddress() {

    }

    @Expose
    @SerializedName("houseNo")
    public String houseNo;

    @Expose
    @SerializedName("street")
    public String street;

    @Expose
    @SerializedName("city")
    public String city;

    @Expose
    @SerializedName("state")
    public String state;

    @Expose
    @SerializedName("zipCode")
    public int zipCode;

    @Expose
    @SerializedName("country")
    public String country;

    @Expose
    @SerializedName("latitude")
    public double latitude;

    @Expose
    @SerializedName("longitude")
    public double longitude;


    protected GetAddress(Parcel in) {
        houseNo = in.readString();
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readInt();
        country = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<GetAddress> CREATOR = new Creator<GetAddress>() {
        @Override
        public GetAddress createFromParcel(Parcel in) {
            return new GetAddress(in);
        }

        @Override
        public GetAddress[] newArray(int size) {
            return new GetAddress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(houseNo);
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(zipCode);
        dest.writeString(country);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }


    @Override
    public String toString() {
        return "GetAddress{" +
                "houseNo='" + houseNo + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode=" + zipCode +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
