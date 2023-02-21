package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JobRes  {

    @Expose
    @SerializedName("jobs")
    ArrayList<GetJobRes> jobs;


    public ArrayList<GetJobRes> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<GetJobRes> jobs) {
        this.jobs = jobs;
    }


    public JobRes(ArrayList<GetJobRes> jobs) {
        this.jobs = jobs;
    }
}
