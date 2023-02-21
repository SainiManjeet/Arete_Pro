package com.apnitor.arete.pro.repository;

import android.app.Application;

import com.apnitor.arete.pro.api.ApiService;
import com.apnitor.arete.pro.api.JobSpecificationApi;
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
import com.apnitor.arete.pro.api.response.BaseRes;
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

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class JobSpecRepository {

    private JobSpecificationApi jobSpecificationApi;

    public JobSpecRepository(Application application) {
        jobSpecificationApi = ApiService.getJobSpecificationApi(application);
    }

    public Single<BaseRes<CreateJobRes>> createJob(CreateJobNewReq createJobReq) {
        return jobSpecificationApi.createJob(createJobReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<CreateJobRes>> updateJob(UpdateJobReq updateJobReq) {
        return jobSpecificationApi.updateJob(updateJobReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<GetProviderRes>> getProviders(ChooseProviderReq chooseProviderReq) {
        return jobSpecificationApi.getProviders(chooseProviderReq)
                .subscribeOn(Schedulers.io());
    }

    /* Get Favorite provider*/
    public Single<BaseRes<GetProviderRes>> getFavoriteProviders(ChooseProviderReq chooseProviderReq) {
        return jobSpecificationApi.getFavoriteProviders(chooseProviderReq)
                .subscribeOn(Schedulers.io());
    }


    /* Get Notification*/
    public Single<BaseRes<GetNotificationRes>> getNotification(ChooseProviderReq chooseProviderReq) {
        return jobSpecificationApi.getNotification(chooseProviderReq)
                .subscribeOn(Schedulers.io());
    }



    /* Get rating review*/
    /*public Single<BaseRes<GetProviderRes>> getRatingReview(RatingReviewReq ratingReviewReq) {
        return jobSpecificationApi.getRatingReview(ratingReviewReq)
                .subscribeOn(Schedulers.io());
    }
*/

    public Single<BaseRes<GetMyRatingRes>> getRatingReview(RatingReviewReq ratingReviewReq) {
        return jobSpecificationApi.getRatingReview(ratingReviewReq)
                .subscribeOn(Schedulers.io());
    }


    public Single<BaseRes<GetJobRes>> getJobs(GetJobReq getJobReq) {
        return jobSpecificationApi.getJobs(getJobReq)
                .subscribeOn(Schedulers.io());
    }
   // Get My Bids

    public Single<BaseRes<GetJobRes>> getMyBids(GetJobReq getJobReq) {
        return jobSpecificationApi.getMyBids(getJobReq)
                .subscribeOn(Schedulers.io());
    }


    public Single<BaseRes<MakeBidRes>> makeBid(MakeBidReq makeBidReq) {
        return jobSpecificationApi.makeBid(makeBidReq)
                .subscribeOn(Schedulers.io());
    }

    // Update Bid
    public Single<BaseRes<UpdateBidReq>> updateBid(UpdateBidReq makeBidReq) {
        return jobSpecificationApi.updateBid(makeBidReq)
                .subscribeOn(Schedulers.io());
    }

    /**
     * TODO
     * <p>
     * Why it's name is cancelBid
     * <p>
     * As per my understanding this is Make Bid
     *
     * @param makeBidReq
     * @return
     */

    public Single<BaseRes<GetJobRes>> cancelBid(MakeBidReq makeBidReq) {
        return jobSpecificationApi.cancelBid(makeBidReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<List<NotificationRes>>> getNotifications() {
        return jobSpecificationApi.getNotifications()
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<JobRes>> clientJobs(ClientJobReq clientJobReq) {
        return jobSpecificationApi.clientJobs(clientJobReq)
                .subscribeOn(Schedulers.io());
    }


    // Manjeet view Bids ************
    public Single<BaseRes<ViewBidsRes>> bidsOnJOb(ViewBidsReq viewBidsReq) {
        return jobSpecificationApi.bidsOnJob(viewBidsReq)
                .subscribeOn(Schedulers.io());
    }

    public Single<BaseRes<AssignJobRes>> assignJob(AssignJobReq assignJobReq) {
        return jobSpecificationApi.assignJob(assignJobReq)
                .subscribeOn(Schedulers.io());
    }


    public Single<BaseRes<ProviderJobRes>> providerJobs(ProviderJobReq providerJobReq) {
        return jobSpecificationApi.providerJobs(providerJobReq)
                .subscribeOn(Schedulers.io());
    }

    // Rating
    public Single<BaseRes<ProviderRatingReq>> providerRating(ProviderRatingReq providerJobReq) {
        return jobSpecificationApi.addRating(providerJobReq)
                .subscribeOn(Schedulers.io());
    }

    // Complete Job
    public Single<BaseRes<CompleteJReq>> completeJob(CompleteJReq createJobReq) {
        return jobSpecificationApi.completeJob(createJobReq)
                .subscribeOn(Schedulers.io());
    }

    // Start Job
    public Single<BaseRes<StartJobReq>> startJob(StartJobReq StartJob) {
        return jobSpecificationApi.startJob(StartJob)
                .subscribeOn(Schedulers.io());
    }

    // Start Job
    public Single<BaseRes<StartJobReq>> payForJob(StartJobReq StartJob) {
        return jobSpecificationApi.payForJob(StartJob)
                .subscribeOn(Schedulers.io());
    }

    // Cancel Job
    public Single<BaseRes<StartJobReq>> cancelJob(StartJobReq cancelJob) {
        return jobSpecificationApi.cancelJob(cancelJob)
                .subscribeOn(Schedulers.io());
    }

    // Cancel Bid
    public Single<BaseRes<BidReq>> cancelBid(CancelBidReq cancelBid) {
        return jobSpecificationApi.cancelBid(cancelBid)
                .subscribeOn(Schedulers.io());
    }


}

