package com.apnitor.arete.pro.api.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileReq {


    @Expose
    @SerializedName("imageUrl")
    public String imageUrl;


    @Expose
    @SerializedName("authToken")
    public String authToken;


    @Expose
    @SerializedName("hourlyRate")
    public float hourlyRate;

    @Expose
    @SerializedName("aboutMe")
    public String aboutMe;

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("email")
    public String email;

/*    @Expose
    @SerializedName("address")
    public GetAddress address;*/

    @Expose
    @SerializedName("address")
    public List<GetAddress> address;

    @Expose
    @SerializedName("providerAddress")
    public GetAddress providerAddress;

    @Expose
    @SerializedName("speciality")
    public ArrayList<String> speciality;



   /* @Expose
    @SerializedName("address")
    public String mAddress;*/

    @Expose
    @SerializedName("birthDate")
    public String birthDate;

    @Expose
    @SerializedName("gender")
    public String gender;

    @Expose
    @SerializedName("radius")
    public int radius;

    @Expose
    @SerializedName("zipCode")
    public String zipcode;

    //String type

    @Expose
    @SerializedName("type")
    public String type;

    /*@Expose
    @SerializedName("providerAddress")
    public String providerAddress;*/

    public AvailabilityReq availability;

    public AvailabilityReq availabilityFixed;

    public String fixed;

    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, GetAddress address) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        //  this.address = address;
        this.authToken = authToken;
    }

    public UpdateProfileReq(String authToken, String imageUrl) {
        this.imageUrl = imageUrl;
        this.authToken = authToken;
    }


    public UpdateProfileReq(String authToken, AvailabilityReq availabilityReq) {
        this.imageUrl = imageUrl;
        this.authToken = authToken;
        this.availability = availabilityReq;
    }


    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, String zipcode, int radius, ArrayList<GetAddress> address, GetAddress providerAddress, ArrayList<String> speciality) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.authToken = authToken;
        this.zipcode = zipcode;
        this.radius = radius;
        this.providerAddress = providerAddress;
        this.speciality = speciality;
    }

    // Constructor for client only
    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.authToken = authToken;
    }


    // With Type

    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, String zipcode, int radius, AvailabilityReq availabilityReq,String type
    ) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        //  this.mAddress = address;
        this.authToken = authToken;
        this.zipcode = zipcode;
        this.radius = radius;
        this.availability = availabilityReq;
        this.type=type;
    }


    // Type without address
    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, String zipcode, int radius, AvailabilityReq availabilityReq
           /* ,ArrayList<String> speciality*/) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        //  this.mAddress = address;
        this.authToken = authToken;
        this.zipcode = zipcode;
        this.radius = radius;
        this.availability = availabilityReq;
       //this.speciality=speciality;
    }

    // Availability
    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, String zipcode, int radius, AvailabilityReq availabilityReq, ArrayList<GetAddress> address, GetAddress providerAddress
  /* ,ArrayList<String> speciality */) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.authToken = authToken;
        this.zipcode = zipcode;
        this.radius = radius;
        this.availability = availabilityReq;
        this.address = address;
        this.providerAddress = providerAddress;
      // this.speciality=speciality;
    }

    // Availability Fixed
    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, String zipcode, int radius, AvailabilityReq availabilityFixed, ArrayList<GetAddress> address, GetAddress providerAddress
            ,ArrayList<String> speciality ,String fixed) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.authToken = authToken;
        this.zipcode = zipcode;
        this.radius = radius;
        this.availability = availabilityFixed;
        this.address = address;
        this.providerAddress = providerAddress;
        this.speciality=speciality;
        this.fixed =fixed;
    }

    // Availability with type n with address
    public UpdateProfileReq(String authToken, String imageUrl, String phone,
                            String email, String aboutMe, float hourlyRate, String gender, String birthDate, String zipcode, int radius, AvailabilityReq availabilityReq, ArrayList<GetAddress> address, GetAddress providerAddress,String type
    ) {
        this.imageUrl = imageUrl;
        this.aboutMe = aboutMe;
        this.hourlyRate = hourlyRate;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.authToken = authToken;
        this.zipcode = zipcode;
        this.radius = radius;
        this.availability = availabilityReq;
        this.address = address;
        this.providerAddress = providerAddress;
        this.type=type;
    }



}
