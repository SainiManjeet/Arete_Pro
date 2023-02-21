package com.apnitor.arete.pro.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseRes<T> {

    @Expose
    @SerializedName("isSuccess")
    public boolean isSuccess;

    @Expose
    @SerializedName("data")
    private T data;


    @Expose
    @SerializedName("errorCode")
    public int errorCode;

    @Expose
    @SerializedName("errorMessage")
    public String errorMessage;

    @Expose
    @SerializedName("cause")
    private String cause;

    public BaseRes(boolean isSuccess, T data, int errorCode, String errorMessage, String cause) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.cause = cause;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
