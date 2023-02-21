package com.apnitor.arete.pro.profile;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.AvailabilityReq;
import com.apnitor.arete.pro.api.request.RatingReviewReq;
import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.api.response.GetMyRatingRes;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.createjob.MapsActivity;
import com.apnitor.arete.pro.dashboard.HomeActivity;
import com.apnitor.arete.pro.provider.HomeProviderActivity;
import com.apnitor.arete.pro.util.ConnectivityHelper;
import com.apnitor.arete.pro.util.PermissionUtitlity;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.util.UploadImage;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import com.apnitor.arete.pro.viewmodel.ProfileViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class ProfileActivity extends BaseActivity {
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
    GetMyRatingRes ratingResponse;
    String myLatitude = "", myLongitude = "";
    String mComingFrom = "";
    LogInRes logInRes;
    private GetAddress getProviderAddress;
    private String email = "", phone = "";
    private UpdateProfileReq mProfileReq;
    private ProfileViewModel viewModel;
    private int GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    private UploadImage mUploadImage;
    private Uri mUri;
    private NestedScrollView nestedScrollView;
    private float hourlyRate;
    private String userType, mSpeciality;
    private TextView reviews;
    private JobSpecViewModel jobSpecViewModel;
    private CheckBox cleaningHouse, cleaningCarpet, cleaningWindow;
    private CardView cardLocation, cardSpeciality;
    private AvailabilityReq availabilityReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        setToolBar(getResources().getString(R.string.profile));

        if (getIntent().hasExtra("ComingFrom")) {
            mComingFrom = getIntent().getStringExtra("ComingFrom");
        }

        viewModel = getViewModel(ProfileViewModel.class);
        logInRes = getPrefHelper().getLogInRes();
        mTitle = logInRes.firstName + " " + logInRes.lastName;


        saveAvailability();

        setUpLayout();
        setViewsEditableStatus(true);
        setDataInViewObjects(logInRes);


        viewModel.getUpdateProfileResLiveData().observe(this, res -> {
            //  showSnackBar(R.id.my_nav_host_fragment,"You are successfully logged in.");
            mProfileReq.availability = availabilityReq;
            getPrefHelper().profileUpdated(mProfileReq);

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

        /**
         * TODO
         *
         * Put serviceProvider in constants
         */
        userType = logInRes.getUserTypes().get(0);
        if (userType.equalsIgnoreCase("serviceProvider")) {
            /**
             * TODO
             *
             * Show speciality and send it to the API only if type is provider
             */
            //layoutSpeciality.setVisibility(View.VISIBLE);
            cardLocation.setVisibility(View.VISIBLE);
            cardSpeciality.setVisibility(View.VISIBLE);
            if (logInRes.hourlyRate == 0) {
                UIUtility.showDialogWithSingleButton(ProfileActivity.this, getString(R.string.alert_provider_profile), "Ok");
            } else {
                enableSaveButton();
                /**
                 * Provider may have Reviews here so make a call here
                 */
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
                                    reviews.setText(getResources().getString(R.string.no_review));
                                }
                            }
                        });
            }
        } else if (logInRes.gender.isEmpty()) {
            UIUtility.showDialogWithSingleButton(ProfileActivity.this, getString(R.string.alert_client_profile), "Ok");
        } else {
            enableSaveButton();
        }
    }


    private void saveAvailability() {
        availabilityReq = new AvailabilityReq();
        if (logInRes.availability != null) {
            if (logInRes.availability.availabilityDays != null && logInRes.availability.availabilityDays.size() > 0) {
                availabilityReq.setAvailabilityDays(logInRes.availability.availabilityDays);
            }
            if (logInRes.availability.nonavailabilityDays != null && logInRes.availability.nonavailabilityDays.size() > 0) {
                availabilityReq.setNonavailabilityDays(logInRes.availability.nonavailabilityDays);
            }
            if (logInRes.availability.type != null && !logInRes.availability.type.isEmpty()) {
                availabilityReq.setType(logInRes.availability.type);
            }
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
                    showErrorOnEditText(mEtZipCode, "Please select Zip Code");
                    /**
                     * TODO
                     * Disable Save Button
                     */
                } else {
                    mEtZipCode.setError(null);
                    /**
                     * If user is Client then enable button
                     */
                    if (!userType.equalsIgnoreCase("serviceProvider")) {
                        enableSaveButton();
                    }
                }
                //MandatoryFieldsValidations();
            }
        });
        mEtLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mEtLocation.getText().toString().isEmpty()) {
                    showErrorOnEditText(mEtLocation, "Please select Zip Code");
                }
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

        if (userType.equalsIgnoreCase("serviceProvider")) {
            if (mEtZipCode.getText().toString().isEmpty()) {
                showErrorOnEditText(mEtZipCode, "Please select Zip Code");
                validated = false;
            } else {
                mEtZipCode.setError(null);
//            getLatLongFromZipCode();
            }
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


        if (validated) {
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
            //
            getProviderAddress = logInRes.getProviderAddress();
            if (getProviderAddress != null) {
                mGetAddress = new ArrayList<>();
                mGetAddress.add(getProviderAddress);
                if (getProviderAddress.street != null && getProviderAddress.state != null && getProviderAddress.country != null)
                    mEtLocation.setText(getProviderAddress.street + ", " + getProviderAddress.state + ", " + getProviderAddress.country);
            }
            //
            mEtAboutMe = findViewById(R.id.et_about);
            mEtZipCode = findViewById(R.id.et_zipcode);// Zip Code
            mEtPrice = findViewById(R.id.et_price);
            mEtEmail = findViewById(R.id.et_email);
            mEtMobile = findViewById(R.id.et_contact);
            mProgressBar = findViewById(R.id.profileProgress);
            mTiLPriceParent = findViewById(R.id.til_price);
            nestedScrollView = findViewById(R.id.nested_scroll_view);
            cardLocation = findViewById(R.id.cardlocation);
            cardSpeciality = findViewById(R.id.cardSpeciality);
            //
            cleaningHouse = findViewById(R.id.cbHouseCleaning);
            cleaningCarpet = findViewById(R.id.cbCarpetCleaning);
            cleaningWindow = findViewById(R.id.cbWindowCleaning);


            if (logInRes.speciality != null) {
                Log.d(LOG_TAG, " speciality: " + logInRes.speciality);
                mSpeciality = String.valueOf(logInRes.speciality);
                if (mSpeciality.contains("cleaningCarpet")) {
                    cleaningCarpet.setChecked(true);
                }
                if (mSpeciality.contains("cleaningHouse")) {
                    cleaningHouse.setChecked(true);
                }
                if (mSpeciality.contains("cleaningWindow")) {
                    cleaningWindow.setChecked(true);
                }
            }


            if (logInRes.radius != 0) {
                mEtDistance.setText("" + logInRes.radius + " Miles");
            }

            // set Address
            if (logInRes.providerAddress != null) {
                Log.d(LOG_TAG, " Address is :" + logInRes.providerAddress.toString());
            }

            if (getPrefHelper().getUserType().equalsIgnoreCase("serviceProvider")) {
                reviews.setVisibility(View.VISIBLE);
            }

            reviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent in = new Intent(ProfileActivity.this, RatingReview.class);
                    startActivity(in);*/

                    if (ratingResponse != null && ratingResponse.providerReviews != null && ratingResponse.providerReviews.size() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("Reviews", ratingResponse.providerReviews);
                        startActivity(RatingReview.class, bundle);
                    }

                }
            });
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

            findViewById(R.id.iv_editprofile).setOnClickListener(v -> {
                findViewById(R.id.iv_editprofile).setVisibility(View.INVISIBLE);
                setViewsEditableStatus(true);
            });
            //
            findViewById(R.id.iv_profile).setOnClickListener(v -> showPictureDialog());
            //
            mEtBirthday.setOnClickListener(v -> openDatePicker());
            mEtGender.setOnClickListener(v -> openGenderDialog());

            mEtDistance.setOnClickListener(v -> openDistanceDialog());


            mBtnSave.setOnClickListener(v -> {
                if (MandatoryFieldsValidations()) {
                    String birth = UIUtility.getEditTextValue(mEtBirthday);
                    String gender = UIUtility.getEditTextValue(mEtGender);
                    if (userType.equalsIgnoreCase("serviceProvider")) {
                        hourlyRate = Float.parseFloat(UIUtility.getEditTextValue(mEtPrice));
                    } else {
                        hourlyRate = 0;
                    }

                    //   String address = UIUtility.getEditTextValue(mEtLocation);
                    String aboutme = UIUtility.getEditTextValue(mEtAboutMe);
                    phone = UIUtility.getEditTextValue(mEtMobile);
                    email = UIUtility.getEditTextValue(mEtEmail);
                    String imgUrl = getPrefHelper().getProfilePhoto();

                    String zipcode = UIUtility.getEditTextValue(mEtZipCode);
                    String editTextValue = UIUtility.getEditTextValue(mEtDistance);

                    if (!editTextValue.equals("")) {
                        editTextValue = editTextValue.substring(0, 2);
                    } else {
                        /**
                         * This is dummy value in case of Client
                         */
                        editTextValue = "0";
                    }

                    int radius = Integer.parseInt(editTextValue);


                    if (getProviderAddress != null) {
                        String providerAddress = getProviderAddress.country + "," + getProviderAddress.state + "," + getProviderAddress.city;
                        Log.d("Vali Success==", "ELSE*********" + " ProviderAddress=" + providerAddress);
                    }
                    ArrayList<String> cleaningTypeList = new ArrayList<>();
                    String cleaningType = "";
                    if (cleaningHouse.isChecked()) {
                        cleaningType = "cleaningHouse";
                        cleaningTypeList.add(cleaningType);
                    }
                    if (cleaningCarpet.isChecked()) {
                        cleaningType = "cleaningCarpet";
                        cleaningTypeList.add(cleaningType);
                    }
                    if (cleaningWindow.isChecked()) {
                        cleaningType = "cleaningWindow";
                        cleaningTypeList.add(cleaningType);
                    }
                    //
                    if (!cleaningHouse.isChecked()) {
                        cleaningTypeList.remove("cleaningHouse");
                    }
                    if (!cleaningCarpet.isChecked()) {
                        cleaningTypeList.remove("cleaningCarpet");
                    }
                    if (!cleaningWindow.isChecked()) {


                        cleaningTypeList.remove("cleaningWindow");
                    }
                    // cleaningTypeList.add(cleaningType);

                    HashSet hs = new HashSet();
                    hs.addAll(cleaningTypeList);
                    cleaningTypeList.clear();
                    cleaningTypeList.addAll(hs);

                    if (userType.equalsIgnoreCase("serviceProvider")) {
                        if (mGetAddress != null) {
                            mProfileReq = new UpdateProfileReq(getPrefHelper().getAuthToken(), imgUrl, phone, email, aboutme, hourlyRate, gender, birth, zipcode, radius, mGetAddress, getProviderAddress, cleaningTypeList);
                            viewModel.updateProfile(mProfileReq);
                        } else {
                            showToast(getResources().getString(R.string.address_validation));
                        }
                    } else {
                        mProfileReq = new UpdateProfileReq(getPrefHelper().getAuthToken(), imgUrl, phone, email, aboutme, hourlyRate, gender, birth);
                        viewModel.updateProfile(mProfileReq);
                    }

                } else {
                    return;
                }

            });
            mEtLocation.setOnClickListener(v -> {
                Bundle mBundle = new Bundle();
                mBundle.putString("prevActivity", "Profile");
                mBundle.putString("latitude", latitude);
                mBundle.putString("longitude", longitude);
                startActivityForResultWithExtra(MapsActivity.class, 101, mBundle);
            });

            mIvProfilePic.setOnClickListener(v -> {
                if (ConnectivityHelper.isConnectedToNetwork(ProfileActivity.this)) {
                    showPictureDialog();
                } else {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        nestedScrollView.setOnTouchListener((view, event) -> {
            view.requestFocusFromTouch();
            hideKeyboard(ProfileActivity.this);
            return false;
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
       /* if (isEditable) {
            mEtPrice.setVisibility(View.VISIBLE);
            mBtnSave.setVisibility(View.VISIBLE);
        } else {
            Log.e("Is Editable==","=ELSE="+isEditable);
            mEtPrice.setVisibility(View.GONE);
            mBtnSave.setVisibility(View.GONE);
        }*/

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

        if (logInRes.speciality != null) {
            mSpeciality = String.valueOf(logInRes.speciality);
            if (mSpeciality.contains("cleaningCarpet")) {
                cleaningCarpet.setChecked(true);
            } else if (mSpeciality.contains("cleaningHouse")) {
                cleaningHouse.setChecked(true);
            } else if (mSpeciality.contains("cleaningWindow")) {
                cleaningWindow.setChecked(true);
            }
        }
        //set saved lat long
        if (logInRes.address != null && logInRes.address.size() > 0) {
            latitude = String.valueOf(logInRes.address.get(0).latitude);
            longitude = String.valueOf(logInRes.address.get(0).longitude);
        }

        Glide.with(this).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                .centerCrop()).into(mIvProfilePic);

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
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.Dialog_Theme,
                (view, year, monthOfYear, dayOfMonth) -> {
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
                .itemsCallback((dialog, itemView, which, text) -> {
                    mEtGender.setText(text.toString());
                    dialog.dismiss();
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
                .itemsCallback((dialog, itemView, which, text) -> {
                    mEtDistance.setText(text.toString());
                    dialog.dismiss();
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
                        getProviderAddress = (GetAddress) bundle.getParcelable("Address");
                        mGetAddress.add(getProviderAddress);
                        mEtLocation.setText(getProviderAddress.street + ", " + getProviderAddress.state + ", " + getProviderAddress.country);
                        mEtZipCode.setText("" + getProviderAddress.zipCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == GALLERY) {
            try {
                Uri selectedImage = data.getData();
                //  mIvProfilePic.setImageURI(selectedImage);//M1
                openCropActivity(selectedImage);
                //   mIvProfilePic.setImageURI(selectedImage);

                Glide.with(this).load(selectedImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop()).into(mIvProfilePic);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                Uri selectedImage = data.getData();
                //  mIvProfilePic.setImageURI(selectedImage);
                openCropActivity(selectedImage);
                // mIvProfilePic.setImageURI(selectedImage);

                Glide.with(this).load(selectedImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop()).into(mIvProfilePic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (ConnectivityHelper.isConnectedToNetwork(ProfileActivity.this)) {

                    try {
                        mUploadImage = new UploadImage(ProfileActivity.this);
                        mUri = result.getUri();
                        //  mIvProfilePic.setImageURI(mUri);

                        Glide.with(this).load(mUri).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                                .centerCrop()).into(mIvProfilePic);


                        mProgressBar.setVisibility(View.VISIBLE);
                        mUploadImage.imageActivityResult(mUri.toString(), mProgressBar);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
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
            case PermissionUtitlity.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
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
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ProfileActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (PermissionUtitlity.checkReadStoragePermission(ProfileActivity.this)) {
                                choosePhotoFromGallary();
                            }
                            break;
                        case 1:

                            if (PermissionUtitlity.checkPermissionCamera(ProfileActivity.this)) {
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
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void dispatchTakePictureIntent() {

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,

                    REQUEST_TAKE_PHOTO);
            File pictureFile = null;
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.apnitor.arete.pro",
                        pictureFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void openCropActivity(Uri image_uri) {
        CropImage.activity(image_uri)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(ProfileActivity.this);
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
