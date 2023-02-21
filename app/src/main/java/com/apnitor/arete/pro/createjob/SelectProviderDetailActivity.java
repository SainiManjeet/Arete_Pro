package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.AssignJobReq;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.databinding.ActivitySelectProviderDetailBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SelectProviderDetailActivity extends BaseActivity {
    private ActivitySelectProviderDetailBinding binding;
    private GetProviderRes providerRes;
    private int mPosition;
    String jobId;
    private JobSpecViewModel jobSpecViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_provider_detail);
        binding = bindContentView(R.layout.activity_select_provider_detail);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
//        setToolBar("Provider detail");
        providerRes = getIntent().getParcelableExtra("providerRes");
        jobId = getIntent().getStringExtra("jobId");
        mPosition = getIntent().getIntExtra("position",0);
        binding.setProvider(providerRes);

        LinearLayoutManager mManager = new LinearLayoutManager(this);
        binding.secondaryLayout.reviewRecycler.setLayoutManager(mManager);

        binding.btnHire.setOnClickListener(v -> {
          /*  Intent mIntent = new Intent(); // w bf
            mIntent.putExtra("providerId", providerRes.providerId);
            mIntent.putExtra("providerName", providerRes.providerName);
            mIntent.putExtra("providerImage", providerRes.providerImage);
            setResult(111, mIntent);
            finish();*/

            // SEND JOB ID
            assignJobToProvider(jobId);


        });

        setValues();

        setAdapter();
    }

    @SuppressLint("SetTextI18n")
    private void setValues() {

        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                // Collapsed
                binding.collapsingToolBar.setTitle(providerRes.providerName);
                binding.txtColorBack.setBackgroundResource(R.color.colorWhite);

            } else if (verticalOffset == 0) {
                // Expanded
                binding.collapsingToolBar.setTitle("");

                binding.txtColorBack.setBackgroundResource(R.color.colorPrimary);
            } else {
                // Somewhere in between
                binding.collapsingToolBar.setTitle("");
                binding.txtColorBack.setBackgroundResource(R.color.colorPrimary);
            }
        });

        binding.collapsingToolBar.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        binding.collapsingToolBar.setTitle(providerRes.providerName);


       /* Drawable drawable = binding.rating.getProgressDrawable();
        drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
*/

        Float ratingh = Float.valueOf(providerRes.providerRating);
        binding.rating.setRating(ratingh);
        binding.tvName.setText(providerRes.providerName);
        /* Set no of rating*/
        binding.tvNoOfReview.setText("No of Rating" + "(" + providerRes.providerRating + ")");

        if (providerRes.providerPrice != null) {
            binding.secondaryLayout.tvPricePerHour.setVisibility(View.VISIBLE);
            /*String perHour = providerRes.providerPrice.substring(0, providerRes.providerPrice.length() - 1);
            binding.secondaryLayout.tvPricePerHour.setText("$" + perHour + "\nPer Hour");*/
            binding.secondaryLayout.tvPricePerHour.setText("$" + providerRes.providerPrice + "\nPer Hour");
        }

        //binding.secondaryLayout.tvTotalJobs.setText(providerRes.jobDone + "\nHours");
        binding.secondaryLayout.tvTotalJobs.setText(providerRes.jobDone + "\nJobs");
       /* if (null == providerRes.totalHours) {
            binding.secondaryLayout.tvTotalHour.setText("0\nHours Worked");
        } else {
            binding.secondaryLayout.tvTotalHour.setText(providerRes.totalHours + "\nHours");
        }*/
        Glide.with(this).load(providerRes.providerImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                .centerCrop()).into(binding.ivProfile);

        if (providerRes.aboutMe != null) {
            binding.secondaryLayout.etAbout.setText(providerRes.aboutMe);
        }

    }

    private void setAdapter() {
        if (providerRes.providerReviews.size() > 0) {
            Log.e("REVIEW data", "INSIDE" + providerRes.providerReviews.size());
            AdapterReviewsProvider adapter = new AdapterReviewsProvider(SelectProviderDetailActivity.this, providerRes.providerReviews);
            binding.secondaryLayout.reviewRecycler.setAdapter(adapter);
        }

    }

    private void assignJobToProvider(String jobId) {
        AssignJobReq assignJobReq = new AssignJobReq(getPrefHelper().getAuthToken(), jobId, providerRes.providerId);
        jobSpecViewModel.assignJob(assignJobReq);

        jobSpecViewModel.getAssignJobResponse().observe(this, response -> {

            Intent mIntent = new Intent();
            mIntent.putExtra("providerId", providerRes.providerId);
            mIntent.putExtra("providerName", providerRes.providerName);
            mIntent.putExtra("position", mPosition);
            mIntent.putExtra("providerImage", providerRes.providerImage);
            mIntent.putExtra("providerHourlyRate", providerRes.providerPrice);
            mIntent.putExtra("providerHourlyRate", providerRes.providerPrice);
            setResult(RequestCodes.HIRE_PROVIDER_REQUEST, mIntent);
            finish(); //
            //

        });
    }

}
