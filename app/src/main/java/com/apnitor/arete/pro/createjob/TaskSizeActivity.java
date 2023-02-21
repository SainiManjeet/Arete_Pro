package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.databinding.ActivityTaskSizeBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class TaskSizeActivity extends BaseActivity {
    String LOG_TAG = "TaskSizeActivity";
    String mSqft;
    String mCleaningType;
    private ActivityTaskSizeBinding binding;
    private int bedroom, bathroom, kitchen, other;
    private int mFirstFloor, mSecondFloor, mFrenchWindows, mPatioDoor, mGardenWindows, mWardrobeMirror, mScreens, mSkylight, mStories;
    private float totalSqft = 0.0f;
    private String mTaskSize = "Task Size(Full House Clean)";
    private int mCleanRoom, mCleanBath, mCleanEntryHall, mCleanStaircase;
    private int mProtectRoom, mProtectBath, mProtectEntryHall, mProtectStaircase;
    private int mDeoRoom, mDeoBath, mDeoEntryHall, mDeoStaircase;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_size);
        binding = bindContentView(R.layout.activity_task_size);
        setToolBar(getResources().getString(R.string.task_size));
        setValues();
        binding.seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                mSqft = seekParams.progress + "";
                binding.txtStart.setText(seekParams.progress + "");
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });


        binding.btnFullClean.setOnClickListener(v -> {
            mTaskSize = "Task Size (Full House Clean)";
            binding.btnPartialClean.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
            binding.btnPartialClean.setTextColor(getApplication().getResources().getColor(R.color.colorBlack));

            binding.btnFullClean.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.btnFullClean.setTextColor(getApplication().getResources().getColor(R.color.colorWhite));

            binding.tvBedroomsValue.setText(bedroom + "");
            binding.tvBathroomsValue.setText(bathroom + "");
            binding.tvKitchenValue.setText(kitchen + "");
            binding.tvOthersValue.setText(other + "");


            //Here value must come from pref
            setfullClean();
        });


        binding.btnPartialClean.setOnClickListener(v -> {
            mTaskSize = "Task Size (Partial House Clean)";
            binding.btnFullClean.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
            binding.btnFullClean.setTextColor(getApplication().getResources().getColor(R.color.colorBlack));

            binding.btnPartialClean.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.btnPartialClean.setTextColor(getApplication().getResources().getColor(R.color.colorWhite));

            binding.sbBedrooms.setEnabled(true);
            binding.sbBathrooms.setEnabled(true);
            binding.sbKitchen.setEnabled(true);
            binding.sbOthers.setEnabled(true);

            binding.seekBar.setEnabled(true);
            binding.seekBar.setVisibility(View.VISIBLE);

            binding.seekBar.setMax(totalSqft);
            binding.txtEnd.setText("" + totalSqft);

            binding.relativeLayoutSeek.setVisibility(View.VISIBLE);
            binding.noOFBedrooms.setEnabled(true);
            binding.noOfKitchens.setEnabled(true);
            binding.noOfBathroom.setEnabled(true);
            binding.noOfOther.setEnabled(true);

            binding.tvBedroomsValue.setText(bedroom + "");
            binding.tvBathroomsValue.setText(bathroom + "");
            binding.tvKitchenValue.setText(kitchen + "");
            binding.tvOthersValue.setText(other + "");


        });
    }


    @SuppressLint("SetTextI18n")
    private void setValues() {
        mCleaningType = getIntent().getStringExtra("mCleaningType");

        if (mCleaningType.equalsIgnoreCase("House")) {

            bedroom = getIntent().getIntExtra("Bedrooms", 0);
            bathroom = getIntent().getIntExtra("Bathrooms", 0);
            kitchen = getIntent().getIntExtra("Kitchen", 0);
            other = getIntent().getIntExtra("Others", 0);

            binding.tvBedroomsValue.setText(bedroom + "");
            binding.tvBathroomsValue.setText(bathroom + "");
            binding.tvKitchenValue.setText(kitchen + "");
            binding.tvOthersValue.setText(other + "");

            binding.noOFBedrooms.setValue(bedroom);
            binding.noOfKitchens.setValue(kitchen);
            binding.noOfBathroom.setValue(bathroom);
            binding.noOfOther.setValue(other);

            binding.noOFBedrooms.setMaxValue(bedroom);
            binding.noOfBathroom.setMaxValue(bathroom);
            binding.noOfKitchens.setMaxValue(kitchen);
            binding.noOfOther.setMaxValue(other);

            binding.tvBedroomsValue.setText(bedroom + "");
            binding.tvBathroomsValue.setText(bathroom + "");
            binding.tvKitchenValue.setText(kitchen + "");
            binding.tvOthersValue.setText(other + "");


            binding.noOFBedrooms.setOnValueChangedListener((picker, oldVal, newVal) -> binding.tvBedroomsValue.setText(newVal + ""));
            binding.noOfKitchens.setOnValueChangedListener((picker, oldVal, newVal) -> binding.tvKitchenValue.setText(newVal + ""));
            binding.noOfBathroom.setOnValueChangedListener((picker, oldVal, newVal) -> binding.tvBathroomsValue.setText(newVal + ""));
            binding.noOfOther.setOnValueChangedListener((picker, oldVal, newVal) -> binding.tvOthersValue.setText(newVal + ""));


            Log.d("BedroomVal=", "==" + bedroom + "=Kit=" + kitchen);
            binding.noOFBedrooms.setValue(bedroom);
            binding.noOfKitchens.setValue(kitchen);
            binding.tvBedroomsValue.setText(bedroom + "");

            binding.tvBedroomsValue.setText(bedroom + "");
            binding.tvBathroomsValue.setText(bathroom + "");
            binding.tvKitchenValue.setText(kitchen + "");
            binding.tvOthersValue.setText(other + "");

            binding.noOfOther.setValue(other);
       /* if (!Sqft.toString().trim().equals("0.0")) {
            binding.txtStart.setText("" + 0);
            // binding.etSqft.setText("" + Sqft);
            //binding.etSqft.setSelection(Objects.requireNonNull(binding.etSqft.getText()).length());
        }*/

            //mSqft = "" + totalSqft;
        }
        if (mCleaningType.equals("Carpet")) {

            bathroom = 0;
            kitchen = 0;
            binding.noOfKitchens.setValue(0);
            binding.noOFBedrooms.setValue(0);
            binding.relBathrooms.setVisibility(View.GONE);
            binding.relKitchen.setVisibility(View.GONE);
            binding.txtStart.setText("" + totalSqft);

            binding.carpetCleaningLayout.setVisibility(View.VISIBLE);
            binding.houseCleaningLayout.setVisibility(View.GONE);

            mCleanRoom = getIntent().getIntExtra("mCleanRoom", 0);
            mProtectRoom = getIntent().getIntExtra("mProtectRoom", 0);
            mDeoRoom = getIntent().getIntExtra("mDeoRoom", 0);

            mCleanBath = getIntent().getIntExtra("mCleanBath", 0);
            mProtectBath = getIntent().getIntExtra("mProtectBath", 0);
            mDeoBath = getIntent().getIntExtra("mDeoBath", 0);

            mCleanEntryHall = getIntent().getIntExtra("mCleanEntryHall", 0);
            mProtectEntryHall = getIntent().getIntExtra("mProtectEntryHall", 0);
            mDeoEntryHall = getIntent().getIntExtra("mDeoEntryHall", 0);

            mCleanStaircase = getIntent().getIntExtra("mCleanStaircase", 0);
            mProtectStaircase = getIntent().getIntExtra("mProtectStaircase", 0);
            mDeoStaircase = getIntent().getIntExtra("mDeoStaircase", 0);
            // set Values
            binding.carpetCleaning.npCleanRoom.setValue(mCleanRoom);
            binding.carpetCleaning.npCleanBath.setValue(mCleanBath);
            binding.carpetCleaning.npCleanEntryHall.setValue(mCleanEntryHall);
            binding.carpetCleaning.npCleanStaircase.setValue(mCleanStaircase);

            binding.carpetCleaning.npProtectRoom.setValue(mProtectRoom);
            binding.carpetCleaning.npProtectBath.setValue(mProtectBath);
            binding.carpetCleaning.npProtectEntryHall.setValue(mProtectEntryHall);
            binding.carpetCleaning.npProtectStaircase.setValue(mProtectStaircase);

            binding.carpetCleaning.npDeodorizeRoom.setValue(mDeoRoom);
            binding.carpetCleaning.npDeodorizeRoomBath.setValue(mDeoBath);
            binding.carpetCleaning.npDeodorizeRoomEntryHall.setValue(mDeoEntryHall);
            binding.carpetCleaning.npDeodorizeStaircase.setValue(mDeoStaircase);
//
        }


        if (mCleaningType.equals("Window")) {
            binding.windowCleaningLayout.setVisibility(View.VISIBLE);
            binding.houseCleaningLayout.setVisibility(View.GONE);

            // Get Intent
            mFirstFloor = getIntent().getIntExtra("mFirstFloor", 0);
            mSecondFloor = getIntent().getIntExtra("mSecondFloor", 0);
            mFrenchWindows = getIntent().getIntExtra("mFrenchWindows", 0);
            mPatioDoor = getIntent().getIntExtra("mPatioDoor", 0);
            mGardenWindows = getIntent().getIntExtra("mGardenWindows", 0);
            mWardrobeMirror = getIntent().getIntExtra("mWardrobeMirror", 0);
            mScreens = getIntent().getIntExtra("mScreens", 0);
            mSkylight = getIntent().getIntExtra("mSkylight", 0);
            mStories = getIntent().getIntExtra("mStories", 0);

            // Set value
            binding.windowCleaning.npWindowFirstFloor.setValue(mFirstFloor);
            binding.windowCleaning.npWindowSecondFloor.setValue(mSecondFloor);
            binding.windowCleaning.npWindowFrench.setValue(mFrenchWindows);
            binding.windowCleaning.npPatioDoor.setValue(mPatioDoor);
            binding.windowCleaning.npWindowGarden.setValue(mGardenWindows);
            binding.windowCleaning.npWardrobeMirror.setValue(mWardrobeMirror);
            binding.windowCleaning.npScreens.setValue(mScreens);
            binding.windowCleaning.npSkylight.setValue(mSkylight);
            binding.windowCleaning.npStories.setValue(mStories);
        }
        setSqftValue();
        setFullCleanDisabled();
    }

    private void setSqftValue() {
        double Sqft = getIntent().getDoubleExtra("SQFT", 0.0);
        totalSqft = (float) Sqft;
        binding.seekBar.setProgress(totalSqft);
        binding.txtStart.setText(String.valueOf(totalSqft));

        mSqft = String.valueOf(totalSqft);

    }

    private void setFullCleanDisabled() {
        binding.noOFBedrooms.setEnabled(false);
        binding.noOfKitchens.setEnabled(false);
        binding.noOfBathroom.setEnabled(false);
        binding.noOfOther.setEnabled(false);
        binding.seekBar.setEnabled(false);
    }

    private void getValuesForWindowCleaning() {
        mFirstFloor = binding.windowCleaning.npWindowFirstFloor.getValue();
        mSecondFloor = binding.windowCleaning.npWindowSecondFloor.getValue();
        mFrenchWindows = binding.windowCleaning.npWindowFrench.getValue();
        mPatioDoor = binding.windowCleaning.npPatioDoor.getValue();
        mGardenWindows = binding.windowCleaning.npWindowGarden.getValue();
        mWardrobeMirror = binding.windowCleaning.npWardrobeMirror.getValue();
        mScreens = binding.windowCleaning.npScreens.getValue();
        mSkylight = binding.windowCleaning.npSkylight.getValue();
        mStories = binding.windowCleaning.npStories.getValue();
    }

    private void getValuesForCarpetCleaning() {
        mCleanRoom = binding.carpetCleaning.npCleanRoom.getValue();
        mCleanBath = binding.carpetCleaning.npCleanBath.getValue();
        mCleanEntryHall = binding.carpetCleaning.npCleanEntryHall.getValue();
        mCleanStaircase = binding.carpetCleaning.npCleanStaircase.getValue();

        mProtectRoom = binding.carpetCleaning.npProtectRoom.getValue();
        mProtectBath = binding.carpetCleaning.npProtectBath.getValue();
        mProtectEntryHall = binding.carpetCleaning.npProtectEntryHall.getValue();
        mProtectStaircase = binding.carpetCleaning.npProtectStaircase.getValue();

        mDeoRoom = binding.carpetCleaning.npDeodorizeRoom.getValue();
        mDeoBath = binding.carpetCleaning.npDeodorizeRoomBath.getValue();
        mDeoEntryHall = binding.carpetCleaning.npDeodorizeRoomEntryHall.getValue();
        mDeoStaircase = binding.carpetCleaning.npDeodorizeStaircase.getValue();
    }


    public void onDoneClick(View view) {

      /*  if (mCleaningType.equalsIgnoreCase("Carpet")) {
            getValuesForCarpetCleaning();
        }
        if (mCleaningType.equalsIgnoreCase("Window")) {
            getValuesForWindowCleaning();
        }*/

        String sqft = mSqft;
        bedroom = binding.noOFBedrooms.getValue();
        bathroom = binding.noOfBathroom.getValue();

        other = binding.noOfOther.getValue();
        kitchen = binding.noOfKitchens.getValue();


        if (mCleaningType.equals("House")) {
            if (sqft == null || sqft.isEmpty() || binding.txtStart.getText().toString().isEmpty() || binding.txtStart.getText().toString().equalsIgnoreCase("0.0")) {
                Toast.makeText(TaskSizeActivity.this, "Please choose a valid sqft value", Toast.LENGTH_SHORT).show();
            } else if (sqft.startsWith("0") || binding.txtStart.getText().toString().startsWith("0")) {
                Toast.makeText(TaskSizeActivity.this, "Please choose a valid sqft value", Toast.LENGTH_SHORT).show();
            } else if (bedroom == 0 && bathroom == 0 && kitchen == 0 && other == 0) {
                showToast("Please select at least one room");
            } else {
                saveDataInPrefrences();
                Intent mIntent = new Intent();
                Log.d("mCleaningType=", "mCleaningType" + mCleaningType);
                mIntent.putExtra("cleaningType", "House");
                mIntent.putExtra("SQFT", sqft);
                mIntent.putExtra("Bedrooms", bedroom);
                mIntent.putExtra("Kitchen", kitchen);
                mIntent.putExtra("Bathrooms", bathroom);
                mIntent.putExtra("Others", other);
                mIntent.putExtra("TaskSize", mTaskSize);
                setResult(111, mIntent);
                finish();
            }
        } else {
            saveDataInPrefrences();
            Intent mIntent = new Intent();
            if (mCleaningType.equals("Window")) {
                getValuesForWindowCleaning();
                mIntent.putExtra("cleaningType", "Window");
                mIntent.putExtra("firstFloor", mFirstFloor);
                mIntent.putExtra("secondFloor", mSecondFloor);
                mIntent.putExtra("frenchWindows", mFrenchWindows);
                mIntent.putExtra("patioDoor", mPatioDoor);
                mIntent.putExtra("gardenWindows", mGardenWindows);
                mIntent.putExtra("wardrobeMirror", mWardrobeMirror);
                mIntent.putExtra("screens", mScreens);
                mIntent.putExtra("skylight", mSkylight);
                mIntent.putExtra("stories", mStories);

            } else if (mCleaningType.equals("Carpet")) {
                getValuesForCarpetCleaning();

                mIntent.putExtra("cleaningType", "Carpet");
                mIntent.putExtra("roomClean", mCleanRoom);
                mIntent.putExtra("roomProtect", mProtectRoom);
                mIntent.putExtra("roomDeo", mDeoRoom);

                mIntent.putExtra("bathClean", mCleanBath);
                mIntent.putExtra("bathProtect", mProtectBath);
                mIntent.putExtra("bathDeo", mDeoBath);

                mIntent.putExtra("entryClean", mCleanEntryHall);
                mIntent.putExtra("entryProtect", mProtectEntryHall);
                mIntent.putExtra("entryDeo", mDeoEntryHall);

                mIntent.putExtra("staircaseClean", mCleanStaircase);
                mIntent.putExtra("staircaseProtect", mProtectStaircase);
                mIntent.putExtra("staircaseDeo", mDeoStaircase);

            }

            switch (mCleaningType) {
                case "Carpet":
                    if (mCleanRoom == 0 && mProtectRoom == 0 && mDeoRoom == 0 && mCleanBath == 0 && mProtectBath == 0 && mDeoBath == 0 && mCleanEntryHall == 0 && mProtectEntryHall == 0
                            && mDeoEntryHall == 0 && mCleanStaircase == 0 && mProtectStaircase == 0 && mDeoStaircase == 0) {
                        showToast(getResources().getString(R.string.task_size_validation));
                    } else {
                        setResult(111, mIntent);
                        finish();
                    }
                    break;
                case "Window":
                    if (mFirstFloor == 0 && mSecondFloor == 0 && mFrenchWindows == 0 && mPatioDoor == 0 && mGardenWindows == 0 && mWardrobeMirror == 0 && mScreens == 0 && mSkylight == 0 && mStories == 0) {
                        showToast(getResources().getString(R.string.task_size_window_validation));
                    } else {
                        setResult(111, mIntent);
                        finish();
                    }
                    break;
                default:
                    setResult(111, mIntent);
                    finish();
                    break;
            }

        }
    }

    private void saveDataInPrefrences() {
        if (mCleaningType.equals("Window")) {
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_FIRST_FLOOR, mFirstFloor);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_SECOND_FLOOR, mSecondFloor);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_FRENCH_WINDOWS, mFrenchWindows);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_PATIO_DOOR, mPatioDoor);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_GARDEN_WINDOW, mGardenWindows);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_WARDROBE_MIRROR, mWardrobeMirror);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_SCREENS, mScreens);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_SKYLIGHT, mSkylight);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_STORIES, mStories);
        } else {
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_TOTAL_BEDROOM, bedroom);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_TOTAL_BATHROOM, bathroom);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_TOTAL_KITCHEN, kitchen);
            PreferenceHandler.writeInteger(TaskSizeActivity.this, PreferenceHandler.PREF_TOTAL_OTHER, other);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setfullClean() {

        binding.noOFBedrooms.setEnabled(false);
        binding.noOfKitchens.setEnabled(false);
        binding.noOfBathroom.setEnabled(false);
        binding.noOfOther.setEnabled(false);

        binding.sbBathrooms.setProgress(bathroom);
        binding.sbBathrooms.setEnabled(false);
        binding.sbKitchen.setProgress(kitchen);
        binding.sbKitchen.setEnabled(false);
        binding.sbOthers.setProgress(other);
        binding.sbOthers.setEnabled(false);
        // binding.etSqft.setEnabled(false);
        binding.seekBar.setEnabled(false);

        binding.tvBedroomsValue.setText(bedroom + "");
        binding.tvBathroomsValue.setText(bathroom + "");
        binding.tvKitchenValue.setText(kitchen + "");
        binding.tvOthersValue.setText(other + "");
        //binding.etSqft.setText(String.valueOf(totalSqft));

        binding.seekBar.setMax(totalSqft);
        binding.txtEnd.setText("" + totalSqft);

        // binding.txtStart.setText("0");
        binding.seekBar.setProgress(totalSqft);

        setValues();

    }

}
