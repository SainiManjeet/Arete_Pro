package com.apnitor.arete.pro.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRooms implements Parcelable {
    public static final Creator<GetRooms> CREATOR = new Creator<GetRooms>() {
        @Override
        public GetRooms createFromParcel(Parcel in) {
            return new GetRooms(in);
        }

        @Override
        public GetRooms[] newArray(int size) {
            return new GetRooms[size];
        }
    };
    @Expose
    @SerializedName("clean")
    public String clean;

    @Expose
    @SerializedName("protect")
    public String protect;

    @Expose
    @SerializedName("deodrize")
    public String deodrize;


    public GetRooms() {

    }

    protected GetRooms(Parcel in) {
        clean = in.readString();
        protect = in.readString();
        deodrize = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clean);
        dest.writeString(protect);
        dest.writeString(deodrize);

    }


}
