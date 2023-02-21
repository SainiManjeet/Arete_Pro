package com.apnitor.arete.pro.dashboard;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ProviderJobReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.ReviewActivity;
import com.apnitor.arete.pro.databinding.FragmentProviderPastBinding;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProviderPastFragment extends BaseFragment implements FindJobClickCallback {
    public FragmentProviderPastBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    private HomeActivity homeActivity;
    private List<GetJobRes> mGetJobRes;
    private ProviderPastJobsAdapter adapter;
    ArrayList<GetJobRes> mPastJobsList = new ArrayList<>();

    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeActivity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProviderPastBinding.inflate(inflater, container, false);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mManager);
        mGetJobRes = new ArrayList<>();

        binding.refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Past");
                jobSpecViewModel.getProviderJobs(providerJobReq, false);
            }
        });
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPastJobsList.size() < 1) {
            getServiceProviderJobs(true);
        } else {
            getServiceProviderJobs(false);
        }
    }

    private void getServiceProviderJobs(boolean isShowLoader) {
        ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Past");
        jobSpecViewModel.getProviderJobs(providerJobReq, isShowLoader);
        jobSpecViewModel.getProviderJobResponse().observe(this, providerJobRes -> {
            if (binding.refresh.isRefreshing()) {
                binding.refresh.setRefreshing(false);
            }
            mPastJobsList.clear();
            mPastJobsList.addAll(providerJobRes.getJobs());
            //
//            showToast("Got Service Provider Past Jobs*");
            //
            adapter = new ProviderPastJobsAdapter(getActivity(), mPastJobsList, this);
            binding.recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onListItemClick(Object object) {
        GetJobRes res = (GetJobRes) object;
        Intent mIntent = new Intent(getActivity(), UpcomingJobDetailActivity.class);
        mIntent.putExtra("jobRes", res);
        startActivity(mIntent);
    }

    @Override
    public void onListItemClickWithPositionClick(Object object, int position) {

    }


    @Override
    public void onRatingClick(Object object) {

        GetJobRes res = (GetJobRes) object;
        Intent mIntent = new Intent(getActivity(), ReviewActivity.class);
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
