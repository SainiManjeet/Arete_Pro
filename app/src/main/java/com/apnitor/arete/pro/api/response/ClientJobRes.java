package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientJobRes {

   /* public ClientJobRes() {
    }*/

    /*@Expose
    @SerializedName("bedrooms")
    public Integer bedrooms;

    @Expose
    @SerializedName("bathrooms")
    public Integer bathrooms;
*/
    @Expose
    @SerializedName("jobId")
    public String jobId;

    @Expose
    @SerializedName("when")
    public String when;


   /* public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public ClientJobRes(Integer bedrooms, Integer bathrooms, String jobId, String when) {
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.jobId = jobId;
        this.when = when;
    }*/

    public ClientJobRes(String jobId, String when) {
        this.jobId = jobId;
        this.when = when;
    }
}
