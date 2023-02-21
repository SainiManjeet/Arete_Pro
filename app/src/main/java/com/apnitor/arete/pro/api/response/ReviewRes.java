package com.apnitor.arete.pro.api.response;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReviewRes  implements Parcelable {
    public static final Creator<ReviewRes> CREATOR = new Creator<ReviewRes>() {
        @Override
        public ReviewRes createFromParcel(Parcel in) {
            return new ReviewRes(in);
        }

        @Override
        public ReviewRes[] newArray(int size) {
            return new ReviewRes[size];
        }
    };
    @Expose
    @SerializedName("clientId")
    public String clientId;
    @Expose
    @SerializedName("comment")
    public String comment;
    @Expose
    @SerializedName("name")
    public  String providerName;
    @Expose
    @SerializedName("imageUrl")
    public String imageUrl;

    @Expose
    @SerializedName("rating")
    public String rating;


   /* Added check if it effect somewhere*/
    @Expose
    @SerializedName("favorite")
    public String favorite;



    protected ReviewRes(Parcel in) {
        providerName = in.readString();
        clientId = in.readString();
        comment = in.readString();
        imageUrl = in.readString();
        rating = in.readString();
        favorite = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerName);
        dest.writeString(clientId);
        dest.writeString(comment);
        dest.writeString(imageUrl);
        dest.writeString(rating);
        dest.writeString(favorite);
    }
}
