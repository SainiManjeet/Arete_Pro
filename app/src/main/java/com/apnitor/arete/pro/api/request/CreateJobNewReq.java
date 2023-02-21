package com.apnitor.arete.pro.api.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.GetRooms;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;

import java.util.ArrayList;
import java.util.List;

public class CreateJobNewReq implements Parcelable {

    public static final Creator<CreateJobNewReq> CREATOR = new Creator<CreateJobNewReq>() {
        @Override
        public CreateJobNewReq createFromParcel(Parcel in) {
            return new CreateJobNewReq(in);
        }

        @Override
        public CreateJobNewReq[] newArray(int size) {
            return new CreateJobNewReq[size];
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
    public int bedrooms;

    @Expose
    @SerializedName("bathrooms")
    public int bathrooms;

    @Expose
    @SerializedName("kitchen")
    public int kitchen;

    @Expose
    @SerializedName("others")
    public int others;

    @Expose
    @SerializedName("sqft")
    public double sqft;

    @Expose
    @SerializedName("address")
    public GetAddress address;

    @Expose
    @SerializedName("date")
    public String date;

    @Expose
    @SerializedName("endDate")
    public String endDate;

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

    @Expose
    @SerializedName("numberOfWindows")
    public int numberOfWindows;
    @Expose
    @SerializedName("firstFloorWindows")
    public int firstFloorWindows;
    @Expose
    @SerializedName("secondFloorWindows")
    public int secondFloorWindows;
    @Expose
    @SerializedName("frenchWindows")
    public int frenchWindows;
    @Expose
    @SerializedName("slidingWindows")
    public int slidingWindows;
    @Expose
    @SerializedName("gardenWindows")
    public int gardenWindows;
    @Expose
    @SerializedName("wardrobeMirrors")
    public int wardrobeMirrors;
    @Expose
    @SerializedName("screens")
    public int screens;
    @Expose
    @SerializedName("skylights")
    public int skylights;
    @Expose
    @SerializedName("stories")
    public int stories;


    // Carpet
    @Expose
    @SerializedName("rooms")
    public GetRooms rooms;
    @Expose
    @SerializedName("bath")
    public GetRooms bath;
    @Expose
    @SerializedName("entry")
    public GetRooms entry;
    @Expose
    @SerializedName("stairCase")
    public GetRooms stairCase;

    public CreateJobNewReq() {
    }


    public CreateJobNewReq(String authToken, String cleaningType, int bedrooms, int bathrooms, int kitchen, int others, Double sqft, GetAddress address, String date, String endDate, String time
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
        this.endDate = endDate;
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

    // Future Date
    public CreateJobNewReq(String authToken, String cleaningType, int bedrooms, int bathrooms, int kitchen, int others, Double sqft, GetAddress address, String date, String time
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

    // Window
    public CreateJobNewReq(String authToken, String cleaningType
            , GetAddress address, String date, String time
            , String howOften, String when, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                           ArrayList<ExtraCleaningRes> extraCleaning, float estTime, String estPrice, String price, String providerId, String description, String jobType, int firstFloorWindows,
                           int secondFloorWindows, int frenchWindows, int slidingWindows, int gardenWindows, int wardrobeMirrors, int screens, int skylights, int stories) {
        this.authToken = authToken;
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
        this.firstFloorWindows = firstFloorWindows;
        this.secondFloorWindows = secondFloorWindows;
        this.frenchWindows = frenchWindows;
        this.slidingWindows = slidingWindows;
        this.gardenWindows = gardenWindows;
        this.wardrobeMirrors = wardrobeMirrors;
        this.screens = screens;
        this.skylights = skylights;
        this.stories = stories;
    }

    // Window Future Date
    public CreateJobNewReq(String authToken, String cleaningType
            , GetAddress address, String date, String endDate, String time
            , String howOften, String when, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                           ArrayList<ExtraCleaningRes> extraCleaning, float estTime, String estPrice, String price, String providerId, String description, String jobType, int firstFloorWindows,
                           int secondFloorWindows, int frenchWindows, int slidingWindows, int gardenWindows, int wardrobeMirrors, int screens, int skylights, int stories) {
        this.authToken = authToken;
        this.address = address;
        this.date = date;
        this.endDate = endDate;
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
        this.firstFloorWindows = firstFloorWindows;
        this.secondFloorWindows = secondFloorWindows;
        this.frenchWindows = frenchWindows;
        this.slidingWindows = slidingWindows;
        this.gardenWindows = gardenWindows;
        this.wardrobeMirrors = wardrobeMirrors;
        this.screens = screens;
        this.skylights = skylights;
        this.stories = stories;
    }

    // Carpet
    public CreateJobNewReq(String authToken, String cleaningType, GetAddress address, String date, String time
            , String howOften, String when, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                           ArrayList<ExtraCleaningRes> extraCleaning, float estTime, String estPrice, String price, String providerId, String description, String jobType,
                           GetRooms rooms, GetRooms bath, GetRooms entry, GetRooms stairCase) {

        this.authToken = authToken;
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
        this.rooms = rooms;
        this.bath = bath;
        this.entry = entry;
        this.stairCase = stairCase;
    }

    // Carpet future Date
    public CreateJobNewReq(String authToken, String cleaningType, GetAddress address, String date, String endDate, String time
            , String howOften, String when, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                           ArrayList<ExtraCleaningRes> extraCleaning, float estTime, String estPrice, String price, String providerId, String description, String jobType,
                           GetRooms rooms, GetRooms bath, GetRooms entry, GetRooms stairCase) {

        this.authToken = authToken;
        this.address = address;
        this.date = date;
        this.endDate = endDate;
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
        this.rooms = rooms;
        this.bath = bath;
        this.entry = entry;
        this.stairCase = stairCase;
    }


    protected CreateJobNewReq(Parcel in) {
        authToken = in.readString();
        providerId = in.readString();
        name = in.readString();
        bedrooms = in.readInt();
        bathrooms = in.readInt();
        kitchen = in.readInt();
        others = in.readInt();
        sqft = in.readDouble();
        address = in.readParcelable(GetAddress.class.getClassLoader());
        date = in.readString();
        endDate = in.readString();
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
        firstFloorWindows = in.readInt();
        secondFloorWindows = in.readInt();
        frenchWindows = in.readInt();
        slidingWindows = in.readInt();
        gardenWindows = in.readInt();
        wardrobeMirrors = in.readInt();
        screens = in.readInt();
        skylights = in.readInt();
        stories = in.readInt();
        rooms = in.readParcelable(GetRooms.class.getClassLoader());
        bath = in.readParcelable(GetRooms.class.getClassLoader());
        entry = in.readParcelable(GetRooms.class.getClassLoader());
        stairCase = in.readParcelable(GetRooms.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authToken);
        dest.writeString(providerId);
        dest.writeString(name);
        dest.writeInt(bedrooms);
        dest.writeInt(bathrooms);
        dest.writeInt(kitchen);
        dest.writeInt(others);
        dest.writeDouble(sqft);
        dest.writeParcelable(address, flags);
        dest.writeString(date);
        dest.writeString(endDate);
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
        //
        dest.writeInt(firstFloorWindows);
        dest.writeInt(secondFloorWindows);
        dest.writeInt(frenchWindows);
        dest.writeInt(slidingWindows);
        dest.writeInt(gardenWindows);
        dest.writeInt(wardrobeMirrors);
        dest.writeInt(screens);
        dest.writeInt(skylights);
        dest.writeInt(stories);
        //
        dest.writeParcelable(rooms, flags);
        dest.writeParcelable(bath, flags);
        dest.writeParcelable(entry, flags);
        dest.writeParcelable(stairCase, flags);
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
