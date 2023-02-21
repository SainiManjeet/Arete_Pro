package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.GetRooms;
import com.apnitor.arete.pro.api.request.ChooseProviderReq;
import com.apnitor.arete.pro.api.request.CreateJobNewReq;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.databinding.ActivityCreateJobBinding;
import com.apnitor.arete.pro.interfaces.ListItemCancelCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;
import com.apnitor.arete.pro.materialshowcaseview.MaterialShowcaseSequence;
import com.apnitor.arete.pro.materialshowcaseview.ShowcaseConfig;
import com.apnitor.arete.pro.materialshowcaseview.shape.RectangleShape;
import com.apnitor.arete.pro.materialshowcaseview.shape.Shape;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.util.PermissionUtitlity;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.PriceCalculation;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.util.UploadImage;
import com.apnitor.arete.pro.util.Validation;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import com.shawnlin.numberpicker.NumberPicker;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.tittojose.www.timerangepicker_library.TimeRangePickerDialog;

public class CreateJobActivity extends BaseActivity implements View.OnClickListener, TimeRangePickerDialog.OnTimeRangeSelectedListener, ListItemClickCallback, ListItemClickWithPositionCallback, ListItemCancelCallback {
    public static final String LOG_TAG = "CreateJobActivity";
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    private static final String SHOWCASE_ID = "ggg"; // It can be anything : Entered String key is valid for One time only
    public ArrayList<GetProviderRes> mProvidersList = new ArrayList<>();
    ArrayList<GetAddress> mGetAddress;
    CreateJobNewReq createJobReq = new CreateJobNewReq();
    boolean isShowTime = true;
    boolean isShowDate = true;
    String TAG = "CreateJobActivity";
    Dialog customView;
    int mApiCallCount = 0;
    String mFutureTime = "0:00 - 24:00";
    int currentTime = 0, selectedTime = 0;
    String mCleaningType = "House";
    LogInRes logInRes;
    GetAddress mUserSavedAddress;
    String mSqft;
    private ActivityCreateJobBinding binding;
    private String cleaning = "", when = "", whenValue = "", time = "", date = "", howOften = "", jobType = "", fixedPrice = "";
    private String providerId = "", providerName, providerImage, estTime = "", estPrice = "", spcNotesForProvider = "";
    private int bedrooms = 0, bathrooms = 0, others = 0, kitchen = 0;
    private Double sqft = 0.0;
    private Double taskTimeDbl = 0.0;
    private Double taskPriceDbl = 0.0;
    private Double extraPriceDbl = 0.0;
    private Double extraTimeDbl = 0.0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private UploadImage mUploadImage;
    private Uri mUri;
    private JobSpecViewModel jobSpecViewModel;
    private ArrayList<ImageUrlReq> mImageList;
    private ArrayList<ExtraCleaningRes> mExtraCleaning;
    private int GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    private ImageAdapter mImageAdapter;
    private String mStartDate, mEndDate, mTaskSize = "Task Size(Full House Clean)";
    private ScrollView scrollView;
    private String mProviderType = "Other", mProviderHourlyPrice;
    private String mCurrentDateTime;
    private int mFirstFloor, mSecondFloor, mFrenchWindows, mPatioDoor, mGardenWindows, mWardrobeMirror, mScreens, mSkylight, mStories;
    private int mCleanRoom, mCleanBath, mCleanEntryHall, mCleanStaircase;
    private int mProtectRoom, mProtectBath, mProtectEntryHall, mProtectStaircase;
    private int mDeoRoom, mDeoBath, mDeoEntryHall, mDeoStaircase;
    private GetRooms getRooms, getBath, getEntry, getStairCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        mCurrentDateTime = sdf.format(d);

        bindViews();
        setToolBar(getResources().getString(R.string.create_job));
        showUserAddress();
        String json = PreferenceHandler.readString(this, PreferenceHandler.PREF_KEY_CREATE_JOB, "");
        if (!json.isEmpty()) {
            getLastSavedObject(json);
        } else {
            createJobReq.images = new ArrayList<>();
            setCleaningType("House");
        }
        changeTextAccToProviderType();
        //  getDataFromPrefrences();   Test it then remove
        scrollView = findViewById(R.id.scrollView);
        checkIfFieldSelected();
        //getFavoriteProvider();
    }

    void showUserAddress() {
        //
        logInRes = getPrefHelper().getLogInRes();
        try {
            Log.d(LOG_TAG, " User's saved address is " + logInRes.getAddress().get(0).toString());
            if (logInRes.getAddress() != null) {
                mUserSavedAddress = logInRes.getAddress().get(0);
                mGetAddress = new ArrayList<>();
                mGetAddress.add(mUserSavedAddress);
                createJobReq.address = mGetAddress.get(0);
                binding.tvAddress.setText(createJobReq.address.street + "," + createJobReq.address.state);
                //
                checkIfFieldSelected();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindViews() {
        binding = bindContentView(R.layout.activity_create_job);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);

        binding.tvHouseCleaning.setOnClickListener(this);
        binding.tvcarpetCleaning.setOnClickListener(this);
        binding.tvWindowCleaning.setOnClickListener(this);

        binding.cardAddress.setOnClickListener(this);
        binding.cardRoomDetail.setOnClickListener(this);

        binding.cardExtra.setOnClickListener(this);
        binding.btnDone.setOnClickListener(this);
        binding.linTime.setOnClickListener(this);
        binding.linDate.setOnClickListener(this);
        binding.tvWhenNow.setOnClickListener(this);
        binding.tvWhenToday.setOnClickListener(this);
        binding.tvWhenFuture.setOnClickListener(this);
        binding.tvDaily.setOnClickListener(this);
        binding.tvWeekly.setOnClickListener(this);
        binding.tvByWeekly.setOnClickListener(this);
        binding.tvMonthly.setOnClickListener(this);
        binding.addImage.setOnClickListener(this);
        binding.linMakeBid.setOnClickListener(this);
        binding.linFixedPrice.setOnClickListener(this);
        binding.linChooseProvider.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.imageRecycler.setLayoutManager(manager);
        binding.imageRecycler.setNestedScrollingEnabled(false);
        mImageList = new ArrayList<>();
        mExtraCleaning = new ArrayList<>();
        mGetAddress = new ArrayList<>();
        mImageAdapter = new ImageAdapter(CreateJobActivity.this, mImageList, null, this);
        binding.imageRecycler.setAdapter(mImageAdapter);
        jobSpecViewModel.createJobLiveData().observe(this, createJobRes ->
                Toast.makeText(this, "Your job created successfully." + createJobRes.jobId, Toast.LENGTH_SHORT).show());
    }

    // Method to change text name (Estimate Price when type is "Bid" otherwise "Booked")
    private void changeTextAccToProviderType() {
        // Set Price
        if (jobType.equalsIgnoreCase("Bid")) {
            binding.estPriceText.setText(getResources().getString(R.string.estimated_price));// Price
            binding.estTimeText.setText(getResources().getString(R.string.est_time_hour));// Hour
        } else {
            binding.estPriceText.setText(getResources().getString(R.string.booked_price));
            binding.estTimeText.setText(getResources().getString(R.string.booked_hour));
        }
    }

    private void checkIfFieldSelected() {
        if (binding.tvAddress.getText().toString().length() > 0) {
            binding.cardAddress.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            binding.tvAddress.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.tvTitleAddress.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.imgAddress.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }

        // Task Size
        if (binding.tvBedrooms.getText().toString().length() > 0) {
            binding.cardRoomDetail.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            binding.tvBedrooms.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.tvTitleRoomDetail.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.roomImg.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }

        // Extra Cleaning
        if (binding.tvExtraItems.getText().toString().length() > 0) {
            binding.cardExtra.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            binding.tvExtraItems.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.tvTitleExtra.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        }
    }

    private void selectedText(TextView view, TextView view1, TextView view2) {
        selectCleaningType(view, R.drawable.selected_text, R.color.colorWhite);
        selectCleaningType(view1, R.drawable.unselected_text, R.color.colorPrimary);
        selectCleaningType(view2, R.drawable.unselected_text, R.color.colorPrimary);
    }

    private void setCleaningType(String cleaningType) {

        if (cleaningType.equalsIgnoreCase("House")) {
            selectCleaningType(binding.tvHouseCleaning, R.drawable.selected_text, R.color.colorWhite);
            unSelectCarpetCleaning();
            unSelectWindowCleaning();
            setCleaning(cleaningType);
        } else if (cleaningType.equalsIgnoreCase("Carpet")) {
            selectCleaningType(binding.tvcarpetCleaning, R.drawable.selected_text, R.color.colorWhite);
            unSelectHouseCleaning();
            unSelectWindowCleaning();
            setCleaning(cleaningType);
        } else {
            selectCleaningType(binding.tvWindowCleaning, R.drawable.selected_text, R.color.colorWhite);
            unSelectHouseCleaning();
            unSelectCarpetCleaning();
            setCleaning(cleaningType);
        }
    }

    private void setCleaning(String cleaningType) {
        cleaning = cleaningType;
        mCleaningType = cleaningType;
        createJobReq.cleaningType = cleaning;
    }

    private void unSelectWindowCleaning() {
        selectCleaningType(binding.tvWindowCleaning, R.drawable.unselected_text, R.color.colorPrimary);
    }

    private void unSelectCarpetCleaning() {
        selectCleaningType(binding.tvcarpetCleaning, R.drawable.unselected_text, R.color.colorPrimary);
    }

    private void unSelectHouseCleaning() {
        selectCleaningType(binding.tvHouseCleaning, R.drawable.unselected_text, R.color.colorPrimary);
    }

    private void selectCleaningType(TextView tvHouseCleaning, int selected_text, int colorWhite) {
        tvHouseCleaning.setBackgroundResource(selected_text);
        tvHouseCleaning.setTextColor(ContextCompat.getColor(this, colorWhite));
    }

    private void selectedText(TextView view, TextView view1, TextView view2, TextView view3) {
        selectCleaningType(view, R.drawable.selected_text, R.color.colorWhite);
        selectCleaningType(view1, R.drawable.unselected_text, R.color.colorPrimary);
        selectCleaningType(view2, R.drawable.unselected_text, R.color.colorPrimary);
        selectCleaningType(view3, R.drawable.unselected_text, R.color.colorPrimary);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvHouseCleaning:
                setCleaningType("House");
                clearTaskSizeUI();
                binding.cardExtra.setVisibility(View.VISIBLE);
                clearDataForPreviouslySelectedProvider();
                break;
            case R.id.tvcarpetCleaning:
                setCleaningType("Carpet");
                clearTaskSizeUI();
                binding.cardExtra.setVisibility(View.GONE);
                clearDataForPreviouslySelectedProvider();

                break;
            case R.id.tvWindowCleaning:
                setCleaningType("Window");
                clearTaskSizeUI();
                binding.cardExtra.setVisibility(View.GONE);
                clearDataForPreviouslySelectedProvider();
                break;
            case R.id.cardRoomDetail:
                if (Validation.isValidatedAddress(this, cleaning, binding.tvAddress.getText().toString())) {
                    if (mCleaningType.equalsIgnoreCase("House")) {
                        showHouseSizeDialog();

                    } else if (mCleaningType.equalsIgnoreCase("Window")) {
                        Intent intent = new Intent(this, TaskSizeActivity.class);
                        intent.putExtra("SQFT", sqft);
                        intent.putExtra("mCleaningType", cleaning);
                        intent.putExtra("mFirstFloor", mFirstFloor);
                        intent.putExtra("mSecondFloor", mSecondFloor);
                        intent.putExtra("mFrenchWindows", mFrenchWindows);
                        intent.putExtra("mPatioDoor", mPatioDoor);
                        intent.putExtra("mGardenWindows", mGardenWindows);
                        intent.putExtra("mWardrobeMirror", mWardrobeMirror);
                        intent.putExtra("mScreens", mScreens);
                        intent.putExtra("mSkylight", mSkylight);
                        intent.putExtra("mStories", mStories);
                        startActivityForResult(intent, 111);
                    } else {
                        Intent intent = new Intent(this, TaskSizeActivity.class);
                        intent.putExtra("SQFT", sqft);
                        intent.putExtra("mCleaningType", cleaning);
                        intent.putExtra("mCleanRoom", mCleanRoom);
                        intent.putExtra("mProtectRoom", mProtectRoom);
                        intent.putExtra("mDeoRoom", mDeoRoom);
                        intent.putExtra("mCleanBath", mCleanBath);
                        intent.putExtra("mProtectBath", mProtectBath);
                        intent.putExtra("mDeoBath", mDeoBath);
                        intent.putExtra("mCleanEntryHall", mCleanEntryHall);
                        intent.putExtra("mProtectEntryHall", mProtectEntryHall);
                        intent.putExtra("mDeoEntryHall", mDeoEntryHall);
                        intent.putExtra("mCleanStaircase", mCleanStaircase);
                        intent.putExtra("mProtectStaircase", mProtectStaircase);
                        intent.putExtra("mDeoStaircase", mDeoStaircase);
                        startActivityForResult(intent, 111);
                    }
                }
                break;
            case R.id.cardAddress:
                if (Validation.isValidatedCleaningType(this, cleaning)) {
                    String latitude = "";
                    String longitude = "";
                    Bundle mBundle = new Bundle();
                    if (mGetAddress != null && mGetAddress.size() > 0) {
                        if (mGetAddress.get(0).latitude > 0) {
                            latitude = mGetAddress.get(0).latitude + "";
                            longitude = mGetAddress.get(0).longitude + "";
                            mBundle.putString("latitude", latitude);
                            mBundle.putString("longitude", longitude);
                        }
                    }
                    mBundle.putString("prevActivity", "CreateJob");
                    mBundle.putString("cleaning", cleaning);
                    startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
                }
                break;
            case R.id.cardExtra:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    Log.e(TAG, "=" + binding.tvExtraItems.getText().toString());
                    PreferenceHandler.writeString(this, PreferenceHandler.PREF_EXTRA_CLEAN, binding.tvExtraItems.getText().toString());
                    Bundle mBundle = new Bundle();
                    mBundle.putString("name", binding.tvExtraItems.getText().toString());
                    startActivityForResultWithExtra(ExtraCleaningActivity.class, 102, mBundle);
                }
                break;
            case R.id.linDate:
                if (Validation.isValidatedAddress(this, cleaning, binding.tvAddress.getText().toString())) {
                    //selectDate(false);
                    selectDateRange(false);
                }
                break;
            case R.id.linTime:
                if (Validation.isValidatedAddress(this, cleaning, binding.tvAddress.getText().toString())) {
                    if (whenValue.equalsIgnoreCase("Future")) {
                        selectDateRange(true);
                    } else
                        selectTime();
                }
                break;

            case R.id.tvWhenNow:
                if (Validation.isValidatedRoomDetails(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString())) {
                    selectedText(binding.tvWhenNow, binding.tvWhenToday, binding.tvWhenFuture);
                    time = currentTime();
                    date = currentDate();
                    binding.linDate.setVisibility(View.GONE);
                    binding.linTime.setVisibility(View.GONE);
                    when = "Now";
                    createJobReq.time = when;
                }
                break;
            case R.id.tvWhenToday:
                if (Validation.isValidatedRoomDetails(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString())) {
                    //this value to check if user changed time or not
                    whenValue = "Today";
                    date = currentDate();
                    binding.linDate.setVisibility(View.GONE);
                    binding.linTime.setVisibility(View.VISIBLE);
                    if (isShowTime)
                        selectTime();
                    isShowTime = true;
                }
                break;
            case R.id.tvWhenFuture:
                if (Validation.isValidatedRoomDetails(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString())) {
                    //this value to check if user changed time and date or not
                    whenValue = "Future";
                    binding.linDate.setVisibility(View.VISIBLE);
                    binding.linTime.setVisibility(View.VISIBLE);
                   /* if (isShowDate) // W
                        selectDate(true);
                    isShowDate = true;*/
                    //
                    if (isShowDate)
                        selectDateRange(true);
                    isShowDate = true;
                }
                break;
            case R.id.tvDaily:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvDaily, binding.tvWeekly, binding.tvByWeekly, binding.tvMonthly);
                    howOften = "Just Once";
                    createJobReq.howOften = howOften;
                    createJobReq.date = howOften;
                }
                break;
            case R.id.tvWeekly:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvWeekly, binding.tvDaily, binding.tvByWeekly, binding.tvMonthly);
                    howOften = "Weekly";
                    createJobReq.howOften = howOften;
                }
                break;
            case R.id.tvByWeekly:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvByWeekly, binding.tvWeekly, binding.tvDaily, binding.tvMonthly);
                    howOften = "Bi Weekly";
                    createJobReq.howOften = howOften;
                }
                break;
            case R.id.tvMonthly:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvMonthly, binding.tvWeekly, binding.tvByWeekly, binding.tvDaily);
                    howOften = "Monthly";
                    createJobReq.howOften = howOften;
                }
                break;
            case R.id.add_image:
                // Functionailty was working : But due to some issues it been commented for now
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    if (PermissionUtitlity.checkPermission(CreateJobActivity.this)) {
                        mUploadImage = new UploadImage(this);
                        showPictureDialog();
                    }
                }
                break;
            case R.id.linMakeBid:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    jobType = "Bid";
                    changeTextAccToProviderType(); //
                    selectProviderType(binding.tvMakeBidName, binding.tvChooseProvider, binding.tvFixedPrice, jobType);
                }
                break;
            case R.id.linFixedPrice:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("estTime", estTime);
                    mBundle.putString("estPrice", estPrice);
                    startActivityForResultWithExtra(FixedPriceActivity.class, 104, mBundle);
                }
                break;
            case R.id.linChooseProvider:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    if (mProvidersList.size() < 1) {
                        getFavoriteProvider();
                        getResOfFavProvider();
                    } else {
                        showDialogWithLayout();
                    }
                }
                break;
            case R.id.btnDone:
                spcNotesForProvider = UIUtility.getEditTextValue(binding.etSpcNotes);
                if (validationSuccess()) {
                    float mTime = 00;
                    if (estTime != null && !estTime.isEmpty()) {
                        mTime = Float.parseFloat(estTime);
                    }
                    switch (mCleaningType.toLowerCase()) {
                        case "house":
                            if (mEndDate != null && !mEndDate.isEmpty()) {
                                createJobReq = new CreateJobNewReq(getPrefHelper().getAuthToken(), cleaning, bedrooms,
                                        bathrooms, kitchen, others, sqft, mGetAddress.get(0), mStartDate, mEndDate, time, howOften, when, spcNotesForProvider,
                                        mImageList, mExtraCleaning, mTime, estPrice, fixedPrice, providerId, spcNotesForProvider, jobType);
                            } else {
                                createJobReq = new CreateJobNewReq(getPrefHelper().getAuthToken(), cleaning, bedrooms,
                                        bathrooms, kitchen, others, sqft, mGetAddress.get(0), date, time, howOften, when, spcNotesForProvider,
                                        mImageList, mExtraCleaning, mTime, estPrice, fixedPrice, providerId, spcNotesForProvider, jobType);
                            }
                            break;
                        case "window":
                            if (mEndDate != null && !mEndDate.isEmpty()) {
                                createJobReq = new CreateJobNewReq(getPrefHelper().getAuthToken(), cleaning, mGetAddress.get(0), mStartDate, mEndDate, time, howOften, when, spcNotesForProvider,
                                        mImageList, mExtraCleaning, mTime, estPrice, fixedPrice, providerId, spcNotesForProvider, jobType,
                                        mFirstFloor, mSecondFloor, mFrenchWindows, mPatioDoor, mGardenWindows, mWardrobeMirror, mScreens, mSkylight, mStories);
                            } else {
                                createJobReq = new CreateJobNewReq(getPrefHelper().getAuthToken(), cleaning, mGetAddress.get(0), date, time, howOften, when, spcNotesForProvider,
                                        mImageList, mExtraCleaning, mTime, estPrice, fixedPrice, providerId, spcNotesForProvider, jobType,
                                        mFirstFloor, mSecondFloor, mFrenchWindows, mPatioDoor, mGardenWindows, mWardrobeMirror, mScreens, mSkylight, mStories);
                            }
                            break;
                        case "carpet":
                            if (mEndDate != null && !mEndDate.isEmpty()) {
                                createJobReq = new CreateJobNewReq(getPrefHelper().getAuthToken(), cleaning, mGetAddress.get(0), mStartDate, mEndDate, time, howOften, when, spcNotesForProvider,
                                        mImageList, mExtraCleaning, mTime, estPrice, fixedPrice, providerId, spcNotesForProvider, jobType,
                                        getRooms, getBath, getEntry, getStairCase);
                            } else {
                                createJobReq = new CreateJobNewReq(getPrefHelper().getAuthToken(), cleaning, mGetAddress.get(0), date, time, howOften, when, spcNotesForProvider,
                                        mImageList, mExtraCleaning, mTime, estPrice, fixedPrice, providerId, spcNotesForProvider, jobType,
                                        getRooms, getBath, getEntry, getStairCase);
                            }
                            break;
                    }

                    Intent mIntent = new Intent(this, ConfirmJobActivity.class);
                    mIntent.putExtra("createJob", createJobReq);
                    mIntent.putExtra("providerName", providerName);
                    mIntent.putExtra("providerImage", providerImage);
                    mIntent.putExtra("providerId", providerId);
                    mIntent.putExtra("cleaningType", mCleaningType);
                    mIntent.putExtra("tasks", binding.tvBedrooms.getText().toString());
                    startActivityForResult(mIntent, RequestCodes.CONFIRM_JOB_REQUEST);
                    startWithAnim();
                }
                break;
            default:
                break;
        }
    }

    private void clearDataForPreviouslySelectedProvider() {
        providerId = "";
        providerName = "";
        providerImage = "";
        mProviderHourlyPrice = "";
        jobType = "";
        estPrice = "";
        estTime = "";
        binding.yourPrice.setText("");
        binding.estTime.setText(estTime);
        binding.estPrice.setText(estPrice);
        Glide.with(this).load(R.drawable.ic_person_black_24dp).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                .centerCrop().circleCrop()).into(binding.imgChooseProvider);

        fixedPrice = "";
        binding.imgFixedPrice.setImageResource(R.drawable.ic_set_price);
        binding.imgMakeBid.setImageResource(R.drawable.ic_request_bid);
        binding.tvMakeBidName.setTextColor(getApplicationContext().getResources().getColor(R.color.secondary_text));
        binding.tvChooseProvider.setTextColor(getApplicationContext().getResources().getColor(R.color.secondary_text));
        binding.tvFixedPrice.setTextColor(getApplicationContext().getResources().getColor(R.color.secondary_text));
    }

    /*private void selectCleaningType(AppCompatTextView tvHouseCleaning, AppCompatTextView tvcarpetCleaning, AppCompatTextView tvWindowCleaning, String cleaningType) {
        selectedText(tvHouseCleaning, tvcarpetCleaning, tvWindowCleaning);
        setCleaning(cleaningType);
    }*/

    private void getResOfFavProvider() {
        jobSpecViewModel.getProvidersResLiveData()
                .observe(this, res -> {
                    mProvidersList.clear();
                    mProvidersList.addAll(res.providerRes);
                    //
                    if (mProvidersList.size() > 0) {
                        if (mApiCallCount == 0) {
                            showDialogWithLayout();
                            mApiCallCount++;
                        }

                    } else {
                        if (mApiCallCount == 0) {
                            startActivityForResult(SelectProviderActivity.class, 103);
                            mApiCallCount++;
                        }
                    }

                });
    }

    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(CreateJobActivity.this);
        pictureDialog.setTitle("Select Picture");
        String[] pictureDialogItems = {
                "Choose from Gallery",
                "Capture using Camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (PermissionUtitlity.checkPermission(CreateJobActivity.this)) {
                                choosePhotoFromGallary();
                            }
                            break;
                        case 1:
                            if (PermissionUtitlity.checkPermissionCamera(CreateJobActivity.this)) {
                                dispatchTakePictureIntent();
                            }
                            break;
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                // openCropActivity(mUri);
                try {
                    /*TODO set image in list to show on recycler view Item*/
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (int i = 0; i < count; i++) {
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            ImageUrlReq mUrlReq = new ImageUrlReq();
                            mUrlReq.imageUrl = mUploadImage.saveImage(imageUri);
                            mImageList.add(mUrlReq);
                        }

                    } else if (data.getData() != null) {
                        mUri = data.getData();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        ImageUrlReq mUrlReq = new ImageUrlReq();
                        mUrlReq.imageUrl = mUploadImage.saveImage(mUri);
                        mImageList.add(mUrlReq);
                    }
                    mImageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CreateJobActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mUri = mUploadImage.getImageUri(photo);
                Log.e("capture", "" + mUri.toString());
                // mUri = Uri.fromFile(new File(mUploadImage.mCurrentPhotoPath));
                /*TODO set cropped image in list to show on recycler view Item*/
                ImageUrlReq mUrlReq = new ImageUrlReq();
                mUrlReq.imageUrl = mUploadImage.saveImage(mUri);
                mImageList.add(mUrlReq);
                mImageAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CreateJobActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 111) {
            //String mCleaningType = "";

            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    mCleaningType = bundle.getString("cleaningType");
                    if (mCleaningType.equalsIgnoreCase("House")) {
                        bedrooms = bundle.getInt("Bedrooms");
                        taskTimeDbl = 0.0;
                        taskPriceDbl = 0.0;
                        bedrooms = bundle.getInt("Bedrooms");
                        bathrooms = bundle.getInt("Bathrooms");
                        kitchen = bundle.getInt("Kitchen");
                        others = bundle.getInt("Others");
                        String strSqft = bundle.getString("SQFT");
                        sqft = Double.parseDouble(strSqft);
                        mTaskSize = bundle.getString("TaskSize");
                        Log.e("mTaskSize==", "mTaskSize=" + mTaskSize);
                        // Set Task Size "TITLE"
                        binding.tvTitleRoomDetail.setText(mTaskSize);
                        // Float.valueOf(String.valueOf(strSqft));
                        //
                        //    PreferenceHandler.writeFloat(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_SQFT, Float.valueOf(String.valueOf(strSqft)));
                        /*Convert the minutes in Hour and then add extra services price and set on textview*/
                        calculateHouseTaskPrice();
                        createJobReq.bedrooms = bedrooms;
                        createJobReq.bathrooms = bathrooms;
                        createJobReq.kitchen = kitchen;
                        createJobReq.others = others;
                        createJobReq.sqft = sqft;
                        binding.cardRoomDetail.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                        binding.tvTitleRoomDetail.setTextColor(getResources().getColor(R.color.colorWhite));
                        binding.roomImg.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        binding.tvBedrooms.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                    } else if (mCleaningType.equalsIgnoreCase("Window")) {
                        mFirstFloor = bundle.getInt("firstFloor");
                        mSecondFloor = bundle.getInt("secondFloor");
                        mFrenchWindows = bundle.getInt("frenchWindows");
                        mPatioDoor = bundle.getInt("patioDoor");
                        mGardenWindows = bundle.getInt("gardenWindows");
                        mWardrobeMirror = bundle.getInt("wardrobeMirror");
                        mScreens = bundle.getInt("screens");
                        mSkylight = bundle.getInt("skylight");
                        mStories = bundle.getInt("stories");

                        mTaskSize = mFirstFloor + " First Floor" + getResources().getString(R.string.comma) + mSecondFloor + " Second Floor," + mFrenchWindows + " " + getResources().getString(R.string.French_type) + getResources().getString(R.string.comma) +
                                mPatioDoor + " " + getResources().getString(R.string.patio_doors_selection) + getResources().getString(R.string.comma) + mGardenWindows + " " + getResources().getString(R.string.garden_windows_selection) + getResources().getString(R.string.comma) +
                                mWardrobeMirror + " " + getResources().getString(R.string.mirror) + getResources().getString(R.string.comma) + mScreens + " " + getResources().getString(R.string.screens_sel) + getResources().getString(R.string.comma) +
                                mSkylight + " " + getResources().getString(R.string.skylight_sel) + getResources().getString(R.string.comma) + mStories + " " + getResources().getString(R.string.stories_sel);


                        binding.tvBedrooms.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                        calculateWindowsTaskTimePrice();
                        binding.tvBedrooms.setText(mTaskSize);
                        binding.cardRoomDetail.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                        binding.tvTitleRoomDetail.setTextColor(getResources().getColor(R.color.colorWhite));
                        binding.roomImg.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    }
                    // CARPET
                    else {
                        mCleanRoom = bundle.getInt("roomClean");
                        mProtectRoom = bundle.getInt("roomProtect");
                        mDeoRoom = bundle.getInt("roomDeo");
                        mCleanBath = bundle.getInt("bathClean");
                        mProtectBath = bundle.getInt("bathProtect");
                        mDeoBath = bundle.getInt("bathDeo");
                        mCleanEntryHall = bundle.getInt("entryClean");
                        mProtectEntryHall = bundle.getInt("entryProtect");
                        mDeoEntryHall = bundle.getInt("entryDeo");
                        mCleanStaircase = bundle.getInt("staircaseClean");
                        mProtectStaircase = bundle.getInt("staircaseProtect");
                        mDeoStaircase = bundle.getInt("staircaseDeo");


                        mTaskSize = getResources().getString(R.string.rooms) + " : " + mCleanRoom + " " + getResources().getString(R.string.clean) + getResources().getString(R.string.comma) + " " + mProtectRoom + " " +
                                getResources().getString(R.string.protect) + getResources().getString(R.string.comma) + " " + mDeoRoom + " " + "Deodorize" + " " + "\n" +
                                "Bath/Laundry" + " : " + mCleanBath + " " + getResources().getString(R.string.clean) + "," + mProtectBath + " " + getResources().getString(R.string.protect) + getResources().getString(R.string.comma) +
                                mDeoBath + " Deodorize" + " " + "\n" + "Entry/Hall" + " : " + mCleanEntryHall + " " + getResources().getString(R.string.clean) + "," + mProtectEntryHall + " " + getResources().getString(R.string.protect) + getResources().getString(R.string.comma) +
                                mDeoEntryHall + " " + getResources().getString(R.string.deodorize) + " " + "\n" + getResources().getString(R.string.staircase) + " : " + mCleanStaircase + " " + getResources().getString(R.string.clean) + getResources().getString(R.string.comma) +
                                mProtectStaircase + " " + getResources().getString(R.string.protect) + getResources().getString(R.string.comma) + mDeoStaircase + " " + getResources().getString(R.string.deodorize) + " ";
                        getRooms = new GetRooms();
                        getRooms.clean = String.valueOf(mCleanRoom);
                        getRooms.protect = String.valueOf(mProtectRoom);
                        getRooms.deodrize = String.valueOf(mDeoRoom);
                        Log.e(LOG_TAG, "getRoom Clean: " + getRooms.clean + " RoomProtect :" + getRooms.protect);

                        getBath = new GetRooms();
                        getBath.clean = String.valueOf(mCleanBath);
                        getBath.protect = String.valueOf(mProtectBath);
                        getBath.deodrize = String.valueOf(mDeoBath);

                        getEntry = new GetRooms();
                        getEntry.clean = String.valueOf(mCleanEntryHall);
                        getEntry.protect = String.valueOf(mProtectEntryHall);
                        getEntry.deodrize = String.valueOf(mDeoEntryHall);

                        getStairCase = new GetRooms();
                        getStairCase.clean = String.valueOf(mCleanStaircase);
                        getStairCase.protect = String.valueOf(mProtectStaircase);
                        getStairCase.deodrize = String.valueOf(mDeoStaircase);
                        Log.d(LOG_TAG, "taskSizeCarpet: " + mTaskSize);
                        binding.tvBedrooms.setText(mTaskSize);
                        binding.tvBedrooms.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                        calculateCarpetTaskTimePrice();
                        binding.cardRoomDetail.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                        binding.tvTitleRoomDetail.setTextColor(getResources().getColor(R.color.colorWhite));
                        binding.roomImg.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    }
                }


            }
        } else if (requestCode == 101) {

            if (data != null) {
                try {
                    Bundle bundle = data.getExtras();
                    mGetAddress.clear();
                    if (bundle != null) {
                        GetAddress getAddress = bundle.getParcelable("Address");
                        mGetAddress.add(getAddress);
                        createJobReq.address = mGetAddress.get(0);
                        binding.tvAddress.setText(createJobReq.address.street + "," + createJobReq.address.state);
                        if (mCleaningType.equalsIgnoreCase("house")) {
                            bedrooms = data.getIntExtra("Bedroom", 0);
                            bathrooms = data.getIntExtra("Bathroom", 0);
                            sqft = new Double(data.getFloatExtra("SQFT", 0f));
                            kitchen = data.getIntExtra("Kitchen", 0);
                            others = data.getIntExtra("Other", 0);
                        }
                        // Get data from Map Activity
                        calculateHouseTaskPrice();
                        createJobReq.bedrooms = bedrooms;
                        createJobReq.bathrooms = bathrooms;
                        createJobReq.kitchen = kitchen;
                        createJobReq.others = others;
                        createJobReq.sqft = sqft;
                        // Change the color on Address selection
                        checkIfFieldSelected();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == 102) {
            if (data != null) {
                extraTimeDbl = 0.0;
                extraPriceDbl = 0.0;
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String name = bundle.getString("extraName");
                    double extraTime = bundle.getDouble("extraTime");
                    if (mExtraCleaning.size() > 0) {
                        mExtraCleaning.clear();
                    }
                    mExtraCleaning = bundle.getParcelableArrayList("extraList");
                    binding.tvExtraItems.setText(name);

                    // 5
                    if (binding.tvExtraItems.getText().toString().length() > 0) {
                        binding.cardExtra.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
                        binding.tvExtraItems.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                        binding.tvTitleExtra.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
                        binding.ImgExtra.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    }

                    /*Convert the minutes in Hour and then add task time and set on textview*/
                    extraTimeDbl = extraTime;
                    calculateExtraTimePrice();
                }
            }
        } else if (requestCode == 103) {

            Log.d(LOG_TAG, " In 103");
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {

                    if (customView != null && customView.isShowing()) {
                        customView.dismiss();
                    }
                    providerId = bundle.getString("providerId");
                    providerName = bundle.getString("providerName");
                    providerImage = bundle.getString("providerImage");
                    // Provider Hourly Price
                    mProviderHourlyPrice = bundle.getString("providerHourlyRate");
                    //Log.e("providerHourlyPrice==", "providerHourlyPrice==" + providerHourlyPrice);

                    binding.tvChooseProvider.setText(providerName);
                    jobType = "Service Provider";
                    selectProviderType(binding.tvChooseProvider, binding.tvMakeBidName, binding.tvFixedPrice, jobType);
                    Glide.with(this).load(providerImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                            .centerCrop().circleCrop()).into(binding.imgChooseProvider);
                    changeTextAccToProviderType(); //

                    // Added
                    if (jobType.equalsIgnoreCase("Bid") || jobType.equalsIgnoreCase("Fixed Price")) { //
                        binding.estTimeText.setText(getResources().getString(R.string.est_time_hour));// Hour
                    } else {
                        binding.estTimeText.setText(getResources().getString(R.string.booked_hour));
                    }




                   /* mProviderType = "ServiceProvider";
                    // calculateHouseTaskPrice();  // W bf
                    // TODO here calculate price acc to type
                    if (mCleaningType.equalsIgnoreCase("House")) {
                        calculateHouseTaskPrice();
                    } else if (mCleaningType.equalsIgnoreCase("Window")) {
                        calculateWindowsTaskTimePrice();
                    } else {
                        calculateCarpetTaskTimePrice();
                    }*/
                    calculatePriceWithTypeServiceProvider();

                    if (customView != null && customView.isShowing()) {
                        customView.dismiss();
                    }
                }
            }
        } else if (requestCode == 104) {
            Log.d(LOG_TAG, " in 104");
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    fixedPrice = bundle.getString("fixedPrice");
                    jobType = "Fixed Price";
                    selectProviderType(binding.tvFixedPrice, binding.tvMakeBidName, binding.tvChooseProvider, jobType);
                    //
                    Log.d(LOG_TAG, " Job type " + jobType);
                    if (jobType.equalsIgnoreCase("Bid") || jobType.equalsIgnoreCase("Fixed Price")) { //
                        binding.estTimeText.setText(getResources().getString(R.string.est_time_hour));// Hour
                        binding.estPriceText.setText(getResources().getString(R.string.estimated_price));// price
                    } else {
                        binding.estTimeText.setText(getResources().getString(R.string.booked_hour));
                    }

                }
            }
        } else if (resultCode == ResultCodes.CONFIRM_JOB_RESULT) {

            // It should go back to HomeActivity

            setResult(ResultCodes.CREATE_JOB_RESULT, data);

            finish();
        }


    }


    private void dispatchTakePictureIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_TAKE_PHOTO);

            File pictureFile = null;
//            try {
//                pictureFile = getPictureFile();
//            } catch (IOException ex) {
//                Toast.makeText(this,
//                        "Photo file can't be created, please try again",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.apnitor.arete.pro",
                        pictureFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = mUploadImage.createImageFile();
                if (!photoFile.exists()) {
                    photoFile.createNewFile();
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Log.e("filepath", photoFile.toString());
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtitlity.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallary();
                }
                break;
            case PermissionUtitlity.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPictureDialog();
                }
                break;
            case PermissionUtitlity.CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                break;
            case PermissionUtitlity.LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Bundle mBundle = new Bundle();
                   /* if (mGetAddress != null && mGetAddress.size() > 0) {
                        latitude = mGetAddress.get(0).latitude.toString();
                        longitude = mGetAddress.get(0).longitude.toString();
                        mBundle.putString("latitude", latitude);
                        mBundle.putString("longitude", longitude);
                    }
*/
                    mBundle.putString("latitude", "30.7333");
                    mBundle.putString("longitude", " 76.7794");
                    mBundle.putString("prevActivity", "CreateJob");
                    mBundle.putString("cleaning", cleaning);
                    startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
                }
                break;
        }
    }

    private void selectDateRange(boolean isTimeOpen) {

        Dialog customView = new Dialog(this);
        customView.setContentView(R.layout.dialog_select_date);

        DateRangeCalendarView calendarView = customView.findViewById(R.id.calendar);

        TextView ok = customView.findViewById(R.id.txtOk);
        TextView cancel = customView.findViewById(R.id.txtCancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(LOG_TAG, "In Ok");

                //  selectTimeFuture();
                showTimeSelectorDilaog();

                customView.dismiss();
            }
        });
        cancel.setOnClickListener(v -> customView.dismiss());

        calendarView.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                selectedDate(startDate);
                Date mDate = startDate.getTime();


                String[] separated = startDate.getTime().toString().split("00");
                mStartDate = separated[0];
                Log.d(LOG_TAG, "Start Date is " + mStartDate + "End Date is " + mEndDate);
                Log.e(LOG_TAG, "startDate  is " + startDate.getTime());
                date = mStartDate;
                binding.tvWhenDate.setText(selectedDate(startDate));
                createJobReq.date = selectedDate(startDate);
                when = "Future";
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                String endingDate = endDate.getTime().toString();
                mEndDate = endingDate;

                String[] separated = startDate.getTime().toString().split("00");
                mStartDate = separated[0];

                // End Date
                String[] separated2 = endDate.getTime().toString().split("00");
                String endDt = separated2[0];
                mEndDate = endDt;
                Log.d(LOG_TAG, "Start Date is " + mStartDate + "End Date is " + mEndDate);
                date = mStartDate + "-" + mEndDate;
                // end date goes here

                Log.d(LOG_TAG, " mStartDate: " + mStartDate + " EndDate : " + mEndDate);
                binding.tvWhenDate.setText(selectedDate(startDate) + " to " + selectedDate(endDate));
                createJobReq.date = selectedDate(startDate) + " to " + selectedDate(endDate);
                date = selectedDate(startDate) + " to " + selectedDate(endDate);
                //  mStartDate = selectedDate(startDate) + " to ";
                mStartDate = selectedDate(startDate);
                mEndDate = selectedDate(endDate);
                when = "Future";
                /*if (isTimeOpen) {
                    selectTimeFuture();
                }*/

            }
        });
        customView.show();

    }


 /*   private void selectDate(boolean isTimeOpen) {
        Log.e("Select Date", "===");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        binding.tvWhenDate.setText(date);
                        createJobReq.date = date;

                        if (isTimeOpen) {
                            selectTime();
                        }
                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);// M
        datePickerDialog.show();
    }*/

   /* private void selectTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        time = getTime(hourOfDay, minute);
                        binding.tvWhenTime.setText(time);
                        createJobReq.time = time;
                        //if user changed date and time then show selected item and change "when" value else it show previous value
                        if (whenValue.equals("Today")) {
                            when = "Today";
                            selectedText(binding.tvWhenToday, binding.tvWhenNow, binding.tvWhenFuture);
                        } else if (whenValue.equals("Future")) {
                            when = "Future";
                            selectedText(binding.tvWhenFuture, binding.tvWhenNow, binding.tvWhenToday);
                        }
                    }

                }, mHour, mMinute, true);

        timePickerDialog.show();
    }*/


    private void selectTime() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        int mSecond = c.get(Calendar.SECOND);
        // Launch Time Picker Dialog
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                        time = getTime(hourOfDay, minute);
                        binding.tvWhenTime.setText(time);
                        createJobReq.time = time;
                        //if user changed date and time then show selected item and change "when" value else it show previous value
                        if (whenValue.equals("Today")) {
                            when = "Today";
                            selectedText(binding.tvWhenToday, binding.tvWhenNow, binding.tvWhenFuture);
                        } else if (whenValue.equals("Future")) {
                            when = "Future";
                            selectedText(binding.tvWhenFuture, binding.tvWhenNow, binding.tvWhenToday);
                        }
                    }
                },

                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
        );

        if (whenValue.equalsIgnoreCase(getString(R.string.today))) { //
            tpd.setMinTime(mHour, mMinute, mSecond);
        }

        //  tpd.setMaxTime(mHour, mMinute, mSecond);
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    // New
    private void selectTimeFuture() {
        final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                CreateJobActivity.this, true);
        timePickerDialog.show(getSupportFragmentManager(), TIMERANGEPICKER_TAG);


    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }

    @SuppressLint("SimpleDateFormat")
    private String currentTime() {
        Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        Time tme = new Time(mHour, mMinute, 0);
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }

    private String currentDate() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        return mDay + "-" + getFilteredMonth((mMonth + 1) + "") + "-" + mYear;

    }

    private String selectedDate(Calendar c) {
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        return mDay + "-" + getFilteredMonth((mMonth + 1) + "") + "-" + mYear;
    }

    private void selectProviderType(AppCompatTextView text1, AppCompatTextView text2, AppCompatTextView text3, String jobType) {
        //  binding.btnNext.setImageResource(R.drawable.ic_tick);
        text1.setTextColor(getResources().getColor(R.color.colorPrimary));
        text2.setTextColor(getResources().getColor(R.color.secondary_text));
        text3.setTextColor(getResources().getColor(R.color.secondary_text));

        if (jobType.equals("Fixed Price")) {
            binding.yourPrice.setText("$" + fixedPrice);
            binding.linPrice.setWeightSum(3);
            binding.priceView2.setVisibility(View.VISIBLE);
            binding.yourPrice.setVisibility(View.VISIBLE);
            binding.llyourPrice.setVisibility(View.VISIBLE);

            binding.imgFixedPrice.setImageResource(R.drawable.ic_set_price_green);
            binding.imgMakeBid.setImageResource(R.drawable.ic_request_bid);
            binding.imgChooseProvider.setImageResource(R.drawable.ic_choose_provider);
        } else {
            if (jobType.equals("Bid")) {
                binding.imgMakeBid.setImageResource(R.drawable.ic_request_bid_green);
                binding.imgChooseProvider.setImageResource(R.drawable.ic_choose_provider);
            } else {

                binding.imgMakeBid.setImageResource(R.drawable.ic_request_bid);
                binding.imgChooseProvider.setImageResource(R.drawable.ic_choose_provider_green);
            }
            binding.imgFixedPrice.setImageResource(R.drawable.ic_set_price);
            fixedPrice = "";
            binding.linPrice.setWeightSum(2);
            binding.yourPrice.setVisibility(View.GONE);
            binding.priceView2.setVisibility(View.GONE);
            binding.llyourPrice.setVisibility(View.GONE);
        }
    }

    private boolean validationSuccess() {

        if (!Validation.isValidatedCleaningType(this, cleaning)) {
            return false;
        }
        if (!Validation.isValidatedString(this, binding.tvAddress.getText().toString(), "Please select address")) {
            return false;
        }
      /*  if (!Validation.isValidatedString(this, binding.tvBedrooms.getText().toString(), "Please select room details")) {
            return false;
        }*/
        if (!Validation.isValidatedString(this, when, "Please select when")) {
            return false;
        }

        if (!Validation.isValidatedString(this, howOften, "Please select how often")) {

        }
        if (!Validation.isValidatedString(this, jobType, "Please choose provider")) {
            return false;
        }
        /*if (!Validation.isValidatedString(this, estTime, "Please choose all the fields")) {
            return false;
        }*/
        if (!Validation.isValidatedString(this, estPrice, "Please choose all the fields")) {
            return false;
        }
        return true;
    }

    private void getLastSavedObject(String json) {
        createJobReq = new Gson().fromJson(json, CreateJobNewReq.class);
        howOften = createJobReq.howOften;
        date = createJobReq.date;
        time = createJobReq.time;
        when = createJobReq.when;

        //address
        mGetAddress.clear();
        mGetAddress.add(createJobReq.address);

        // Room Detail
        /*sqft = createJobReq.sqft;
        cleaning = createJobReq.cleaningType;
        bedrooms = createJobReq.bedrooms;

        kitchen = createJobReq.kitchen;
        bathrooms = createJobReq.bathrooms;
        others = createJobReq.others;*/

        clearTaskSizeUI();

        //about provider
        estPrice = createJobReq.estPrice;
        estTime = "" + createJobReq.estTime;
        fixedPrice = createJobReq.price;

        providerId = createJobReq.providerId;
        jobType = createJobReq.jobType;
        if (!jobType.equals("Fixed Price")) {
            fixedPrice = "";
        }
        //extras
        mExtraCleaning.clear();

        // We dont need to show Previously selected Extra Cleaning here: So that is why this is commented
        /*if (createJobReq.extraCleaning != null) {
            mExtraCleaning.addAll(createJobReq.extraCleaning);
        }*/
        mImageList.clear();

        // calculate est time and price
        calculateExtraTimePrice();

        // Calculate price acc to cleaning type
        calculatePriceAccToCleaningType();

        setLastSavedJob(createJobReq);
    }

    @SuppressLint("SetTextI18n")
    void setLastSavedJob(CreateJobNewReq createJobReq) {

        //set cleaning Type
        if (createJobReq.cleaningType.equalsIgnoreCase("House")) {
            binding.tvHouseCleaning.performClick();
        } else if (createJobReq.cleaningType.equalsIgnoreCase("Carpet")) {
            binding.tvcarpetCleaning.performClick();
        } else if (createJobReq.cleaningType.equalsIgnoreCase("Window")) {
            binding.tvWindowCleaning.performClick();
        }

        //set address
        //binding.tvAddress.setText(createJobReq.address.get(0).street + "," + createJobReq.address.get(0).city + "," + createJobReq.address.get(0).state);
        binding.tvAddress.setText(createJobReq.address.street);

        //set Price
        // binding.estPrice.setText(getString(R.string.doller) + createJobReq.estPrice);// Check n then Remove

        //set date time
        binding.tvWhenDate.setText(createJobReq.date);
        //set when
        createJobReq.when = "";
        createJobReq.howOften = "";
        when = "";
        howOften = "";
        jobType = "";
        createJobReq.jobType = "";

        if (createJobReq.when.equalsIgnoreCase("Now")) {
            // selectedText(binding.tvWhenNow, binding.tvWhenToday, binding.tvWhenFuture);
        } else if (createJobReq.when.equalsIgnoreCase("Today")) {
            isShowTime = false;
            //selectedText(binding.tvWhenToday, binding.tvWhenNow, binding.tvWhenFuture);
        } else if (createJobReq.when.equalsIgnoreCase("Future")) {
            isShowDate = false;
            //selectedText(binding.tvWhenFuture, binding.tvWhenToday, binding.tvWhenNow);
        }

        // binding.estTime.setText("" + createJobReq.estTime); // Check n then Remove


        binding.yourPrice.setText(getString(R.string.doller) + createJobReq.price);
        switch (createJobReq.jobType) {
            case "Fixed Price":
                //selectProviderType(binding.tvFixedPrice, binding.tvMakeBidName, binding.tvChooseProvider, createJobReq.jobType);
                binding.yourPrice.setText(getString(R.string.doller) + createJobReq.price);
                break;
            case "Bid":
                // selectProviderType(binding.tvMakeBidName, binding.tvChooseProvider, binding.tvFixedPrice, createJobReq.jobType);
                break;
            default:
                break;
        }
        //set extra cleaning

       /* try {
            String extraCleaning = "";
            for (int i = 0; i < createJobReq.extraCleaning.size(); i++) {
                ExtraCleaningRes res = createJobReq.extraCleaning.get(i);
                if (res.isSelected) {
                    if (i == 0) {
                        extraCleaning = res.name;
                    } else
                        extraCleaning = extraCleaning + ", " + res.name;
                }
                extraTimeDbl = extraTimeDbl + res.extraTime;
                binding.tvExtraItems.setText(extraCleaning);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //set Images Adapter
        // mImageAdapter.notifyDataSetChanged();
        //}
    }


    private void calculatePriceAccToCleaningType() {
        if (createJobReq.cleaningType.equalsIgnoreCase("House")) {
            if (sqft != null) {
                calculateHouseTaskPrice();
            }
        } else if (createJobReq.cleaningType.equalsIgnoreCase("Carpet")) {
            createJobReq.rooms.clean = String.valueOf(mCleanRoom);
            createJobReq.rooms.protect = String.valueOf(mProtectRoom);
            createJobReq.rooms.deodrize = String.valueOf(mDeoRoom);

            createJobReq.bath.clean = String.valueOf(mCleanBath);

            createJobReq.bath.protect = String.valueOf(mProtectBath);
            createJobReq.bath.deodrize = String.valueOf(mDeoBath);

            createJobReq.entry.clean = String.valueOf(mCleanEntryHall);
            createJobReq.entry.protect = String.valueOf(mProtectEntryHall);
            createJobReq.entry.deodrize = String.valueOf(mDeoEntryHall);

            createJobReq.stairCase.clean = String.valueOf(mCleanStaircase);
            createJobReq.stairCase.protect = String.valueOf(mProtectStaircase);
            createJobReq.stairCase.deodrize = String.valueOf(mDeoStaircase);
            calculateCarpetTaskTimePrice();
        }
        /*Cleaning type is window*/
        else {
            createJobReq.firstFloorWindows = mFirstFloor;
            createJobReq.secondFloorWindows = mSecondFloor;
            createJobReq.frenchWindows = mFrenchWindows;
            createJobReq.slidingWindows = mPatioDoor;
            createJobReq.gardenWindows = mGardenWindows;
            createJobReq.wardrobeMirrors = mWardrobeMirror;
            createJobReq.screens = mScreens;
            createJobReq.skylights = mSkylight;
            createJobReq.stories = mStories;
            calculateWindowsTaskTimePrice();

        }

    }


    private void calculateHouseTaskPrice() {

       /* NORMAL PRICE

        Average between:

        1- 15 minutes per bedroom/other rooms + 20 minutes per bath + 30 minutes per kitchen.
        2 - SQFT/700= hours

        That will give us the estimated time and then we multiply by the recommended price per hour of each city. Iowa will be $25.
        */

        int roomTime = bedrooms * 15;
        int bathroomTime = bathrooms * 20;
        int kitchenTime = kitchen * 30;
        double sqftTime = (sqft / (700)) * 60;
        Log.d(LOG_TAG, "House Price   " + sqft);
        Log.d(LOG_TAG, "House Price  roomTime " + roomTime + " bathroomTime " + bathroomTime + " kitchenTime " + kitchenTime + " sqftTime " + sqftTime);
        taskTimeDbl = (roomTime + kitchenTime + sqftTime + bathroomTime) / 120;
        Double totalTime = taskTimeDbl + extraTimeDbl;
        DecimalFormat df = new DecimalFormat("#.##");
        totalTime = Double.valueOf(df.format(totalTime));
        estTime = totalTime.toString();
        binding.estTime.setText(estTime);
        //* add extra services price and task price and set on textview*//*
        taskPriceDbl = taskTimeDbl * 25;
        Double finalPrice = taskPriceDbl + extraPriceDbl;
        finalPrice = Double.valueOf(df.format(finalPrice));
        estPrice = finalPrice.toString();

        Double finalTime = Double.valueOf(estTime);
        finalTime = Double.valueOf(df.format(finalTime));

        if (mProviderType.equalsIgnoreCase("ServiceProvider")) {
            Double providerHour = Double.valueOf(mProviderHourlyPrice);
            Double fPrice = providerHour * finalTime;
            fPrice = Double.valueOf(new DecimalFormat("##.##").format(fPrice));

            binding.estPrice.setText(getString(R.string.doller) + " " + fPrice);
            estPrice = String.valueOf(fPrice);
        } else {
            binding.estPrice.setText(getString(R.string.doller) + " " + estPrice);
        }

        String tasks = "";
        if (bedrooms > 0) {
            if (bedrooms == 1) {
                tasks += bedrooms + " Bedroom";
            } else {
                tasks += bedrooms + " Bedrooms";
            }
        }
        if (kitchen > 0) {
            if (kitchen == 1) {
                tasks += ", " + kitchen + " Kitchen";
            } else {
                tasks += ", " + kitchen + " Kitchens";
            }
        }

        if (bathrooms > 0) {
            if (bathrooms == 1) {
                tasks += ", " + bathrooms + " Bathroom";
            } else {
                tasks += ", " + bathrooms + " Bathrooms";
            }
        }
        if (others > 0) {
            if (others == 1) {
                tasks += ", " + others + " Other";
            } else {
                tasks += ", " + others + " Others";
            }
        }
        if (sqft != 0.0)
            tasks += ", " + sqft + " SQFT ";

        binding.tvBedrooms.setText(tasks);


    }

    private void calculateCarpetTaskTimePrice() {
//        // ROOMS
//        int mRoomsPrice = 0;
//        for (int i = 0; i < mCleanRoom; i++) {
//            if (i == 0) {
//                mRoomsPrice += 55;
//            } else {
//                mRoomsPrice += 35;
//            }
//        }
//        int roomProtect = mProtectRoom * 15;
//        int roomDeodorize = mDeoRoom * 15;
//        mRoomsPrice = mRoomsPrice + roomProtect + roomDeodorize;
//
//        //BATH
//        int mBathPrice = 0;
//        int bathClean = mCleanBath * 15;
//        int bathProtect = mProtectBath * 15;
//        int bathDeo = mDeoBath * 15;
//        mBathPrice = bathClean + bathProtect + bathDeo;
//
//        //HALLWAY
//        int mHallwayPrice = 0;
//        int hallwayClean = mCleanEntryHall * 15;
//        int hallwayProtect = mProtectEntryHall * 15;
//        int hallwayDeo = mDeoEntryHall * 15;
//        mHallwayPrice = hallwayClean + hallwayProtect + hallwayDeo;
//
//
//        // STAIRS
//        int mStairsPrice = 0;
//        int stairClean = mCleanStaircase * 35;
//        int stairProtect = mProtectStaircase * 15;
//        int stairDeodorize = mDeoStaircase * 15;
//        mStairsPrice = stairClean + stairProtect + stairDeodorize;
//        Log.d(LOG_TAG, "Carpet Price mRoomsPrice " + mRoomsPrice + " mBathPrice " + mBathPrice + " mHallwayPrice " + mHallwayPrice + " mStairsPrice " + mStairsPrice);
//        int taskPrice = mRoomsPrice + mBathPrice + mHallwayPrice + mStairsPrice;
//        if (taskPrice < 151) {
//            taskPrice = 150;
//        }
//
//        Log.d(LOG_TAG, " Carpet Price : " + taskPrice);
//
//
//        estPrice = String.valueOf(taskPrice); //
        estPrice = PriceCalculation.getCarpetPrice(mCleanRoom, mProtectRoom, mDeoRoom, mCleanBath, mProtectBath, mDeoBath, mCleanEntryHall, mProtectEntryHall,
                mDeoEntryHall, mCleanStaircase, mProtectStaircase, mDeoStaircase);
        binding.estPrice.setText(estPrice);


    }

    private void calculateWindowsTaskTimePrice() {
//        // WINDOW FIRST FLOOR
//        float mWindowFFPrice = 0.0f;
//        for (int i = 0; i < mFirstFloor; i++) {
//            mWindowFFPrice += 3.50;
//        }
//
//        // WINDOW SECOND FLOOR
//        float mWindoSFPrice = 0.0f;
//        for (int i = 0; i < mSecondFloor; i++) {
//            mWindoSFPrice += 5.00;
//        }
//
//        // SCREENS
//        float mScreensPrice = 0.0f;
//        mScreensPrice = mScreens * 1.0f;
//
//        // FRENCH DOOR
//        float mFrenchPrice = 0.0f;
//        mFrenchPrice = mFrenchWindows * 5 * 1.0f;
//
//        // SLIDING DOOR
//        float mSlidingPrice = 0.0f;
//        mSlidingPrice = mPatioDoor * 10 * 1.0f;
//
//        // MIRRORS
//        float mMirrorsPrice = 0.0f;
//        mMirrorsPrice = mWardrobeMirror * 4 * 1.0f;
//
//        float taskPrice = mWindowFFPrice + mWindoSFPrice + mScreensPrice + mFrenchPrice + mSlidingPrice + mMirrorsPrice;
//        Log.d(LOG_TAG, "Window Price mWindowFFPrice " + mWindowFFPrice + " mWindoSFPrice " + mWindoSFPrice + " mScreensPrice " + mScreensPrice + " mFrenchPrice " + mFrenchPrice + " mSlidingPrice " + mSlidingPrice + " mMirrorsPrice " + mMirrorsPrice);
//
//        if (taskPrice < 126) {
//            taskPrice = 125;
//        }
//        estPrice = String.valueOf(taskPrice);
        estPrice = PriceCalculation.getWindowPrice(mFirstFloor, mSecondFloor, mScreens, mFrenchWindows, mPatioDoor, mWardrobeMirror);
        binding.estPrice.setText(estPrice);
        binding.tvBedrooms.setTextColor(getResources().getColor(R.color.colorWhite));

    }

    private void calculateExtraTimePrice() {
        /*
        Deep clean: 15 minutes per room.
        Inside Cabinets: Add 30 minutes.
        Inside Fridge: Add 15 minutes.
        Inside Oven: Add 15 minutes.
        Laundry: Add 1 hour.
        Interior Windows. Add 5 minutes per room ( living room, Kitchen, rooms... )
        */
        int deepCleanTime = 0, insideCabinetPrice = 0, insideFridgePrice = 0, insideOvenPrice = 0, laundryPrice = 0, ineteriorWindowsPrice = 0;

        if (mExtraCleaning != null && mExtraCleaning.size() > 0) {
            for (int i = 0; i < mExtraCleaning.size(); i++) {
                switch (mExtraCleaning.get(i).name.toString()) {
                    case "Deep Clean":
                        deepCleanTime = 15 * bedrooms;
                        break;
                    case "Inside Cabinets":
                        insideCabinetPrice = 30;
                        break;
                    case "Inside Fridge":
                        insideFridgePrice = 15;
                        break;
                    case "Inside Oven":
                        insideOvenPrice = 15;
                        break;
                    case "Laundry":
                        laundryPrice = 60;
                        break;
                    case "Interior Windows":
                        ineteriorWindowsPrice = 5 * bedrooms;
                        break;

                }
            }
        }
        int finalP = deepCleanTime + insideCabinetPrice + insideFridgePrice + insideOvenPrice + laundryPrice + ineteriorWindowsPrice;
        extraTimeDbl = Double.valueOf(finalP);
        //*Convert the minutes in Hour and then add task time and set on textview*//*
        extraTimeDbl = extraTimeDbl / 60;

        Double extraTime = extraTimeDbl + taskTimeDbl;
        DecimalFormat df = new DecimalFormat("#.##");
        extraTime = Double.valueOf(df.format(extraTime));
        estTime = extraTime.toString();
        binding.estTime.setText(estTime);
        //* add extra services price and task price and set on textview*//*
        extraPriceDbl = extraTimeDbl * 25;
        Double finalPrice = extraPriceDbl + taskPriceDbl;
        finalPrice = Double.valueOf(df.format(finalPrice));
        estPrice = finalPrice.toString();

        //Log.e("EstimatedPrice==","Extra"+getString(R.string.doller) + " " + estPrice);
        binding.estPrice.setText(getString(R.string.doller) + " " + estPrice);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mApiCallCount = 0;


    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        new Handler().postDelayed(() -> {
            // Magic here
            sequenceGuide();
        }, 100); // Millisecond 1000 = 1 sec


    }

    private void sequenceGuide() {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        scrollView = // your scrollview
                binding.scrollView;

        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();

        Shape shape = new RectangleShape(width, height);

        config.setShape(shape);
        config.setDelay(250);

        // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID, scrollView);

        sequence.setConfig(config);


        int cardCleaningHeight = binding.cardCleaning.getHeight();
        cardCleaningHeight = binding.cardCleaning.getMeasuredHeight();

        config.setContentTextColor(ContextCompat.getColor(CreateJobActivity.this, R.color.redColor));

        sequence.addSequenceItem(binding.cardCleaning,
                "STEP 1: Select Cleaning Type", "* This is Mandatory Field", "GOT IT", true, cardCleaningHeight);


        int cardAddressHeight = binding.cardAddress.getMeasuredHeight();

        sequence.addSequenceItem(binding.cardAddress,
                "STEP 2: Select Address", "* This is Mandatory Field", "GOT IT", true, cardAddressHeight);
        int cardRoomDetailHeight = binding.cardRoomDetail.getMeasuredHeight();

        sequence.addSequenceItem(binding.cardRoomDetail,
                "STEP 3: Select Task Size", "* This is Mandatory Field", "GOT IT", true, cardRoomDetailHeight);

        int cardWhenHeight = binding.cardWhen.getMeasuredHeight();

        sequence.addSequenceItem(binding.cardWhen,
                "STEP 4: Select When", "* This is Mandatory Field", "GOT IT", true, cardWhenHeight);

        int cardHowOftenHeight = binding.cardHowOften.getMeasuredHeight();

        sequence.addSequenceItem(binding.cardHowOften,
                "STEP 5: Select How Often", "* This is Mandatory Field", "GOT IT", true, cardHowOftenHeight);

        int cardExtraHeight = binding.cardExtra.getMeasuredHeight();

        config.setContentTextColor(ContextCompat.getColor(CreateJobActivity.this, R.color.colorBlack));

        sequence.addSequenceItem(binding.cardExtra,
                "STEP 6: Choose Extra Cleaning", "Optional Field", "GOT IT", true, cardExtraHeight);
        int cardImagesHeight = binding.cardImages.getMeasuredHeight();

        sequence.addSequenceItem(binding.cardImages,
                "STEP 7: Upload Images", "Optional Field", "GOT IT", true, cardImagesHeight);
        int cardSelectTypeHeight = binding.cardSelectType.getMeasuredHeight();

        config.setContentTextColor(ContextCompat.getColor(CreateJobActivity.this, R.color.redColor));

        sequence.addSequenceItem(binding.cardSelectType,
                "STEP 8: Choose Provider ", "* This is Mandatory Field", "GOT IT", true, cardSelectTypeHeight);

        int cardSpcNotesHeight = binding.cardSpcNotes.getMeasuredHeight();
        config.setContentTextColor(ContextCompat.getColor(CreateJobActivity.this, R.color.colorBlack));
        // Special Notes
        sequence.addSequenceItem(binding.cardSpcNotes,
                "STEP 9: Add Special Notes for Provider", "Optional Field", "GOT IT", true, cardSpcNotesHeight);
        // Done

        int btnDoneHeight = binding.btnDone.getMeasuredHeight();

        config.setContentTextColor(ContextCompat.getColor(CreateJobActivity.this, R.color.colorPrimaryDark));
        sequence.addSequenceItem(binding.btnDone,
                "STEP 10: Click on Done Button", "That's it! You are ready to create your first job", "GOT IT", true, btnDoneHeight);
        sequence.start();
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        String startTime = startHour + " : " + startMin;
        String endTime = endHour + " : " + endMin;
        Log.d("startTime==", startTime + "==endTime==" + endTime);
        //
        if (!startTime.equalsIgnoreCase(endTime)) {
            time = startTime + " - " + endTime;
            binding.tvWhenTime.setText(time);
            createJobReq.time = time;
            //if user changed date and time then show selected item and change "when" value else it show previous value
            if (whenValue.equals("Today")) {
                when = "Today";
                selectedText(binding.tvWhenToday, binding.tvWhenNow, binding.tvWhenFuture);
            } else if (whenValue.equals("Future")) {
                when = "Future";
                selectedText(binding.tvWhenFuture, binding.tvWhenNow, binding.tvWhenToday);
            }
        } else {
            Toast.makeText(this, "Start and End time cannot be same.", Toast.LENGTH_SHORT).show();
            binding.tvWhenTime.setText(R.string.time_range);
        }

    }


    // Read Task Size detail from Prefrences

    private void getDataFromPrefrences() {

        bedrooms = PreferenceHandler.readInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_BEDROOM, 0);
        bathrooms = PreferenceHandler.readInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_BATHROOM, 0);
        kitchen = PreferenceHandler.readInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_KITCHEN, 0);
        others = PreferenceHandler.readInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_OTHER, 0);

        float totalSqft = 0;
        try {
            totalSqft = PreferenceHandler.readFloat(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_SQFT, 0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tasks = "";
        if (bedrooms > 0) {
            if (bedrooms == 1) {
                tasks += bedrooms + " Bedroom";
            } else {
                tasks += bedrooms + " Bedrooms";
            }
        }
        if (kitchen > 0) {
            if (kitchen == 1) {
                tasks += ", " + kitchen + " Kitchen";
            } else {
                tasks += ", " + kitchen + " Kitchens";
            }
        }
        if (bathrooms > 0) {
            if (bathrooms == 1) {
                tasks += ", " + bathrooms + " Bathroom";
            } else {
                tasks += ", " + bathrooms + " Bathrooms";
            }
        }
        if (others > 0) {
            if (others == 1) {
                tasks += ", " + others + " Other";
            } else {
                tasks += ", " + others + " Others";
            }
            //  String tasks = bedrooms + " Bedrooms," + kitchen + " kitchens," + bathrooms + " Bathrooms," + others + " others," + totalSqft + " SQFT";
            binding.tvBedrooms.setText(tasks + totalSqft + " SQFT");
        }
    }

    public void showDialogWithLayout() {
        try {
            customView = new Dialog(this);
            customView.setContentView(R.layout.layout_choose_provider);
            customView.setTitle(getResources().getString(R.string.choose_provider));

            RecyclerView recyclerView = customView.findViewById(R.id.recyclerView);

            ImageView cancel = customView.findViewById(R.id.imgCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mApiCallCount = 0;
                    customView.dismiss();
                }
            });
            // Choose from Other service provider
            TextView otherProvider = customView.findViewById(R.id.txtOther);
            otherProvider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(SelectProviderActivity.class, 103);
                }

            });

            customView.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mApiCallCount = 0;
                }
            });

            LinearLayoutManager mManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mManager);

            AdapterSelectProvider adapter = new AdapterSelectProvider(CreateJobActivity.this, mProvidersList, this, this, "Favorite");
            recyclerView.setAdapter(adapter);

//            //jobSpecViewModel = getViewModel(JobSpecViewModel.class);
//            jobSpecViewModel.getProvidersResLiveData()
//                    .observe(this, res -> {
//                        AdapterSelectProvider adapter = new AdapterSelectProvider(CreateJobActivity.this, res.providerRes, this, "Favorite");
//                        recyclerView.setAdapter(adapter);
//                    });

            customView.show();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void getFavoriteProvider() {
        jobSpecViewModel.getMyFavoriteProviders(new ChooseProviderReq(getPrefHelper().getAuthToken()), true);
    }

    @Override
    public void onListItemClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        Intent mIntent = new Intent(this, SelectProviderDetailActivity.class);
        mIntent.putExtra("providerRes", res);
        startActivity(mIntent);

    }

    @Override
    public void onHireClick(Object object) {
        GetProviderRes res = (GetProviderRes) object;
        providerId = res.providerId;
        providerName = res.providerName;
        providerImage = res.providerImage;
        // Provider Hourly Price
        mProviderHourlyPrice = res.providerPrice;
        //
        binding.tvChooseProvider.setText(res.providerName);
        jobType = "Service Provider";
        selectProviderType(binding.tvChooseProvider, binding.tvMakeBidName, binding.tvFixedPrice, jobType);
        Glide.with(this).load(res.providerImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                .centerCrop().circleCrop()).into(binding.imgChooseProvider);
        changeTextAccToProviderType(); //

       /* mProviderType = "ServiceProvider";
        calculateHouseTaskPrice();*/

        calculatePriceWithTypeServiceProvider();

        if (customView != null && customView.isShowing()) {
            customView.dismiss();
        }
    }

    private String getFilteredMonth(String date) {
        String filteredDate = "";

        switch (date) {
            case "1":
                filteredDate = "Jan";
                break;
            case "2":
                filteredDate = "Feb";
                break;
            case "3":
                filteredDate = "Mar";
                break;
            case "4":
                filteredDate = "Apr";
                break;
            case "5":
                filteredDate = "May";
                break;
            case "6":
                filteredDate = "Jun";
                break;
            case "7":
                filteredDate = "Jul";
                break;
            case "8":
                filteredDate = "Aug";
                break;
            case "9":
                filteredDate = "Sep";
                break;
            case "10":
                filteredDate = "Oct";
                break;
            case "11":
                filteredDate = "Nov";
                break;
            case "12":
                filteredDate = "Dec";
                break;
        }


        return filteredDate;

    }

    public void showTimeSelectorDilaog() {

        try {

            View customView = View.inflate(CreateJobActivity.this, R.layout.dialog_select_time, null);
            MaterialDialog builder;
            builder = new MaterialDialog.Builder(CreateJobActivity.this)
                    .customView(customView, true)
                    .cancelable(true).title("Select Time Range").build();
            TextView startText = customView.findViewById(R.id.txtStart);
            TextView endText = customView.findViewById(R.id.txtEnd);
            AppCompatButton mSubmitButton = customView.findViewById(R.id.btnTaskSize);

            // get seekbar from view
            final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) customView.findViewById(R.id.rangeSeekbar1);


            // set listener
            rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                @Override
                public void valueChanged(Number minValue, Number maxValue) {
                    startText.setText(String.valueOf(minValue));
                    endText.setText(String.valueOf(maxValue));
                }
            });

            // set final value listener
            rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
                @Override
                public void finalValue(Number minValue, Number maxValue) {
//                    Log.d("CRS=>", String.valueOf(minValue)+":00" + " - " + String.valueOf(maxValue)+":00");
                    String minVal = String.valueOf(minValue);
                    String maxVal = String.valueOf(maxValue);

//                    if (!minVal.equalsIgnoreCase("0") && minVal.length()==1){
//                        minVal="0"+minVal;
//                    }
//
//                    if (!maxVal.equalsIgnoreCase("0") && minVal.length()==1){
//                        maxVal="0"+maxVal;
//                    }

                    if (minVal.equals("0")) {
                        minVal = "0";
                    } else if (minVal.equals("1")) {
                        minVal = "01";
                    } else if (minVal.equals("2")) {
                        minVal = "02";
                    } else if (minVal.equals("3")) {
                        minVal = "03";
                    } else if (minVal.equals("4")) {
                        minVal = "04";
                    } else if (minVal.equals("5")) {
                        minVal = "05";
                    } else if (minVal.equals("6")) {
                        minVal = "06";
                    } else if (minVal.equals("7")) {
                        minVal = "07";
                    } else if (minVal.equals("8")) {
                        minVal = "08";
                    } else if (minVal.equals("9")) {
                        minVal = "09";
                    }

                    if (maxVal.equals("0")) {
                        maxVal = "0";
                    } else if (maxVal.equals("1")) {
                        maxVal = "01";
                    } else if (maxVal.equals("2")) {
                        maxVal = "02";
                    } else if (maxVal.equals("3")) {
                        maxVal = "03";
                    } else if (maxVal.equals("4")) {
                        maxVal = "04";
                    } else if (maxVal.equals("5")) {
                        maxVal = "05";
                    } else if (maxVal.equals("6")) {
                        maxVal = "06";
                    } else if (maxVal.equals("7")) {
                        maxVal = "07";
                    } else if (maxVal.equals("8")) {
                        maxVal = "08";
                    } else if (maxVal.equals("9")) {
                        maxVal = "09";
                    }

                    currentTime = Integer.parseInt(mCurrentDateTime);
                    selectedTime = Integer.parseInt(minVal);
                    Log.v(LOG_TAG, "mCurrentDateTime=" + currentTime + "SElectedTime=" + selectedTime);
                    mFutureTime = minVal + ":00" + " - " + maxVal + ":00";
                    time = mFutureTime;
                }
            });
            mSubmitButton.setOnClickListener(v -> {

                if (selectedTime > 0 && selectedTime < currentTime) {
                    Toast.makeText(CreateJobActivity.this, "Please choose future time.", Toast.LENGTH_SHORT).show();
                } else {
                    binding.tvWhenTime.setText(mFutureTime);
                    createJobReq.time = mFutureTime;
                    //if user changed date and time then show selected item and change "when" value else it show previous value
                    if (whenValue.equals("Today")) {
                        when = "Today";
                        selectedText(binding.tvWhenToday, binding.tvWhenNow, binding.tvWhenFuture);
                    } else if (whenValue.equals("Future")) {
                        when = "Future";
                        selectedText(binding.tvWhenFuture, binding.tvWhenNow, binding.tvWhenToday);
                    }
                    //
                    builder.dismiss();
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onListItemWithPositionClick(Object object, int position) {

    }

    @Override
    public void onListCancelWithPositionClick(Object object, int position) {
        ImageUrlReq cancelledImage = (ImageUrlReq) object;
        //
        mImageList.remove(cancelledImage);
        //
        mImageAdapter.notifyDataSetChanged();
    }

    void clearTaskSizeUI() {
        binding.cardRoomDetail.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
        binding.tvTitleRoomDetail.setTextColor(getApplicationContext().getResources().getColor(R.color.secondary_text));
        binding.tvTitleRoomDetail.setText("Task Size");
        binding.roomImg.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        binding.tvBedrooms.setText("");
    }


    private void calculatePriceWithTypeServiceProvider() {
        if (jobType.equalsIgnoreCase("Bid") || jobType.equalsIgnoreCase("Fixed Price")) { //
            binding.estTimeText.setText(getResources().getString(R.string.est_time_hour));// Hour
        } else {
            binding.estTimeText.setText(getResources().getString(R.string.booked_hour));
        }
        mProviderType = "ServiceProvider";
        if (mCleaningType.equalsIgnoreCase("House")) {
            calculateHouseTaskPrice();
        } else if (mCleaningType.equalsIgnoreCase("Window")) {
            calculateWindowsTaskTimePrice();
        } else {
            calculateCarpetTaskTimePrice();
        }
    }

    public void showHouseSizeDialog() {
        try {

            View customView = View.inflate(CreateJobActivity.this, R.layout.dialog_task_size, null);
            MaterialDialog builder;
            builder = new MaterialDialog.Builder(CreateJobActivity.this)
                    .customView(customView, true)
                    .cancelable(true).title("How Big is Your House?").build();

            TextView startText = customView.findViewById(R.id.txtStart);
            TextView endText = customView.findViewById(R.id.txtEnd);
            IndicatorSeekBar seekBar = customView.findViewById(R.id.seekBar);
            NumberPicker bedroomPicker = customView.findViewById(R.id.noOFBedrooms);
            NumberPicker kitchenPicker = customView.findViewById(R.id.noOfKitchens);
            NumberPicker bathroomPicker = customView.findViewById(R.id.noOfBathroom);
            NumberPicker otherPicker = customView.findViewById(R.id.noOfOther);
            AppCompatButton mSubmitButton = customView.findViewById(R.id.btnTaskSize);


            seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
                @Override
                public void onSeeking(SeekParams seekParams) {
                    String sqft = seekParams.progress + "";
                    mSqft = sqft;
                    startText.setText(seekParams.progress + "");
                }

                @Override
                public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                }
            });

            mSubmitButton.setOnClickListener(v -> {
                String sqft = mSqft;
                int bedroom = getSelectedValue(bedroomPicker);
                int bathroom = getSelectedValue(bathroomPicker);
                int kitchen = getSelectedValue(kitchenPicker);
                int other = getSelectedValue(otherPicker);
                if (sqft == null || sqft.isEmpty()) {
                    Toast.makeText(CreateJobActivity.this, "Please choose a valid sqft value", Toast.LENGTH_SHORT).show();
                } else if (sqft.startsWith("0")) {
                    Toast.makeText(CreateJobActivity.this, "Please choose a valid sqft value", Toast.LENGTH_SHORT).show();
                } else {
                    PreferenceHandler.writeInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_BEDROOM, bedroom);
                    PreferenceHandler.writeInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_BATHROOM, bathroom);
                    PreferenceHandler.writeInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_KITCHEN, kitchen);
                    PreferenceHandler.writeInteger(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_OTHER, other);
                    Float sqrFootArea = Float.valueOf(mSqft);
                    //
                    builder.dismiss();
                    //
                    Intent intent = new Intent(this, TaskSizeActivity.class);
                    intent.putExtra("SQFT", Double.parseDouble(sqft));
                    intent.putExtra("mCleaningType", "House");
                    intent.putExtra("Bedrooms", bedroom);
                    intent.putExtra("Kitchen", kitchen);
                    intent.putExtra("Bathrooms", bathroom);
                    intent.putExtra("Others", other);
                    startActivityForResult(intent, 111);

                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Integer getSelectedValue(NumberPicker numberPicker) {

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(20);

        // Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);

        // Set scroller enabled
        numberPicker.setScrollerEnabled(true);

        // Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);
        return numberPicker.getValue();
    }

}
