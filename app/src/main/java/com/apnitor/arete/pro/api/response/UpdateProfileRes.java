package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.Availability;

import java.util.List;

public class UpdateProfileRes {

    @Expose
    @SerializedName("authToken")
    public String authToken;

    @Expose
    @SerializedName("userId")
    public String userId;

    @Expose
    @SerializedName("username")
    public String username;

    @Expose
    @SerializedName("firstName")
    public String firstName;

    @Expose
    @SerializedName("lastName")
    public String lastName;

    @Expose
    @SerializedName("email")
    public String email;

    @Expose
    @SerializedName("imageUrl")
    public String profilePhotoUrl;

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("hourlyRate")
    public String hourlyRate;

    @Expose
    @SerializedName("aboutMe")
    public String aboutMe;

    @Expose
    @SerializedName("birthDate")
    public String birthdate;

    @Expose
    @SerializedName("gender")
    public String gender;

    /*@Expose
    @SerializedName("address")
    public GetAddress address; */

    @Expose
    @SerializedName("address")
    public List<GetAddress> address;

    @Expose
    @SerializedName("userTypes")
    public List<String> userTypes;

    @Expose
    @SerializedName("availability")
    public Availability availability;

    @Expose
    @SerializedName("zipCode")
    public String zipcode;

    @Expose
    @SerializedName("radius")
    public int radius;



}
