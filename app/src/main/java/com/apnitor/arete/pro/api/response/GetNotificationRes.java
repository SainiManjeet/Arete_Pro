package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationRes implements Parcelable {

    public static final Creator<GetNotificationRes> CREATOR = new Creator<GetNotificationRes>() {
        @Override
        public GetNotificationRes createFromParcel(Parcel in) {
            return new GetNotificationRes(in);
        }

        @Override
        public GetNotificationRes[] newArray(int size) {
            return new GetNotificationRes[size];
        }
    };
    @Expose
    @SerializedName("isDeletedNotification")
    public String isDeletedNotification;
    @Expose
    @SerializedName("createdOn")
    public String createdOn;
    @Expose
    @SerializedName("_id")
    public String _id;

    @SerializedName("type")
    public String type;

    @SerializedName("title")
    public String title;

    @SerializedName("message")
    public String message;

    @SerializedName("from")
    public String from;
    @SerializedName("id")
    public String id;



   @Expose
    @SerializedName("notificationList")
    public ArrayList<GetNotificationRes> providerRes;



    public GetNotificationRes() {
    }


    public GetNotificationRes(
            ArrayList<ReviewRes> providerReviews) {


    }

    protected GetNotificationRes(Parcel in) {
        isDeletedNotification = in.readString();
        createdOn = in.readString();
        _id = in.readString();
        type = in.readString();
        title = in.readString();
        message = in.readString();
        from = in.readString();
        id = in.readString();



    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(isDeletedNotification);
        dest.writeString(createdOn);
        dest.writeString(_id);
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(from);
        dest.writeString(id);

    }

    @Override
    public int describeContents() {
        return 0;
    }


}
