package com.apnitor.arete.pro.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ProviderJobReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.ReviewActivity;
import com.apnitor.arete.pro.dashboard.ProviderPastJobsAdapter;
import com.apnitor.arete.pro.dashboard.UpcomingJobDetailActivity;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.viewjob.JobDetailActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PastJobsProviderActivity extends BaseActivity implements FindJobClickCallback {
    private static final String LOG_TAG = "PastJobsProvider";
    RecyclerView mRvPastJobs;
    SwipeRefreshLayout mPullToRefresh;
    ProviderPastJobsAdapter mProviderPastJobsAdapter;
    private JobSpecViewModel jobSpecViewModel;
    private ArrayList<GetJobRes> mPastJobsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs_provider);

        setUpToolBar(getResources().getString(R.string.past_jobs));

        setUpLayout();
        //
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPastJobsList.size() < 1) {
            mPullToRefresh.setRefreshing(true);
            getServiceProviderJobs(false);
        } else {
            getServiceProviderJobs(false);
        }
    }

    private void getServiceProviderJobs(boolean isShowLoader) {
        ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Past");
        jobSpecViewModel.getProviderJobs(providerJobReq, isShowLoader);
        jobSpecViewModel.getProviderJobResponse().observe(this, providerJobRes -> {
            if (mPullToRefresh.isRefreshing()) {
                mPullToRefresh.setRefreshing(false);
            }
            mPastJobsList.clear();
            mPastJobsList.addAll(providerJobRes.getJobs());
            //
            Collections.reverse(mPastJobsList);
            //
            mProviderPastJobsAdapter = new ProviderPastJobsAdapter(this, mPastJobsList, this);
            mRvPastJobs.setAdapter(mProviderPastJobsAdapter);
        });
    }

    private void setUpLayout() {
        mRvPastJobs = findViewById(R.id.recyclerView);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRvPastJobs.setLayoutManager(mManager);
        //
        mPullToRefresh = findViewById(R.id.refresh);
        mPullToRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Past");
                jobSpecViewModel.getProviderJobs(providerJobReq, false);
            }
        });
    }

    public String getAuthToken() {
        SharedPreferences mSharedPreferenceHelper = Objects.requireNonNull(this).getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        return mSharedPreferenceHelper.getString("authToken", "");
    }

    @Override
    public void onListItemClick(Object object) {
        GetJobRes res = (GetJobRes) object;
        Intent mIntent = new Intent(this, UpcomingJobDetailActivity.class);
        mIntent.putExtra("jobRes", res);
        startActivity(mIntent);
    }

    @Override
    public void onListItemClickWithPositionClick(Object object, int position) {
        GetJobRes res = (GetJobRes) object;
        // Job Detail
        Intent mIntent = new Intent(PastJobsProviderActivity.this, JobDetailActivity.class);
        mIntent.putExtra("jobRes", res);
        mIntent.putExtra("type", "pastJobs");
        mIntent.putExtra("providerNotes", "");
        String adapPosition = String.valueOf(position);
        mIntent.putExtra("position", adapPosition);
        startActivityForResult(mIntent, RequestCodes.JOB_DETAIL_REQUEST);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
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
    public void onStartJobWithPosition(Object object, int Position) {

    }

    @Override
    public void onCancelBidOnlyClick(Object object) {

    }

    @Override
    public void onMakeBidClick(Object object) {

    }

    @Override
    public void onMakeBidWithPositionClick(Object object, int position) {

    }

    @Override
    public void onCancelBidClick(Object object) {

    }
}
