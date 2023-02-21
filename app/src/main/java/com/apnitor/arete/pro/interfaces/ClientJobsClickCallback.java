package com.apnitor.arete.pro.interfaces;

public interface ClientJobsClickCallback extends RateAndPayCallback {

    void onViewBids(Object object);
    void onEditJobClick(Object object);
    void onEditJobClickWithPosition(Object object ,int position) ;
    void onCancelJobClick(Object object);

}
