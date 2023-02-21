package com.apnitor.arete.pro.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.CancelBidReq;
import com.apnitor.arete.pro.api.request.GetJobReq;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.ReviewActivity;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.viewjob.AdapMyBids;
import com.apnitor.arete.pro.viewjob.PlaceBidActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MyBidsProviderActivity extends BaseActivity implements FindJobClickCallback, Observer {
    public ArrayList<GetJobRes> mAllBidsList = new ArrayList<>();
    RecyclerView mRvMyBids;
    SwipeRefreshLayout mPullToRefresh;
    GetJobRes mGetJobRes;
    String LOG_TAG = "MyBidsProviderActivity";
    myBidBroadcastReceiver broadcastReceiver;
    private JobSpecViewModel jobSpecViewModel;
    private boolean loadmore;
    private AdapMyBids adapter;
    private String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs_provider);

      //  Log.e(LOG_TAG," onCreate"+" MyBidsProviderActivity");

        broadcastReceiver = new myBidBroadcastReceiver();
        //
        setUpToolBar("My Bids");
        //
        setUpLayout();
        //
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAllBidsList.size() < 1) {
            mPullToRefresh.setRefreshing(true);
            getMyBidsApiCall(false);
        } else
            getMyBidsApiCall(false);

    }

    private void getMyBidsApiCall(boolean isShowLoader) {
        jobSpecViewModel.getMyBid(new GetJobReq(getAuthToken()), isShowLoader);
        jobSpecViewModel.getMyBidResLiveData()
                .observe(this, res -> {
                    if (mPullToRefresh.isRefreshing()) {
                        mPullToRefresh.setRefreshing(false);
                    }
                    loadmore = true;
                    mAllBidsList.clear();

                    Iterator<GetJobRes> iterator = res.jobs.iterator();
                    while (iterator.hasNext()) {
                        GetJobRes job = iterator.next();
                        if (job.bid.bidStatus.equalsIgnoreCase("Cancelled")||job.jobType.equalsIgnoreCase("Fixed Price")||job.jobType.equalsIgnoreCase(getResources().getString(R.string.service_provider))) {
                            iterator.remove();
                        }
                    }
                    mAllBidsList.addAll(res.jobs);
                    Collections.reverse(mAllBidsList);
                    if (res.jobs.size() > 0) {
                        adapter = new AdapMyBids(this, mAllBidsList, this);
                        mRvMyBids.setAdapter(adapter);
                    }
                });
    }

    private void setUpLayout() {
        mRvMyBids = findViewById(R.id.recyclerView);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRvMyBids.setLayoutManager(mManager);
        //
        mPullToRefresh = findViewById(R.id.refresh);
        mPullToRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobSpecViewModel.getMyBid(new GetJobReq(getAuthToken()), false);
            }
        });
    }

    public String getAuthToken() {
        SharedPreferences mSharedPreferenceHelper = Objects.requireNonNull(this).getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        return mSharedPreferenceHelper.getString("authToken", "");
    }

    @Override
    public void onListItemClick(Object object) {
        Log.d(LOG_TAG, "onListItemWithPositionClick Object is " + object.toString());
        GetJobRes res = (GetJobRes) object;
        Intent mIntent = new Intent(this, PlaceBidActivity.class);
        mIntent.putExtra("type", "modifyBid");
        mIntent.putExtra("bidId", res.bid.bidId);
        mIntent.putExtra("jobRes", res);
        startActivity(mIntent);
    }

    @Override
    public void onListItemClickWithPositionClick(Object object, int position) {

    }

    @Override
    public void onMakeBidClick(Object object) {
        mGetJobRes = (GetJobRes) object;
        if (mGetJobRes.jobType.equals("Bid")) {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("jobRes", mGetJobRes);
            //startActivity(PlaceBidActivity.class,mBundle);

            Intent intent = new Intent(this, PlaceBidActivity.class);
            intent.putExtras(mBundle);
            intent.putExtra("type", "bidPlace"); // M 18
            startActivityForResult(intent, RequestCodes.PLACE_BID_REQUEST);

        } else if (mGetJobRes.jobType.equals("Fixed Price")) {
            MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), mGetJobRes.jobId);
            jobSpecViewModel.makeBid(mProviderReq);
            message = "Bid Placed Successfully..";
            observeBidResponse(message, mGetJobRes);
        } else {
            //MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(),res.jobId,"Accept");
            MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), mGetJobRes.jobId, "Accepted");
            jobSpecViewModel.makeBid(mProviderReq);
            showToast("Job accepted.");
            message = "Job accepted.";

            observeBidResponse(message, mGetJobRes);
        }


    }

    @Override
    public void onMakeBidWithPositionClick(Object object, int position) {

    }


    private void observeBidResponse(String message, GetJobRes job) {
        jobSpecViewModel.makeBidResLiveData()
                .observe(this, res -> {
                    showToast(message);
                    job.bidPlaced = true;
                    // Added to change bid Status
                    job.bid.bidStatus = "Active";
                    // Added to refresh adapter
                    job.statusOfJob = 2;
                    adapter.notifyDataSetChanged();

                });
    }

    @Override
    public void onRatingClick(Object object) {
        GetJobRes res = (GetJobRes) object;
        Intent mIntent = new Intent(this, ReviewActivity.class);
        mIntent.putExtra("type", "provider");
        mIntent.putExtra("to", res.clientId);
        mIntent.putExtra("jobId", res.jobId);
        startActivity(mIntent);
    }

    @Override
    public void onCancelBidClick(Object object) {
        GetJobRes res = (GetJobRes) object;
        MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), res.jobId, "Rejected");
        jobSpecViewModel.makeBid(mProviderReq);
    }



    @Override
    public void onStartJobWithPosition(Object object, int Position) {

    }

    @Override
    public void onCancelBidOnlyClick(Object object) {
        GetJobRes job = (GetJobRes) object;
        cancelBid(job);
        job.bid.bidStatus = "Cancelled";
    }

    private void cancelBid(GetJobRes mJob) {
        CancelBidReq cancelBidReq = new CancelBidReq(getPrefHelper().getAuthToken(), mJob.bid.bidId);
        jobSpecViewModel.cancelBid(cancelBidReq);
        jobSpecViewModel.cancelBidResLiveData()
                .observe(this, res -> {
                    showToast("Bid Cancelled successfully.");
                    mAllBidsList.remove(mJob);
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public class myBidBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String type = intent.getStringExtra("type");
                Log.v(LOG_TAG," onReceive type="+type);

                if (type.equalsIgnoreCase(RequestCodes.JOB_UPDATED)) {
                    if (mAllBidsList.size() < 1) {
                        Log.v(LOG_TAG," onReceive ="+mAllBidsList.size());
                        getMyBidsApiCall(false);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
