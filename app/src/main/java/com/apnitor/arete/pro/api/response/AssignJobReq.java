package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssignJobReq {


   @Expose
   @SerializedName("authToken")
   private String authToken;

   @Expose
   @SerializedName("jobId")
   private String jobId;

   @Expose
   @SerializedName("providerId")
   private String providerId;

    public AssignJobReq(String authToken, String jobId, String providerId) {
        this.authToken = authToken;
        this.jobId = jobId;
        this.providerId = providerId;
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

   public String getProviderId() {
        return providerId;
   }

   public void setProviderId(String providerId) {
        this.providerId = providerId;
   }
}
