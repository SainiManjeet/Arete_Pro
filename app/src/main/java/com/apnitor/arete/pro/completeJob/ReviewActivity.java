package com.apnitor.arete.pro.completeJob;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.request.ProviderRatingReq;
import com.apnitor.arete.pro.databinding.ActivityReviewBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.Objects;

public class
ReviewActivity extends BaseActivity {
    public static final String LOG_TAG = "ReviewActivity";
    //  int mRating = -1;
    float mRating = -1;
    String mType, mTo, mJobId, mName, mImage, mProviderId, mPosition;
    String mPostWalkNotes = "";
    int position = -1;
    private ActivityReviewBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    private boolean isProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.activity_review);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        mTo = intent.getStringExtra("to");
        mJobId = intent.getStringExtra("jobId");
        //
        if (getIntent().hasExtra("post_walk")) {
            mPostWalkNotes = getIntent().getStringExtra("post_walk");
            position = getIntent().getIntExtra("position", -1);
        }


        if (mType.equalsIgnoreCase("client")) {
            mName = intent.getStringExtra("name");
            mProviderId = intent.getStringExtra("providerId");

            mPosition = intent.getStringExtra("position");

            binding.tvName.setText(mName);
            mImage = intent.getStringExtra("image");
            if (mImage != null && !mImage.isEmpty())
                Glide.with(ReviewActivity.this).load(mImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop().circleCrop()).into(binding.ivProfile);

            jobSpecViewModel.getMyFavoriteProviders(new ChooseProviderReq(getPrefHelper().getAuthToken()), false);
            showProviderList();
        }


        binding.btnSubmit.setOnClickListener(v -> {
            if (Objects.requireNonNull(binding.etSpcNotes.getText()).length() > 0 && !binding.etSpcNotes.getText().toString().trim().isEmpty() && mRating != -1) {

                if (mType.equalsIgnoreCase("client") && mRating == 5) {
                    Log.d(LOG_TAG, " isProvider " + isProvider);
                    if (isProvider) {
                        addRating(false);
                    } else {
                        showAlert("Do you want to add " + mName + " as your Favorite Provider");
                    }
                } else {
                    addRating(false);
                }

            } else {
                Toast.makeText(getApplicationContext(), "Give your Rating & feedback first!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.rating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            mRating = rating;
            Log.d(LOG_TAG, "Rating=" + mRating);
        });

    }

    private void addRating(Boolean addFav) {
        Log.d(LOG_TAG, "mType=" + mType + "to=" + mTo + "jobId=" + mJobId);
        ProviderRatingReq providerRatingReq = new ProviderRatingReq(getPrefHelper().getAuthToken(), mTo, mRating, Objects.requireNonNull(binding.etSpcNotes.getText()).toString(),
                mJobId, addFav);
        jobSpecViewModel.providerRating(providerRatingReq);
        jobSpecViewModel.getAddRAtingResponse().observe(this, provideRes -> {
//            finish();
            //
            if (getPrefHelper().getUserType().equals("serviceProvider") || getPrefHelper().getUserType().equals("Service Provider")) {
//                Intent in = new Intent(ReviewActivity.this, HomeProviderActivity.class);
//                startActivity(in);
                finish();
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RequestCodes.REQ_CODE_ADDED_REVIEW, intent);
            } else {
                // W bf
               /* Intent in = new Intent(ReviewActivity.this, HomeActivity.class);
                startActivity(in);*/
                //s\
                Log.e(LOG_TAG, "Review Activity" + mPosition);
                Intent intent = new Intent();
                intent.putExtra("position", mPosition);
                //  setResult(ResultCodes.PAYMENT_RESULT, intent);
                setResult(RequestCodes.PAYMENT_REQUEST, intent);
                finish();
                //
            }
        });
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
        builder.setTitle(message)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    //  Call API here instead of submit Button
                    addRating(true);

                });
        builder.setNegativeButton("No", (dialogInterface, i) -> addRating(false));
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showProviderList() {
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        jobSpecViewModel.getProvidersResLiveData()
                .observe(this, res -> {
                    if (res.providerRes.size() > 0) {
                        for (int i = 0; i < res.providerRes.size(); i++) {
                            if (mProviderId.equalsIgnoreCase(res.providerRes.get(i).providerId)) {
                                isProvider = true;
                                break;
                            }
                        }
                    }
                });
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LOG_TAG," InActiVityResult rqt: "+requestCode+" Result Code "+resultCode);
        Toast.makeText(ReviewActivity.this, "In Activity Review=", Toast.LENGTH_LONG).show();

        if (resultCode == ResultCodes.PAYMENT_RESULT ||requestCode == 1008 ) {
            mPosition= data.getStringExtra("position");
            Log.e(LOG_TAG," POSITION inResult:"+mPosition);

        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!mPostWalkNotes.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra("post_walk", mPostWalkNotes);
            intent.putExtra("position", position);
            setResult(RequestCodes.REQ_CODE_ADDED_POST_WALK, intent);
        }
    }
}
