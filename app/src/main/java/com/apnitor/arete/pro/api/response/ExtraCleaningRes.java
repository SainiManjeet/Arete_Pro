package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraCleaningRes implements Parcelable {
    public static final Creator<ExtraCleaningRes> CREATOR = new Creator<ExtraCleaningRes>() {
        @Override
        public ExtraCleaningRes createFromParcel(Parcel in) {
            return new ExtraCleaningRes(in);
        }

        @Override
        public ExtraCleaningRes[] newArray(int size) {
            return new ExtraCleaningRes[size];
        }
    };
    @Expose
    @SerializedName("name")
    public String name;


    public Integer image;
    public Integer imageSelected;

    public String description;

    public Integer extraTime;
    public boolean isSelected;

    public ExtraCleaningRes() {

    }

    protected ExtraCleaningRes(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            image = null;
        } else {
            image = in.readInt();
        }

        if (in.readByte() == 0) {
            imageSelected = null;
        } else {
            imageSelected = in.readInt();
        }
        description = in.readString();
        if (in.readByte() == 0) {
            extraTime = null;
        } else {
            extraTime = in.readInt();
        }
        isSelected = in.readByte() != 0;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (image == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(image);
        }
        if (imageSelected == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imageSelected);
        }
        dest.writeString(description);
        if (extraTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(extraTime);
        }
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
