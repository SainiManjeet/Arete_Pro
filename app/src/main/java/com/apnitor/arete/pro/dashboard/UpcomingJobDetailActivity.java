package com.apnitor.arete.pro.dashboard;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.StartJobReq;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.completeJob.PostWalkNotesActivity;
import com.apnitor.arete.pro.completeJob.PreWalkNotesActivity;
import com.apnitor.arete.pro.completeJob.ReviewActivity;
import com.apnitor.arete.pro.createjob.ImageAdapter;
import com.apnitor.arete.pro.databinding.ActivityUpcomingJobDetailBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

public class UpcomingJobDetailActivity extends BaseActivity {
    private ActivityUpcomingJobDetailBinding binding;
    private JobSpecViewModel mJobSpecViewModel;
    private GetJobRes jobRes;
    private String message = "",mProviderNotes;
    String LOG_TAG="UpcomingJobDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobRes = getIntent().getParcelableExtra("jobRes");
        Log.d(LOG_TAG, " Job Detail is "+jobRes.toString());
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);
        String mType = getIntent().getStringExtra("Type");

        setContentView(R.layout.activity_upcoming_job_detail);
        binding = bindContentView(R.layout.activity_upcoming_job_detail);
        setToolBar("Job Detail");

        // Show Start Button only if job status is 1 or 2
        if (jobRes.statusOfJob == 1) {
            binding.btnBid.setVisibility(View.GONE);
        }
        else if (jobRes.statusOfJob == 2) {
            binding.btnBid.setVisibility(View.VISIBLE);
        } else if (jobRes.statusOfJob == 3) {
            binding.btnBid.setText("Complete Job");
            binding.btnBid.setVisibility(View.VISIBLE);
        }


        binding.setCallback(jobRes);

       /* jobRes = getIntent().getParcelableExtra("jobRes");
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);
        binding.setCallback(jobRes);*/
        setValues();
        observeBidResponse(message);

    }

    private void setValues() {
        try {
            mProviderNotes=jobRes.description;
            if (mProviderNotes!=null && !mProviderNotes.isEmpty()){
                binding.viewProviderNotes.setVisibility(View.VISIBLE);
                binding.providerNotesTitle.setVisibility(View.VISIBLE);
                binding.providerNotesType.setVisibility(View.VISIBLE);
                binding.providerNotesType.setText(mProviderNotes);
            }
            GetAddress mAddress=jobRes.address;

           String mCleaningType = jobRes.cleaningType + " Cleaning";

           Log.d(LOG_TAG , " CleaningType "+mCleaningType);

            binding.cleaningType.setText(mCleaningType);
            if (mCleaningType.equalsIgnoreCase(getResources().getString(R.string.house_cleaning))) {
                String tasks = "";
                if (jobRes.bedrooms > 0) {
                    tasks += jobRes.bedrooms + " Bedrooms";
                }

                if (jobRes.bathrooms > 0) {
                    tasks += ", " + jobRes.bathrooms + " Bathrooms";
                }
                if (jobRes.others > 0) {
                    tasks += ", " + jobRes.others + " Others";
                }
                try {
                    if (jobRes.kitchen != 0 && jobRes.kitchen > 0) {
                        tasks += ", " + jobRes.kitchen + " Kitchens";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tasks += ", " + jobRes.sqft + " SQFT ";
                binding.taskSize.setText(tasks);
            }
            else if (mCleaningType.equalsIgnoreCase(getResources().getString(R.string.carpet_cleaning))) {
                String carpet = "";
                if (jobRes.rooms != null && jobRes.bath != null && jobRes.entry != null && jobRes.stairCase != null) {
                    carpet = getResources().getString(R.string.rooms) + " : " + jobRes.rooms.clean + " " + getResources().getString(R.string.clean) + getResources().getString(R.string.comma) + " " + jobRes.rooms.protect + " " +
                            getResources().getString(R.string.protect) + getResources().getString(R.string.comma) + " " + jobRes.rooms.deodrize + " " + "Deodorize" + " " + "\n" +
                            "Bath/Laundry" + " : " + jobRes.bath.clean + getResources().getString(R.string.clean) + "," + jobRes.bath.protect + " " + getResources().getString(R.string.protect) + getResources().getString(R.string.comma) +
                            jobRes.bath.deodrize + "Deodorize" + " " + "\n" + "Entry/Hall" + " : " + jobRes.entry.clean + getResources().getString(R.string.clean) + "," + jobRes.entry.protect + getResources().getString(R.string.protect) + getResources().getString(R.string.comma) +
                            jobRes.entry.deodrize + getResources().getString(R.string.deodorize) + " " + "\n" + getResources().getString(R.string.staircase) + " : " + jobRes.stairCase.clean + getResources().getString(R.string.clean) + getResources().getString(R.string.comma) +
                            jobRes.stairCase.protect + getResources().getString(R.string.protect) + getResources().getString(R.string.comma) + jobRes.stairCase.deodrize + getResources().getString(R.string.deodorize) + " ";
                }
                binding.taskSize.setText(carpet);
            }
            else{
                String windows = "";

                if (jobRes.firstFloorWindows > 1) {
                    windows += jobRes.firstFloorWindows + " First Floor Windows";
                } else {
                    windows += jobRes.firstFloorWindows + " First Floor Window";
                }
                if (jobRes.secondFloorWindows > 1) {
                    windows += ", " + jobRes.secondFloorWindows + " Second Floor Windows";
                } else {
                    windows += ", " + jobRes.secondFloorWindows + " Second Floor Window";
                }
                if (jobRes.frenchWindows > 1) {
                    windows += ", " + jobRes.frenchWindows + " frenchWindows";
                } else {
                    windows += ", " + jobRes.frenchWindows + " frenchWindow";
                }
                if (jobRes.slidingWindows > 1) {
                    windows += ", " + jobRes.slidingWindows + " sliding Window";
                } else {
                    windows += ", " + jobRes.slidingWindows + " sliding Windows";
                }
                if (jobRes.gardenWindows > 1) {
                    windows += ", " + jobRes.gardenWindows + " garden Windows";
                } else {
                    windows += ", " + jobRes.gardenWindows + " garden Window";
                }
                windows += ", " + jobRes.wardrobeMirrors + " wardrobe Mirrors";
                windows += ", " + jobRes.screens + " screens";
                windows += ", " + jobRes.skylights + " skylights";
                windows += ", " + jobRes.stories + " stories";
                binding.taskSize.setText(windows);
            }


            binding.address.setText(mAddress.street);

            if (jobRes.when.equals("Now")) {
                binding.when.setText(jobRes.when);
            } else if (jobRes.when.equals("Today")) {
                binding.when.setText(jobRes.when + " at " + jobRes.time);
            } else {
                //binding.when.setText(jobRes.time + " on " + jobRes.date);

                binding.when.setText(jobRes.time);
            }




//            binding.taskSize.setText( jobRes.bedrooms+" Bedrooms, " + jobRes.bathrooms + " Bathrooms, " + jobRes.kitchen + " Kitchen, "+jobRes.others+" Others, " + jobRes.sqft + " SQFT" );
            binding.pictureRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.estTime.setText("" + jobRes.estTime);
            binding.estPrice.setText("$ " + jobRes.estPrice);
            if (jobRes.jobType.equals("Fixed Price")) {
                binding.yourPrice.setText("$ " + jobRes.price);
                binding.linPrice.setWeightSum(3);
                binding.priceView2.setVisibility(View.VISIBLE);
                binding.yourPrice.setVisibility(View.VISIBLE);
            } else {
                binding.linPrice.setWeightSum(2);
                binding.yourPrice.setVisibility(View.GONE);
                binding.priceView2.setVisibility(View.GONE);
            }

            if (jobRes.preWalkNotes!=null){
                Log.d(LOG_TAG, "PreWalk Notes " +jobRes.preWalkNotes);
            }

            if (jobRes.images == null) {
                binding.pictureRecycler.setVisibility(View.GONE);
                binding.pictureTitle.setVisibility(View.GONE);
                binding.viewPicture.setVisibility(View.GONE);
            } else {
                binding.pictureRecycler.setVisibility(View.GONE);
                binding.pictureTitle.setVisibility(View.GONE);
                binding.viewPicture.setVisibility(View.GONE);
                ImageAdapter mAdapter = new ImageAdapter(this, jobRes.images, null,null);
                binding.pictureRecycler.setAdapter(mAdapter);
            }

            if (jobRes.extraCleaning == null) {
                binding.extra.setVisibility(View.GONE);
                binding.extraTitle.setVisibility(View.GONE);
                binding.viewExtra.setVisibility(View.GONE);
            } else {
                binding.extra.setVisibility(View.VISIBLE);
                binding.extraTitle.setVisibility(View.VISIBLE);
                binding.viewExtra.setVisibility(View.VISIBLE);

                String extraCleaning = "";
                for (int i = 0; i < jobRes.extraCleaning.size(); i++) {
                    ExtraCleaningRes res = jobRes.extraCleaning.get(i);
                    if (res.isSelected) {
                        if (i == 0) {
                            extraCleaning = res.name;
                        } else
                            extraCleaning = extraCleaning + ", " + res.name;
                    }
                    binding.extra.setText(extraCleaning);
                }
            }


            /*   *//*Check the provider type and then set button accordingly*//*
            if(jobRes.jobType.equals("Service Provider")){
                binding.btnReject.setVisibility(View.VISIBLE);
                binding.btnView.setVisibility(View.VISIBLE);
                binding.btnBid.setText("Accept");
                binding.btnLin.setWeightSum(2);
            }else{
                binding.btnLin.setWeightSum(1);
                binding.btnView.setVisibility(View.GONE);
                binding.btnReject.setVisibility(View.GONE);
                if (jobRes.jobType.equals("Bid")){
                    binding.btnBid.setText("Bid");
                }else{
                    binding.btnBid.setText("I'm Interested");
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onStartJob(View view) {
        // showToast("Functionality is not implemented.");

        if (jobRes.statusOfJob == 3) {
            completeJob();
        } else {
            startJob();
        }

    }

    private void startJob() {
        StartJobReq startJobReq = new StartJobReq(getPrefHelper().getAuthToken(), jobRes.jobId);
        mJobSpecViewModel.startJob(startJobReq);
        startJobResponse(message);

    }

    private void completeJob() {
        Intent mIntent = null;
        if (jobRes.preWalkNotes != null && !jobRes.preWalkNotes.isEmpty()) {
            if (jobRes.postWalkNotes != null && !jobRes.postWalkNotes.isEmpty()) {
                mIntent = new Intent(this, ReviewActivity.class);
            } else {
                mIntent = new Intent(this, PostWalkNotesActivity.class);
            }
        } else {
            mIntent = new Intent(this, PreWalkNotesActivity.class);
        }
        mIntent.putExtra("completeJob", jobRes);
        startActivity(mIntent);
    }

    private void observeBidResponse(String message) {
        mJobSpecViewModel.makeBidResLiveData()
                .observe(this, res -> {

                    showToast(message);
                });
    }

    private void startJobResponse(String message) {
        mJobSpecViewModel.startJobReqLiveData()
                .observe(this, res -> {
                    showToast(message);
                    completeJob();
                });


    }


}
