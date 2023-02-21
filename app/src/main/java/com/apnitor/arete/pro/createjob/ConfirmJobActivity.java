package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.CreateJobNewReq;
import com.apnitor.arete.pro.api.response.CreateJobRes;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.databinding.ActivityConfirmJobBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.service.UploadImageService;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ConfirmJobActivity extends BaseActivity {
    // ArrayList<ImageUrlReq> mImageList;
    private static final String LOG_TAG = "ConfirmJobActivity";
    private ActivityConfirmJobBinding binding;
    private CreateJobNewReq mJobReq;

    private GetJobRes mReq;
    private JobSpecViewModel mJobSpecViewModel;// use
    // private UploadImage mUploadImage;

    private String mProviderName, mProviderImage, mProviderId, mCleaningType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_job);
        binding = bindContentView(R.layout.activity_confirm_job);
        setToolBar(getResources().getString(R.string.confirm_job));
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);
        mJobReq = getIntent().getParcelableExtra("createJob");
        mProviderName = getIntent().getStringExtra("providerName");
        mProviderImage = getIntent().getStringExtra("providerImage");
        mProviderId = getIntent().getStringExtra("providerId");
        mCleaningType = getIntent().getStringExtra("cleaningType");
        Log.d(LOG_TAG, "mProviderName = " + mProviderName + " CleaningType: " + mCleaningType);


        // mUploadImage = new UploadImage(this);

        binding.setCallback(mJobReq);
        setValues();

        mJobSpecViewModel.createJobLiveData().observe(this, createJobRes -> {

            if (mJobReq.images.size() > 0) {
                Log.v(LOG_TAG, "JobId=" + createJobRes.jobId);
                Intent mServiceIntent = new Intent(this, UploadImageService.class);
                mServiceIntent.putExtra("jobId", createJobRes.jobId);
                mServiceIntent.putExtra("authToken", getPrefHelper().getAuthToken());
                mServiceIntent.putExtra("createJob", mJobReq);
                if (Build.VERSION.SDK_INT > 25) {
                    startForegroundService(mServiceIntent);
                } else {
                    startService(mServiceIntent);
                }
            }
            showDialogOnJobCreated(createJobRes);
        });

    }

    public void showDialogOnJobCreated(CreateJobRes createJobRes) {
        try {
            Dialog customView = new Dialog(this);
            customView.setContentView(R.layout.dialog_create_job);
            ImageView imageView = customView.findViewById(R.id.img);
            Glide.with(getApplicationContext()).load(R.drawable.job_completed).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                    .centerCrop()).into(imageView);
            customView.show();
            final Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                public void run() {
                    customView.dismiss();
                    timer2.cancel();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Job", mJobReq);
                    bundle.putString("JobId", createJobRes.jobId);

                    //
                    bundle.putString("providerImg", mProviderImage);
                    bundle.putString("providerId", mProviderId);
                    bundle.putString("providerName", mProviderName);

                    //
                    //bundle.putString("image", mReq);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);

                    setResult(ResultCodes.CONFIRM_JOB_RESULT, intent);

                    finish();

                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_edit, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_job:
                finish();
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setValues() {
        try {
            GetAddress mAddress = mJobReq.address;
            binding.cleaningType.setText(mJobReq.cleaningType + " Cleaning");
            binding.address.setText(mAddress.street + ", " + mAddress.state);

            switch (mJobReq.when) {
                case "Now":
                    binding.when.setText(mJobReq.when);
                    break;
                case "Today":
                    binding.when.setText(mJobReq.when + " at " + mJobReq.time);
                    break;
                default:
                    binding.when.setText(mJobReq.time + " on " + mJobReq.date);
                    break;
            }

            /*if (kitchen == 1) {
                tasks += ", " + kitchen + " Kitchen";
            } else {
                tasks += ", " + kitchen + " Kitchens";
            }
            */

            String tasks = "";
            if (mJobReq.bedrooms > 0) {
                if (mJobReq.bedrooms == 1) {
                    tasks += mJobReq.bedrooms + " Bedroom";
                } else {
                    tasks += mJobReq.bedrooms + " Bedrooms";
                }
            }
            if (mJobReq.kitchen > 0) {
                if (mJobReq.kitchen == 1) {
                    tasks += ", " + mJobReq.kitchen + " Kitchen";
                } else {
                    tasks += ", " + mJobReq.kitchen + " Kitchens";
                }
            }
            if (mJobReq.bathrooms > 0) {
                if (mJobReq.bathrooms == 1) {
                    tasks += ", " + mJobReq.bathrooms + " Bathroom";
                } else {
                    tasks += ", " + mJobReq.bathrooms + " Bathrooms";
                }
            }
            if (mJobReq.others > 0) {
                if (mJobReq.others == 1) {
                    tasks += ", " + mJobReq.others + " Other";
                } else {
                    tasks += ", " + mJobReq.others + " Others";
                }
            }
            tasks += ", " + mJobReq.sqft + " SQFT ";




            if (mCleaningType.equalsIgnoreCase("house"))
                binding.taskSize.setText(tasks);
            if (mCleaningType.equalsIgnoreCase("carpet"))
                binding.taskSize.setText(getIntent().getStringExtra("tasks"));
            if (mCleaningType.equalsIgnoreCase("window"))
                binding.taskSize.setText(getIntent().getStringExtra("tasks"));

//            binding.taskSize.setText( mJobReq.bedrooms+" Bedrooms, " + mJobReq.bathrooms + " Bathrooms, " + mJobReq.kitchen + " Kitchen, "+mJobReq.others+" Others, " + mJobReq.sqft + " SQFT" );
            binding.pictureRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.estTime.setText("" + mJobReq.estTime);
            binding.estPrice.setText("$ " + mJobReq.estPrice);
            if (mJobReq.jobType.equals("Fixed Price")) {
                binding.yourPrice.setText("$ " + mJobReq.price);
                binding.linPrice.setWeightSum(3);
                binding.priceView2.setVisibility(View.VISIBLE);
                binding.yourPrice.setVisibility(View.VISIBLE);
            } else {
                binding.linPrice.setWeightSum(2);
                binding.yourPrice.setVisibility(View.GONE);
                binding.priceView2.setVisibility(View.GONE);
            }
            if (mJobReq.images.isEmpty()) {
                binding.pictureRecycler.setVisibility(View.GONE);
                binding.pictureTitle.setVisibility(View.GONE);
                binding.viewPicture.setVisibility(View.GONE);
            } else {

                binding.pictureRecycler.setVisibility(View.VISIBLE);
                binding.pictureTitle.setVisibility(View.VISIBLE);
                binding.viewPicture.setVisibility(View.VISIBLE);
                binding.pictureTitle.setText("Uploaded Picture(s)");
                ImageAdapter mAdapter = new ImageAdapter(this, mJobReq.images, null, null);
                binding.pictureRecycler.setAdapter(mAdapter);
            }

            if (mJobReq.extraCleaning.isEmpty()) {
                binding.extra.setVisibility(View.GONE);
                binding.extraTitle.setVisibility(View.GONE);
                binding.viewExtra.setVisibility(View.GONE);
            } else {
                binding.extra.setVisibility(View.VISIBLE);
                binding.extraTitle.setVisibility(View.VISIBLE);
                binding.viewExtra.setVisibility(View.VISIBLE);
            }
            String extraCleaning = "";
            for (int i = 0; i < mJobReq.extraCleaning.size(); i++) {
                ExtraCleaningRes res = mJobReq.extraCleaning.get(i);
                if (res.isSelected) {

                    if (i == 0) {
                        extraCleaning = res.name;
                    } else
                        extraCleaning = extraCleaning + ", " + res.name;
                }
                binding.extra.setText(extraCleaning);
            }
            // Check is Special Notes Available
            if (mJobReq.specialNotesForProvider != null && !mJobReq.specialNotesForProvider.isEmpty()) {
                binding.viewProviderNotes.setVisibility(View.VISIBLE);
                binding.providerNotesTitle.setVisibility(View.VISIBLE);
                binding.providerNotesType.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onConfirmClick(View view) {
        // Check for Bid/Fixed Price : Provider id must be empty in that case
        if (mJobReq.jobType.equalsIgnoreCase("Bid") || mJobReq.jobType.equalsIgnoreCase("Fixed Price")) {
            mJobReq.providerId = "";
        }
        Log.d(LOG_TAG, " API PAYLOAD " + mJobReq.toString());
        // Clear all images
//        mJobReq.images.clear();
        mJobSpecViewModel.createJob(mJobReq);
        saveLastJob(mJobReq);

    }

    void saveLastJob(CreateJobNewReq mJobReq) {
        String createJobReqJson = new Gson().toJson(mJobReq);
        PreferenceHandler.writeString(this, PreferenceHandler.PREF_KEY_CREATE_JOB, createJobReqJson);
    }

    public void onEditClick(View view) {
        finish();
    }


}
