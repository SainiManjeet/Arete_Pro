package com.apnitor.arete.pro.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ClientJobReq;
import com.apnitor.arete.pro.api.request.StartJobReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.PaymentActivity;
import com.apnitor.arete.pro.completeJob.ReviewActivity;
import com.apnitor.arete.pro.createjob.EditJobActivity;
import com.apnitor.arete.pro.createjob.HireServiceProviderActivity;
import com.apnitor.arete.pro.createjob.RequestJobsAdapter;
import com.apnitor.arete.pro.interfaces.ClientJobsClickCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ClientRequestsFragment extends BaseFragment implements ClientJobsClickCallback, Observer {
    public static ClientRequestsFragment jobsFragment;
    static String LOG_TAG = "ClientRequestsFragment";
    private static RequestJobsAdapter mReqJobsAdapter;
    RelativeLayout mRlParent;
    RequestBidBroadcastReceiver broadcastReceiver;
    private RecyclerView mRvRequests;
    private TextView message;
    private HomeActivity activity;
    private int mVari = 0;
    private TextView appIconTextView;
    private TextView plusIconTextView;
    private JobSpecViewModel jobSpecViewModel;
    private ImageView imgNoJobFound;
    private SwipeRefreshLayout refresh;

    public static void update() {
        //  Data need to be update here:  Best approach update data from Broadcast Receiver
        Log.e(LOG_TAG, " inside update()");
        /*jobsFragment = new ClientRequestsFragment();*/
        /*jobsFragment.update();*/
        //mReqJobsAdapter.notify();
        // mReqJobsAdapter.updateFragments();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_request, container, false);
        setUpLayout(mView);
        broadcastReceiver = new RequestBidBroadcastReceiver();
        Log.v(LOG_TAG, " Inside ClientRequestFragment ");
        apiCall(false);

        return mView;
    }

    private void setUpLayout(View view) {
        mRvRequests = view.findViewById(R.id.rv_requests);
        mRlParent = view.findViewById(R.id.rl_parent);
        message = view.findViewById(R.id.create_job_message);
        appIconTextView = view.findViewById(R.id.app_icon_click_message);
        imgNoJobFound = view.findViewById(R.id.ivNoJob);
        plusIconTextView = view.findViewById(R.id.plus_icon_click_message);
        refresh = view.findViewById(R.id.refresh);
        //
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mRvRequests.setLayoutManager(mManager);
        //
        activity = (HomeActivity) getActivity();
        mReqJobsAdapter = new RequestJobsAdapter(getActivity(), activity.mJobList, ClientRequestsFragment.this);
        mRvRequests.setAdapter(mReqJobsAdapter);


        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiCall(false);
            }
        });

    }

    private void apiCall(boolean isShowLoader) {

        Log.v(LOG_TAG, " apiCall");
        if (!isShowLoader) {
            ((HomeActivity) getActivity()).isShowProgress(true);
        }
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        ClientJobReq clientJobReq = new ClientJobReq(HomeActivity.mLoginToken, "Upcomming");
        jobSpecViewModel.clientJobs(clientJobReq, isShowLoader);

        jobSpecViewModel.getClientJobs().observe(this, clientJobs -> {

            if (refresh.isRefreshing()) {
                refresh.setRefreshing(false);
            }
            ((HomeActivity) getActivity()).isShowProgress(false);
            //
            activity.mJobList = clientJobs.getJobs();
            //
            //  Collections.reverse(activity.mJobList);
            //
            mReqJobsAdapter = new RequestJobsAdapter(getActivity(), activity.mJobList, ClientRequestsFragment.this);
            mRvRequests.setAdapter(mReqJobsAdapter);
            //  message.setVisibility(View.GONE);//
            showCreateJobImage();


        });
    }

    @Override
    public void onViewBids(Object object) {
        mVari = 1;
        GetJobRes job = (GetJobRes) object;
        Bundle bundle = new Bundle();
        bundle.putString("authToken", getAuthToken());
        bundle.putParcelable("job", job);
        bundle.putString("jobId", job.jobId);
        bundle.putString("mType", "Bids");
        Intent intent = new Intent(getActivity(), HireServiceProviderActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, RequestCodes.HIRE_PROVIDER_REQUEST);
        getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);

    }

    @Override
    public void onEditJobClick(Object object) {
        Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show();
//        GetJobRes job = (GetJobRes) object;
//        String createJobReqJson = new Gson().toJson(job);
//        PreferenceHandler.writeString(getActivity(), PreferenceHandler.PREF_KEY_UPDATE_JOB, createJobReqJson);
//        Intent mIntent = new Intent(getActivity(), EditJobActivity.class);
//        startActivityForResult(mIntent, RequestCodes.CONFIRM_JOB_REQUEST);
//        getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    @Override
    public void onEditJobClickWithPosition(Object object, int position) {
//        Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show();
        GetJobRes job = (GetJobRes) object;
        String createJobReqJson = new Gson().toJson(job);

        PreferenceHandler.writeString(getActivity(), PreferenceHandler.PREF_KEY_UPDATE_JOB, createJobReqJson);



        String adapPosition = String.valueOf(position);
        Intent mIntent = new Intent(getActivity(), EditJobActivity.class);
        mIntent.putExtra("position", adapPosition);
        mIntent.putExtra("GetJobRes", job);
        mIntent.putExtra("type", job.cleaningType);
        startActivityForResult(mIntent, RequestCodes.CONFIRM_JOB_REQUEST);
        getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    @Override
    public void onCancelJobClick(Object object) {
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
                    activity.mJobList.remove(mJob);
                    mReqJobsAdapter.notifyDataSetChanged();
                    showCreateJobImage();
                });
    }


    @Override
    public void onPayJobClick(Object object) {


       /* GetJobRes job = (GetJobRes) object;

        Log.v(LOG_TAG, job.assignedTo.providerId);
        Intent mIntent = new Intent(getActivity(), PaymentActivity.class);
        mIntent.putExtra("type", "client");
        mIntent.putExtra("jobId", job.jobId);
        mIntent.putExtra("to", job.providerId);
        mIntent.putExtra("price", job.estPrice);
        mIntent.putExtra("clientPrice", job.price);
        mIntent.putExtra("name", job.assignedTo.providerName);
        mIntent.putExtra("image", job.assignedTo.providerImg);
        mIntent.putExtra("providerId", job.assignedTo.providerId);//
        startActivity(mIntent);*/
    }

    @Override
    public void onPayJobClickWithPosition(Object object, int position) {
        GetJobRes job = (GetJobRes) object;
        Log.v(LOG_TAG, " jobType:" + job.jobType);


        Intent mIntent = new Intent(getActivity(), PaymentActivity.class);
        mIntent.putExtra("type", "client");
        mIntent.putExtra("jobId", job.jobId);
        mIntent.putExtra("to", job.providerId);
        mIntent.putExtra("price", job.estPrice);
        // mIntent.putExtra("clientPrice", job.price);
        //
        if (job.assignedTo.bookedPrice != null && !job.assignedTo.bookedPrice.isEmpty() && job.jobType.equalsIgnoreCase("Bid")) {
            // This is bid price : applicable is case of job type Bid only
            mIntent.putExtra("clientPrice", job.assignedTo.bookedPrice);
        } else {
            mIntent.putExtra("clientPrice", job.price);
        }

        mIntent.putExtra("name", job.assignedTo.providerName);
        mIntent.putExtra("image", job.assignedTo.providerImg);
        mIntent.putExtra("providerId", job.assignedTo.providerId);



        //
        //mIntent.putExtra("bidPrice", job.bidPrice);
        //
        String adapPosition = String.valueOf(position);
        mIntent.putExtra("position", adapPosition);
       startActivityForResult(mIntent, RequestCodes.PAYMENT_REQUEST);


       /* GetJobRes job = (GetJobRes) object;
        Log.v(LOG_TAG, " POSITION ="+ position);
        Intent mIntent = new Intent(getActivity(), PaymentActivity.class);
        mIntent.putExtra("type", "client");
        mIntent.putExtra("jobId", job.jobId);
        mIntent.putExtra("to", job.providerId);
        mIntent.putExtra("price", job.estPrice);
        mIntent.putExtra("clientPrice", job.price);
        mIntent.putExtra("name", job.assignedTo.providerName);
        mIntent.putExtra("image", job.assignedTo.providerImg);
        mIntent.putExtra("providerId", job.assignedTo.providerId);//
        String adapPosition = String.valueOf(position);
        mIntent.putExtra("position", adapPosition);
        startActivity(mIntent);*/

    }

    @Override
    public void onRatingClick(Object object) {
        GetJobRes job = (GetJobRes) object;
        Intent in = new Intent(getContext(), ReviewActivity.class);
        in.putExtra("type", "client");
        in.putExtra("jobId", job.jobId);
        in.putExtra("to", job.providerId);
        if (job.assignedTo.providerName != null && job.assignedTo.providerName.isEmpty()) {
            Log.e("Payment ", " name=" + job.name);
            in.putExtra("name", job.assignedTo.providerName);
        }
        startActivity(in);
        getActivity().overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, " InActiVityResult rqt: " + requestCode + " Result Code " + resultCode);

        if (resultCode == ResultCodes.HIRE_PROVIDER_RESULT) {
           // showToast(" In HIRE_PROVIDER_RESULT*");
            Log.d(LOG_TAG, " InHIRE_PROVIDER_RESULT: " + requestCode + " Result Code " + resultCode);
            GetJobRes job = Objects.requireNonNull(data.getExtras()).getParcelable("job");

            int indexofJob = activity.mJobList.indexOf(job);

            if (indexofJob >= 0) {
                GetJobRes jobRes = activity.mJobList.get(indexofJob);
                assert job != null;
                jobRes.assignedTo = job.assignedTo;
                jobRes.statusOfJob = 2;
                //
                jobRes.assignedTo.bookedPrice = job.assignedTo.bookedPrice;

                activity.mJobList.get(indexofJob).statusOfJob = 2;
                activity.mJobList.get(indexofJob).assignedTo = job.assignedTo;

                activity.mJobList.get(indexofJob).assignedTo.bookedPrice = job.assignedTo.bookedPrice;

                mReqJobsAdapter.notifyDataSetChanged();
            }

        }
        //
        if (resultCode == RequestCodes.HIRE_PROVIDER_REQUEST) {

            int mPosition = data.getIntExtra("position", -1);
            Log.d(LOG_TAG, " mPosition " + mPosition);
            activity.mJobList.get(mPosition).statusOfJob = 2;
            mReqJobsAdapter.notifyDataSetChanged();
        }
        if (resultCode == ResultCodes.CONFIRM_JOB_RESULT) {
//            Toast.makeText(getActivity(), "In CONFIRM_JOB Result*", Toast.LENGTH_LONG).show();
            GetJobRes jobRes = data.getParcelableExtra("Job");
            String JobId = data.getStringExtra("JobId");

            String position = data.getStringExtra("position");
            String bedroom = data.getStringExtra("bedroom");

            Log.e(LOG_TAG, " POsition is : " + position + "BedRoom :" + jobRes.bedrooms + jobRes.kitchen);
            jobRes.jobId = JobId;
            activity.mJobList.set(Integer.parseInt(position), jobRes);
            mReqJobsAdapter.notifyDataSetChanged();
        }


        // if (resultCode == ResultCodes.PAYMENT_RESULT ||resultCode == 2006 ) {


        if (requestCode == 1008) {
            apiCall(false);

//            String position = data.getStringExtra("position"); // CRASH detected here :Position is empty because data intent is null
           /* Log.e(LOG_TAG, " POSITION=" + position);
            if (activity.mJobList.size() > 0) {
                activity.mJobList.remove(Integer.parseInt(position));
                mReqJobsAdapter.notifyItemChanged(Integer.parseInt(position));
                mReqJobsAdapter.notifyDataSetChanged();
            }*/

            //


        }


    }

    @Override
    public void update(Observable o, Object arg) {
        if (mReqJobsAdapter != null) {
            imgNoJobFound.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            appIconTextView.setVisibility(View.GONE);
            plusIconTextView.setVisibility(View.GONE);
            mReqJobsAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //
        registerBroadCastReceiver();
        //
        ((HomeActivity) getActivity()).isShowProgress(true);
        //
        Log.v(LOG_TAG, " onResume ");

        if (activity.mJobList.size() > 1) {
            mReqJobsAdapter = new RequestJobsAdapter(getActivity(), activity.mJobList, ClientRequestsFragment.this);
            mRvRequests.setAdapter(mReqJobsAdapter);
        }

    }

    void showCreateJobImage() {
        if (activity.mJobList.size() > 0) {
            imgNoJobFound.setVisibility(View.GONE);
            //
            mRlParent.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            //
            message.setVisibility(View.GONE);
            appIconTextView.setVisibility(View.GONE);
            plusIconTextView.setVisibility(View.GONE);
        } else {
            imgNoJobFound.setVisibility(View.VISIBLE);
            //
            mRlParent.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            //
            message.setVisibility(View.VISIBLE);
            appIconTextView.setVisibility(View.VISIBLE);
            plusIconTextView.setVisibility(View.VISIBLE);
        }
    }

    void registerBroadCastReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RequestCodes.NOTIFICATION_NEW_BID);
            getActivity().registerReceiver(broadcastReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateBidPlacedJob(String jobId) {
        for (int i = 0; i < activity.mJobList.size(); i++) {
            if (activity.mJobList.get(i).jobId.equalsIgnoreCase(jobId)) {
                //
                activity.mJobList.get(i).bidAvailable = true;
                activity.mJobList.get(i).statusOfJob = 1;
                //
                mReqJobsAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    void updateStartedJob(String jobId) {
        for (int i = 0; i < activity.mJobList.size(); i++) {
            if (activity.mJobList.get(i).jobId.equalsIgnoreCase(jobId)) {
                //
                activity.mJobList.get(i).statusOfJob = 3;
                activity.mJobList.get(i).jobId = jobId;

                //
                mReqJobsAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    /*Job Accepted*/
    void updateAcceptJob(String jobId) {
        for (int i = 0; i < activity.mJobList.size(); i++) {
            if (activity.mJobList.get(i).jobId.equalsIgnoreCase(jobId)) {
                activity.mJobList.get(i).statusOfJob = 2;
                activity.mJobList.get(i).jobId = jobId;
                mReqJobsAdapter.notifyDataSetChanged();
                break;
            }
        }
    }


    void updateCompletedJob(String jobId) {
        for (int i = 0; i < activity.mJobList.size(); i++) {
            if (activity.mJobList.get(i).jobId.equalsIgnoreCase(jobId)) {
                //
                activity.mJobList.get(i).statusOfJob = 4;
                activity.mJobList.get(i).jobId = jobId;


                //
                mReqJobsAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public class RequestBidBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String jobId = intent.getStringExtra("jobId");
                String type = intent.getStringExtra("type");
                //
                if (activity.mJobList != null && activity.mJobList.size() > 0) {
                    switch (type) {
                        case RequestCodes.BID_PLACED:
                            updateBidPlacedJob(jobId);
                            break;
                        case RequestCodes.JOB_STARTED:
                            updateStartedJob(jobId);
                            break;
                        case RequestCodes.JOB_COMPLETED:
                            updateCompletedJob(jobId);
                            break;
                        case RequestCodes.JOB_ACCEPTED:
                            updateAcceptJob(jobId);
                            break;
                        case RequestCodes.BID_INTERESTED:
                            Log.e(LOG_TAG, " In INtrest sent");
                            updateBidPlacedJob(jobId);
                            break;

                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
