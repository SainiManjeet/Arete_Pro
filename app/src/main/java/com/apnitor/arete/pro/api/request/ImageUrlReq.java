package com.apnitor.arete.pro.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUrlReq implements Parcelable {

    @Expose
    @SerializedName("imageUrl")
    public String imageUrl;

    public ImageUrlReq() {}

    protected ImageUrlReq(Parcel in) {
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageUrlReq> CREATOR = new Creator<ImageUrlReq>() {
        @Override
        public ImageUrlReq createFromParcel(Parcel in) {
            return new ImageUrlReq(in);
        }

        @Override
        public ImageUrlReq[] newArray(int size) {
            return new ImageUrlReq[size];
        }
    };
}
