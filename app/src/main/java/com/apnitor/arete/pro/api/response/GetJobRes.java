package com.apnitor.arete.pro.api.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.AssignedTo;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.GetRooms;
import com.apnitor.arete.pro.api.request.CreateJobNewReq;
import com.apnitor.arete.pro.api.request.ImageUrlReq;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class GetJobRes implements Parcelable {

    @Expose
    @SerializedName("id")
    public String clientId;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("bedrooms")
    public int bedrooms;

    @Expose
    @SerializedName("kitchen")
    public int kitchen;

    @Expose
    @SerializedName("bathrooms")
    public int bathrooms;

    @Expose
    @SerializedName("others")
    public int others;

    @Expose
    @SerializedName("sqft")
    public double sqft;

   /* @Expose
    @SerializedName("address")
    public List<GetAddress> address;*/

    @Expose
    @SerializedName("address")
    public GetAddress address;


    @Expose
    @SerializedName("assignedTo")
    public AssignedTo assignedTo;

    // Bid Detail
    @Expose
    @SerializedName("bid")
    public BidReq bid;


    @Expose
    @SerializedName("date")
    public String date;

    @Expose
    @SerializedName("endDate")
    public String endDate;


    @Expose
    @SerializedName("cleaningType")
    public String cleaningType;


    @Expose
    @SerializedName("time")
    public String time;

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

    @Expose
    @SerializedName("jobType")
    public String jobType;

    @Expose
    @SerializedName("jobId")
    public String jobId;

    @Expose
    @SerializedName("bidAvailable")
    public boolean bidAvailable;


    @Expose
    @SerializedName("statusOfJob")
    public int statusOfJob;


    @Expose
    @SerializedName("estPrice")
    public String estPrice;

    @Expose
    @SerializedName("estTime")
    public double estTime;

    @Expose
    @SerializedName("jobs")
    public ArrayList<GetJobRes> jobs;

    @Expose
    @SerializedName("alreadyBidPlaced")
    public boolean bidPlaced;

    // newly added
    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("priceType")
    public String priceType;


    @Expose
    @SerializedName("providerId")
    public String providerId;


    @Expose
    @SerializedName("description")
    public String description;

    // Bid Price
    @Expose
    @SerializedName("bidPrice")
    public String bidPrice;

    @Expose
    @SerializedName("ratingGiven")
    public boolean ratingGiven;

    @Expose
    @SerializedName("preWalkNotes")
    public String preWalkNotes;

    @Expose
    @SerializedName("postWalkNotes")
    public String postWalkNotes;

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



    public long timeInMillisSec;

    public GetJobRes(String authToken, String jobId, String cleaningType, int bedrooms, int bathrooms, int kitchen, int others, Double sqft, String date,String endDate, String time
            , String howOften, String when, String priceType, String specialNotesForProvider, ArrayList<ImageUrlReq> images,
                     ArrayList<ExtraCleaningRes> extraCleaning, double estTime, String estPrice, String price, String providerId, String description, String jobType,
                     int firstFloorWindows, int secondFloorWindows, int frenchWindows, int slidingWindows, int gardenWindows, int wardrobeMirrors, int screens, int skylights, int stories,
                     GetRooms rooms, GetRooms bath, GetRooms entry, GetRooms stairCase) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.priceType = priceType;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.kitchen = kitchen;
        this.others = others;
        this.sqft = sqft;
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

        //
        this.firstFloorWindows = firstFloorWindows;
        this.secondFloorWindows = secondFloorWindows;
        this.frenchWindows = frenchWindows;
        this.slidingWindows = slidingWindows;
        this.gardenWindows = gardenWindows;
        this.wardrobeMirrors = wardrobeMirrors;
        this.screens = screens;
        this.skylights = skylights;
        this.stories = stories;
        this.firstFloorWindows = firstFloorWindows;

        this.rooms = rooms;
        this.bath = bath;
        this.entry = entry;
        this.stairCase = stairCase;
    }

    public GetJobRes(CreateJobNewReq jobReq, String jobId) {

        this.jobId = jobId;
        this.authToken = jobReq.authToken;
        this.bedrooms = jobReq.bedrooms;
        this.bathrooms = jobReq.bathrooms;
        this.kitchen = jobReq.kitchen;
        this.others = jobReq.others;
        this.sqft = jobReq.sqft;
       /* ArrayList<GetAddress> addressArrayList = new ArrayList<GetAddress>();
        addressArrayList.add(jobReq.address);
        this.address = addressArrayList;*/

        this.address = jobReq.address;
        this.date = jobReq.date;
        this.endDate = jobReq.endDate;
        this.time = jobReq.time;
        this.howOften = jobReq.howOften;
        this.when = jobReq.when;
        this.specialNotesForProvider = jobReq.specialNotesForProvider;
        this.images = jobReq.images;
        this.extraCleaning = jobReq.extraCleaning;
        //this.estTime = Double.parseDouble(jobReq.estTime);

        this.estTime = jobReq.estTime;
        this.price = jobReq.price;
        this.providerId = jobReq.providerId;
        this.description = jobReq.description;
        this.jobType = jobReq.jobType;
        this.cleaningType = jobReq.cleaningType;
        this.estPrice = jobReq.estPrice;
        //
        this.firstFloorWindows = jobReq.firstFloorWindows;
        this.secondFloorWindows = jobReq.secondFloorWindows;
        this.frenchWindows = jobReq.frenchWindows;
        this.slidingWindows = jobReq.slidingWindows;
        this.gardenWindows = jobReq.gardenWindows;
        this.wardrobeMirrors = jobReq.wardrobeMirrors;
        this.wardrobeMirrors = jobReq.wardrobeMirrors;
        this.screens = jobReq.screens;
        this.skylights = jobReq.skylights;
        this.stories = jobReq.stories;

        this.rooms = jobReq.rooms;
        this.bath = jobReq.bath;
        this.entry = jobReq.entry;
        this.stairCase = jobReq.stairCase;

    }


    protected GetJobRes(Parcel in) {
        clientId = in.readString();
        name = in.readString();
        priceType = in.readString();
        providerId = in.readString();
        description = in.readString();
        authToken = in.readString();
        // ratingGiven=in.readString();//
        bidPrice = in.readString();// Newly Added
        bedrooms = in.readInt();
        bathrooms = in.readInt();
        others = in.readInt();
        sqft = in.readDouble();
        //address = in.createTypedArrayList(GetAddress.CREATOR);
        address = in.readParcelable(GetAddress.class.getClassLoader());
        date = in.readString();
        endDate = in.readString();
        cleaningType = in.readString();
        time = in.readString();
        howOften = in.readString();
        when = in.readString();
        specialNotesForProvider = in.readString();
        images = in.createTypedArrayList(ImageUrlReq.CREATOR);
        extraCleaning = in.createTypedArrayList(ExtraCleaningRes.CREATOR);
        price = in.readString();
        jobType = in.readString();
        jobId = in.readString();
        preWalkNotes = in.readString();
        postWalkNotes = in.readString();
        bidAvailable = in.readByte() != 0;
        statusOfJob = in.readInt();
        estPrice = in.readString();
        estTime = in.readDouble();
        jobs = in.createTypedArrayList(GetJobRes.CREATOR);
        bidPlaced = in.readByte() != 0;
        ratingGiven = in.readByte() != 0;//
        bid = in.readParcelable(BidReq.class.getClassLoader());
        assignedTo = in.readParcelable(AssignedTo.class.getClassLoader());
        firstFloorWindows = in.readInt();
        secondFloorWindows = in.readInt();
        frenchWindows = in.readInt();
        slidingWindows = in.readInt();
        gardenWindows = in.readInt();
        wardrobeMirrors = in.readInt();
        screens = in.readInt();
        skylights = in.readInt();
        stories = in.readInt();
        rooms = in.readParcelable(GetAddress.class.getClassLoader());
        bath = in.readParcelable(GetAddress.class.getClassLoader());
        entry = in.readParcelable(GetAddress.class.getClassLoader());
        stairCase = in.readParcelable(GetAddress.class.getClassLoader());
    }

    public GetJobRes() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientId);
        dest.writeString(name);
        dest.writeString(priceType);
        dest.writeString(providerId);
        dest.writeString(description);
        dest.writeString(authToken);
        dest.writeString(bidPrice);// New
        //  dest.writeString(ratingGiven);
        dest.writeInt(bedrooms);
        dest.writeInt(bathrooms);
        dest.writeInt(others);
        dest.writeDouble(sqft);
        //dest.writeTypedList(address);
        dest.writeParcelable(address, flags);
        dest.writeString(date);
        dest.writeString(endDate);
        dest.writeString(cleaningType);
        dest.writeString(time);
        dest.writeString(howOften);
        dest.writeString(when);
        dest.writeString(specialNotesForProvider);
        dest.writeTypedList(images);
        dest.writeTypedList(extraCleaning);
        dest.writeString(price);
        dest.writeString(jobType);
        dest.writeString(jobId);
        dest.writeString(preWalkNotes);
        dest.writeString(postWalkNotes);
        dest.writeByte((byte) (bidAvailable ? 1 : 0));
        dest.writeInt(statusOfJob);
        dest.writeString(estPrice);
        dest.writeDouble(estTime);
        dest.writeTypedList(jobs);
        dest.writeByte((byte) (bidPlaced ? 1 : 0));
        dest.writeByte((byte) (ratingGiven ? 1 : 0));
        dest.writeParcelable(bid, flags);
        dest.writeParcelable(assignedTo, flags);
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
        dest.writeParcelable(rooms, flags);
        dest.writeParcelable(bath, flags);
        dest.writeParcelable(entry, flags);
        dest.writeParcelable(stairCase, flags);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetJobRes> CREATOR = new Creator<GetJobRes>() {
        @Override
        public GetJobRes createFromParcel(Parcel in) {
            return new GetJobRes(in);
        }

        @Override
        public GetJobRes[] newArray(int size) {
            return new GetJobRes[size];
        }
    };


    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof GetJobRes)) {
            return false;
        }

        GetJobRes jobRes = (GetJobRes) obj;

        if (this.jobId.equalsIgnoreCase(jobRes.jobId))
            return true;

        return false;
    }


    @Override
    public int hashCode() {
        return jobId.hashCode();
    }

    @Override
    public String toString() {
        return "GetJobRes{" +
                "clientId='" + clientId + '\'' +
                ", name='" + name + '\'' +
                ", bedrooms=" + bedrooms +
                ", kitchen=" + kitchen +
                ", bathrooms=" + bathrooms +
                ", others=" + others +
                ", sqft=" + sqft +
                ", address=" + address +
                ", assignedTo=" + assignedTo +
                ", bid=" + bid +
                ", date='" + date + '\'' +
                ", cleaningType='" + cleaningType + '\'' +
                ", time='" + time + '\'' +
                ", howOften='" + howOften + '\'' +
                ", when='" + when + '\'' +
                ", specialNotesForProvider='" + specialNotesForProvider + '\'' +
                ", images=" + images +
                ", extraCleaning=" + extraCleaning +
                ", price='" + price + '\'' +
                ", jobType='" + jobType + '\'' +
                ", jobId='" + jobId + '\'' +
                ", bidAvailable=" + bidAvailable +
                ", statusOfJob=" + statusOfJob +
                ", estPrice='" + estPrice + '\'' +
                ", estTime=" + estTime +
                ", jobs=" + jobs +
                ", bidPlaced=" + bidPlaced +
                ", authToken='" + authToken + '\'' +
                ", priceType='" + priceType + '\'' +
                ", providerId='" + providerId + '\'' +
                ", description='" + description + '\'' +
                ", bidPrice='" + bidPrice + '\'' +
                ", ratingGiven=" + ratingGiven +
                ", preWalkNotes='" + preWalkNotes + '\'' +
                ", postWalkNotes='" + postWalkNotes + '\'' +
                '}';
    }
}
