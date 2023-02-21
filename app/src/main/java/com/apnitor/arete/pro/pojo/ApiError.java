package com.apnitor.arete.pro.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiError {

    private Throwable error;
    @Expose
    @SerializedName("errorMessage")
    private String errorMessage;
    private int errorCode;

    private ApiError(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    private ApiError(Throwable error, String errorMessage, int errorCode) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static ApiError create(String errorMessage, int errorCode) {
        return new ApiError(errorMessage, errorCode);
    }

    public static ApiError create(Throwable error, String errorMessage, int errorCode) {
        return new ApiError(error, errorMessage, errorCode);
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /*   public void setErrorMessage(String errorMessage) {
           this.errorMessage = errorMessage;
       }
       }*/
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;


    }
}