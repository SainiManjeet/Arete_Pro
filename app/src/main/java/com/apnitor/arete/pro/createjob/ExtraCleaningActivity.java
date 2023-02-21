package com.apnitor.arete.pro.createjob;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.databinding.ActivityExtraCleaningBinding;
import com.apnitor.arete.pro.profile.BaseActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.LinearLayoutManager;

public class ExtraCleaningActivity extends BaseActivity {
    AdapterExtraCleaning mAdapter;
    private ArrayList<ExtraCleaningRes> mCleaningRes;
    private ArrayList<ExtraCleaningRes> mSavedList;
    private String extraItems = "";
    private Double extraTime = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_cleaning);
        ActivityExtraCleaningBinding binding = bindContentView(R.layout.activity_extra_cleaning);
        mCleaningRes = new ArrayList<>();
        mSavedList = new ArrayList<>();
        addItems();
        setToolBar("Extras");


        LinearLayoutManager mManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mManager);
        mAdapter = new AdapterExtraCleaning(this, mCleaningRes, null);
        binding.recyclerView.setAdapter(mAdapter);
    }

    private void setupItem() {
        // Get Value from bundle
        Bundle mBundle = getIntent().getExtras();
        String mName = mBundle.getString("name", "");

        String[] itemsArray= mName.split(Pattern.quote(","));
        for (String anItemsArray : itemsArray) {
            String item = anItemsArray.trim();

            switch (item) {
                case "Deep Clean":
                    mCleaningRes.get(0).setSelected(true);
                    break;
                case "Inside Cabinets":
                    mCleaningRes.get(1).setSelected(true);
                    break;
                case "Inside Fridge":
                    mCleaningRes.get(2).setSelected(true);
                    break;
                case "Inside Oven":
                    mCleaningRes.get(3).setSelected(true);
                    break;
                case "Laundry":
                    mCleaningRes.get(4).setSelected(true);
                    break;
                case "Interior Windows":
                    mCleaningRes.get(5).setSelected(true);
                    break;

            }
        }
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();

    }

    /* This method is created from  xml*/
    public void OnSaveClick(View view) {
        mSavedList.clear();
        for (int i = 0; i < mCleaningRes.size(); i++) {
            ExtraCleaningRes res = mCleaningRes.get(i);
            if (res.isSelected) {
                if (i == 0) {
                    extraItems = res.name;
                } else if (mCleaningRes.size() - 1 == i) {
                    extraItems = extraItems + ", " + res.name;
                } else {
                    extraItems = extraItems + ", " + res.name;
                }
                extraTime = extraTime + res.extraTime;
                mSavedList.add(res);
            }
        }

        if (extraItems.startsWith(","))
            extraItems = extraItems.substring(1, extraItems.length());

        Intent mIntent = new Intent();
        mIntent.putParcelableArrayListExtra("extraList", mSavedList);
        mIntent.putExtra("extraName", extraItems);
        mIntent.putExtra("extraTime", extraTime);
        setResult(102, mIntent);
        finish();
    }


    /*Deep clean: 15 minutes per room.
    Inside Cabinets: Add 30 minutes.
    Inside Fridge: Add 15 minutes.
    Inside Oven: Add 15 minutes.
            Laundry: Add 1 hour.
    Interior Windows. Add 5 minutes per room ( living room, Kitchen, rooms... )*/



    private void addItems() {
        ExtraCleaningRes mRes = new ExtraCleaningRes();
        mRes.name = "Deep Clean";
        mRes.description = getResources().getString(R.string.extra_detail);
        mRes.extraTime = 15;
        mRes.image = R.drawable.ic_keep_clean;
        mRes.imageSelected = R.drawable.ic_keep_clean_green;
        mCleaningRes.add(mRes);
        ExtraCleaningRes mRes1 = new ExtraCleaningRes();
        mRes1.name = "Inside Cabinets";
        mRes1.description = "";
        mRes1.extraTime = 30;
        mRes1.image = R.drawable.ic_inside_cabinets;
        //
        mRes1.image = R.drawable.ic_keep_clean;
        mRes1.imageSelected = R.drawable.ic_inside_cabinets_green;
        mCleaningRes.add(mRes1);
        ExtraCleaningRes mRes2 = new ExtraCleaningRes();
        mRes2.name = "Inside Fridge";
        mRes2.description = getResources().getString(R.string.extra_detail_2);
        mRes2.extraTime = 15;
        mRes2.image = R.drawable.ic_inside_fridge;

        mRes2.image = R.drawable.ic_keep_clean;
        mRes2.imageSelected = R.drawable.ic_inside_fridge_green;
        mCleaningRes.add(mRes2);

        ExtraCleaningRes mRes3 = new ExtraCleaningRes();
        mRes3.name = "Inside Oven";
        mRes3.description = "";
        mRes3.extraTime = 15;
        mRes3.image = R.drawable.ic_inside_oven;
        mRes3.image = R.drawable.ic_keep_clean;
        mRes3.imageSelected = R.drawable.ic_inside_oven_green;
        mCleaningRes.add(mRes3);

        ExtraCleaningRes mRes4 = new ExtraCleaningRes();
        mRes4.name = "Laundry";
        mRes4.description = "";
        mRes4.extraTime = 1;
        mRes4.image = R.drawable.ic_washing;
        mRes4.image = R.drawable.ic_keep_clean;
        mRes4.imageSelected = R.drawable.ic_washing_green;
        mCleaningRes.add(mRes4);

        ExtraCleaningRes mRes5 = new ExtraCleaningRes();
        mRes5.name = "Interior Windows";
        mRes5.description = "";
        mRes5.extraTime = 5;
        mRes5.image = R.drawable.ic_interior_windows;
        mRes5.image = R.drawable.ic_keep_clean;
        mRes5.imageSelected = R.drawable.ic_interior_windows_green;
        mCleaningRes.add(mRes5);
        setupItem();
    }
}
