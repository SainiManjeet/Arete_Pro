package com.apnitor.arete.pro.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartJobReq implements Parcelable {

    public static final Creator<StartJobReq> CREATOR = new Creator<StartJobReq>() {
        @Override
        public StartJobReq createFromParcel(Parcel in) {
            return new StartJobReq(in);
        }

        @Override
        public StartJobReq[] newArray(int size) {
            return new StartJobReq[size];
        }
    };
    @Expose
    @SerializedName("authToken")
    public String authToken;
    @Expose
    @SerializedName("jobId")
    public String jobId;


    public StartJobReq(String authToken, String jobId) {
        this.authToken = authToken;
        this.jobId = jobId;

    }

    protected StartJobReq(Parcel in) {
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
