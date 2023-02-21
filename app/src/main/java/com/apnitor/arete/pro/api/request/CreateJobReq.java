package com.apnitor.arete.pro.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;

import java.util.ArrayList;
import java.util.List;

public class CreateJobReq implements Parcelable {

    public static final Creator<CreateJobReq> CREATOR = new Creator<CreateJobReq>() {
        @Override
        public CreateJobReq createFromParcel(Parcel in) {
            return new CreateJobReq(in);
        }

        @Override
        public CreateJobReq[] newArray(int size) {
            return new CreateJobReq[size];
        }
    };
    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("providerId")
    public String providerId;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("bedrooms")
    public Integer bedrooms;

    @Expose
    @SerializedName("bathrooms")
    public Integer bathrooms;

    @Expose
    @SerializedName("kitchen")
    public Integer kitchen;

    @Expose
    @SerializedName("others")
    public Integer others;

    @Expose
    @SerializedName("sqft")
    public Double sqft;

    @Expose
    @SerializedName("address")
    public List<GetAddress> address;

    /*@Expose
    @SerializedName("address")
    public GetAddress address;*/

    @Expose
    @SerializedName("date")
    public String date;

    @Expose
    @SerializedName("time")
    public String time;
    @Expose
    @SerializedName("cleaningType")
    public String cleaningType;

    @Expose
    @SerializedName("howOften")
    public String howOften;

    @Expose
    @SerializedName("when")
    public String when;

    @Expose
    @SerializedName("specialNotesForProvider")
    public String specialNotesForProvider;


    @Expose
    @SerializedName("images")
    public ArrayList<ImageUrlReq> images;
    @Expose
    @SerializedName("extraCleaning")
    public List<ExtraCleaningRes> extraCleaning;

    @Expose
    @SerializedName("clientPrice")
    public String price;

    /*@Expose
    @SerializedName("estTime")
    public String estTime;*/

    @Expose
    @SerializedName("estTime")
    public float estTime;

    @Expose
    @SerializedName("estPrice")
    public String estPrice;

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("jobType")
    public String jobType;

    // Prewalk Notes
 /*   @Expose
    @SerializedName("preWalkNotes")
    public String preWalkNotes;
*/
    public CreateJobReq() {
    }


    public CreateJobReq(String authToken, String cleaningType, Integer bedrooms, Integer bathrooms, Integer kitchen, Integer others, Double sqft, ArrayList<GetAddress> address, String date, String time
            , String howOften, String when, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                        ArrayList<ExtraCleaningRes> extraCleaning, float estTime, String estPrice, String price, String providerId, String description, String jobType) {
        this.authToken = authToken;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.kitchen = kitchen;
        this.others = others;
        this.sqft = sqft;
        this.address = address;
        this.date = date;
        this.time = time;
        this.howOften = howOften;
        this.when = when;
        this.specialNotesForProvider = specialNotesForProvider;
        this.images = images;
        this.extraCleaning = extraCleaning;
        this.estTime = estTime;
        this.price = price;
        this.providerId = providerId;
        this.description = description;
        this.jobType = jobType;
        this.cleaningType = cleaningType;
        this.estPrice = estPrice;

    }


    protected CreateJobReq(Parcel in) {
        authToken = in.readString();
        providerId = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            bedrooms = null;
        } else {
            bedrooms = in.readInt();
        }
        if (in.readByte() == 0) {
            bathrooms = null;
        } else {
            bathrooms = in.readInt();
        }
        if (in.readByte() == 0) {
            kitchen = null;
        } else {
            kitchen = in.readInt();
        }
        if (in.readByte() == 0) {
            others = null;
        } else {
            others = in.readInt();
        }
        if (in.readByte() == 0) {
            sqft = null;
        } else {
            sqft = in.readDouble();
        }
        address = in.createTypedArrayList(GetAddress.CREATOR);
        date = in.readString();
        time = in.readString();
        cleaningType = in.readString();
        howOften = in.readString();
        when = in.readString();
        specialNotesForProvider = in.readString();
        images = in.createTypedArrayList(ImageUrlReq.CREATOR);
        extraCleaning = in.createTypedArrayList(ExtraCleaningRes.CREATOR);
        price = in.readString();
        estTime = in.readFloat();
        estPrice = in.readString();
        description = in.readString();
        jobType = in.readString();
        // preWalkNotes = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authToken);
        dest.writeString(providerId);
        dest.writeString(name);
        if (bedrooms == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bedrooms);
        }
        if (bathrooms == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(bathrooms);
        }
        if (kitchen == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(kitchen);
        }
        if (others == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(others);
        }
        if (sqft == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(sqft);
        }
        dest.writeTypedList(address);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(cleaningType);
        dest.writeString(howOften);
        dest.writeString(when);
        dest.writeString(specialNotesForProvider);
        dest.writeTypedList(images);
        dest.writeTypedList(extraCleaning);
        dest.writeString(price);
        dest.writeFloat(estTime);
        dest.writeString(estPrice);
        dest.writeString(description);
        dest.writeString(jobType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "CreateJobReq{" +
                "authToken='" + authToken + '\'' +
                ", providerId='" + providerId + '\'' +
                ", name='" + name + '\'' +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", kitchen=" + kitchen +
                ", others=" + others +
                ", sqft=" + sqft +
                ", address=" + address +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", cleaningType='" + cleaningType + '\'' +
                ", howOften='" + howOften + '\'' +
                ", when='" + when + '\'' +
                ", specialNotesForProvider='" + specialNotesForProvider + '\'' +
                ", images=" + images +
                ", extraCleaning=" + extraCleaning +
                ", price='" + price + '\'' +
                ", estTime=" + estTime +
                ", estPrice='" + estPrice + '\'' +
                ", description='" + description + '\'' +
                ", jobType='" + jobType + '\'' +
                '}';
    }
}
