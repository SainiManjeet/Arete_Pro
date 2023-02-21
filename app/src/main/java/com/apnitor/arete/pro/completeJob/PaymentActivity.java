package com.apnitor.arete.pro.completeJob;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.StartJobReq;
import com.apnitor.arete.pro.createjob.RequestJobsAdapter;
import com.apnitor.arete.pro.databinding.ActivityPaymentBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

public class PaymentActivity extends BaseActivity {
    private static final String LOG_TAG = "PaymentActivity";
    String mType, mJobId, mTo, mName, mImage, mPrice, mProviderId,mPosition;
    private JobSpecViewModel mJobSpecViewModel;
   // private HomeActivity activity;
    private static RequestJobsAdapter mReqJobsAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPaymentBinding binding = bindContentView(R.layout.activity_payment);
        setToolBar(getResources().getString(R.string.payment));
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);
        getData();
        binding.txtPrice.setText(getString(R.string.pay_price) + " $" + mPrice);
        binding.btnPay.setOnClickListener(v -> payForJob());


     //   activity = (HomeActivity) getApplicationContext();
    }

    private void getData() {
        mType = getIntent().getStringExtra("type");
        mJobId = getIntent().getStringExtra("jobId");
        mTo = getIntent().getStringExtra("to");
        mName = getIntent().getStringExtra("name");
        mImage = getIntent().getStringExtra("image");
        //
        mPosition = getIntent().getStringExtra("position");

        Log.e(LOG_TAG, "position in Pay:"+mPosition);



        if (getIntent().getStringExtra("clientPrice") != null && !getIntent().getStringExtra("clientPrice").isEmpty())
            mPrice = getIntent().getStringExtra("clientPrice");
        else
            mPrice = getIntent().getStringExtra("price");

        mProviderId = getIntent().getStringExtra("providerId");//


        Log.e("PaymentActivity", " name is: " + mName + " jobId " + mJobId);
    }

    private void payForJob() {
        StartJobReq startJobReq = new StartJobReq(getPrefHelper().getAuthToken(), mJobId);
        mJobSpecViewModel.payForJob(startJobReq);
        String message = "";
        payJobResponse(message);
    }


    private void payJobResponse(String message) {
        mJobSpecViewModel.payJobReqLiveData()
                .observe(this, res -> {
                    showToast(message);
                    paySuccessfully();
                });
    }

    /* Start Rating for Provider*/
    private void paySuccessfully() {
        /*Intent in = new Intent(PaymentActivity.this, ReviewActivity.class);
        in.putExtra("type", mType);
        in.putExtra("jobId", mJobId);
        in.putExtra("to", mTo);
        in.putExtra("name", mName);
        in.putExtra("image", mImage);
        in.putExtra("providerId", mProviderId);
        in.putExtra("position", mPosition);
        startActivity(in);*/

        //
       // finish();
        Intent in = new Intent(PaymentActivity.this, ReviewActivity.class);
        in.putExtra("type", mType);
        in.putExtra("jobId", mJobId);
        in.putExtra("to", mTo);
        in.putExtra("name", mName);
        in.putExtra("image", mImage);
        in.putExtra("providerId", mProviderId);
        in.putExtra("position", mPosition);
        in.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(in);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        finish();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(PaymentActivity.this, "In Activity Payment=", Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG," InActiVityResult rqt: "+requestCode+" Result Code "+resultCode);
        if (resultCode == ResultCodes.PAYMENT_RESULT ||resultCode == 2006 ) {
            Toast.makeText(PaymentActivity.this, "In Activity Payment=", Toast.LENGTH_LONG).show();
            String position = data.getStringExtra("position");



        }
    }*/
}
