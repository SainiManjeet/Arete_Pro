package com.apnitor.arete.pro.api.response;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidReq implements Parcelable {
    public static final Creator<BidReq> CREATOR = new Creator<BidReq>() {
        @Override
        public BidReq createFromParcel(Parcel in) {
            return new BidReq(in);
        }

        @Override
        public BidReq[] newArray(int size) {
            return new BidReq[size];
        }
    };
    @Expose
    @SerializedName("bidId")
    public String bidId;
    @Expose
    @SerializedName("bidPrice")
    public String bidPrice;
    @Expose
    @SerializedName("bidStatus")
    public String bidStatus;

    public BidReq() {
    }

    protected BidReq(Parcel in) {
        bidId = in.readString();
        bidPrice = in.readString();
        bidStatus = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bidId);
        dest.writeString(bidPrice);
        dest.writeString(bidStatus);
    }
}
