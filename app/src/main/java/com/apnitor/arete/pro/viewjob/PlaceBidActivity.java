package com.apnitor.arete.pro.viewjob;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.MakeBidReq;
import com.apnitor.arete.pro.api.request.UpdateBidReq;
import com.apnitor.arete.pro.api.response.BidReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.databinding.ActivityPlaceBidBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class PlaceBidActivity extends BaseActivity {
    private static final String LOG_TAG = "PlaceBidActivity";
    ArrayList<String> priceArray = new ArrayList<>();
    private ActivityPlaceBidBinding binding;
    private String price, description;
    private JobSpecViewModel mModel;
    private GetJobRes jobRes;
    private String mType, mBidId;
    private String mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_bid);

        mType = getIntent().getStringExtra("type");
        mPosition = getIntent().getStringExtra("position");


        Log.e(LOG_TAG, " position " + mPosition + mType);

        jobRes = getIntent().getParcelableExtra("jobRes");
        binding = bindContentView(R.layout.activity_place_bid);
        setToolBar("Place Bid");
        binding.estTime.setText("" + jobRes.estTime);
        binding.estPrice.setText("$" + jobRes.estPrice);

       /* if (jobRes.description != null && !jobRes.description.isEmpty()) {
            binding.etDescription.setText(jobRes.description);
        }*/

        addPrice();

        binding.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.etDescription.getText().toString().trim().isEmpty()) {
                    showErrorOnEditText(binding.etDescription, "Please enter description.");
                } else {
                    binding.etDescription.setError(null);
                }
            }
        });

        if (mType.equalsIgnoreCase("modifyBid")) {
            mBidId = getIntent().getStringExtra("bidId");
            updateBid();
        } else {
            makeBid();
        }
    }


    private void addPrice() {
        String mPrice = jobRes.estPrice;
        StringTokenizer tokens = new StringTokenizer(mPrice, ".");
        String filteredPrice = tokens.nextToken();
        binding.npBidPrice.setValue(Integer.parseInt(filteredPrice));
        binding.txtSelectedPrice.setText(getResources().getString(R.string.bid_price_text) + ": " + getResources().getString(R.string.doller) + filteredPrice);
        binding.npBidPrice.setOnValueChangedListener((picker, oldVal, newVal) -> binding.txtSelectedPrice.setText(getResources().getString(R.string.bid_price_text) + " " + getResources().getString(R.string.doller) + newVal + ""));
    }

    private void makeBid() {
        mModel = getViewModel(JobSpecViewModel.class);
        mModel.makeBidResLiveData()
                .observe(this, res -> {
//                    showToast("Bid Placed Successfully..");
                    Bundle mBundle = new Bundle();
                    /**
                     * Set Bid here along with Bid object
                     */
                    BidReq bidReq = new BidReq();
                    bidReq.bidId = res.getBidId();
                    bidReq.bidPrice = price;
                    bidReq.bidStatus = "Active";

                    // Send description
                    mBundle.putString("description", binding.etDescription.getText().toString());
                    //
                    if (jobRes == null)
                        jobRes = new GetJobRes();
                    //
                    jobRes.bid = bidReq;
                    //

                    // jobRes.statusOfJob=0;
                    mBundle.putParcelable("jobRes", jobRes);
                    mBundle.putString("price", price);
                    mBundle.putString("bidPlaced", "true");
                    mBundle.putString("position", mPosition);
                    Intent intent = new Intent();
                    intent.putExtras(mBundle);
                    setResult(ResultCodes.PLACE_BID_RESULT, intent);
                    finish();
                });
    }


    // Update Bid
    private void updateBid() {
        mModel = getViewModel(JobSpecViewModel.class);
        mModel.updateBidResLiveData()
                .observe(this, res -> {
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("jobRes", jobRes);

                    mBundle.putString("price", price);

                    // Send description
                    mBundle.putString("description", binding.etDescription.getText().toString());

                    jobRes.bid.bidPrice = price;
                    //jobRes.bid.

                    /**
                     * TODO
                     *
                     * Add bid description here
                     *
                     */

                    Intent intent = new Intent();
                    intent.putExtras(mBundle);
                    setResult(ResultCodes.MODIFY_BID_RESULT, intent);
                    finish();
                });
    }

    public void onPlaceBid(View view) {
        price = String.valueOf(binding.npBidPrice.getValue());
        if (price.equalsIgnoreCase("Bid Price")) {
            Toast.makeText(this, "Please select Bid Price", Toast.LENGTH_SHORT).show();
            return;
        }
        description = UIUtility.getEditTextValue(binding.etDescription).trim();
        int selectedPrice = Integer.parseInt(price);

        if (selectedPrice == 0) {
            showToast(getString(R.string.price_error));
        } else if (description.isEmpty()) {
            binding.etDescription.setError("Please enter description.");
        } else {
            if (mType.equalsIgnoreCase("modifyBid")) {
                UpdateBidReq mProviderReq = new UpdateBidReq(getPrefHelper().getAuthToken(), price, description, jobRes.jobId, mBidId);
                mModel.updateBid(mProviderReq);
            } else {
                MakeBidReq mProviderReq = new MakeBidReq(getPrefHelper().getAuthToken(), price, description, jobRes.jobId, "");
                mModel.makeBid(mProviderReq);
            }
        }
    }

}
