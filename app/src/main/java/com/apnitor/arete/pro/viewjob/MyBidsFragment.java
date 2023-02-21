package com.apnitor.arete.pro.viewjob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.CancelBidReq;
import com.apnitor.arete.pro.api.request.GetJobReq;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.databinding.FragmentViewJobsBinding;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Class to view jobs
 */

public class MyBidsFragment extends BaseFragment implements FindJobClickCallback, Observer {
    public static final String LOG_TAG = "MyBidsFragment";
    public ArrayList<GetJobRes> mJobList = new ArrayList<>();
    public ArrayList<GetJobRes> mAllBidsList = new ArrayList<>();
    // HomeActivity homeActivity;
    String type = "";
    GetJobRes job;
    List<GetJobRes> bidsResFilter = new ArrayList<>();
    ArrayList<String> mm = new ArrayList<>();
    ArrayList<GetJobRes> mJobListSorting = new ArrayList<>();
    private FragmentViewJobsBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    private boolean loadmore;
    private AdapMyBids adapter;
    private String message = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //this.homeActivity = (HomeActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentViewJobsBinding.inflate(inflater, container, false);

        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mManager);

        binding.refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobSpecViewModel.getMyBid(new GetJobReq(getAuthToken()), false);
            }
        });
        binding.cardViewFilter.setVisibility(View.GONE);
        // Delete Me - i am test comment
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAllBidsList.size() < 1) {
            showProviderList(true);
        } else
            showProviderList(false);
    }


    private void showProviderList(boolean isShowLoader) {
        jobSpecViewModel.getMyBid(new GetJobReq(getAuthToken()), isShowLoader);

        jobSpecViewModel.getMyBidResLiveData()
                .observe(this, res -> {
                    if (binding.refresh.isRefreshing()) {
                        binding.refresh.setRefreshing(false);
                    }
                    loadmore = true;
                    mAllBidsList.clear();
                    mAllBidsList.addAll(res.jobs);

                    Iterator<GetJobRes> iterator = res.jobs.iterator();
                    while (iterator.hasNext()) {
                        GetJobRes job = iterator.next(); // must be called before you can call i.remove()

                        if (job.bid.bidStatus.equalsIgnoreCase("Cancelled")) {
                            iterator.remove();
                        }
                    }

                    if (res.jobs.size() > 0) {
                        //adapter = new AdapterViewJobs(getActivity(), jobs, FindJobsProviderFragment.this);
                        // adapter = new AdapterViewJobs(getActivity(), homeActivity.mJobList, MyBidsFragment.this);
                        adapter = new AdapMyBids(getActivity(), mAllBidsList, MyBidsFragment.this);
                        binding.recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onListItemClick(Object object) {
        GetJobRes res = (GetJobRes) object;
        Log.d(LOG_TAG, " jobType " + res.jobType + "  bidPlaced " + res.bid.bidId);
        if (res.jobType.equals("Bid")) {
            // Go to Modify Bid
            Intent mIntent = new Intent(getActivity(), PlaceBidActivity.class);
            mIntent.putExtra("jobRes", res);
            mIntent.putExtra("type", "modifyBid");
            mIntent.putExtra("bidId", res.bid.bidId);
            startActivityForResult(mIntent, RequestCodes.MODIFY_BID_REQUEST);
        }

    }

    @Override
    public void onListItemClickWithPositionClick(Object object, int position) {

    }

    @Override
    public void onMakeBidClick(Object object) {
        job = (GetJobRes) object;
        if (job.jobType.equals("Bid")) {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("jobRes", job);
            //startActivity(PlaceBidActivity.class,mBundle);

            Intent intent = new Intent(getActivity(), PlaceBidActivity.class);
            intent.putExtras(mBundle);
            intent.putExtra("type", "bidPlace"); // M 18
            startActivityForResult(intent, RequestCodes.PLACE_BID_REQUEST);

        } else if (job.jobType.equals("Fixed Price")) {
            MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), job.jobId);
            jobSpecViewModel.makeBid(mProviderReq);
            message = "Bid Placed Successfully..";
            observeBidResponse(message, job);
        } else {
            //MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(),res.jobId,"Accept");
            MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), job.jobId, "Accepted");
            jobSpecViewModel.makeBid(mProviderReq);
            showToast("Job accepted..");
            message = "Job accepted..";

            observeBidResponse(message, job);
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
    public void onCancelBidClick(Object object) {
        GetJobRes res = (GetJobRes) object;

        // MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(),res.jobId,"Reject");
        MakeBidReq mProviderReq = new MakeBidReq(getAuthToken(), res.jobId, "Rejected");
        jobSpecViewModel.makeBid(mProviderReq);

        /**
         * TODO
         *
         * It should be job rejected successfully
         *
         */

        message = "Bid rejected successfully";


    }

    @Override
    public void onRatingClick(Object object) {

    }



    @Override
    public void onStartJobWithPosition(Object object, int Position) {

    }

    /**
     * Cancel Bid
     *
     * @param object
     */

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




}
