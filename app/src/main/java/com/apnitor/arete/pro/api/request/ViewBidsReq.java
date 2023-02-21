package com.apnitor.arete.pro.api.request;

public class ViewBidsReq {

    private String authToken;
    private String jobId;


    public ViewBidsReq(String authToken, String jobId) {
        this.authToken = authToken;
        this.jobId = jobId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
