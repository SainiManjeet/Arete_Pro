package com.apnitor.arete.pro.api;

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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JobSpecificationApi {

    @POST("createJob")
    Single<BaseRes<CreateJobRes>> createJob(@Body CreateJobNewReq createJobReq);

    @POST("updateJob")
    Single<BaseRes<CreateJobRes>> updateJob(@Body UpdateJobReq updateJobReq);

    @POST("getProviders")
    Single<BaseRes<GetProviderRes>> getProviders(@Body ChooseProviderReq chooseProviderReq);

    @POST("getMyFavoriteProviders")
    Single<BaseRes<GetProviderRes>> getFavoriteProviders(@Body ChooseProviderReq chooseProviderReq);

    @POST("getAllNotifications")
    Single<BaseRes<GetNotificationRes>> getNotification(@Body ChooseProviderReq chooseProviderReq);

    @POST("getRatingsAndReviews")
    Single<BaseRes<GetMyRatingRes>> getRatingReview(@Body RatingReviewReq ratingReviewReq);

    @POST("getJobs")
    Single<BaseRes<GetJobRes>> getJobs(@Body GetJobReq getJobReq);


/*    @POST("getJobsNew")
    Single<BaseRes<GetJobRes>> getJobs(@Body GetJobReq getJobReq);*/

    // Get My Bids
    @POST("getMyBids")
    Single<BaseRes<GetJobRes>> getMyBids(@Body GetJobReq getJobReq);

    @POST("makeBid")
    Single<BaseRes<MakeBidRes>> makeBid(@Body MakeBidReq makeBidReq);

    @GET("cancelBid")
    Single<BaseRes<GetJobRes>> cancelBid(@Body MakeBidReq makeBidReq);

    @GET("notification")
    Single<BaseRes<List<NotificationRes>>> getNotifications();

    @POST("getClientJobs")
    Single<BaseRes<JobRes>> clientJobs(@Body ClientJobReq clientJobReq);

    @POST("getBids")
    Single<BaseRes<ViewBidsRes>> bidsOnJob(@Body ViewBidsReq viewBidsReq);

    @POST("assignJob")
    Single<BaseRes<AssignJobRes>> assignJob(@Body AssignJobReq assignJobReq);

    @POST("getProviderJobs")
    Single<BaseRes<ProviderJobRes>> providerJobs(@Body ProviderJobReq providerJobReq);

    // Provider rating
    @POST("addRating")
    Single<BaseRes<ProviderRatingReq>> addRating(@Body ProviderRatingReq providerJobReq);

    // Update job
    @POST("updateJob")
    Single<BaseRes<CompleteJReq>> completeJob(@Body CompleteJReq completeJobReq);

    // Start Job
    @POST("startJob")
    Single<BaseRes<StartJobReq>> startJob(@Body StartJobReq StartJob);

    // Payment
    @POST("payForJob")
    Single<BaseRes<StartJobReq>> payForJob(@Body StartJobReq StartJob);

    // Cancel Job
    @POST("cancelJob")
    Single<BaseRes<StartJobReq>> cancelJob(@Body StartJobReq cancelJob);

    // Cancel Bid
    @POST("cancelBid")
    Single<BaseRes<BidReq>> cancelBid(@Body CancelBidReq cancelBid);

    // Update Bid
    @POST("UpdateBid")
    Single<BaseRes<UpdateBidReq>> updateBid(@Body UpdateBidReq updateBidReq);

}
