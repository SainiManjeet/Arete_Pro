package com.apnitor.arete.pro.api.request;

public class ProviderRatingReq extends RatingReq{
    public ProviderRatingReq(String authToken, String to, float rating, String review, String jobId, Boolean favorite) {
        super(authToken, to,rating,review,jobId,favorite);
    }
}

