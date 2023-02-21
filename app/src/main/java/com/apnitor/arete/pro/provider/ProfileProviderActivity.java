/*
package com.apnitor.arete.pro.provider;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.RatingReviewReq;
import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.api.response.GetMyRatingRes;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.createjob.MapsActivity;
import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.profile.RatingReview;
import com.apnitor.arete.pro.util.PermissionUtitlity;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.util.UploadImage;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import com.apnitor.arete.pro.viewmodel.ProfileViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class ProfileProviderActivity extends BaseActivity {
    public String mTitle;
  
    String LOG_TAG = "ProfileActivity";
    ImageView mIvProfilePic;
    TextView mTvPrice;
    String latitude, longitude;
    EditText mEtFirstName, mEtLastName, mEtBirthday, mEtGender, mEtLocation, mEtAboutMe, mEtPrice, mEtEmail, mEtMobile, mEtZipCode, mEtDistance;
    Button mBtnSave;
    ProgressBar mProgressBar;
    ArrayList<GetAddress> mGetAddress;
    TextInputLayout mTiLPriceParent;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private GetAddress getAddress;
    private String email = "", phone = "";
    private UpdateProfileReq mProfileReq;
    private ProfileViewModel viewModel;
    private int GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    private UploadImage mUploadImage;
    private Uri mUri;

    private String filePath = null;
    private Uri fileUri; // file url to store image/video
    private String imgPath = null;
    private File destination = null;
    private NestedScrollView nestedScrollView;
    private float hourlyRate;
    private String userType;
    private TextView reviews;
    private JobSpecViewModel jobSpecViewModel;
    GetMyRatingRes ratingResponse;
    String myLatitude = "", myLongitude = "";
    String mComingFrom = "";
    CheckBox mCbHouseCleaning, mCbCarpetCleaning, mCbWindowCleaning;
    TextView mTvHouseCleaning, mTvCarpetCleaning, mTvWindowCleaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_provider);
        setToolBar("Profile Page");

        if (getIntent().hasExtra("ComingFrom")) {
            mComingFrom = getIntent().getStringExtra("ComingFrom");
        }

        viewModel = getViewModel(ProfileViewModel.class);
        LogInRes logInRes = getPrefHelper().getLogInRes();
        mTitle = logInRes.firstName + " " + logInRes.lastName;

        setUpLayout();

        setViewsEditableStatus(true);// Add
        setDataInViewObjects(logInRes);

        */
/**
         * TODO
         *
         * Remove Search jobs withing for client
         *//*


        viewModel.getUpdateProfileResLiveData().observe(this, res -> {
            //  showSnackBar(R.id.my_nav_host_fragment,"You are successfully logged in.");
            getPrefHelper().profileUpdated(mProfileReq);
            */
/**
             * TODO
             *
             * Don't Start Activity again.
             *
             * just finish this activity
             *
             *//*

            if (getPrefHelper().getUserType().equals("serviceProvider")) {
                // CAME FROM HOME ACTIVITY
                if (mComingFrom.equalsIgnoreCase("Home")) {
                    finish();
                    overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
                    return;
                }
                // CAME FROM LOGIN ACTIVITY
                Intent intent = new Intent(this, HomeProviderActivity.class);
                startActivity(intent);
                startWithAnim();
                finish();
            } else {
                startActivity(HomeActivity.class);
            }

            showToast("Profile updated.");
            finish();
        });

        setTextWatcher();

        */
/**
         * TODO
         *
         * Put serviceProvider in constants
         *//*

        userType = logInRes.getUserTypes().get(0);
        if (userType.equalsIgnoreCase("serviceProvider")) {
            if (logInRes.hourlyRate == 0) {
                UIUtility.showDialogWithSingleButton(ProfileProviderActivity.this, getString(R.string.alert_provider_profile), "Ok");
            } else {
                enableSaveButton();
                */
/**
                 * Provider may have Reviews here so make a call here
                 *//*

                jobSpecViewModel = getViewModel(JobSpecViewModel.class);
                jobSpecViewModel.getMyRatingReview(new RatingReviewReq(getPrefHelper().getAuthToken(), getPrefHelper().getUserId()));
                jobSpecViewModel.getReviewResLiveData()
                        .observe(this, res -> {
                            ratingResponse = res;
                            if (ratingResponse != null && ratingResponse.providerReviews != null) {
                                final int numberOfReviews = ratingResponse.providerReviews.size();
                                if (numberOfReviews == 1) {
                                    reviews.setText("1 Review");
                                } else if (numberOfReviews > 1) {
                                    reviews.setText(numberOfReviews + " Reviews");
                                } else {
                                    reviews.setText("No Reviews");
                                }
                            }
                        });
            }
        } else if (logInRes.gender.isEmpty()) {
            UIUtility.showDialogWithSingleButton(ProfileProviderActivity.this, getString(R.string.alert_client_profile), "Ok");
        } else {
            enableSaveButton();
        }
    }

    private void setTextWatcher() {
        mEtGender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateGender();
                MandatoryFieldsValidations();
            }
        });


        mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEtPrice.getText().toString().isEmpty()) {
                    showErrorOnEditText(mEtPrice, "Please enter your hourly price.");
                } else {
                    mEtPrice.setError(null);
                }
                //MandatoryFieldsValidations();
            }
        });

        mEtZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mEtZipCode.getText().toString().isEmpty()) {
                    showErrorOnEditText(mEtZipCode, "Please add Address");
                    */
/**
                     * TODO
                     * Disable Save Button
                     *//*

                } else {
                    mEtZipCode.setError(null);
                    */
/**
                     * If user is Client then enable button
                     *//*

                    if (!userType.equalsIgnoreCase("serviceProvider")) {
                        enableSaveButton();
                    }
                }
                //MandatoryFieldsValidations();
            }
        });

        mEtDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEtDistance.setError(null);
                enableSaveButton();

                    */
/*if (!mEtGender.getText().toString().isEmpty() && !mEtZipCode.getText().toString().isEmpty() && !mEtDistance.getText().toString().isEmpty()) {
                        Log.d("Is afterTextChanged", "===");
                        if (getPrefHelper().getUserType().equals("serviceProvider")) {
                            if (mEtPrice.getText().toString().isEmpty()) {
                                showErrorOnEditText(mEtPrice, "Please enter your hourly price.");
                            } else {
                                enableSaveButton();
                            }
                        }

                    }*//*

            }
        });
    }


    private void validateGender() {
        mEtGender.setError(null);
    }

    private void validateHourlyPrice() {
        mEtPrice.setError(null);
    }

    public boolean MandatoryFieldsValidations() {

        boolean validated = true;

        if (mEtGender.getText().toString().isEmpty()) {
            showErrorOnEditText(mEtGender, "Please select gender.");
            validated = false;
            openGenderDialog();
            validated = false;
            return validated;

        } else {
            mEtGender.setError(null);
        }

        Log.d("MendatoryFieldsVali", "ELSE Vali1");
        // mBtnSave.setBackgroundColor(getResources().getColor(R.color.secondary_text));


        if (mEtZipCode.getText().toString().isEmpty()) {
            showErrorOnEditText(mEtZipCode, "Please add Address");
            validated = false;
        }Profi else {
            mEtZipCode.setError(null);
//            getLatLongFromZipCode();
        }
//
//        if (myLatitude.equalsIgnoreCase("0.0") || myLongitude.equalsIgnoreCase("0.0")) {
//            openZipcodeDialog();
//            validated = false;
//        }

        if (getPrefHelper().getUserType().equals("serviceProvider")) {

            if (mEtDistance.getText().toString().isEmpty()) {
                showErrorOnEditText(mEtDistance, "Please choose the Radius.");
                validated = false;
            } else {
                mEtDistance.setError(null);
            }

            if (mEtPrice.getText().toString().equals("0")) {
                showErrorOnEditText(mEtPrice, "Please enter your hourly price.");
                validated = false;
            } else {
                mEtPrice.setError(null);
            }
            if (mEtPrice.getText().toString().isEmpty()) {
                showErrorOnEditText(mEtPrice, "Please enter your hourly price.");
                validated = false;
            } else {
                mEtPrice.setError(null);
            }

        }


        if (validated) { // Already filled up
            enableSaveButton();
        }

        return validated;
    }

    private void openZipcodeDialog() {

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title("Update Zipcode")
                .theme(Theme.LIGHT)
                .content("Please update correct Zipcode in your Profile to filter jobs by distance.")
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .positiveText(R.string.mdtp_ok)
                .negativeText("Cancel");
        materialDialog.show();
    }


    private void setUpLayout() {
        try {
            mIvProfilePic = findViewById(R.id.iv_profile);
            mTvPrice = findViewById(R.id.tv_price);
            reviews = findViewById(R.id.txtReviews);
            mEtFirstName = findViewById(R.id.et_firstname);
            mEtLastName = findViewById(R.id.et_lastname);
            mEtBirthday = findViewById(R.id.et_birthday);
            mEtGender = findViewById(R.id.et_gender);
            mEtDistance = findViewById(R.id.et_miles);// Distance
            mBtnSave = findViewById(R.id.btn_save);
            mEtLocation = findViewById(R.id.et_location);
            mEtAboutMe = findViewById(R.id.et_about);
            mEtZipCode = findViewById(R.id.et_zipcode);// Zip Code
            mEtPrice = findViewById(R.id.et_price);
            mEtEmail = findViewById(R.id.et_email);
            mEtMobile = findViewById(R.id.et_contact);
            mProgressBar = findViewById(R.id.profileProgress);
            mTiLPriceParent = findViewById(R.id.til_price);
            nestedScrollView = findViewById(R.id.nested_scroll_view);
            //
            mCbHouseCleaning = findViewById(R.id.cbHouseCleaning);
            mCbHouseCleaning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
            mCbCarpetCleaning = findViewById(R.id.cbCarpetCleaning);
            mCbCarpetCleaning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
            mCbWindowCleaning = findViewById(R.id.cbWindowCleaning);
            mCbWindowCleaning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
            //
            mTvHouseCleaning = findViewById(R.id.tvHouseCleaning);
            mTvHouseCleaning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCbHouseCleaning.performClick();
                }
            });
            mTvCarpetCleaning = findViewById(R.id.tvCarpetCleaning);
            mTvCarpetCleaning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCbCarpetCleaning.performClick();
                }
            });
            mTvWindowCleaning = findViewById(R.id.tvWindowCleaning);
            mTvWindowCleaning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCbWindowCleaning.performClick();
                }
            });


            Log.e("Type==", getPrefHelper().getUserType());
            if (getPrefHelper().getUserType().equalsIgnoreCase("serviceProvider")) {
                reviews.setVisibility(View.VISIBLE);
            }

            reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   */
/* Intent in = new Intent(ProfileActivity.this, RatingReview.class);
                    startActivity(in);*//*


                    if (ratingResponse != null && ratingResponse.providerReviews != null && ratingResponse.providerReviews.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("Reviews", ratingResponse.providerReviews);
                        startActivity(RatingReview.class, bundle);
                    }

                }
            });

            //
            if (!mEtPrice.getText().toString().isEmpty()) {
                if (phone.isEmpty()) {
                    mEtMobile.setEnabled(true);
                    mEtMobile.setFocusable(true);
                } else {
                    mEtEmail.setEnabled(true);
                    mEtEmail.setFocusable(true);
                }
            }
            if (getPrefHelper().getUserType().equals("serviceProvider")) {
                mEtPrice.setVisibility(View.VISIBLE);
                mTvPrice.setVisibility(View.VISIBLE);

                mEtDistance.setVisibility(View.VISIBLE);


            } else {
                mEtPrice.setVisibility(View.GONE);
                mTvPrice.setVisibility(View.GONE);

                mEtDistance.setVisibility(View.GONE);

            }

            ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "Extra Image");
            collapsingToolbarLayout = (CollapsingToolbarLayout)
                    findViewById(R.id.collapsingToolBar_hotel_details);
            collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

            if (Character.isUpperCase(mTitle.charAt(0))) {
                collapsingToolbarLayout.setTitle(mTitle);
            } else {
                String name = Character.toUpperCase(mTitle.charAt(0)) + mTitle.substring(1);
                collapsingToolbarLayout.setTitle(name);
            }

            findViewById(R.id.iv_editprofile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.iv_editprofile).setVisibility(View.INVISIBLE);
                    setViewsEditableStatus(true);
                }
            });
            //
            findViewById(R.id.iv_profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPictureDialog();
                }
            });
            //
            mEtBirthday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDatePicker();
                }
            });
            mEtGender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openGenderDialog();
                }
            });

            mEtDistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDistanceDialog();
                }
            });


            mBtnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  setViewsEditableStatus(false);
                    //setViewsEditableStatus(true);//Added

                    if (MandatoryFieldsValidations()) {
                        String birth = UIUtility.getEditTextValue(mEtBirthday);
                        String gender = UIUtility.getEditTextValue(mEtGender);
                        if (userType.equalsIgnoreCase("serviceProvider")) {
                            hourlyRate = Float.parseFloat(UIUtility.getEditTextValue(mEtPrice));
                        } else {
                            hourlyRate = 0;
                        }

                        String address = UIUtility.getEditTextValue(mEtLocation);
                        String aboutme = UIUtility.getEditTextValue(mEtAboutMe);
                        phone = UIUtility.getEditTextValue(mEtMobile);
                        email = UIUtility.getEditTextValue(mEtEmail);
                        String imgUrl = getPrefHelper().getProfilePhoto();

                        String zipcode = UIUtility.getEditTextValue(mEtZipCode);
                        String editTextValue = UIUtility.getEditTextValue(mEtDistance);

                        if (!editTextValue.equals("")) {
                            editTextValue = editTextValue.substring(0, 2);
                        } else {
                            */
/**
                             * This is dummy value in case of Client
                             *//*


                            editTextValue = "0";
                        }

                        int radius = Integer.parseInt(editTextValue);


                        Log.d("Vali Success==", "ELSE*********");
                      //  mProfileReq = new UpdateProfileReq(getPrefHelper().getAuthToken(), imgUrl, phone, email, aboutme, hourlyRate, gender, birth, zipcode, radius);
                       // viewModel.updateProfile(mProfileReq);

                    } else {
                        return;
                    }


                }
            });
            mEtLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle mBundle = new Bundle();
                    mBundle.putString("prevActivity", "Profile");
                    mBundle.putString("latitude", latitude);
                    mBundle.putString("longitude", longitude);
                    startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
                }
            });

            mIvProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPictureDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


       */
/* mEtGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mEtPrice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        mEtEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        mEtMobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mEtZipCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        mEtDistance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*//*



        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
               */
/* if (view.hasFocus() && view instanceof EditText)  {
                    view.clearFocus();
                }*//*

                view.requestFocusFromTouch();
                hideKeyboard(ProfileProviderActivity.this);
                return false;
            }
        });

    }

    private void enableSaveButton() {
        mBtnSave.setEnabled(true);
        mBtnSave.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }


    void setViewsEditableStatus(boolean isEditable) {

        mIvProfilePic.setEnabled(isEditable);
        mProgressBar.setVisibility(View.GONE);
        mTvPrice.setEnabled(isEditable);
        mEtFirstName.setEnabled(false);
        mEtLastName.setEnabled(false);

        if (mEtLastName.getText().toString().length() == 0) {
            mEtLastName.setEnabled(true);
        }
        if (mEtFirstName.getText().toString().length() == 0) {
            mEtLastName.setEnabled(true);
        }

        if (phone.isEmpty()) {
            mEtMobile.setEnabled(isEditable);
        } else {
            mEtEmail.setEnabled(isEditable);
        }
        mEtBirthday.setEnabled(isEditable);
        mEtGender.setEnabled(isEditable);
        mEtLocation.setEnabled(isEditable);

        mEtAboutMe.setEnabled(true);
        mEtPrice.setEnabled(isEditable);

        //
       */
/* if (isEditable) {
            mEtPrice.setVisibility(View.VISIBLE);
            mBtnSave.setVisibility(View.VISIBLE);
        } else {
            Log.e("Is Editable==","=ELSE="+isEditable);
            mEtPrice.setVisibility(View.GONE);
            mBtnSave.setVisibility(View.GONE);
        }*//*


        mEtPrice.setVisibility(View.VISIBLE);
        mBtnSave.setVisibility(View.VISIBLE);

        if (getPrefHelper().getUserType().equals("serviceProvider")) {
            mEtPrice.setVisibility(View.VISIBLE);
            mTvPrice.setVisibility(View.VISIBLE);
        } else {
            mEtPrice.setVisibility(View.GONE);
            mTvPrice.setVisibility(View.GONE);
        }
    }


    private void setDataInViewObjects(LogInRes logInRes) {
        String imageUrl = logInRes.profilePhotoUrl;
        String firstName = logInRes.firstName;
        String lastName = logInRes.lastName;
        float price = logInRes.hourlyRate;
        String birthday = logInRes.birthdate;
        String gender = logInRes.gender;
        email = logInRes.email;
        phone = logInRes.phone;
        latitude = "";
        longitude = "";

        int radius = logInRes.radius;
        String zipcode = logInRes.zipcode;

        if (radius != 0) {
            mEtDistance.setText("" + radius + " Miles");
        }

        mEtZipCode.setText(zipcode);


        //set saved lat long
        if (logInRes.address != null) {
            latitude = String.valueOf(logInRes.address.get(0).latitude);
            longitude = String.valueOf(logInRes.address.get(0).longitude);
        }


        Glide.with(this).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                .centerCrop()).into(mIvProfilePic);

       */
/* if (price == 0) {
            mTvPrice.setText("$" + price + " Per hour");
        }*//*


        mEtFirstName.setText(firstName);
        mEtLastName.setText(lastName);

        if (mEtFirstName.getText().toString().length() == 0) {
            mEtFirstName.setEnabled(true);
        }
        if (mEtLastName.getText().toString().length() == 0) {
            mEtLastName.setEnabled(true);
        }


        mEtGender.setText(gender);

        mEtBirthday.setText(birthday);

        if (price == 0) {
            mTvPrice.setText("$" + price + " Per hour");
        } else {
            mEtPrice.setText("" + price);
        }


        mEtAboutMe.setText(logInRes.aboutMe);
        if (null != logInRes.address) {
            mEtLocation.setText(logInRes.address.get(0).street);
        }
        mEtEmail.setText(email);
        mEtMobile.setText(phone);
        //setViewsEditableStatus(false);
        setViewsEditableStatus(true);
    }

    public void openDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        //c.add(Calendar.DATE,1);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.Dialog_Theme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
                            SimpleDateFormat sdfOutput = new SimpleDateFormat("dd MMM yyyy");
                            Date date = null;
                            try {
                                date = sdf.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                String dateString = sdfOutput.format(date);
                                mEtBirthday.setText(dateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);

//        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        //13 years
        final Calendar cal = Calendar.getInstance();
        int years = cal.get(Calendar.YEAR) - 13;
        cal.set(Calendar.YEAR, years);

        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        datePickerDialog.show();
    }

    private void openGenderDialog() {

        String[] langArray = {"Male", "Female"};

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title("Select Gender")
                .theme(Theme.LIGHT)
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .items(langArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        mEtGender.setText(text.toString());
                        dialog.dismiss();
                    }
                })
                .negativeText("Cancel");

        materialDialog.show();
    }

    private void openDistanceDialog() {

        String[] langArray = {"15 Miles", "25 Miles", "50 Miles"};

        MaterialDialog.Builder materialDialog = new MaterialDialog.Builder(this)
                .title("Select Distance")
                .theme(Theme.LIGHT)
                .negativeColor(getResources().getColor(R.color.colorPrimary))
                .items(langArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        mEtDistance.setText(text.toString());
                        dialog.dismiss();
                    }
                })
                .negativeText("Cancel");

        materialDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == 101) {
            mGetAddress = new ArrayList<>();
            if (data != null) {
                try {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        getAddress = (GetAddress) bundle.getParcelable("Address");
                        mGetAddress.add(getAddress);
                        mEtLocation.setText(getAddress.street);// Working

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == GALLERY) {
            try {
                Uri selectedImage = data.getData();
                mIvProfilePic.setImageURI(selectedImage);//M1
                openCropActivity(selectedImage);
                //   mIvProfilePic.setImageURI(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                Uri selectedImage = data.getData();
                mIvProfilePic.setImageURI(selectedImage); //M1
                openCropActivity(selectedImage);
                // mIvProfilePic.setImageURI(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    mUploadImage = new UploadImage(ProfileProviderActivity.this);
                    mUri = result.getUri();
                    mIvProfilePic.setImageURI(mUri);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mUploadImage.imageActivityResult(mUri.toString(), mProgressBar);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileProviderActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
                    mBundle.putString("latitude", latitude);
                    mBundle.putString("longitude", longitude);
                    startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
                }
                break;
        }
    }

    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ProfileProviderActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (PermissionUtitlity.checkPermission(ProfileProviderActivity.this)) {
                                    choosePhotoFromGallary();
                                }
                                break;
                            case 1:
                                if (PermissionUtitlity.checkPermissionCamera(ProfileProviderActivity.this)) {
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
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void openCropActivity(Uri image_uri) {
        CropImage.activity(image_uri)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(ProfileProviderActivity.this);
    }

    private void getLatLongFromZipCode() {
        final Geocoder geocoder = new Geocoder(this);
        final String zipCode = mEtZipCode.getText().toString();
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipCode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                Log.d(LOG_TAG, " Latitude " + address.getLatitude() + " Longitude " + address.getLongitude());
                myLatitude = String.valueOf(address.getLatitude());
                myLongitude = String.valueOf(address.getLongitude());
                PreferenceHandler.writeBoolean(this, PreferenceHandler.PREF_IS_VALID_ZIPCODE, true);
            } else {
                myLatitude = "0.0";
                myLongitude = "0.0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
