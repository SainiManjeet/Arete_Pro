package com.apnitor.arete.pro.profile;

import android.os.Bundle;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.ReviewRes;
import com.apnitor.arete.pro.createjob.AdapterReviewsProvider;
import com.apnitor.arete.pro.databinding.ProfileRatingReviewBinding;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;


public class RatingReview extends BaseActivity {
    public static final String LOG_TAG = "MyRatingReview";
    private ProfileRatingReviewBinding binding;
    private JobSpecViewModel jobSpecViewModel;
    public ArrayList<ReviewRes> reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.profile_rating_review);
        setToolBar(getResources().getString(R.string.review));
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mManager);


        final Bundle extras = getIntent().getExtras();

        if(extras!=null){
            reviews = extras.getParcelableArrayList("Reviews");
        }

        showProviderList();


    }

    private void showProviderList() {
        AdapterReviewsProvider adapter = new AdapterReviewsProvider(RatingReview.this, reviews);
        binding.recyclerView.setAdapter(adapter);

    }
}
