package com.apnitor.arete.pro.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ClientJobReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.PaymentActivity;
import com.apnitor.arete.pro.createjob.PastJobAdapter;
import com.apnitor.arete.pro.interfaces.RateAndPayCallback;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import java.util.ArrayList;
import java.util.Collections;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClientPastFragment extends BaseFragment implements RateAndPayCallback {
    private RecyclerView mRvRequests;
    private PastJobAdapter mReqJobsAdapter;
    //String LOG_TAG = "ClientPastFragment";
    private ArrayList<GetJobRes> mReqList = new ArrayList<>();
    private TextView message;
    private JobSpecViewModel jobSpecViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_past, container, false);
        setUpLayout(mView);
        return mView;
    }

    private void setUpLayout(View view) {
        message = view.findViewById(R.id.message);
        mRvRequests = view.findViewById(R.id.rv_requests);
        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mRvRequests.setLayoutManager(mManager);
        mReqJobsAdapter = new PastJobAdapter(getActivity(), mReqList, ClientPastFragment.this);
        mRvRequests.setAdapter(mReqJobsAdapter);

    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).isShowProgress(true);
        if (mReqList.size() < 1)
            apiCall(false);
        else
            apiCall(false);
    }

    private void apiCall(boolean isShowLoader) {
        if (!isShowLoader) {
            ((HomeActivity) getActivity()).isShowProgress(true);
        }
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        //
        ClientJobReq clientJobReq = new ClientJobReq(HomeActivity.mLoginToken, "Past");
        jobSpecViewModel.clientJobs(clientJobReq, isShowLoader);
        //
        jobSpecViewModel.getClientJobs().observe(this, clientJobs -> {
            ((HomeActivity) getActivity()).isShowProgress(false);
            //
            mReqList = clientJobs.getJobs();
            //
            Collections.reverse(mReqList);
            //
            mReqJobsAdapter = new PastJobAdapter(getActivity(), mReqList, ClientPastFragment.this);
            mRvRequests.setAdapter(mReqJobsAdapter);

            if (mReqList.size() == 0) {
                message.setVisibility(View.VISIBLE);
            } else {
                message.setVisibility(View.GONE);
            }
        });
    }

    private void getCliemtJobs() {
        ClientJobReq clientJobReq = new ClientJobReq(HomeActivity.mLoginToken, "Past");
        // ClientJobReq clientJobReq = new ClientJobReq(HomeActivity.mLoginToken, "Upcomming");
        jobSpecViewModel.clientJobs(clientJobReq, true);
    }

    /**
     * TODO
     * <p>
     * We don't need this.
     * <p>
     * Not sure we will need it in future or not
     *
     * @param object
     */


 /*   @Override
    public void onViewBids(Object object) {
        GetJobRes job = (GetJobRes) object;
        showToast("Bid Clicked");
        getJobBids(job);
    }
*/
  /*  @Override
    public void onEditJobClick(Object object) {
        showToast("Functionality is not implemented yet.");
    }
*/
    @Override
    public void onPayJobClick(Object object) {  // Payment Button

        GetJobRes job = (GetJobRes) object;
        Intent mIntent = new Intent(getActivity(), PaymentActivity.class);
        mIntent.putExtra("type", "client");
        mIntent.putExtra("jobId", job.jobId);

       /* Bundle mBundle=new Bundle();
        mBundle.putParcelable("jobRes",job.jobId);
        mBundle.putString("type","client");*/
        startActivity(mIntent);

    }

    @Override
    public void onPayJobClickWithPosition(Object object, int position) {

    }


    @Override
    public void onRatingClick(Object object) {

    }

    /**
     * TODO
     *
     * In Completed Jobs we won't display the bids
     *
     * @param job
     */

  /*  private void getJobBids(GetJobRes job ){
        ViewBidsReq viewBidsReq = new ViewBidsReq(getAuthToken(), job.jobId);
        jobSpecViewModel.getBidsOnJob(viewBidsReq);
            jobSpecViewModel.getJobBids().observe(this, bids->{
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("bids",bids.getJobBids());
            bundle.putString("authToken",getAuthToken());
            bundle.putString("jobId",job.jobId);
            startActivity(HireServiceProviderActivity.class, bundle);
        });
    }*/

}