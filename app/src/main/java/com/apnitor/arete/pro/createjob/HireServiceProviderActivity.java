package com.apnitor.arete.pro.createjob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.AssignedTo;
import com.apnitor.arete.pro.api.request.ViewBidsReq;
import com.apnitor.arete.pro.api.response.AssignJobReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.databinding.ActivityHireServiceProviderBinding;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class HireServiceProviderActivity extends BaseActivity implements ListItemClickCallback, ListItemClickWithPositionCallback {
    public static final String LOG_TAG = "HireServiceProviderAct";
    ActivityHireServiceProviderBinding binding;
    private List<GetProviderRes> bidsRes = new ArrayList<>();
    private JobSpecViewModel jobSpecViewModel;
    private String authToken;
    private String jobId;
    private String mType;
    private GetJobRes mJob;
    private int mPosition = 0;
    private AdapterSelectProvider adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.activity_hire_service_provider);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            authToken = bundle.getString("authToken");
            jobId = bundle.getString("jobId");
            mJob = bundle.getParcelable("job");
            mType = bundle.getString("mType");
            apiCall();
        }

        setToolBar(getResources().getString(R.string.choose_provider));

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mManager);


        binding.refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorPrimary);
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.refresh.setRefreshing(false);
            }
        });
    }

    private void apiCall() {

        ViewBidsReq viewBidsReq = new ViewBidsReq(authToken, jobId);
        jobSpecViewModel.viewJobs(viewBidsReq);
        jobSpecViewModel.getBidsOnJobs().observe(this, bids -> {
                    Log.d(LOG_TAG, "SIZE=" + bids.getViewBids());
                    bidsRes = bids.getViewBids();
                    mType = mType != null ? "Bids" : "NotFavorite";
                    adapter = new AdapterSelectProvider(HireServiceProviderActivity.this, bidsRes, this, this, mType);
                    binding.recyclerView.setAdapter(adapter);
                }
        );
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void onHireClick(Object object) {
        GetProviderRes serviceProvider = (GetProviderRes) object;
        assignJobToProvider(serviceProvider);

    }


    private void assignJobToProvider(GetProviderRes serviceProvider) {
        AssignJobReq assignJobReq = new AssignJobReq(authToken, jobId, serviceProvider.providerId);
        jobSpecViewModel.assignJob(assignJobReq);

        jobSpecViewModel.getAssignJobResponse().observe(this, response -> {

            AssignedTo assignedTo = new AssignedTo();
            assignedTo.providerId = serviceProvider.providerId;
            assignedTo.providerName = serviceProvider.providerName;
            assignedTo.providerImg = serviceProvider.providerImage;

            //

            assignedTo.bookedPrice = serviceProvider.bidPrice;
            Log.d("BOOKEd price==","=="+assignedTo.bookedPrice );

            mJob.assignedTo = assignedTo;
            showToast(getResources().getString(R.string.job_assigned));

            Bundle bundle = new Bundle();
            bundle.putParcelable("provider", serviceProvider);
            bundle.putParcelable("job", mJob);
            Intent intent = new Intent();
            intent.putExtras(bundle);

            setResult(ResultCodes.HIRE_PROVIDER_RESULT, intent);
            finish();

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //
    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RequestCodes.HIRE_PROVIDER_REQUEST) {
            Log.d(LOG_TAG, " onActivityResult");

          /*  mIntent.putExtra("providerId", data.getStringExtra("providerId"));
            mIntent.putExtra("providerName", data.getStringExtra("providerName"));
            mIntent.putExtra("providerImage", data.getStringExtra("providerImage"));

            mIntent.putExtra("position",  data.getStringExtra(String.valueOf(mPosition)));
            mIntent.putExtra("providerHourlyRate", data.getStringExtra("providerHourlyRate"));
            setResult(111, mIntent);*/
//          Intent mIntent = new Intent(HireServiceProviderActivity.this, PaymentActivity.class);
//          mIntent.putExtra("Position",mPosition);
//          startActivity(mIntent);
            Intent intent = new Intent();
            intent.putExtra("position", mPosition);

            setResult(RequestCodes.HIRE_PROVIDER_REQUEST, intent);
            finish();
        }
    }


    @Override
    public void onListItemWithPositionClick(Object object, int position) {
        mPosition = position;
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent(this, SelectProviderDetailActivity.class);
        mIntent.putExtra("providerRes", res);
        mIntent.putExtra("position", position);
        mIntent.putExtra("jobId", jobId);
        startActivityForResult(mIntent, 111);
    }

    @Override
    public void onListItemClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent(this, SelectProviderDetailActivity.class);
        mIntent.putExtra("providerRes", res);
        mIntent.putExtra("jobId", jobId);
        startActivityForResult(mIntent, RequestCodes.HIRE_PROVIDER_REQUEST);

    }
}
