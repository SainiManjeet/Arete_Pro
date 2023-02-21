package com.apnitor.arete.pro.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.request.ProviderJobReq;
import com.apnitor.arete.pro.api.request.StartJobReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.PostWalkNotesActivity;
import com.apnitor.arete.pro.completeJob.PreWalkNotesActivity;
import com.apnitor.arete.pro.completeJob.ReviewActivity;
import com.apnitor.arete.pro.dashboard.ProviderJobsAdapter;
import com.apnitor.arete.pro.dashboard.UpcomingJobDetailActivity;
import com.apnitor.arete.pro.databinding.FragmentProviderUpcomingBinding;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyJobsProviderFragment extends BaseFragment implements FindJobClickCallback {
    public static final String LOG_TAG = "ProviderUpcoming";
    public static final String BROADCAST_ACTION = "com.apnitor.arete.pro.newassignedjobalert";
    public FragmentProviderUpcomingBinding binding;
    ArrayList<GetJobRes> mUpcomingJobsList = new ArrayList<>();
    boolean isFragmentActive;
    NewAssignedJobAlertABroadcastReceiver myBroadCastReceiver;
    int mStartJobCount = -1;
    private JobSpecViewModel jobSpecViewModel;
    private HomeProviderActivity homeActivity;
    private List<GetJobRes> mGetJobRes;
    private ProviderJobsAdapter adapter;
    private String message = "";
    private int mStartJobPos = -1;
    private int mMakeBidPos = -1;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeActivity = (HomeProviderActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProviderUpcomingBinding.inflate(inflater, container, false);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mManager);
        mGetJobRes = new ArrayList<>();
        myBroadCastReceiver = new NewAssignedJobAlertABroadcastReceiver();
        binding.refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobSpecViewModel.getProviderJobs(new ProviderJobReq(getAuthToken(), "Upcomming"), false);
            }
        });
        binding.cvViewNewJobAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.cvViewNewJobAssigned.setVisibility(View.GONE);
                //
                getServiceProviderJobs(false);
                //
                binding.refresh.setRefreshing(true);
            }
        });
        // API Calling
        getServiceProviderJobs(false);

        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        //
        isFragmentActive = true;
        registerMyReceiver();

    }

   /* private void getServiceProviderJobs(boolean isShowLoader) {
        ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Upcomming");
        //
        jobSpecViewModel.getProviderJobs(providerJobReq, isShowLoader);
        //
        jobSpecViewModel.getProviderJobResponse().observe(this, providerJobRes -> {
            // showToast("Got Service Provider Jobs");
            if (binding.refresh.isRefreshing()) {
                binding.refresh.setRefreshing(false);
            }
            //
            ((HomeProviderActivity) getActivity()).isShowProgress(false);
            binding.cvViewNewJobAssigned.setVisibility(View.GONE);
            //
            mUpcomingJobsList.clear();
            mUpcomingJobsList.addAll(providerJobRes.getJobs());
            //
            Collections.reverse(mUpcomingJobsList);
            //
            adapter = new ProviderJobsAdapter(getActivity(), mUpcomingJobsList, this);
            binding.recyclerView.setAdapter(adapter);

        });
    }*/

    private void getServiceProviderJobs(boolean isShowLoader) {
        ProviderJobReq providerJobReq = new ProviderJobReq(getAuthToken(), "Upcomming");
        //
        jobSpecViewModel.getProviderJobs(providerJobReq, isShowLoader);
        //
        jobSpecViewModel.getProviderJobResponse().observe(this, providerJobRes -> {
            // showToast("Got Service Provider Jobs");
            if (binding.refresh.isRefreshing()) {
                binding.refresh.setRefreshing(false);
            }
            //
            ((HomeProviderActivity) getActivity()).isShowProgress(false);
            binding.cvViewNewJobAssigned.setVisibility(View.GONE);
            //
            mUpcomingJobsList.clear();

            Iterator<GetJobRes> iterator = providerJobRes.getJobs().iterator();
            while (iterator.hasNext()) {
                GetJobRes job = iterator.next();
                if (job.statusOfJob==4) {
                    iterator.remove();
                }
            }
            mUpcomingJobsList.addAll(providerJobRes.getJobs());
            //

            Collections.reverse(mUpcomingJobsList);
            //
            adapter = new ProviderJobsAdapter(getActivity(), mUpcomingJobsList, this);
            binding.recyclerView.setAdapter(adapter);

        });
    }


    @Override
    public void onListItemClick(Object object) {
        GetJobRes res = (GetJobRes) object;
        //  getActivity().finish();//
        Intent mIntent = new Intent(getActivity(), UpcomingJobDetailActivity.class);
        mIntent.putExtra("Type", "viewDetail");

        Log.d(LOG_TAG," jobRes= "+res.toString());
        mIntent.putExtra("jobRes", res);
        startActivity(mIntent);
    }

    @Override
    public void onListItemClickWithPositionClick(Object object, int position) {

    }

    @Override
    public void onMakeBidClick(Object object) { // Accepted MA
        GetJobRes job = (GetJobRes) object;
        Log.d("In Accepted", "Accepted");
        MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), job.jobId, "Accepted");
        jobSpecViewModel.makeBid(mProviderReq);
        //
        mMakeBidPos = mUpcomingJobsList.indexOf(job);
        //
        showToast("Job accepted");
        message = "Job accepted..";
        observeBidResponse(message, job);
    }

    @Override
    public void onMakeBidWithPositionClick(Object object, int position) {

    }

    private void observeBidResponse(String message, GetJobRes job) { // March 12
        Log.d("observeBidResponse", "observeBidResponse");
        jobSpecViewModel.makeBidResLiveData()
                .observe(this, res -> {
                    showToast(message);
                    job.bidPlaced = true;
                    mUpcomingJobsList.get(mMakeBidPos).statusOfJob = 2;
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onCancelBidClick(Object object) { //Reject Button

        //  showToast(getResources().getString(R.string.functionality_pending));

        Log.d("Rejected", "Rejected");
        GetJobRes res = (GetJobRes) object;
        MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), res.jobId, "Rejected");
        jobSpecViewModel.makeBid(mProviderReq);
        message = "Bid rejected successfully";
        showToast(message);

        // TODO : 1) Update status of job (Confirm status from backend)
        // TODO : 2) Remove fragment refresh from here
        adapter.notifyDataSetChanged();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        //observeBidResponse(message, res);
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
    public void onStartJobWithPosition(Object object, int position) {
        mStartJobCount = 1;
        mStartJobPos = position;
        //
        GetJobRes res = (GetJobRes) object;
        if (res.statusOfJob == 3) {
            startPreWalkNotes(res, position);
        } else {
            StartJobReq startJobReq = new StartJobReq(getPrefHelper().getAuthToken(), res.jobId);
            jobSpecViewModel.startJob(startJobReq);
            startJobResponse(message, res);
        }
    }

    // Cancel Job
    @Override
    public void onCancelBidOnlyClick(Object object) {
        GetJobRes job = (GetJobRes) object;
        cancelJob(job);
    }

    private void cancelJob(GetJobRes mJob) {
        String message = "Job Cancelled";
        StartJobReq startJobReq = new StartJobReq(getPrefHelper().getAuthToken(), mJob.jobId);
        jobSpecViewModel.cancelJob(startJobReq);
        cancelJobResponse(mJob, message);
    }

    private void cancelJobResponse(GetJobRes mJob, String message) {
        jobSpecViewModel.cancelJobReqLiveData()
                .observe(this, res -> {
                    showToast(message);
                    mUpcomingJobsList.remove(mJob);
                    adapter.notifyDataSetChanged();
                });
    }

    private void startJobResponse(String message, GetJobRes ress) {
        jobSpecViewModel.startJobReqLiveData()
                .observe(this, res -> {
//                    showToast(message);
                    if (mStartJobCount == 1) {
                        startPreWalkNotes(ress, mStartJobPos);
                        mUpcomingJobsList.get(mStartJobPos).statusOfJob = 3;
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void startPreWalkNotes(GetJobRes ress, int position) {
        Intent mIntent = null;
        if (ress.preWalkNotes != null && !ress.preWalkNotes.isEmpty()) {
            if (ress.postWalkNotes != null && !ress.postWalkNotes.isEmpty()) {
                Intent in = new Intent(getActivity(), ReviewActivity.class);
                in.putExtra("type", "provider");
                in.putExtra("to", ress.clientId);
                in.putExtra("jobId", ress.jobId);
                startActivity(in);
            } else {
                Intent in = new Intent(getActivity(), PostWalkNotesActivity.class);
                in.putExtra("completeJob", ress);
                in.putExtra("preNotes", ress.postWalkNotes);
                in.putExtra("position", position);
                startActivityForResult(in, RequestCodes.REQ_CODE_ADDED_POST_WALK);
            }
        } else {
            mStartJobCount++;
            mIntent = new Intent(getActivity(), PreWalkNotesActivity.class);
            mIntent.putExtra("completeJob", ress);
            mIntent.putExtra("position", position);
            startActivityForResult(mIntent, RequestCodes.REQ_CODE_ADDED_PRE_WALK);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            isFragmentActive = false;
            homeActivity.unregisterReceiver(myBroadCastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerMyReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            homeActivity.registerReceiver(myBroadCastReceiver, intentFilter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Data null here so crashed at LOG
        //Log.d(LOG_TAG, " onActivityResult requestCode" + requestCode + " resultCode " + resultCode + " data " + data.toString());
     // Toast.makeText(homeActivity, "requestCode " + requestCode + " resultCode :" + resultCode, Toast.LENGTH_SHORT).show();
        if (resultCode == RequestCodes.REQ_CODE_ADDED_POST_WALK) {
            if (data != null) {
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    mUpcomingJobsList.get(position).postWalkNotes = data.getStringExtra("post_walk");
                    mUpcomingJobsList.get(position).statusOfJob = 4;
                    adapter.notifyDataSetChanged();

//                        Toast.makeText(homeActivity, "Added PreWalk " + data.getStringExtra("pre_walk"), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            switch (requestCode) {
                case RequestCodes.REQ_CODE_ADDED_PRE_WALK:
                    if (data != null) {
                        int position = data.getIntExtra("position", -1);
                        if (position != -1) {
                            if (resultCode == RequestCodes.REQ_CODE_ADDED_PRE_WALK) {
                                mUpcomingJobsList.get(position).statusOfJob = 4;
                            } else {
                                mUpcomingJobsList.get(position).preWalkNotes = data.getStringExtra("pre_walk");
                            }
                            adapter.notifyDataSetChanged();
//                            Toast.makeText(homeActivity, "Added PreWalk " + data.getStringExtra("pre_walk"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
                case RequestCodes.REQ_CODE_ADDED_REVIEW:

                  //  Toast.makeText(homeActivity, "IN REVIEW " , Toast.LENGTH_SHORT).show();
                    if (data != null) {
                        int position = data.getIntExtra("position", -1);
                        if (position != -1) {
                            mUpcomingJobsList.get(position).statusOfJob = 4;
                           // mUpcomingJobsList.remove(position);// Check n remove
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
            }
        }
    }

    class NewAssignedJobAlertABroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
//                String type = intent.getStringExtra("type");
//                binding.cvViewNewJobAssigned.setVisibility(View.VISIBLE);
//                if (type.equalsIgnoreCase(RequestCodes.JOB_OFFER)) {
//                    binding.txtJobType.setText("Job Offered");
//                } else {
//                    binding.txtJobType.setText("New Job Assigned");
//                }

                Log.e(LOG_TAG, " NewAssignedJobAlertABroadcastReceiver ");

                binding.cvViewNewJobAssigned.performClick();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
