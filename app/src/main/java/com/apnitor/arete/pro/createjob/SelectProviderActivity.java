package com.apnitor.arete.pro.createjob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.databinding.ActivitySelectProviderBinding;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SelectProviderActivity extends BaseActivity implements ListItemClickCallback ,ListItemClickWithPositionCallback {
    public static final String LOG_TAG = "SelectProviderActivity";
    List<GetProviderRes> bidsResFilter = new ArrayList<>();
    List<GetProviderRes> beanList = new ArrayList<>();
    ArrayList<String> sortList = new ArrayList<>();
    ArrayList<GetProviderRes> mJobListSorting = new ArrayList<>();
    private ActivitySelectProviderBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    private List<GetProviderRes> bidsRes;
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
        jobSpecViewModel.getProviders(new ChooseProviderReq(getPrefHelper().getAuthToken()));

        binding.imgFilter.setVisibility(View.VISIBLE);
        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });
    }

    private void showProviderList() {
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        jobSpecViewModel.getProvidersResLiveData()
                .observe(this, res -> {
                    if (binding.refresh.isRefreshing()) {
                        binding.refresh.setRefreshing(false);
                    }

                    //
                    bidsRes = res.providerRes;

                    //
                    sortList.add(String.valueOf(bidsRes));
                    for (int i = 0; i <= sortList.size(); i++) {
                        Collections.sort(sortList);
                    }

                    adapter = new AdapterSelectProvider(SelectProviderActivity.this, res.providerRes, this, this,"NotFavorite");

                    binding.recyclerView.setAdapter(adapter);
                });
    }

    @Override
    public void onListItemClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent(this, SelectProviderDetailActivity.class);
        mIntent.putExtra("providerRes", res);
        startActivityForResult(mIntent, 111);


    }

    @Override
    public void onHireClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent();
        mIntent.putExtra("providerId", res.providerId);
        mIntent.putExtra("providerName", res.providerName);
        mIntent.putExtra("providerImage", res.providerImage);
        mIntent.putExtra("providerHourlyRate", res.providerPrice);// Provider Hourly Price
        setResult(111, mIntent);
        finish();
    }


    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            Intent mIntent = new Intent();
            mIntent.putExtra("providerId", data.getStringExtra("providerId"));
            mIntent.putExtra("providerName", data.getStringExtra("providerName"));
            mIntent.putExtra("providerImage", data.getStringExtra("providerImage"));
            mIntent.putExtra("providerHourlyRate", data.getStringExtra("providerHourlyRate"));
            setResult(111, mIntent);
            finish();
        }
    }

    private void openFilterDialog() {

        String[] langArray = {"Price (Low to High)", "Price (High to Low)"};
        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(SelectProviderActivity.this)
                .title("Filter By")
                .theme(Theme.LIGHT)
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .items(langArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (text.equals("Price (Low to High)")) {
                            sortScheduleByPriceLowToHigh(bidsRes, "LtoH");
                        } else if (text.equals("Price (High to Low)")) {
                            sortScheduleByPriceLowToHigh(bidsRes, "HtoL");
                        } else {
                            Log.e(LOG_TAG, "In Else");
                        }
                        dialog.dismiss();
                    }
                })
                .negativeText("Cancel");

        materialDialog.show();
    }

    //
    // Method for filter
    ArrayList<GetProviderRes> sortScheduleByPriceLowToHigh(List<GetProviderRes> mGetTeamScheduleRes, String sortType) {
        int size = mGetTeamScheduleRes.size();
        if (size > 0) {
            if (sortType.equalsIgnoreCase("HtoL")) {
                Collections.sort(mGetTeamScheduleRes, new SortByPriceHighToLow());
            } else if (sortType.equalsIgnoreCase("LtoH")) {
                Collections.sort(mGetTeamScheduleRes, new SortByPriceLowToHigh());
            }
            adapter = new AdapterSelectProvider(SelectProviderActivity.this, mGetTeamScheduleRes, this,this, "NotFavorite");
            binding.recyclerView.setAdapter(adapter);
        }
        return mJobListSorting;
    }

    @Override
    public void onListItemWithPositionClick(Object object, int position) {
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    // Filter Price Hight to Low
    class SortByPriceHighToLow implements Comparator<GetProviderRes> {
        public int compare(GetProviderRes a, GetProviderRes b) {
            float one = Float.parseFloat(a.providerPrice);
            float two = Float.parseFloat(b.providerPrice);
            return ((int) two) - ((int) one); // H to L

        }
    }

    class SortByPriceLowToHigh implements Comparator<GetProviderRes> {
        public int compare(GetProviderRes a, GetProviderRes b) {
            float one = Float.parseFloat(a.providerPrice);
            float two = Float.parseFloat(b.providerPrice);
            return ((int) one) - ((int) two);
        }
    }

}
