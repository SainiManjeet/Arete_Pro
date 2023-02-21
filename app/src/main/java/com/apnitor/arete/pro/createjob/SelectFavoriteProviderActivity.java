package com.apnitor.arete.pro.createjob;

import android.content.Intent;
import android.os.Bundle;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.databinding.ActivitySelectProviderBinding;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SelectFavoriteProviderActivity extends BaseActivity implements ListItemClickCallback,ListItemClickWithPositionCallback {
    public static final String LOG_TAG = "SelectFavoriteProvider";
    private ActivitySelectProviderBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    private AdapterSelectProvider adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_provider);
        binding = bindContentView(R.layout.activity_select_provider);

        setToolBar(getResources().getString(R.string.choose_provider));

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mManager);
        showProviderList();
        binding.refresh.setOnRefreshListener(() -> jobSpecViewModel.getProviders(new ChooseProviderReq(getPrefHelper().getAuthToken())));
        jobSpecViewModel.getMyFavoriteProviders(new ChooseProviderReq(getPrefHelper().getAuthToken()),true);
    }

    private void showProviderList() {
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        jobSpecViewModel.getProvidersResLiveData()
                .observe(this, res -> {
                    if (binding.refresh.isRefreshing()) {
                        binding.refresh.setRefreshing(false);
                    }
                    adapter = new AdapterSelectProvider(SelectFavoriteProviderActivity.this, res.providerRes, this,this,"NotFavorite");


                    //adapter = new AdapterSelectProvider(HireServiceProviderActivity.this, bidsRes, this,this, mType);

                    binding.recyclerView.setAdapter(adapter);
                });

    }

    @Override
    public void onListItemClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent(this, SelectProviderDetailActivity.class);
        mIntent.putExtra("providerRes", res);
        startActivity(mIntent);

    }

    @Override
    public void onHireClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent();
        mIntent.putExtra("providerId", res.providerId);
        mIntent.putExtra("providerName", res.providerName);
        mIntent.putExtra("providerImage", res.providerImage);
        mIntent.putExtra("providerHourlyRate", res.providerPrice);
        setResult(111, mIntent);
        finish();
    }

    @Override
    public void onListItemWithPositionClick(Object object, int position) {

    }


}
