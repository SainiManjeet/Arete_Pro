package com.apnitor.arete.pro.createjob;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.databinding.ActivityFixedPriceBinding;
import com.apnitor.arete.pro.profile.BaseActivity;

import java.util.StringTokenizer;

public class FixedPriceActivity extends BaseActivity {
    private ActivityFixedPriceBinding binding;
    private String mPrice;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_price);
        binding = bindContentView(R.layout.activity_fixed_price);
        setToolBar(getString(R.string.fixed_price_type));

        Bundle mBundle = getIntent().getExtras();
        assert mBundle != null;
        binding.estTime.setText(mBundle.getString("estTime"));
        mPrice = mBundle.getString("estPrice");
        binding.estPrice.setText(getString(R.string.doller) + mPrice);

        addPrice();
    }

    private void addPrice() {
        StringTokenizer tokens = new StringTokenizer(mPrice, ".");
        String filteredPrice = tokens.nextToken();
        binding.npBidPrice.setValue(Integer.parseInt(filteredPrice));
        binding.txtSelectedPrice.setText(getResources().getString(R.string.price_txt) + ": " + getResources().getString(R.string.doller) + filteredPrice);
        binding.npBidPrice.setOnValueChangedListener((picker, oldVal, newVal) -> binding.txtSelectedPrice.setText(getResources().getString(R.string.price_txt) + " " + getResources().getString(R.string.doller) + newVal + ""));
    }

    public void onSubmitPrice(View view) {
        String price = String.valueOf(binding.npBidPrice.getValue());
        if (price.isEmpty() || price.startsWith("0")) {
            showToast(getString(R.string.price_error));
        } else if (price.equalsIgnoreCase(getResources().getString(R.string.price_txt))) {
            showToast(getString(R.string.choose_price_error));
        } else {
            Intent mIntent = new Intent();
            mIntent.putExtra("fixedPrice", price);
            setResult(104, mIntent);
            finish();
        }
    }
}
