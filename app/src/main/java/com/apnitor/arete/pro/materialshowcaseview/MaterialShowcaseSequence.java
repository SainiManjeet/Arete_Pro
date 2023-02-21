package com.apnitor.arete.pro.materialshowcaseview;

import android.app.Activity;
import android.view.View;
import android.widget.ScrollView;

import java.util.LinkedList;
import java.util.Queue;


public class MaterialShowcaseSequence implements IDetachedListener {

    PrefsManager mPrefsManager;
    Queue<MaterialShowcaseView> mShowcaseQueue;
    private boolean mSingleUse = false;
    Activity mActivity;
    private ShowcaseConfig mConfig;
    private int mSequencePosition = 0;

    ScrollView mScrollView;

    private OnSequenceItemShownListener mOnItemShownListener = null;
    private OnSequenceItemDismissedListener mOnItemDismissedListener = null;
    float scale;
    public MaterialShowcaseSequence(Activity activity) {

        scale = activity.getResources().getDisplayMetrics().density;;
        mActivity = activity;
        mShowcaseQueue = new LinkedList<>();
    }

    public MaterialShowcaseSequence(Activity activity, String sequenceID, ScrollView scrollVIew) {
        this(activity);
        this.singleUse(sequenceID);
        mScrollView = scrollVIew;
    }

    public MaterialShowcaseSequence addSequenceItem(View targetView, String content, String dismissText, boolean scroll, int measuredHeight) {
        addSequenceItem(targetView, "", content, dismissText, scroll, measuredHeight);
        return this;
    }

    public MaterialShowcaseSequence addSequenceItem(View targetView, String title, String content, String dismissText, boolean scroll, int measuredHeight) {

        MaterialShowcaseView sequenceItem = new MaterialShowcaseView.Builder(mActivity)
                .setTarget(targetView)
                .setTitleText(title)
                .setDismissText(dismissText)
                .setContentText(content)
                .setSequence(true)
                .setScrollable(scroll)
                .setViewMeasuredHeight(measuredHeight)
                .build();

        if (mConfig != null) {
            sequenceItem.setConfig(mConfig);
        }

        mShowcaseQueue.add(sequenceItem);
        return this;
    }

    public MaterialShowcaseSequence addSequenceItem(MaterialShowcaseView sequenceItem) {

        if (mConfig != null) {
            sequenceItem.setConfig(mConfig);
        }

        mShowcaseQueue.add(sequenceItem);
        return this;
    }

    public MaterialShowcaseSequence singleUse(String sequenceID) {
        mSingleUse = true;
        mPrefsManager = new PrefsManager(mActivity, sequenceID);
        return this;
    }

    public void setOnItemShownListener(OnSequenceItemShownListener listener) {
        this.mOnItemShownListener = listener;
    }

    public void setOnItemDismissedListener(OnSequenceItemDismissedListener listener) {
        this.mOnItemDismissedListener = listener;
    }

    public boolean hasFired() {

        if (mPrefsManager.getSequenceStatus() == PrefsManager.SEQUENCE_FINISHED) {
            return true;
        }

        return false;
    }

    public void start() {

        /**
         * Check if we've already shot our bolt and bail out if so         *
         */
        if (mSingleUse) {
            if (hasFired()) {
                return;
            }

            /**
             * See if we have started this sequence before, if so then skip to the point we reached before
             * instead of showing the user everything from the start
             */
            mSequencePosition = mPrefsManager.getSequenceStatus();

            if (mSequencePosition > 0) {
                for (int i = 0; i < mSequencePosition; i++) {
                    mShowcaseQueue.poll();
                }
            }
        }


        // do start
        if (mShowcaseQueue.size() > 0)
            showNextItem();
    }


    int previousViewHeight=0;
    MaterialShowcaseView previousView;

    int scrollingPostiion=0;

     private void showNextItem() {



         final int size = mShowcaseQueue.size();
         if (size > 0 && !mActivity.isFinishing()) {
            MaterialShowcaseView sequenceItem = mShowcaseQueue.remove();
            sequenceItem.setDetachedListener(this);

            if(previousView!=null){
                previousViewHeight = previousView.getViewMeasuredHeight();
            }
             previousView = sequenceItem;
            // scrollingPostiion += 10*scale;


            if(mScrollView!=null && sequenceItem.isScrollable()) {
                mScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int viewHeight = previousView.getMeasuredHeight();
                        mScrollView.smoothScrollBy(0, previousViewHeight);
                        sequenceItem.show(mActivity);
                    }
                }, 100);

            }else{
                sequenceItem.show(mActivity);
            }

            if (mOnItemShownListener != null) {
                    mOnItemShownListener.onShow(sequenceItem, mSequencePosition);
                }


            previousView =    sequenceItem;

        } else {
            /**
         * We've reached the end of the sequence, save the fired state
         */
            if (mSingleUse) {
                mPrefsManager.setFired();
            }

             if(mScrollView!=null){
                mScrollView.smoothScrollTo(0,0);
             }
        }
    }



    private void skipTutorial() {

        mShowcaseQueue.clear();

        final int size = mShowcaseQueue.size();
        if (size > 0 && !mActivity.isFinishing()) {
            MaterialShowcaseView sequenceItem = mShowcaseQueue.remove();
            sequenceItem.setDetachedListener(this);
            sequenceItem.show(mActivity);
            if (mOnItemShownListener != null) {
                mOnItemShownListener.onShow(sequenceItem, mSequencePosition);
            }

        } else {
            /**
             * We've reached the end of the sequence, save the fired state
             */
            if (mSingleUse) {
                mPrefsManager.setFired();
            }
        }
    }


    @Override
    public void onShowcaseDetached(MaterialShowcaseView showcaseView, boolean wasDismissed, boolean wasSkipped) {

        showcaseView.setDetachedListener(null);

        /**
         * We're only interested if the showcase was purposefully dismissed
         */
        if (wasDismissed) {

            if (mOnItemDismissedListener != null) {
                mOnItemDismissedListener.onDismiss(showcaseView, mSequencePosition);
            }

            /**
             * If so, update the prefsManager so we can potentially resume this sequence in the future
             */
            if (mPrefsManager != null) {
                mSequencePosition++;
                mPrefsManager.setSequenceStatus(mSequencePosition);
            }


            showNextItem();
        }

        if(wasSkipped){
            if (mOnItemDismissedListener != null) {
                mOnItemDismissedListener.onDismiss(showcaseView, mSequencePosition);
            }

            /**
             * If so, update the prefsManager so we can potentially resume this sequence in the future
             */
            if (mPrefsManager != null) {
                mSequencePosition++;
                mPrefsManager.setSequenceStatus(mSequencePosition);
            }

            skipTutorial();
        }
    }

    public void setConfig(ShowcaseConfig config) {
        this.mConfig = config;
    }

    public interface OnSequenceItemShownListener {
        void onShow(MaterialShowcaseView itemView, int position);
    }

    public interface OnSequenceItemDismissedListener {
        void onDismiss(MaterialShowcaseView itemView, int position);
    }

}
