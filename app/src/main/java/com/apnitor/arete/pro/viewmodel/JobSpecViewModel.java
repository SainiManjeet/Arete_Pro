package com.apnitor.arete.pro.viewmodel;

import android.app.Application;

import com.apnitor.arete.pro.api.request.CancelBidReq;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.request.ClientJobReq;
import com.apnitor.arete.pro.api.request.CompleteJReq;
import com.apnitor.arete.pro.api.request.CreateJobNewReq;
import com.apnitor.arete.pro.api.request.GetJobReq;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.request.ProviderJobReq;
import com.apnitor.arete.pro.api.request.ProviderRatingReq;
import com.apnitor.arete.pro.api.request.RatingReviewReq;
import com.apnitor.arete.pro.api.request.StartJobReq;
import com.apnitor.arete.pro.api.request.UpdateBidReq;
import com.apnitor.arete.pro.api.request.UpdateJobReq;
import com.apnitor.arete.pro.api.request.ViewBidsReq;
import com.apnitor.arete.pro.api.request.ViewBidsRes;
import com.apnitor.arete.pro.api.response.AssignJobReq;
import com.apnitor.arete.pro.api.response.AssignJobRes;
import com.apnitor.arete.pro.api.response.BidReq;
import com.apnitor.arete.pro.api.response.CreateJobRes;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.api.response.GetMyRatingRes;
import com.apnitor.arete.pro.api.response.GetNotificationRes;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.api.response.JobRes;
import com.apnitor.arete.pro.api.response.MakeBidRes;
import com.apnitor.arete.pro.api.response.NotificationRes;
import com.apnitor.arete.pro.api.response.ProviderJobRes;
import com.apnitor.arete.pro.repository.JobSpecRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class JobSpecViewModel extends BaseViewModel {
    private JobSpecRepository jobSpecRepository;
    private MutableLiveData<GetProviderRes> getProvidersList = new MutableLiveData<>();

    private MutableLiveData<GetMyRatingRes> getMyRatingList = new MutableLiveData<>(); //
    private MutableLiveData<GetNotificationRes> getNotificationList = new MutableLiveData<>();

    private MutableLiveData<CreateJobRes> createJobLiveData = new MutableLiveData<>();
    private MutableLiveData<GetJobRes> getJobsList = new MutableLiveData<>();
    private MutableLiveData<MakeBidRes> makeBid = new MutableLiveData<>();
    private MutableLiveData<UpdateBidReq> updateBid = new MutableLiveData<>();

    private MutableLiveData<BidReq> cancelBid = new MutableLiveData<>();

    private MutableLiveData<List<NotificationRes>> notificationList = new MutableLiveData<>();


    private MutableLiveData<CompleteJReq> completeJobLiveData = new MutableLiveData<>();

    private MutableLiveData<StartJobReq> startJob = new MutableLiveData<>();
    private MutableLiveData<StartJobReq> payJob = new MutableLiveData<>();
    private MutableLiveData<StartJobReq> cancelJob = new MutableLiveData<>();
    private MutableLiveData<CancelBidReq> cancelBidReq = new MutableLiveData<>();

    /**
     * Get Client Jobs
     */
    private MutableLiveData<JobRes> clientJobs = new MutableLiveData<>();

    /**
     * Get Bids on a job
     */


    //private MutableLiveData<ViewBidsRes> bids = new MutableLiveData<>();


    // Manjeet view Bids
    private MutableLiveData<ViewBidsRes> bids = new MutableLiveData<>();

    /**
     * Assign job to a Service Provider
     */
    private MutableLiveData<AssignJobRes> assignJobRes = new MutableLiveData<>();


    /**
     * Get Provider jobs
     */

    private MutableLiveData<ProviderJobRes> providerJobs = new MutableLiveData<>();


    /**
     * Add Rating
     */

    private MutableLiveData<ProviderRatingReq> providerRating = new MutableLiveData<>();


    public JobSpecViewModel(@NonNull Application application) {
        super(application);
        jobSpecRepository = new JobSpecRepository(application);
    }

    public LiveData<CreateJobRes> createJobLiveData() {
        return createJobLiveData;
    }

    public void createJob(CreateJobNewReq createJobReq) {
        consumeApi(jobSpecRepository.createJob(createJobReq), data -> createJobLiveData.setValue(data));
    }

    public void updateJob(UpdateJobReq updateJobReq) {
        consumeApi(jobSpecRepository.updateJob(updateJobReq), data -> createJobLiveData.setValue(data));
    }

    public LiveData<GetProviderRes> getProvidersResLiveData() {
        return getProvidersList;
    }

    /*My Rating*/

    public LiveData<GetMyRatingRes> getReviewResLiveData() {
        return getMyRatingList;
    }

    // getNotification
    public LiveData<GetNotificationRes> getNotificationResLiveData() {
        return getNotificationList;
    }

    public void getProviders(ChooseProviderReq chooseProviderReq) {
        consumeApi(jobSpecRepository.getProviders(chooseProviderReq), data -> getProvidersList.setValue(data));
    }

    /* Get Favorite Provider*/
    public void getMyFavoriteProviders(ChooseProviderReq chooseProviderReq, boolean isShowLoader) {
        if (isShowLoader)
            consumeApi(jobSpecRepository.getFavoriteProviders(chooseProviderReq), data -> getProvidersList.setValue(data));
        else
            consumeApiNoLoader(jobSpecRepository.getFavoriteProviders(chooseProviderReq), data -> getProvidersList.setValue(data));
    }

    /* Get Notification*/
    public void getNotification(ChooseProviderReq chooseProviderReq, boolean isShowLoader) {
        if (isShowLoader)
            consumeApi(jobSpecRepository.getNotification(chooseProviderReq), data -> getNotificationList.setValue(data));
        else
            consumeApiNoLoader(jobSpecRepository.getNotification(chooseProviderReq), data -> getNotificationList.setValue(data));
    }

    /* Get rating review*/
    public void getMyRatingReview(RatingReviewReq ratingReviewReq) {
        consumeApi(jobSpecRepository.getRatingReview(ratingReviewReq), data -> getMyRatingList.setValue(data));
    }

    public LiveData<GetJobRes> getJobsResLiveData() {
        return getJobsList;
    }
    // Get My Bids

    public LiveData<GetJobRes> getMyBidResLiveData() {
        return getJobsList;
    }

    public void getJobs(GetJobReq getJobReq, boolean isShowLoader) {
        if (isShowLoader)
            consumeApi(jobSpecRepository.getJobs(getJobReq), data -> getJobsList.setValue(data));
        else
            consumeApiNoLoader(jobSpecRepository.getJobs(getJobReq), data -> getJobsList.setValue(data));
    }

    // Get Bid
    public void getMyBid(GetJobReq getJobReq, boolean isShowLoader) {
        if (isShowLoader)
            consumeApi(jobSpecRepository.getMyBids(getJobReq), data -> getJobsList.setValue(data));
        else
            consumeApiNoLoader(jobSpecRepository.getMyBids(getJobReq), data -> getJobsList.setValue(data));
    }

    public LiveData<MakeBidRes> makeBidResLiveData() {
        return makeBid;
    }

    public LiveData<UpdateBidReq> updateBidResLiveData() {
        return updateBid;
    }

    public void makeBid(MakeBidReq makeBidReq) {

        consumeApi(jobSpecRepository.makeBid(makeBidReq), data -> makeBid.setValue(data));
    }

    // Update bid
    public void updateBid(UpdateBidReq updateBidReq) {

        consumeApi(jobSpecRepository.updateBid(updateBidReq), data -> updateBid.setValue(data));
    }


    public LiveData<StartJobReq> startJobReqLiveData() {
        return startJob;
    }

    // Pay job
    public LiveData<StartJobReq> payJobReqLiveData() {
        return payJob;
    }

    // Cancel Job
    public LiveData<StartJobReq> cancelJobReqLiveData() {
        return cancelJob;
    }

    /**
     * Cancel Bid Response
     */

    public LiveData<BidReq> cancelBidResLiveData() {
        return cancelBid;
    }


    /**
     * TODO
     * <p>
     * This method seems to be making Bid
     **/

    public void cancelBid(String clientId, String providerId) {
        MakeBidReq mProviderReq = new MakeBidReq(clientId, providerId);
        consumeApi(jobSpecRepository.cancelBid(mProviderReq), data -> getJobsList.setValue(data));
    }

    public LiveData<List<NotificationRes>> notificationResListLiveData() {
        return notificationList;
    }

    public void getNotifications() {
        consumeApi(jobSpecRepository.getNotifications(), data -> notificationList.setValue(data));
    }


    /**
     * @return Job Created by Client
     */

    public MutableLiveData<JobRes> getClientJobs() {
        return clientJobs;
    }

    // View Jobs

    public MutableLiveData<ViewBidsRes> getBids() {
        return bids;
    }


    /**
     * API call to get job created by Client
     *
     * @param clientJobReq
     */
    public void clientJobs(ClientJobReq clientJobReq, boolean isShowLoader) {
        if (isShowLoader)
            consumeApi(jobSpecRepository.clientJobs(clientJobReq), data -> clientJobs.setValue(data));
        else
            consumeApiNoLoader(jobSpecRepository.clientJobs(clientJobReq), data -> clientJobs.setValue(data));
    }

    // Manjeet   view jobs
    public void viewJobs(ViewBidsReq viewBidsReq) {
        consumeApi(jobSpecRepository.bidsOnJOb(viewBidsReq), data -> bids.setValue(data));
    }


    /**
     * @return Bids placed on a Job
     */

    // public MutableLiveData<ViewBidsRes> getBids(){


    // View Bids ****************
    public LiveData<ViewBidsRes> getBidsOnJobs() {
        return bids;
    }


    /**
     * API call to get bids placed on a Job
     *
     * @param viewBidsReq
     */

    public void getBidsOnJob(ViewBidsReq viewBidsReq) {
        consumeApi(jobSpecRepository.bidsOnJOb(viewBidsReq), data -> bids.setValue(data));
    }

    // View Bids ***************

    public void viewbidsOnJob(ViewBidsReq viewBidsReq) {
        consumeApi(jobSpecRepository.bidsOnJOb(viewBidsReq), data -> bids.setValue(data));
    }


    /**
     * @return Assigned job response
     */

    public MutableLiveData<AssignJobRes> getAssignJobResponse() {
        return assignJobRes;
    }

    /**
     * API call to assign job to a Service Provider
     *
     * @param assignJobReq
     */
    public void assignJob(AssignJobReq assignJobReq) {
        consumeApi(jobSpecRepository.assignJob(assignJobReq), data -> assignJobRes.setValue(data));
    }


    /**
     * @return Jobs assigned to a Provider
     */

    public MutableLiveData<ProviderJobRes> getProviderJobResponse() {
        return providerJobs;
    }


    /**
     * API call to get jobs assigned to a Service Provider
     *
     * @param providerJobReq
     */

    public void getProviderJobs(ProviderJobReq providerJobReq, boolean isShowLoader) {
        if (isShowLoader)
            consumeApi(jobSpecRepository.providerJobs(providerJobReq), data -> providerJobs.setValue(data));
        else
            consumeApiNoLoader(jobSpecRepository.providerJobs(providerJobReq), data -> providerJobs.setValue(data));
    }


    // Provider rating
    public void providerRating(ProviderRatingReq providerReq) {
        consumeApi(jobSpecRepository.providerRating(providerReq), data -> providerRating.setValue(data));
    }

    public MutableLiveData<ProviderRatingReq> getAddRAtingResponse() {
        return providerRating;
    }

    // Complete Job
    public LiveData<CompleteJReq> completeJobLiveData() {
        return completeJobLiveData;
    }

    public void completeJob(CompleteJReq completeJobReq) {
        consumeApi(jobSpecRepository.completeJob(completeJobReq), data -> completeJobLiveData.setValue(data));
    }

    public void startJob(StartJobReq startJobReq) {
        consumeApi(jobSpecRepository.startJob(startJobReq), data -> startJob.setValue(data));
    }

    public void payForJob(StartJobReq startJobReq) {
        consumeApi(jobSpecRepository.payForJob(startJobReq), data -> payJob.setValue(data));
    }

    // Cancel Job
    public void cancelJob(StartJobReq startJobReq) {
        consumeApi(jobSpecRepository.cancelJob(startJobReq), data -> cancelJob.setValue(data));
    }

    // Cancel Bid
    public void cancelBid(CancelBidReq cancelReq) {
        consumeApi(jobSpecRepository.cancelBid(cancelReq), data -> cancelBid.setValue(data));
    }


}
