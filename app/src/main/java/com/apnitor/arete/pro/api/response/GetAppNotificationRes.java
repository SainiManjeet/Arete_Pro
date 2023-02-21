/*
package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAppNotificationRes implements Parcelable {

    public static final Creator<GetAppNotificationRes> CREATOR = new Creator<GetAppNotificationRes>() {
        @Override
        public GetAppNotificationRes createFromParcel(Parcel in) {
            return new GetAppNotificationRes(in);
        }

        @Override
        public GetAppNotificationRes[] newArray(int size) {
            return new GetAppNotificationRes[size];
        }
    };



   @Expose
    @SerializedName("notificationList")
    public ArrayList<GetAppNotificationRes> providerRes;



    public GetAppNotificationRes() {
    }


    public GetAppNotificationRes(
            ArrayList<ReviewRes> providerReviews) {


    }

    protected GetAppNotificationRes(Parcel in) {
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
*/
