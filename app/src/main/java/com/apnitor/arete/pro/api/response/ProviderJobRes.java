package com.apnitor.arete.pro.api.response;

import java.util.ArrayList;

public class ProviderJobRes {

    private ArrayList<GetJobRes> jobs;

    public ProviderJobRes(ArrayList<GetJobRes> jobs) {
        this.jobs = jobs;
    }

    public ArrayList<GetJobRes> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<GetJobRes> jobs) {
        this.jobs = jobs;
    }

}
