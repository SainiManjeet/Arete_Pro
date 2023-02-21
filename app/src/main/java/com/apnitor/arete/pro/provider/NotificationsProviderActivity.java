package com.apnitor.arete.pro.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.request.ProviderJobReq;
import com.apnitor.arete.pro.api.response.GetNotificationRes;
import com.apnitor.arete.pro.createjob.NotificationAdapter;
import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NotificationsProviderActivity extends BaseActivity implements ListItemClickCallback {
    public ArrayList<GetNotificationRes> mNotificationList = new ArrayList<>();
    RecyclerView mRvNotifications;
    SwipeRefreshLayout mPullToRefresh;
    NotificationAdapter adapter;
    private JobSpecViewModel jobSpecViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_jobs_provider);
        //
        setUpToolBar("Notifications");
        //
        setUpLayout();
        //
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        new HomeActivity().removeBadge();
        mPullToRefresh.setRefreshing(true);
        getNotification(false);
    }


    private void setUpLayout() {
        mRvNotifications = findViewById(R.id.recyclerView);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mRvNotifications.setLayoutManager(mManager);
        //
        mPullToRefresh = findViewById(R.id.refresh);
        mPullToRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        mPullToRefresh.setOnRefreshListener(() -> {
            ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Past");
            jobSpecViewModel.getProviderJobs(providerJobReq, false);
        });
    }

    public String getAuthToken() {
        SharedPreferences mSharedPreferenceHelper = Objects.requireNonNull(this).getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        return mSharedPreferenceHelper.getString("authToken", "");
    }

    private void getNotification(boolean isShowLoader) {

        jobSpecViewModel.getNotification(new ChooseProviderReq(getAuthToken()), isShowLoader);
        showNotificationList();
    }

    private void showNotificationList() {
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        jobSpecViewModel.getNotificationResLiveData()
                .observe(this, res -> {
                    mPullToRefresh.setRefreshing(false);
                    //
                    mNotificationList.clear();
                    mNotificationList.addAll(res.providerRes);
                    //
                    adapter = new NotificationAdapter(this, mNotificationList, this);
                    mRvNotifications.setAdapter(adapter);
                });
    }

    @Override
    public void onListItemClick(Object object) {
//        if (getPrefHelper().getUserType().equalsIgnoreCase("serviceProvider")) {
//            Intent in = new Intent(NotificationsProviderActivity.this, HomeProviderActivity.class);
//            startActivity(in);
//        } else {
//            Intent in = new Intent(NotificationsProviderActivity.this, HomeActivity.class);
//            startActivity(in);
//        }
        finish();
    }

    @Override
    public void onHireClick(Object object) {

    }
}
