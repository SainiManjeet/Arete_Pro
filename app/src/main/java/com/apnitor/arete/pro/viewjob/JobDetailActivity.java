package com.apnitor.arete.pro.viewjob;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.createjob.ImageAdapter;
import com.apnitor.arete.pro.createjob.SingleImageActivity;
import com.apnitor.arete.pro.databinding.ActivityJobDetailBinding;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

public class JobDetailActivity extends BaseActivity implements ListItemClickWithPositionCallback {
    public static final String LOG_TAG = "JobDetailActivity";
    private ActivityJobDetailBinding binding;
    private JobSpecViewModel mJobSpecViewModel;
    private GetJobRes jobRes;
    private String message = "", mProviderNotes, mPosition, mType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.activity_job_detail);
        setToolBar("Job Details");
        jobRes = getIntent().getParcelableExtra("jobRes");
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);

        mPosition = getIntent().getStringExtra("position");
        mType = getIntent().getStringExtra("type");

        binding.setCallback(jobRes);
        setValues();
        observeBidResponse(message);
    }


    private void setValues() {
        try {
            // Here bid description refers to Provider Special Notes
            mProviderNotes = jobRes.description;
            Log.e(LOG_TAG, " jobRes  " + jobRes.toString());
            if (mProviderNotes != null && !mProviderNotes.isEmpty()) {
                binding.viewProviderNotes.setVisibility(View.VISIBLE);
                binding.providerNotesTitle.setVisibility(View.VISIBLE);
                binding.providerNotesType.setVisibility(View.VISIBLE);
                binding.providerNotesType.setText(mProviderNotes);
            }
            GetAddress mAddress = jobRes.address;
            String mCleaningType = jobRes.cleaningType + " Cleaning";
            binding.cleaningType.setText(mCleaningType);

            if (mCleaningType.equalsIgnoreCase(getResources().getString(R.string.house_cleaning))) {
                String tasks = "";

                if (jobRes.bedrooms != 1 && jobRes.bedrooms > 1) {
                    tasks += jobRes.bedrooms + " Bedrooms";
                } else {
                    tasks += jobRes.bedrooms + getResources().getString(R.string.bedroom);
                }

                if (jobRes.bathrooms != 1 && jobRes.bathrooms > 1) {
                    tasks += ", " + jobRes.bathrooms + " Bathrooms";
                } else {
                    tasks += ", " + jobRes.bathrooms + getResources().getString(R.string.space) + getResources().getString(R.string.bathroom);
                }


                if (jobRes.others != 1 && jobRes.others > 1) {
                    tasks += ", " + jobRes.others + " Others";
                } else {
                    tasks += ", " + jobRes.others + getResources().getString(R.string.space) + getResources().getString(R.string.other);
                }
                try {
                    if (jobRes.kitchen != 1 && jobRes.kitchen > 1) {
                        tasks += ", " + jobRes.kitchen + " Kitchen";
                    } else {
                        tasks += ", " + jobRes.kitchen + getResources().getString(R.string.kitchen);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tasks += ", " + jobRes.sqft + " SQFT ";
                binding.taskSize.setText(tasks);
            } else if (mCleaningType.equalsIgnoreCase(getResources().getString(R.string.carpet_cleaning))) {
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
            } else {
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

            String time = "";
            time += jobRes.time;
            String mWhen = jobRes.when;
            if (mWhen.equalsIgnoreCase(getResources().getString(R.string.when_future))) {
                if (jobRes.date != null && jobRes.endDate != null) {
                    time += " " + jobRes.date + " to " + jobRes.endDate;
                }

            } else {
                if (jobRes.date != null)
                    time += " " + jobRes.date;
            }
            binding.when.setText(time);

            binding.pictureRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.estTime.setText("" + jobRes.estTime);
            binding.estPrice.setText("$ " + jobRes.estPrice);
            if (jobRes.jobType.equals("Fixed Price")) {

                if (getPrefHelper().getUserType().equals("serviceProvider")) {
                    binding.txtTitlePrice.setText("Client Price");
                }

                binding.yourPrice.setText("$ " + jobRes.price);
                binding.linPrice.setWeightSum(3);
                binding.priceView2.setVisibility(View.VISIBLE);
                binding.yourPrice.setVisibility(View.VISIBLE);
            } else {
                binding.linPrice.setWeightSum(2);
                binding.yourPrice.setVisibility(View.GONE);
                binding.priceView2.setVisibility(View.GONE);
            }
            if (jobRes.images == null) {
                binding.pictureRecycler.setVisibility(View.GONE);
                binding.pictureTitle.setVisibility(View.GONE);
                binding.viewPicture.setVisibility(View.GONE);
            } else {
                binding.viewUploadedPictures.setVisibility(View.VISIBLE);
                binding.pictureRecycler.setVisibility(View.VISIBLE);
                binding.pictureTitle.setText("Uploaded Picture(s)");
                binding.pictureTitle.setVisibility(View.VISIBLE);
                binding.viewPicture.setVisibility(View.VISIBLE);
                ImageAdapter mAdapter = new ImageAdapter(this, jobRes.images, this, null);
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

            if (mType.equalsIgnoreCase("pastJobs")) {
                binding.btnLin.setVisibility(View.INVISIBLE);
            } else {
                /*Check the provider type and then set button accordingly*/
                if (jobRes.jobType.equals("Service Provider")) {
                    binding.btnReject.setVisibility(View.VISIBLE);
                    binding.btnView.setVisibility(View.VISIBLE);
                    binding.btnBid.setText("Accept");
                    binding.btnLin.setWeightSum(2);
                } else {
                    binding.btnLin.setWeightSum(1);
                    binding.btnView.setVisibility(View.GONE);
                    binding.btnReject.setVisibility(View.GONE);
                    if (jobRes.jobType.equals("Bid")) {
                        binding.btnBid.setText("Bid");
                        // added
                        //model.bidPlaced
                        if (jobRes.bidPlaced) {
                            binding.btnBid.setText("Modify Bid");
                        }
                    } else {
                        binding.btnBid.setText("I'm Interested");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCodes.PLACE_BID_RESULT) {
            GetJobRes job = data.getParcelableExtra("jobRes");
            String price = data.getStringExtra("price");
            String position = data.getStringExtra("position");
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("jobRes", job);
            mBundle.putString("price", price);
            mBundle.putString("bidPlaced", "true");
            mBundle.putString("position", position);
            Intent intent = new Intent();
            intent.putExtras(mBundle);
            setResult(ResultCodes.PLACE_BID_RESULT, intent);
            finish();
        }
    }

    public void onBidClick(View view) {
        if (jobRes.jobType.equals("Bid")) {
            if (jobRes.bidPlaced) {
                showToast(getApplicationContext().getString(R.string.functionality_pending));
            } else {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("jobRes", jobRes);
                Intent intent = new Intent(JobDetailActivity.this, PlaceBidActivity.class);
                intent.putExtras(mBundle);
                intent.putExtra("position", mPosition);
                intent.putExtra("type", "bidPlace");
                startActivityForResult(intent, RequestCodes.PLACE_BID_REQUEST);
            }
        } else if (jobRes.jobType.equals("Fixed Price")) {
            MakeBidReq mProviderReq = new MakeBidReq(getPrefHelper().getAuthToken(), jobRes.jobId);
            mJobSpecViewModel.makeBid(mProviderReq);
            message = "Bid Placed Successfully..";
            observeBidResponse(message);
        }
        // add condition here for bid placed
        else {
            MakeBidReq mProviderReq = new MakeBidReq(getPrefHelper().getAuthToken(), jobRes.jobId, "Accepted");
            mJobSpecViewModel.makeBid(mProviderReq);
            message = "Job accepted..";
            observeBidResponse(message);
        }
    }

    public void onRejectClick(View view) {
        MakeBidReq mProviderReq = new MakeBidReq(getPrefHelper().getAuthToken(), jobRes.jobId, "Rejected");
        mJobSpecViewModel.makeBid(mProviderReq);
        message = getResources().getString(R.string.bid_rejected);
    }

    private void observeBidResponse(String message) {
        mJobSpecViewModel.makeBidResLiveData()
                .observe(this, res -> {
                    showToast(message);
                    finish();
                });
    }

    @Override
    public void onListItemWithPositionClick(Object object, int position) {
        Intent mIntent = new Intent(this, SingleImageActivity.class);
        mIntent.putExtra("jobRes", jobRes);
        mIntent.putExtra("position", position);
        startActivity(mIntent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
