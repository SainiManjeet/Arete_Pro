package com.apnitor.arete.pro.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelBidReq implements Parcelable {

    public static final Creator<CancelBidReq> CREATOR = new Creator<CancelBidReq>() {
        @Override
        public CancelBidReq createFromParcel(Parcel in) {
            return new CancelBidReq(in);
        }

        @Override
        public CancelBidReq[] newArray(int size) {
            return new CancelBidReq[size];
        }
    };
    @Expose
    @SerializedName("authToken")
    public String authToken;
    @Expose
    @SerializedName("bidId")
    public String jobId;


    public CancelBidReq(String authToken, String jobId) {
        this.authToken = authToken;
        this.jobId = jobId;

    }

    protected CancelBidReq(Parcel in) {
        authToken = in.readString();
        jobId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authToken);
        dest.writeString(jobId);
    }
}
