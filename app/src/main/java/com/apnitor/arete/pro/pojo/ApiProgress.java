package com.apnitor.arete.pro.pojo;

public class ApiProgress {

    private boolean isInProgress;
    private String progressMessage;

    private ApiProgress(boolean isInProgress) {
        this.isInProgress = isInProgress;
    }

    private ApiProgress(boolean isInProgress, String progressMessage) {
        this.isInProgress = isInProgress;
        this.progressMessage = progressMessage;
    }

    public static ApiProgress start() {
        return new ApiProgress(true, "Please wait..");
    }

    public static ApiProgress start(String progressMessage) {
        return new ApiProgress(true, progressMessage);
    }

    public static ApiProgress stop() {
        return new ApiProgress(false);
    }

    public boolean isInProgress() {
        return isInProgress;
    }

 /*   public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }*/

    public String getProgressMessage() {
        return progressMessage;
    }

   /* public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }*/
}
