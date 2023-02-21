package com.apnitor.arete.pro.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.response.GetNotificationRes;
import com.apnitor.arete.pro.createjob.NotificationAdapter;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationFragment extends BaseFragment implements ListItemClickCallback {

    private static final String LOG_TAG = "NotificationFragment";
    private RecyclerView mRvRequests;
    public ArrayList<GetNotificationRes> mNotificationList=new ArrayList<>();
    private JobSpecViewModel jobSpecViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_notification, container, false);
        setUpLayout(mView);
        return mView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).removeBadge();
        //
        HomeActivity.mCurrentFragPos = 2;
        //
        if (mNotificationList.size() < 1)
            getNotification(true);
        else
            getNotification(false);

    }

    private void setUpLayout(View view) {
        mRvRequests = view.findViewById(R.id.rvNotification);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mRvRequests.setLayoutManager(mManager);
        showNotificationList();

    }

    private void getNotification(boolean isShowLoader) {
        if (!isShowLoader) {
            ((HomeActivity) getActivity()).isShowProgress(true);
        }
        jobSpecViewModel.getNotification(new ChooseProviderReq(getAuthToken()), isShowLoader);
    }

    private void showNotificationList() {
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        jobSpecViewModel.getNotificationResLiveData()
                .observe(this, res -> {
                    ((HomeActivity) getActivity()).isShowProgress(false);
                    mNotificationList.clear();
                    mNotificationList.addAll(res.providerRes);
                    //
                    NotificationAdapter adapter = new NotificationAdapter(getActivity(), mNotificationList, this);
                    mRvRequests.setAdapter(adapter);
                });
    }

    @Override
    public void onListItemClick(Object object) {
        Intent in = new Intent(getActivity(), HomeActivity.class);
        startActivity(in);
    }

    @Override
    public void onHireClick(Object object) {

    }
}
