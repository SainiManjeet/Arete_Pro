package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.CompleteJReq;
import com.apnitor.arete.pro.api.request.CreateJobReq;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.databinding.ActivityCreateJobBinding;
import com.apnitor.arete.pro.interfaces.ListItemCancelCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.service.UploadImageService;
import com.apnitor.arete.pro.util.PermissionUtitlity;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.util.UploadImage;
import com.apnitor.arete.pro.util.Validation;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import tourguide.tourguide.TourGuide;

public class EditJobActivity extends BaseActivity implements View.OnClickListener, ListItemCancelCallback {
    ArrayList<GetAddress> mGetAddress;
    GetJobRes getJobReq = new GetJobRes();
    boolean isShowTime = true;
    boolean isShowDate = true;
    String LOG_TAG = "EditJobActivity";
    String json = "";
    private ActivityCreateJobBinding binding;
    private String cleaning = "", when = "", whenValue = "", time = "", date = "", howOften = "", jobType = "", fixedPrice = "";
    private String providerId = "", providerName, providerImage, estTime = "", estPrice = "", spcNotesForProvider = "";
    private int bedrooms = 0, bathrooms = 0, others = 0, kitchen = 0;
    private Double sqft = 0.0, taskTimeDbl = 0.0, taskPriceDbl = 0.0, extraPriceDbl = 0.0, extraTimeDbl = 0.0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private UploadImage mUploadImage;
    private Uri mUri;
    private JobSpecViewModel jobSpecViewModel;
    private ArrayList<ImageUrlReq> mImageList;
    private ArrayList<ExtraCleaningRes> mExtraCleaning;
    private int GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    private ImageAdapter mImageAdapter;
    private CardView cardAddress;
    private TourGuide mTourGuideHandler;
    private String mFirstTime = "TRUE";
    private String mType, mPosition;
    private int mCleanRoom, mCleanBath, mCleanEntryHall, mCleanStaircase;
    private int mProtectRoom, mProtectBath, mProtectEntryHall, mProtectStaircase;
    private int mDeoRoom, mDeoBath, mDeoEntryHall, mDeoStaircase;
    private int mFirstFloor, mSecondFloor, mFrenchWindows, mPatioDoor, mGardenWindows, mWardrobeMirror, mScreens, mSkylight, mStories;
    GetJobRes mJob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        //
        bindViews();
        //
        setToolBar("Edit Job");
        //
        Intent intent = getIntent();
        mType = intent.getStringExtra("type");
        disableCleaningType(mType);
        mPosition = intent.getStringExtra("position");

        mJob = getIntent().getParcelableExtra("GetJobRes");
        if (mJob != null) {
            getLastSavedObject("");
        }
//        json = PreferenceHandler.readString(this, PreferenceHandler.PREF_KEY_UPDATE_JOB, "");
//        if (!json.isEmpty()) {
//            getLastSavedObject(json);
//        }
        checkIfFieldSelected();
    }

    private void disableCleaningType(String cleaningType) {
        Log.d(LOG_TAG, " Cleaning Type is " + cleaningType);
        //
        binding.tvHouseCleaning.setEnabled(false);
        binding.tvcarpetCleaning.setEnabled(false);
        binding.tvWindowCleaning.setEnabled(false);
        //
        switch (cleaningType) {
            case "House":
                binding.tvHouseCleaning.setEnabled(true);
                break;
            case "Carpet":
                binding.tvcarpetCleaning.setEnabled(true);
                break;
            case "Window":
                binding.tvWindowCleaning.setEnabled(true);
                break;

        }
    }

    private void bindViews() {
        cardAddress = findViewById(R.id.cardAddress);
        binding = bindContentView(R.layout.activity_create_job);
        jobSpecViewModel = getViewModel(JobSpecViewModel.class);
        binding.tvHouseCleaning.setOnClickListener(this);
        binding.tvcarpetCleaning.setOnClickListener(this);
        binding.tvWindowCleaning.setOnClickListener(this);
        binding.cardRoomDetail.setOnClickListener(this);
        binding.cardAddress.setOnClickListener(this);
        binding.cardExtra.setOnClickListener(this);
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
        //binding.btnNext.setOnClickListener(this);
        binding.btnDone.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.imageRecycler.setLayoutManager(manager);
        binding.imageRecycler.setNestedScrollingEnabled(false);
        mImageList = new ArrayList<>();
        mExtraCleaning = new ArrayList<>();
        mGetAddress = new ArrayList<>();
        mImageAdapter = new ImageAdapter(EditJobActivity.this, mImageList, null, this);
        binding.imageRecycler.setAdapter(mImageAdapter);
        jobSpecViewModel.createJobLiveData().observe(this, createJobRes -> {
            Toast.makeText(this, "Your job created successfully." + createJobRes.jobId, Toast.LENGTH_SHORT).show();
        });
    }

    private void checkIfFieldSelected() {
        if (binding.tvAddress.getText().toString().length() > 0) {
            binding.cardAddress.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            binding.tvAddress.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.tvTitleAddress.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.imgAddress.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }
        // Task Size
        if (binding.tvAddress.getText().toString().length() > 0) {
            binding.cardRoomDetail.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorPrimaryDark));
            binding.tvBedrooms.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.tvTitleRoomDetail.setTextColor(getApplicationContext().getResources().getColor(R.color.colorWhite));
            binding.roomImg.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
        }
        //Extra Cleaning
        Log.e("EXTRA==", "==" + binding.tvExtraItems.getText().toString());
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
        getJobReq.cleaningType = cleaning;
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
                //selectCleaningType(binding.tvHouseCleaning, binding.tvcarpetCleaning, binding.tvWindowCleaning, "House");
                break;
            case R.id.tvcarpetCleaning:
                //selectCleaningType(binding.tvcarpetCleaning, binding.tvWindowCleaning, binding.tvHouseCleaning, "Carpet");
                setCleaningType("Carpet");
                break;
            case R.id.tvWindowCleaning:
                // selectCleaningType(binding.tvWindowCleaning, binding.tvHouseCleaning, binding.tvcarpetCleaning, "Window");
                setCleaningType("Window");
                break;
            case R.id.cardRoomDetail:
                if (Validation.isValidatedAddress(this, cleaning, binding.tvAddress.getText().toString())) {
                    Intent intent = new Intent(this, TaskSizeActivity.class);
                    intent.putExtra("SQFT", sqft);
                    intent.putExtra("mCleaningType", cleaning);
                    intent.putExtra("Bedrooms", bedrooms);
                    intent.putExtra("Kitchen", kitchen);
                    intent.putExtra("Bathrooms", bathrooms);
                    intent.putExtra("Others", others);
                    startActivityForResult(intent, 111);
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
                            mBundle.putString("prevActivity", "CreateJob");
                            mBundle.putString("latitude", latitude);
                            mBundle.putString("longitude", longitude);
                        }
                    }
                    startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
                }
                break;
            case R.id.cardExtra:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    Log.e(LOG_TAG, "=" + binding.tvExtraItems.getText().toString());
                    Bundle mBundle = new Bundle();
                    mBundle.putString("name", binding.tvExtraItems.getText().toString());
                    PreferenceHandler.writeString(this, PreferenceHandler.PREF_EXTRA_CLEAN, binding.tvExtraItems.getText().toString());
//                    startActivityForResult(ExtraCleaningActivity.class, 102);
                    startActivityForResultWithExtra(ExtraCleaningActivity.class, 102, mBundle);
                }
                break;
            case R.id.linDate:
                if (Validation.isValidatedAddress(this, cleaning, binding.tvAddress.getText().toString())) {
                    selectDate(false);
                }
                break;
            case R.id.linTime:
                if (Validation.isValidatedAddress(this, cleaning, binding.tvAddress.getText().toString())) {
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
                    getJobReq.time = when;
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
                    if (isShowDate)
                        selectDate(true);
                    isShowDate = true;
                }
                break;
            case R.id.tvDaily:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvDaily, binding.tvWeekly, binding.tvByWeekly, binding.tvMonthly);
                    howOften = "Just Once";
                    getJobReq.howOften = howOften;
                    getJobReq.date = howOften;
                }
                break;
            case R.id.tvWeekly:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvWeekly, binding.tvDaily, binding.tvByWeekly, binding.tvMonthly);
                    howOften = "Weekly";
                    getJobReq.howOften = howOften;
                }
                break;
            case R.id.tvByWeekly:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvByWeekly, binding.tvWeekly, binding.tvDaily, binding.tvMonthly);
                    howOften = "Bi Weekly";
                    getJobReq.howOften = howOften;
                }
                break;
            case R.id.tvMonthly:
                if (Validation.isValidatedWhen(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when)) {
                    selectedText(binding.tvMonthly, binding.tvWeekly, binding.tvByWeekly, binding.tvDaily);
                    howOften = "Monthly";
                    getJobReq.howOften = howOften;
                }
                break;
            case R.id.add_image:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    mUploadImage = new UploadImage(this);
                    showPictureDialog();
                }
                break;
            case R.id.linMakeBid:
                if (Validation.isValidatedHowOften(this, cleaning, binding.tvAddress.getText().toString(), binding.tvBedrooms.getText().toString(), when, howOften)) {
                    jobType = "Bid";
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
                    startActivityForResult(SelectProviderActivity.class, 103);
                }
                break;
            case R.id.btnDone:
                spcNotesForProvider = UIUtility.getEditTextValue(binding.etSpcNotes);
                if (validationSuccess("btnDone")) {
                    if (getJobReq.images != null && getJobReq.images.size() > 0) {
                        getJobReq.images.clear();
                        getJobReq.images.addAll(mImageList);
                    }
                    CompleteJReq completeJReq = new CompleteJReq(getPrefHelper().getAuthToken(), getJobReq.jobId, getJobReq.cleaningType, getJobReq.bedrooms, getJobReq.bathrooms, getJobReq.kitchen, getJobReq.others, getJobReq.sqft,
                            getJobReq.date, getJobReq.time, getJobReq.howOften, getJobReq.when, getJobReq.priceType, getJobReq.specialNotesForProvider, getJobReq.images, (ArrayList<ExtraCleaningRes>) getJobReq.extraCleaning,
                            Double.valueOf(estTime),
                            estPrice, getJobReq.price, getJobReq.providerId, getJobReq.description, getJobReq.jobType);
                    //
                    jobSpecViewModel.completeJob(completeJReq);
                    //
                    jobSpecViewModel.completeJobLiveData().observe(this, createJobRes -> {
                        saveLastJob(completeJReq);
                        if (getJobReq.images != null && getJobReq.images.size() > 0) {
                            CreateJobReq createJobReq = new CreateJobReq();
                            createJobReq.images = getJobReq.images;
                            //
                            Intent mServiceIntent = new Intent(this, UploadImageService.class);
                            mServiceIntent.putExtra("jobId", getJobReq.jobId);
                            mServiceIntent.putExtra("authToken", getPrefHelper().getAuthToken());
                            mServiceIntent.putExtra("createJob", createJobReq);
                            if (Build.VERSION.SDK_INT > 25) {
                                startForegroundService(mServiceIntent);
                            } else {
                                startService(mServiceIntent);
                            }
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Job", getJobReq);
                    bundle.putString("position", mPosition);
                    bundle.putString("JobId", getJobReq.jobId);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(ResultCodes.CONFIRM_JOB_RESULT, intent);
                    finish();
                    overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
                }
                break;
            default:
                break;
        }
    }

    void saveLastJob(CompleteJReq completeJReq) {
        if (!json.isEmpty()) {
            getJobReq = new Gson().fromJson(json, GetJobRes.class);
            getJobReq.estPrice = estPrice;
            String createJobReqJson = new Gson().toJson(getJobReq);
            PreferenceHandler.writeString(this, PreferenceHandler.PREF_KEY_CREATE_JOB, createJobReqJson);
        }
    }


    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(EditJobActivity.this);
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (PermissionUtitlity.checkPermission(EditJobActivity.this)) {
                                    choosePhotoFromGallary();
                                }
                                break;
                            case 1:
                                if (PermissionUtitlity.checkPermissionCamera(EditJobActivity.this)) {
                                    dispatchTakePictureIntent();
                                }
                                break;
                        }
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
                    Toast.makeText(EditJobActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditJobActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 111) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    taskTimeDbl = 0.0;
                    taskPriceDbl = 0.0;
                    bedrooms = bundle.getInt("Bedrooms");
                    bathrooms = bundle.getInt("Bathrooms");
                    kitchen = bundle.getInt("Kitchen");
                    others = bundle.getInt("Others");
                    String strSqft = bundle.getString("SQFT");
                    if (strSqft != null)
                        sqft = Double.parseDouble(strSqft);
                    else
                        sqft = 0.0d;
                    // Float.valueOf(String.valueOf(strSqft));
                    //
                    //    PreferenceHandler.writeFloat(CreateJobActivity.this, PreferenceHandler.PREF_TOTAL_SQFT, Float.valueOf(String.valueOf(strSqft)));  //MAnjert
                    /*Convert the minutes in Hour and then add extra services price and set on textview*/
                    calculateTaskTimePrice();
                    // new For Edit job only
                    getJobReq.bedrooms = bedrooms;
                    getJobReq.bathrooms = bathrooms;
                    getJobReq.kitchen = kitchen;
                    getJobReq.others = others;
                    getJobReq.sqft = sqft;


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
                        // getJobReq.address = new ArrayList<>();
                        //getJobReq.address.addAll(mGetAddress);
                        getJobReq.address = mGetAddress.get(0);

                        //binding.tvAddress.setText(getJobReq.address.get(0).street + "," + getJobReq.address.get(0).city + "," + getJobReq.address.get(0).state);
                        // binding.tvAddress.setText(getJobReq.address.get(0).street + "," + getJobReq.address.get(0).state);
                        binding.tvAddress.setText(getJobReq.address.street + "," + getJobReq.address.state);


                        bedrooms = data.getIntExtra("Bedroom", 0);
                        bathrooms = data.getIntExtra("Bathroom", 0);

                        sqft = new Double(data.getFloatExtra("SQFT", 0f));

                        kitchen = data.getIntExtra("Kitchen", 0);
                        others = data.getIntExtra("Other", 0);

                        // Added by manjeet(Get data from Map Activity)

                        calculateTaskTimePrice();
                        getJobReq.bedrooms = bedrooms;
                        getJobReq.bathrooms = bathrooms;
                        getJobReq.kitchen = kitchen;
                        getJobReq.others = others;
                        getJobReq.sqft = sqft;

                       /* if(binding.tvAddress.getText().toString().length()>0){
                            Log.e(LOG_TAG,"IN Adress IF");
                            presentShowcaseViewTask(1000);
                        }
*/

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
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    providerId = bundle.getString("providerId");
                    providerName = bundle.getString("providerName");
                    providerImage = bundle.getString("providerImage");
                    binding.tvChooseProvider.setText(providerName);
                    jobType = "Service Provider";
                    selectProviderType(binding.tvChooseProvider, binding.tvMakeBidName, binding.tvFixedPrice, jobType);
                    Glide.with(this).load(providerImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                            .centerCrop().circleCrop()).into(binding.imgChooseProvider);
                }
            }
        } else if (requestCode == 104) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    fixedPrice = bundle.getString("fixedPrice");
                    jobType = "Fixed Price";
                    selectProviderType(binding.tvFixedPrice, binding.tvMakeBidName, binding.tvChooseProvider, jobType);
                }
            }
        }
    }


    private void dispatchTakePictureIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_TAKE_PHOTO);
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
            case PermissionUtitlity.CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                break;
            case PermissionUtitlity.LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("latitude", "30.7333");
                    mBundle.putString("longitude", " 76.7794");
                    startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
                }
                break;
        }
    }


    private void selectDate(boolean isTimeOpen) {

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
                        getJobReq.date = date;

                        if (isTimeOpen) {

                            selectTime();
                        }

                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);// M
        datePickerDialog.show();
    }


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
                        getJobReq.time = time;
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

        if (whenValue.equalsIgnoreCase("Today")) { //
            tpd.setMinTime(mHour, mMinute, mSecond);
        }

        //  tpd.setMaxTime(mHour, mMinute, mSecond);
        tpd.show(getFragmentManager(), "TimePickerDialog");
    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }

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


        return mDay + "-" + mMonth + 1 + "-" + mYear;

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

    private boolean validationSuccess(String field) {

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
            return false;
        }
        return Validation.isValidatedString(this, jobType, "Please choose provider");
    }


    private void getLastSavedObject(String json) {
        Log.e(LOG_TAG, "getLastSavedObject " + json);
        getJobReq = new Gson().fromJson(json, GetJobRes.class);
        getJobReq = mJob;
        howOften = getJobReq.howOften;
        date = getJobReq.date;
        time = getJobReq.time;
        when = getJobReq.when;
        spcNotesForProvider = getJobReq.specialNotesForProvider;
        // Address
        mGetAddress.clear();
        //mGetAddress.addAll(getJobReq.address);
        mGetAddress.add(getJobReq.address);
        // Room Detail
        sqft = getJobReq.sqft;
        cleaning = getJobReq.cleaningType;
        if (getJobReq.bedrooms != 0) {
            bedrooms = getJobReq.bedrooms;
        }
        if (getJobReq.kitchen != 0) {
            kitchen = getJobReq.kitchen;
        }
        if (getJobReq.bathrooms != 0) {
            bathrooms = getJobReq.bathrooms;
        }

        if (getJobReq.others != 0) {
            others = getJobReq.others;
        }

        // About provider
        estPrice = getJobReq.estPrice;
        estTime = String.valueOf(getJobReq.estTime);
        fixedPrice = getJobReq.price;

        providerId = getJobReq.providerId;
        jobType = getJobReq.jobType;
        if (!jobType.equals("Fixed Price")) {
            fixedPrice = "";
        }
        // Extras
        mExtraCleaning.clear();
        if (getJobReq.extraCleaning != null) {
            mExtraCleaning.addAll(getJobReq.extraCleaning);
        }
        // Images
        mImageList.clear();
        if (getJobReq.images != null) {
            mImageList.addAll(getJobReq.images);
            mImageAdapter.notifyDataSetChanged();
        }
        // calculate est time and price
        calculateExtraTimePrice();

        setLastSavedJob(getJobReq);

        if (getJobReq.cleaningType.equalsIgnoreCase("Window")) {
            setDataForWindowClean();
        }
        calculatePriceAccToCleaningType();
    }

    void setLastSavedJob(GetJobRes getJobReq) {
        //set cleaning Type
        if (getJobReq.cleaningType.equalsIgnoreCase("House")) {
            binding.tvHouseCleaning.performClick();
        } else if (getJobReq.cleaningType.equalsIgnoreCase("Carpet")) {
            binding.tvcarpetCleaning.performClick();
        } else if (getJobReq.cleaningType.equalsIgnoreCase("Window")) {
            binding.tvWindowCleaning.performClick();
        }

        //set address
        //  binding.tvAddress.setText(getJobReq.address.get(0).street); // Do uncomment this: Crash detected here (Crash Fixed)
        //binding.tvAddress.setText(getJobReq.address.get(0).street + "," + getJobReq.address.get(0).city + "," + getJobReq.address.get(0).state);
        //  binding.tvAddress.setText(getJobReq.address.get(0).street);
        binding.tvAddress.setText(getJobReq.address.street);

        //set task size
        binding.estPrice.setText("$ " + getJobReq.estPrice);
        //set date time
        binding.tvWhenDate.setText(getJobReq.date);
        binding.tvWhenTime.setText(getJobReq.time);
        //set when
        if (getJobReq.when.equalsIgnoreCase("Now")) {
            selectedText(binding.tvWhenNow, binding.tvWhenToday, binding.tvWhenFuture);
        } else if (getJobReq.when.equalsIgnoreCase("Today")) {
            isShowTime = false;
            selectedText(binding.tvWhenToday, binding.tvWhenNow, binding.tvWhenFuture);
        } else if (getJobReq.when.equalsIgnoreCase("Future")) {
            isShowDate = false;
            selectedText(binding.tvWhenFuture, binding.tvWhenToday, binding.tvWhenNow);
        }
        //set how often
        if (getJobReq.howOften.equalsIgnoreCase("Just Once")) {
            selectedText(binding.tvDaily, binding.tvWeekly, binding.tvByWeekly, binding.tvMonthly);
        } else if (getJobReq.howOften.equalsIgnoreCase("Weekly")) {
            selectedText(binding.tvWeekly, binding.tvDaily, binding.tvByWeekly, binding.tvMonthly);
        } else if (getJobReq.howOften.equalsIgnoreCase("Bi Weekly")) {
            selectedText(binding.tvByWeekly, binding.tvWeekly, binding.tvDaily, binding.tvMonthly);
        } else if (getJobReq.howOften.equalsIgnoreCase("Monthly")) {
            selectedText(binding.tvMonthly, binding.tvByWeekly, binding.tvWeekly, binding.tvDaily);
        }
        //estTime, estPrice, client price and choose provider
        binding.etSpcNotes.setText(getJobReq.specialNotesForProvider);
        binding.estTime.setText("" + getJobReq.estTime); // 11
        binding.yourPrice.setText("$" + getJobReq.price);
        if (getJobReq.jobType.equals("Fixed Price")) {
            selectProviderType(binding.tvFixedPrice, binding.tvMakeBidName, binding.tvChooseProvider, getJobReq.jobType);
            binding.yourPrice.setText("$" + getJobReq.price);
        } else if (getJobReq.jobType.equals("Bid")) {
            selectProviderType(binding.tvMakeBidName, binding.tvChooseProvider, binding.tvFixedPrice, getJobReq.jobType);
        } else {
            selectProviderType(binding.tvChooseProvider, binding.tvMakeBidName, binding.tvFixedPrice, getJobReq.jobType);
        }


        try {
            //set extra cleaning
            if (getJobReq.extraCleaning != null) {
                String extraCleaning = "";
                for (int i = 0; i < getJobReq.extraCleaning.size(); i++) {
                    ExtraCleaningRes res = getJobReq.extraCleaning.get(i);
                    if (res.isSelected) {
                        if (i == 0) {
                            extraCleaning = res.name;
                        } else
                            extraCleaning = extraCleaning + ", " + res.name;
                    }
                    extraTimeDbl = extraTimeDbl + res.extraTime;
                    binding.tvExtraItems.setText(extraCleaning);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set Images Adapter
        mImageAdapter.notifyDataSetChanged();
    }


    private void calculatePriceAccToCleaningType() {
        if (getJobReq.cleaningType.equalsIgnoreCase("House")) {
            if (sqft != null) {
                calculateTaskTimePrice();
            }
        } else if (getJobReq.cleaningType.equalsIgnoreCase("Carpet")) {
            getJobReq.rooms.clean = String.valueOf(mCleanRoom);
            getJobReq.rooms.protect = String.valueOf(mProtectRoom);
            getJobReq.rooms.deodrize = String.valueOf(mDeoRoom);

            getJobReq.bath.clean = String.valueOf(mCleanBath);
            getJobReq.bath.protect = String.valueOf(mProtectBath);
            getJobReq.bath.deodrize = String.valueOf(mDeoBath);

            getJobReq.entry.clean = String.valueOf(mCleanEntryHall);
            getJobReq.entry.protect = String.valueOf(mProtectEntryHall);
            getJobReq.entry.deodrize = String.valueOf(mDeoEntryHall);

            getJobReq.stairCase.clean = String.valueOf(mCleanStaircase);
            getJobReq.stairCase.protect = String.valueOf(mProtectStaircase);
            getJobReq.stairCase.deodrize = String.valueOf(mDeoStaircase);
            //
            String carpet = "";
            if (getJobReq.rooms != null && getJobReq.bath != null && getJobReq.entry != null && getJobReq.stairCase != null) {

                String rooms = getResources().getString(R.string.rooms) + " : " + getJobReq.rooms.clean + " " + getResources().getString(R.string.clean) + getResources().getString(R.string.comma) + " " + getJobReq.rooms.protect + " " +
                        getResources().getString(R.string.protect) + getResources().getString(R.string.comma) + " " + getJobReq.rooms.deodrize + " " + "Deodorize" + " " + "\n";
                String bathLaundry = "Bath/Laundry" + " : " + getJobReq.bath.clean +" "+ getResources().getString(R.string.clean) + ", " + getJobReq.bath.protect + " " + getResources().getString(R.string.protect) + ", " +
                        getJobReq.bath.deodrize + " Deodorize" + " " + "\n";
                String entryHall = "Entry/Hall" + " : " + getJobReq.entry.clean +" "+ getResources().getString(R.string.clean) + ", " + getJobReq.entry.protect +" "+ getResources().getString(R.string.protect) + ", " +
                        getJobReq.entry.deodrize +" "+ getResources().getString(R.string.deodorize) + " " + "\n";
                String stairCase =
                       getResources().getString(R.string.staircase) + " : " + getJobReq.stairCase.clean +" "+ getResources().getString(R.string.clean) + ", " +
                                getJobReq.stairCase.protect + " "+getResources().getString(R.string.protect) + ", " + getJobReq.stairCase.deodrize +" "+ getResources().getString(R.string.deodorize) + " ";

                carpet = rooms + bathLaundry + entryHall + stairCase;
            }
            binding.tvBedrooms.setText(carpet);
            calculateCarpetTaskTimePrice();
        }
        /*Cleaning type is window*/
        else {
            getJobReq.firstFloorWindows = mFirstFloor;
            getJobReq.secondFloorWindows = mSecondFloor;
            getJobReq.frenchWindows = mFrenchWindows;
            getJobReq.slidingWindows = mPatioDoor;
            getJobReq.gardenWindows = mGardenWindows;
            getJobReq.wardrobeMirrors = mWardrobeMirror;
            getJobReq.screens = mScreens;
            getJobReq.skylights = mSkylight;
            getJobReq.stories = mStories;

            //setDataForWindowClean();
            calculateWindowsTaskTimePrice();

        }

    }

    private void setDataForWindowClean() {

        Log.e(LOG_TAG, "All Data=" + getJobReq.firstFloorWindows + " second:" + getJobReq.secondFloorWindows + getJobReq.frenchWindows);
        String windows = "";
        if (getJobReq.firstFloorWindows > 0) {
            if (getJobReq.firstFloorWindows > 1) {
                windows += getJobReq.firstFloorWindows + " First Floor Windows";
            } else {
                Log.e(LOG_TAG, "DATA=" + getJobReq.firstFloorWindows);
                windows += getJobReq.firstFloorWindows + " First Floor Window";
            }
        }
        if (getJobReq.secondFloorWindows > 0) {
            if (getJobReq.secondFloorWindows > 1) {
                windows += ", " + getJobReq.secondFloorWindows + " Second Floor Windows";
            } else {
                windows += ", " + getJobReq.secondFloorWindows + " Second Floor Window";
            }
        }
        if (getJobReq.frenchWindows > 0) {
            if (getJobReq.frenchWindows > 1) {
                windows += ", " + getJobReq.frenchWindows + " frenchWindows";
            } else {
                windows += ", " + getJobReq.frenchWindows + " frenchWindow";
            }
        }
        if (getJobReq.slidingWindows > 0) {
            if (getJobReq.slidingWindows > 1) {
                windows += ", " + getJobReq.slidingWindows + " sliding Window";
            } else {
                windows += ", " + getJobReq.slidingWindows + " sliding Windows";
            }
        }
        if (getJobReq.gardenWindows > 0) {
            if (getJobReq.gardenWindows > 1) {
                windows += ", " + getJobReq.gardenWindows + " garden Windows";
            } else {
                windows += ", " + getJobReq.gardenWindows + " garden Window";
            }
        }
        if (getJobReq.wardrobeMirrors > 0) {
            windows += ", " + getJobReq.wardrobeMirrors + " wardrobe Mirrors";
        }
        if (getJobReq.screens > 0) {
            windows += ", " + getJobReq.screens + " screens";
        }
        if (getJobReq.skylights > 0) {
            windows += ", " + getJobReq.skylights + " skylights";
        }
        if (getJobReq.stories > 0) {
            windows += ", " + getJobReq.stories + " stories";
        }

        Log.d(LOG_TAG, "result---" + windows);
        binding.tvBedrooms.setText(windows);
    }

    private void calculateCarpetTaskTimePrice() {
        // ROOMS
        int mRoomsPrice = 0;
        for (int i = 0; i < mCleanRoom; i++) {
            if (i == 0) {
                mRoomsPrice += 55;
            } else {
                mRoomsPrice += 35;
            }
        }
        int roomProtect = mCleanRoom * 15;
        int roomDeodorize = mCleanRoom * 15;
        mRoomsPrice = mRoomsPrice + roomProtect + roomDeodorize;

        //BATH
        int mBathPrice = 0;
        int bathClean = mCleanBath * 15;
        int bathProtect = mProtectBath * 15;
        int bathDeo = mDeoBath * 15;
        mBathPrice = bathClean + bathProtect + bathDeo;

        //HALLWAY
        int mHallwayPrice = 0;
        int hallwayClean = mCleanEntryHall * 15;
        int hallwayProtect = mProtectEntryHall * 15;
        int hallwayDeo = mDeoEntryHall * 15;
        mHallwayPrice = hallwayClean + hallwayProtect + hallwayDeo;


        // STAIRS
        int mStairsPrice = 0;
        int stairClean = mCleanStaircase * 35;
        int stairProtect = mProtectStaircase * 35;
        int stairDeodorize = mCleanStaircase * 15;
        mStairsPrice = stairClean + stairProtect + stairDeodorize;

        int taskPrice = mRoomsPrice + mBathPrice + mHallwayPrice + mStairsPrice;
        if (taskPrice < 151) {
            taskPrice = 150;
        }
        binding.estPrice.setText(taskPrice + "");

    }

    private void calculateWindowsTaskTimePrice() {
        // WINDOW FIRST FLOOR
        float mWindowFFPrice = 0.0f;
        for (int i = 0; i < mFirstFloor; i++) {
            mWindowFFPrice += 3.50;
        }

        // WINDOW SECOND FLOOR
        float mWindoSFPrice = 0.0f;
        for (int i = 0; i < mSecondFloor; i++) {
            mWindoSFPrice += 5.00;
        }

        // SCREENS
        float mScreensPrice = 0.0f;
        mScreensPrice = mSecondFloor * 1.0f;

        // FRENCH DOOR
        float mFrenchPrice = 0.0f;
        mFrenchPrice = mFrenchWindows * 5 * 1.0f;

        // SLIDING DOOR
        float mSlidingPrice = 0.0f;
        mSlidingPrice = mPatioDoor * 10 * 1.0f;

        // MIRRORS
        float mMirrorsPrice = 0.0f;
        mMirrorsPrice = mWardrobeMirror * 4 * 1.0f;

        float taskPrice = mWindowFFPrice + mWindoSFPrice + mScreensPrice + mFrenchPrice + mSlidingPrice + mMirrorsPrice;
        if (taskPrice < 126) {
            taskPrice = 125;
        }
        estPrice = String.valueOf(taskPrice);
        binding.estPrice.setText(taskPrice + "");
        binding.tvBedrooms.setTextColor(getResources().getColor(R.color.colorWhite));

    }

    private void calculateTaskTimePrice() {
        int roomTime = bedrooms * 15;
        int bathroomTime = bathrooms * 30;
        int kitchenTime = kitchen * 45;
        double sqftTime = sqft / 10;
        taskTimeDbl = (roomTime + kitchenTime + sqftTime + bathroomTime) / 60;

        Double totalTime = taskTimeDbl + extraTimeDbl;
        DecimalFormat df = new DecimalFormat("#.##");
        totalTime = Double.valueOf(df.format(totalTime));
        estTime = totalTime.toString();
        binding.estTime.setText(estTime);
        /* add extra services price and task price and set on textview*/
        taskPriceDbl = taskTimeDbl * 50;
        Double finalPrice = taskPriceDbl + extraPriceDbl;
        finalPrice = Double.valueOf(df.format(finalPrice));
        estPrice = finalPrice.toString();
        binding.estPrice.setText("$ " + estPrice);
        //
        getJobReq.estPrice = estPrice;
        //
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
        tasks += ", " + sqft + " SQFT ";
        binding.tvBedrooms.setText(tasks);

    }

    private void calculateExtraTimePrice() {
        /*Convert the minutes in Hour and then add task time and set on textview*/
        extraTimeDbl = extraTimeDbl / 60;
        Double extraTime = extraTimeDbl + taskTimeDbl;
        DecimalFormat df = new DecimalFormat("#.##");
        extraTime = Double.valueOf(df.format(extraTime));
        estTime = extraTime.toString();
        binding.estTime.setText(estTime);
        /* add extra services price and task price and set on textview*/
        extraPriceDbl = extraTimeDbl * 50;
        Double finalPrice = extraPriceDbl + taskPriceDbl;
        finalPrice = Double.valueOf(df.format(finalPrice));
        estPrice = finalPrice.toString();
        binding.estPrice.setText("$ " + estPrice);
    }


    @Override
    public void onListCancelWithPositionClick(Object object, int position) {
        ImageUrlReq cancelledImage = (ImageUrlReq) object;
        getJobReq.images.remove(cancelledImage);
        //
        mImageList.remove(cancelledImage);
        mImageAdapter.notifyDataSetChanged();
    }


}
