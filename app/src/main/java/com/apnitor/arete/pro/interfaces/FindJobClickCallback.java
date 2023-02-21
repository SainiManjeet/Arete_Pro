package com.apnitor.arete.pro.interfaces;

public interface FindJobClickCallback {
    void onListItemClick(Object object);


    void onListItemClickWithPositionClick(Object object,int position);

    void onMakeBidClick(Object object);

    void onMakeBidWithPositionClick(Object object,int position);

    void onCancelBidClick(Object object); // Accept/Reject

    void onRatingClick(Object object);

//    void onStartJob(Object object);

    void onStartJobWithPosition(Object object ,int Position);

    void onCancelBidOnlyClick(Object object); // Cancel Bid


    /*void onCancelJobWithPosition(Object object,int position);*/

}
