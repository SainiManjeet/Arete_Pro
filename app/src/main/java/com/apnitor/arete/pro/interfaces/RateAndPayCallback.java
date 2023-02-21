package com.apnitor.arete.pro.interfaces;

public interface RateAndPayCallback {
    void onRatingClick(Object object);
    void onPayJobClick(Object object);
    void onPayJobClickWithPosition(Object object,int position);
}
