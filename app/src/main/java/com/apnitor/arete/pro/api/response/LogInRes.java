package com.apnitor.arete.pro.api.response;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.AvailabilityDays;

import java.util.ArrayList;
import java.util.List;

public class LogInRes {

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
    public Float hourlyRate;

    @Expose
    @SerializedName("aboutMe")
    public String aboutMe;

    @Expose
    @SerializedName("birthDate")
    public String birthdate;

    @Expose
    @SerializedName("gender")
    public String gender;

    @Expose
    @SerializedName("countryName")
    public String countryName;

    @Expose
    @SerializedName("state")
    public String state;

    @Expose
    @SerializedName("city")
    public String city;

    @Expose
    @SerializedName("address")
    public ArrayList<GetAddress> address;


    @Expose
    @SerializedName("providerAddress")
    public GetAddress providerAddress;

    @Expose
    @SerializedName("availability")
    public AvailabilityDays availability;
    /**
     * TODO
     * <p>
     * User type should not be array
     */


    @Expose
    @SerializedName("userTypes")
    public List<String> userTypes;
    @Expose
    @SerializedName("speciality")
    public List<String> speciality;
    @Expose
    @SerializedName("zipCode")
    public String zipcode;
    @Expose
    @SerializedName("radius")
    public int radius;
    @Expose
    @SerializedName("notificationCount")
    public int notificationCount;


    public LogInRes(String authToken, String userId, String username, String firstName, String lastName,
                    String email, String profilePhotoUrl,
                    String phone, Float hourlyRate, String aboutMe, String birthdate, String gender,
                    String countryName, String state, String city, ArrayList<GetAddress> address,
                    List<String> userTypes, String zipcode, int radius, AvailabilityDays availability) {
        this.authToken = authToken;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhotoUrl = profilePhotoUrl;
        this.phone = phone;
        this.hourlyRate = hourlyRate;
        this.aboutMe = aboutMe;
        this.birthdate = birthdate;
        this.gender = gender;
        this.countryName = countryName;
        this.state = state;
        this.city = city;
        this.address = address;
        this.userTypes = userTypes;
        this.zipcode = zipcode;
        this.radius = radius;
        this.availability = availability;
    }

    public LogInRes(String authToken, String userId, String username, String firstName, String lastName,
                    String email, String profilePhotoUrl,
                    String phone, Float hourlyRate, String aboutMe, String birthdate, String gender,
                    String countryName, String state, String city, ArrayList<GetAddress> address,
                    List<String> userTypes, String zipcode, int radius, GetAddress providerAddress) {
        this.authToken = authToken;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhotoUrl = profilePhotoUrl;
        this.phone = phone;
        this.hourlyRate = hourlyRate;
        this.aboutMe = aboutMe;
        this.birthdate = birthdate;
        this.gender = gender;
        this.countryName = countryName;
        this.state = state;
        this.city = city;
        this.address = address;
        this.userTypes = userTypes;
        this.zipcode = zipcode;
        this.radius = radius;
        this.providerAddress = providerAddress;

    }

    public LogInRes(String authToken, String userId, String username, String firstName, String lastName,
                    String email, String profilePhotoUrl,
                    String phone, Float hourlyRate, String aboutMe, String birthdate, String gender,
                    String countryName, String state, String city,
                    List<String> userTypes, String zipcode, int radius, List<String> speciality, String providerAddress, String address, String availability) {
        this.authToken = authToken;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhotoUrl = profilePhotoUrl;
        this.phone = phone;
        this.hourlyRate = hourlyRate;
        this.aboutMe = aboutMe;
        this.birthdate = birthdate;
        this.gender = gender;
        this.countryName = countryName;
        this.state = state;
        this.city = city;
        if (!address.isEmpty()) {
            ArrayList<GetAddress> mList = new ArrayList<>();
            GetAddress getAddress = new Gson().fromJson(address, GetAddress.class);
            mList.add(getAddress);
            this.address = mList;
        }
        if (!providerAddress.isEmpty())
            this.providerAddress = new Gson().fromJson(providerAddress, GetAddress.class);
        this.userTypes = userTypes;
        this.zipcode = zipcode;
        this.radius = radius;
        // this.availability = availability;

        if (!availability.isEmpty()) {
            this.availability = new Gson().fromJson(availability, AvailabilityDays.class);
        }
        this.speciality = speciality;
    }

    public LogInRes(String authToken, String userId, String username, String firstName,
                    String lastName, String email, String profilePhotoUrl, String phone,
                    String countryName, String state, String city, List<String> userTypes, Float hourlyRate, String aboutMe, String gender, String birthdate) {
        this.authToken = authToken;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePhotoUrl = profilePhotoUrl;
        this.phone = phone;
        this.countryName = countryName;
        this.state = state;
        this.city = city;
        this.hourlyRate = hourlyRate;
        this.aboutMe = aboutMe;
        this.gender = gender;
        this.birthdate = birthdate;
        this.userTypes = userTypes;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public AvailabilityDays getAvailability() {
        return availability;
    }

    public void setAvailability(AvailabilityDays availability) {
        this.availability = availability;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ArrayList<GetAddress> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<GetAddress> address) {
        this.address = address;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(List<String> userTypes) {
        this.userTypes = userTypes;
    }

    //

    public List<String> getSpeciality() {
        return speciality;
    }

    public void setspeciality(List<String> speciality) {
        this.speciality = speciality;
    }


    public GetAddress getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(GetAddress providerAddress) {
        this.providerAddress = providerAddress;
    }
}
