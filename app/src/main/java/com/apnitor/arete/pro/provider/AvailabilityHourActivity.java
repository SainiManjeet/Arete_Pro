package com.apnitor.arete.pro.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.request.AvailabilityDays;
import com.apnitor.arete.pro.api.request.AvailabilityElements;
import com.apnitor.arete.pro.api.request.AvailabilityReq;
import com.apnitor.arete.pro.api.request.UpdateProfileReq;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.application.SharedPreferenceHelper;
import com.apnitor.arete.pro.databinding.ActivityAvailabilityHourNewBinding;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.viewmodel.ProfileViewModel;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import androidx.core.content.ContextCompat;

public class AvailabilityHourActivity extends BaseActivity implements View.OnClickListener {
    String LOG_TAG = "AvailabilityHourActivity";
    ArrayList<String> selectedDayList = new ArrayList<>();
    ArrayList<String> mDayList = new ArrayList<>();
    HashSet hSelectedDays;
    LogInRes logInRes;
    ArrayList<GetAddress> mGetAddress;
    TextView[] mDaysOfWeek;
    TextView[] mStartTimeOfWeek, mEndTimeOfWeek;
    CrystalRangeSeekbar[] mRangeBarOfWeek;
    // Fixed
    TextView[] mDaysOfWeekFixed;
    Calendar mPreviousCalendar;
    List<Calendar> calendars = new ArrayList<>();
    ArrayList<AvailabilityElements> mSelectedDaysList = new ArrayList<>();
    ArrayList<AvailabilityElements> mFlexibleSelectedDaysList = new ArrayList<>();
    ArrayList<Integer> mDays = new ArrayList<>();
    ArrayList<Integer> mFlexibleDays = new ArrayList<>();
    ArrayList<String> mStartTimeList = new ArrayList<>();
    ArrayList<String> mEndTimeList = new ArrayList<>();
    String mType = "Fixed";
    ArrayList<String> specialityList = new ArrayList<>();
    private ActivityAvailabilityHourNewBinding binding;
    private int MON = 1;
    private int TUE = 1;
    private int WED = 1;
    private int THU = 1;
    private int FRI = 1;
    private int SAT = 1;
    private int SUN = 1;
    private int Mf = 1;
    private int Tf = 1;
    private int Wf = 1;
    private int Thf = 1;
    private int Ff = 1;
    private int Sf = 1;
    private int Suf = 1;
    private UpdateProfileReq mProfileReq;
    private ProfileViewModel viewModel;
    private SharedPreferences mPreferences;
    private int mButtonSelection = 0; // 0 means "Fixed" and 1 means "Flexible"
    private AvailabilityReq availabilityReq;
    private AvailabilityDays savedAvailability;
    private GetAddress getProviderAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.activity_availability_hour_new);
        viewModel = getViewModel(ProfileViewModel.class);
        setToolBar("Availability");
        availabilityReq = new AvailabilityReq();
        mPreferences = getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);

        logInRes = getPrefHelper().getLogInRes();
        if (null != logInRes.address) {
            mGetAddress = logInRes.address;
        }
        getProviderAddress = logInRes.providerAddress;

        if (getProviderAddress != null) {
            mGetAddress = new ArrayList<>();
            mGetAddress.add(getProviderAddress);
        }
        //
        if (logInRes.speciality != null && logInRes.speciality.size() > 0) {
            Log.d(LOG_TAG, " speciality: " + logInRes.speciality);
            specialityList.addAll(logInRes.speciality);
        }

        if (logInRes.availability != null) {
            savedAvailability = logInRes.availability;
        }

        savedAvailability = logInRes.getAvailability();


        hSelectedDays = new HashSet();

        bindViews();

        if (savedAvailability != null) {

            getSavedFlexibleAvailability(); // Remove from here if not worked

            if (savedAvailability.availabilityDays != null && savedAvailability.availabilityDays.size() > 0) {
                for (int i = 0; i < savedAvailability.availabilityDays.size(); i++) {

                    //setSavedValues(savedAvailability.getAvailabilityDays().get(0).getStartTime(), savedAvailability.getAvailabilityDays().get(0).getEndTime());
                    // setAvailabilityTime(savedAvailability.getAvailabilityDays().get(i).getDay(), savedAvailability.getAvailabilityDays().get(i).getStartTime(), savedAvailability.getAvailabilityDays().get(i).getEndTime());
                    mStartTimeList.add(savedAvailability.getAvailabilityDays().get(i).getStartTime());
                    mEndTimeList.add(savedAvailability.getAvailabilityDays().get(i).getEndTime());
                }
            }
            //Show Avalability values
            checkAvailabilityType();
            // Non savedAvailability
            if (savedAvailability.nonavailabilityDays != null && savedAvailability.nonavailabilityDays.size() > 0) {
                getNonAvailabilityDays();
            }
        }
        if (savedAvailability != null) {
            if (savedAvailability.getType() != null) {
                if (savedAvailability.getType().equalsIgnoreCase("Fixed")) {
                    binding.btnFixed.performClick();
                } else {
                    binding.btnFlexible.performClick();
                }
            } else {
                binding.btnFixed.performClick();
            }
        } else {
            binding.btnFixed.performClick();
        }
    }

    private void getNonAvailabilityDays() {
        /*Show Non Availability values in Claender*/
        for (int i = 0; i < savedAvailability.getNonavailabilityDays().size(); i++) {

            String month = savedAvailability.getNonavailabilityDays().get(i).substring(0, 1);
            String day = savedAvailability.getNonavailabilityDays().get(i).substring(2, 4);
            String year = savedAvailability.getNonavailabilityDays().get(i).substring(5, 9);

            String mFinalDay = month + "-" + day + "-" + year;

            selectedDayList.add(mFinalDay);
            binding.txtSelectedDays.setText(selectedDayList.toString());

            // Set values
            int mMonth = Integer.parseInt(month.trim());
            mMonth = mMonth - 1;
            int mDay = Integer.parseInt(day.trim());
            int mYear = Integer.parseInt(year);
            mPreviousCalendar = Calendar.getInstance();
            mPreviousCalendar.set(mYear, mMonth, mDay);
            calendars.add(mPreviousCalendar);
        }

        setDateOnCalender(calendars);
        if (selectedDayList.size() > 0) {
            binding.imgClear.setVisibility(View.VISIBLE);
            binding.txtSelectedDays.setText(selectedDayList.toString());
        } else {
            binding.imgClear.setVisibility(View.GONE);
            binding.txtSelectedDays.setText("");
        }
    }

    void checkAvailabilityType() {
        if (savedAvailability.getType() != null) {
            if (savedAvailability.getType().equalsIgnoreCase("Fixed")) {
                Log.d(LOG_TAG, " checkAvailabilityType: " + savedAvailability.getType());
                if (savedAvailability.getAvailabilityDays() != null && savedAvailability.getAvailabilityDays().size() > 0) {
                    setSavedValues(savedAvailability.getAvailabilityDays().get(0).getStartTime(), savedAvailability.getAvailabilityDays().get(0).getEndTime());
                    if (savedAvailability != null && savedAvailability.getAvailabilityDays() != null && savedAvailability.getAvailabilityDays().size() > 0) {
                        for (int i = 0; i < savedAvailability.availabilityDays.size(); i++) {
                            setFixedDayWiseTag(savedAvailability.getAvailabilityDays().get(i).getDay());
                            setAvailabilityTimeForFixed((savedAvailability.getAvailabilityDays().get(i).getDay())-1, savedAvailability.getAvailabilityDays().get(i).getStartTime(), savedAvailability.getAvailabilityDays().get(i).getEndTime());
                        }
                    }
                }
            } else {
                Log.d(LOG_TAG, " checkAvailabilityType: " + savedAvailability.getType());
                if (savedAvailability != null && savedAvailability.getAvailabilityDays() != null && savedAvailability.getAvailabilityDays().size() > 0) {
                    for (int i = 0; i < savedAvailability.availabilityDays.size(); i++) {
                        setFlexibleDayWiseTag(savedAvailability.getAvailabilityDays().get(i).getDay());
                        setAvailabilityTime((savedAvailability.getAvailabilityDays().get(i).getDay())-1, savedAvailability.getAvailabilityDays().get(i).getStartTime(), savedAvailability.getAvailabilityDays().get(i).getEndTime());
                    }
                }
            }
        } else {
            if (savedAvailability.getAvailabilityDays() != null && savedAvailability.getAvailabilityDays().size() > 0) {
                setSavedValues(savedAvailability.getAvailabilityDays().get(0).getStartTime(), savedAvailability.getAvailabilityDays().get(0).getEndTime());
                if (savedAvailability != null && savedAvailability.getAvailabilityDays() != null && savedAvailability.getAvailabilityDays().size() > 0) {
                    for (int i = 0; i < savedAvailability.availabilityDays.size(); i++) {
                       setAvailabilityTimeForFixed((savedAvailability.getAvailabilityDays().get(i).getDay())-1, savedAvailability.getAvailabilityDays().get(i).getStartTime(), savedAvailability.getAvailabilityDays().get(i).getEndTime());
                    }
                }
            }
        }
    }

    private void setFlexibleDayWiseTag(int day) {
        if (day == 1) {
            MON = 0;
        } else if (day == 2) {
            TUE = 0;
        } else if (day == 3) {
            WED = 0;
        } else if (day == 4) {
            THU = 0;
        } else if (day == 5) {
            FRI = 0;
        } else if (day == 6) {
            SAT = 0;
        } else if (day == 7) {
            SUN = 0;
        }
    }


    private void setFixedDayWiseTag(int day) {
        if (day == 1) {
            Mf = 0;
        } else if (day == 2) {
            Tf = 0;
        } else if (day == 3) {
            Wf = 0;
        } else if (day == 4) {
            Thf = 0;
        } else if (day == 5) {
            Ff = 0;
        } else if (day == 6) {
            Sf = 0;
        } else if (day == 7) {
            Suf = 0;
        }
    }


    private void setDateOnCalender(List<Calendar> calendars) {
        binding.calendarView.setSelectedDates(calendars);
    }

    private void setSavedValues(String startValue, String endValue) {
        binding.txtStart.setText(startValue);
        binding.txtEnd.setText(endValue);
    }

    private void bindViews() {
        mDaysOfWeek = new TextView[]{binding.textMonF, binding.textTuF, binding.textWedF, binding.textThuF, binding.textFriF, binding.textSatF, binding.textSunF};
        mStartTimeOfWeek = new TextView[]{binding.txtStartMon, binding.txtStartTue, binding.txtStartWedF, binding.txtStartThuF, binding.txtStartFriF, binding.txtStartSatF, binding.txtStartSunF};
        mEndTimeOfWeek = new TextView[]{binding.txtEndMon, binding.txtEndTue, binding.txtEndWedF, binding.txtEndThuF, binding.txtEndFriF, binding.txtEndSatF, binding.txtEndSunF};
        mRangeBarOfWeek = new CrystalRangeSeekbar[]{binding.rangeSeekbarMon, binding.rangeSeekbarTue, binding.rangeSeekbarWedF, binding.rangeSeekbarThuF, binding.rangeSeekbarFriF, binding.rangeSeekbarSatF, binding.rangeSeekbarSunF};
        // Fixed Tab
        mDaysOfWeekFixed = new TextView[]{binding.textMon, binding.textTu, binding.textWed, binding.textThu, binding.textFri, binding.textSat, binding.textSun};

        binding.btnFixed.setOnClickListener(this);
        binding.btnFlexible.setOnClickListener(this);
        binding.textMonF.setOnClickListener(this);
        binding.textTuF.setOnClickListener(this);
        binding.textWedF.setOnClickListener(this);
        binding.textThuF.setOnClickListener(this);
        binding.textFriF.setOnClickListener(this);
        binding.textSatF.setOnClickListener(this);
        binding.textSunF.setOnClickListener(this);

        // Text for Fixed:
        binding.textMon.setOnClickListener(this);
        binding.textTu.setOnClickListener(this);
        binding.textWed.setOnClickListener(this);
        binding.textThu.setOnClickListener(this);
        binding.textFri.setOnClickListener(this);
        binding.textSat.setOnClickListener(this);
        binding.textSun.setOnClickListener(this);

        binding.imgClear.setOnClickListener(this);

        Calendar min = Calendar.getInstance();
        binding.calendarView.setMinimumDate(min);


        /*Set Availability Hour*/
        setAvailabilityHourFixed();


        binding.calendarView.setOnDayClickListener(eventDay -> {
            Log.d("DAY CLICKED==", "==");

            if (eventDay.isEnabled()) {

                Calendar calYear = eventDay.getCalendar();
                String currentYear = String.valueOf(calYear.get(Calendar.YEAR));

                Log.e("currentYear==", currentYear);


                String currentString = String.valueOf(eventDay.getCalendar().getTime());
                String[] separated = currentString.split("00");
                String day = separated[0];
                Log.d(LOG_TAG, " day is: " + "" + day.trim());

                String monthOnly = day.substring(4, 7);
                String mFilterMonth = getFilteredMonth(monthOnly);

                String date = day.substring(7);

                String mFinalDay = mFilterMonth.trim() + "-" + date.trim() + "-" + currentYear.trim();
                Log.d(LOG_TAG, " FinalDate: " + "" + mFinalDay);

                if (selectedDayList.contains(mFinalDay)) {
                    selectedDayList.remove(mFinalDay);
                } else {
                    selectedDayList.add(mFinalDay);
                }

                // Remove duplicates
                mDayList.clear();
                mDayList.addAll(selectedDayList);
                Log.d("Inside Day==", "last==" + mDayList.toString());
                if (selectedDayList.size() > 0) {
                    binding.imgClear.setVisibility(View.VISIBLE);
                    binding.txtSelectedDays.setText(selectedDayList.toString());
                } else {
                    binding.imgClear.setVisibility(View.GONE);
                    binding.txtSelectedDays.setText("");
                }

            }
        });


        // API Calling
        binding.btnDone.setOnClickListener(v -> {
            String phone = mPreferences.getString("phone", "");
            String email = mPreferences.getString("email", "");
            String aboutMe = mPreferences.getString("AboutME", "");
            String gender = mPreferences.getString("gender", "");
            String birthDate = mPreferences.getString("birthDate", "");
            String profilePhotoUrl = mPreferences.getString("profilePhotoUrl", "");

            float hourlyRate = mPreferences.getFloat(SharedPreferenceHelper.HOURLY_RATE, 0);
            String zipcode = mPreferences.getString(SharedPreferenceHelper.ZIPCODE, "");
            int radius = mPreferences.getInt(SharedPreferenceHelper.RADIUS, 0);


            availabilityReq.setNonavailabilityDays(selectedDayList);

            if (mButtonSelection == 0) {
                mType = "Fixed";
            } else {
                mType = "Flexible";
            }
            if (mType.equals("Fixed")) {
                for (int i = 0; i < mSelectedDaysList.size(); i++) {
                    mSelectedDaysList.get(i).setStartTime(binding.txtStart.getText().toString());
                    mSelectedDaysList.get(i).setEndTime(binding.txtEnd.getText().toString());
                }

                availabilityReq.setAvailabilityDays(mSelectedDaysList);//
            } else {
                setStartEndTime(mFlexibleSelectedDaysList);
                availabilityReq.setAvailabilityDays(mFlexibleSelectedDaysList);//
            }

            availabilityReq.setType(mType);


            if (mGetAddress != null) {

                mProfileReq = new UpdateProfileReq(getPrefHelper().getAuthToken(), profilePhotoUrl, phone, email, aboutMe, hourlyRate, gender, birthDate, zipcode, radius,
                        availabilityReq, mGetAddress, getProviderAddress);
            } else {
                mProfileReq = new UpdateProfileReq(getPrefHelper().getAuthToken(), profilePhotoUrl, phone, email, aboutMe, hourlyRate, gender, birthDate, zipcode, radius,
                        availabilityReq);


            }
            viewModel.updateProfile(mProfileReq);

            viewModel.getUpdateProfileResLiveData().observe(AvailabilityHourActivity.this, res -> {
                showToast("Availability updated.");
                mProfileReq.speciality = specialityList;
                getPrefHelper().profileUpdated(mProfileReq);
                finish();
            });

        });
    }

    private void setStartEndTime(List<AvailabilityElements> list) {
        for (int i = 0; i < list.size(); i++) {
            //
            int day = list.get(i).getDay();
            if (day == 1) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartMon.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndMon.getText().toString());
            }
            if (day == 2) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartTue.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndTue.getText().toString());
            }
            if (day == 3) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartWedF.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndWedF.getText().toString());
            }
            if (day == 4) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartThuF.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndThuF.getText().toString());
            }
            if (day == 5) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartFriF.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndFriF.getText().toString());
            }
            if (day == 6) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartSatF.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndSatF.getText().toString());
            }
            if (day == 7) {
                mFlexibleSelectedDaysList.get(i).setStartTime(binding.txtStartSunF.getText().toString());
                mFlexibleSelectedDaysList.get(i).setEndTime(binding.txtEndSunF.getText().toString());
            }


        }
    }

    private String getFilteredMonth(String date) {
        date = date.toLowerCase();
        String filteredDate = "";
        if (date.contains("jan")) {
            filteredDate = "1";
        }
        if (date.contains("feb")) {
            filteredDate = "2";
        }
        if (date.contains("mar")) {
            filteredDate = "3";
        }
        if (date.contains("apr")) {
            filteredDate = "4";
        }
        if (date.contains("may")) {
            filteredDate = "5";
        }
        if (date.contains("jun")) {
            filteredDate = "6";
        }
        if (date.contains("jul")) {
            filteredDate = "7";
        }
        if (date.contains("aug")) {
            filteredDate = "8";
        }
        if (date.contains("sep")) {
            filteredDate = "9";
        }
        if (date.contains("oct")) {
            filteredDate = "10";
        }
        if (date.contains("nov")) {
            filteredDate = "11";
        }
        if (date.contains("dec")) {
            filteredDate = "12";
        }
        return filteredDate;
    }


    private void setAvailabilityTime(int dayOfWeek, String startTime, String endTime) {
        mDaysOfWeek[dayOfWeek].setBackgroundResource(R.drawable.circle_border);
        mDaysOfWeek[dayOfWeek].setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        mRangeBarOfWeek[dayOfWeek].setMinValue(0);
        mRangeBarOfWeek[dayOfWeek].setMaxValue(24);

        mRangeBarOfWeek[dayOfWeek].setLeft(Integer.valueOf(startTime));
        mRangeBarOfWeek[dayOfWeek].setRight(Integer.valueOf(endTime));
        // mRangeBarOfWeek[dayOfWeek].setMinStartValue(Float.parseFloat(startTime)).apply(); // uncomment this
        // mRangeBarOfWeek[dayOfWeek].setMaxStartValue(Float.parseFloat(endTime)).apply();
        mStartTimeOfWeek[dayOfWeek].setText(startTime);
        mEndTimeOfWeek[dayOfWeek].setText(endTime);

    }

    // Set data for Fixed Tab
    private void setAvailabilityTimeForFixed(int dayOfWeek, String startTime, String endTime) {
        mDaysOfWeekFixed[dayOfWeek].setBackgroundResource(R.drawable.circle_border);
        mDaysOfWeekFixed[dayOfWeek].setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        binding.txtStart.setText(startTime);
        binding.txtEnd.setText(endTime);
        binding.rangeSeekbar1.setMinValue(0);
        binding.rangeSeekbar1.setMaxValue(24);

        binding.rangeSeekbar1.setMinStartValue(Float.valueOf(23));
        binding.rangeSeekbar1.setMaxStartValue(Float.valueOf(1));
    }

    private void setAvailabilityHourFixed() {
        // set listener
        binding.rangeSeekbar1.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStart.setText(String.valueOf(minValue));
            binding.txtEnd.setText(String.valueOf(maxValue));
        });

        // MONDAY
        binding.rangeSeekbarMon.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartMon.setText(String.valueOf(minValue));
            binding.txtEndMon.setText(String.valueOf(maxValue));
        });
        // TUESDAY
        binding.rangeSeekbarTue.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartTue.setText(String.valueOf(minValue));
            binding.txtEndTue.setText(String.valueOf(maxValue));
        });
        // WEDNESDAY
        binding.rangeSeekbarWedF.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartWedF.setText(String.valueOf(minValue));
            binding.txtEndWedF.setText(String.valueOf(maxValue));
        });
        // THURSDAY
        binding.rangeSeekbarThuF.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartThuF.setText(String.valueOf(minValue));
            binding.txtEndThuF.setText(String.valueOf(maxValue));
        });
        // FRIDAY
        binding.rangeSeekbarFriF.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartFriF.setText(String.valueOf(minValue));
            binding.txtEndFriF.setText(String.valueOf(maxValue));
        });
        // SATURDAY
        binding.rangeSeekbarSatF.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartSatF.setText(String.valueOf(minValue));
            binding.txtEndSatF.setText(String.valueOf(maxValue));
        });
        // SUNDAY
        binding.rangeSeekbarSunF.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            binding.txtStartSunF.setText(String.valueOf(minValue));
            binding.txtEndSunF.setText(String.valueOf(maxValue));
        });
    }


    private void getSavedFlexibleAvailability() {

        // CHECK TYPE  and clear data accordingly

        //  Log.d(LOG_TAG, " getSavedFlexibleAvailability" + " SavedType:" + savedAvailability.getType() + " CurrentTab:" + mType);

        if (savedAvailability != null) {

            if (savedAvailability.getType() != null) {
                if (savedAvailability.getAvailabilityDays() != null) {
                    if (savedAvailability.getType().equalsIgnoreCase("Fixed")) {
                        Log.d(LOG_TAG, " getSavedFlexibleAvailability" + " Fixed");
                        for (int i = 0; i < savedAvailability.getAvailabilityDays().size(); i++) {
                            AvailabilityElements availabilityDay = new AvailabilityElements();
                            availabilityDay.setDay(savedAvailability.getAvailabilityDays().get(i).getDay());
                            availabilityDay.setStartTime(savedAvailability.getAvailabilityDays().get(i).getStartTime());
                            availabilityDay.setEndTime(savedAvailability.getAvailabilityDays().get(i).getEndTime());
                            mDays.add(savedAvailability.getAvailabilityDays().get(i).getDay());
                            mSelectedDaysList.add(availabilityDay);
                        }
                    } else if (savedAvailability.getType().equalsIgnoreCase("Flexible")) {
                        Log.d(LOG_TAG, " getSavedFlexibleAvailability" + " Flexible");
                        for (int i = 0; i < savedAvailability.getAvailabilityDays().size(); i++) {
                            AvailabilityElements availabilityDay = new AvailabilityElements();
                            availabilityDay.setDay(savedAvailability.getAvailabilityDays().get(i).getDay());
                            availabilityDay.setStartTime(savedAvailability.getAvailabilityDays().get(i).getStartTime());
                            availabilityDay.setEndTime(savedAvailability.getAvailabilityDays().get(i).getEndTime());
                            mFlexibleDays.add(savedAvailability.getAvailabilityDays().get(i).getDay());
                            mFlexibleSelectedDaysList.add(availabilityDay);
                        }
                    } else {
                        Log.d(LOG_TAG, " getSavedFlexibleAvailability" + " ELSE");
                        AvailabilityElements availabilityDay = new AvailabilityElements();
                        mSelectedDaysList.add(availabilityDay);

                    }
                } else {
                    AvailabilityElements availabilityDay = new AvailabilityElements();
                    mSelectedDaysList.add(availabilityDay);
                }
            } else {
                if (savedAvailability != null) {
                    if (savedAvailability.getAvailabilityDays() != null) {
                        for (int i = 0; i < savedAvailability.getAvailabilityDays().size(); i++) {
                            AvailabilityElements availabilityDay = new AvailabilityElements();
                            availabilityDay.setDay(savedAvailability.getAvailabilityDays().get(i).getDay());
                            availabilityDay.setStartTime(savedAvailability.getAvailabilityDays().get(i).getStartTime());
                            availabilityDay.setEndTime(savedAvailability.getAvailabilityDays().get(i).getEndTime());
                            mSelectedDaysList.add(availabilityDay);
                        }
                    } else {
                        AvailabilityElements availabilityDay = new AvailabilityElements();
                        mSelectedDaysList.add(availabilityDay);
                    }
                }
            }
            // removeDuplicateValues();
            //
        }

        // Working before
   /*    if (savedAvailability != null) {
            if (savedAvailability.getAvailabilityDays() != null) {
                for (int i = 0; i < savedAvailability.getAvailabilityDays().size(); i++) {
                    AvailabilityElements availabilityDay = new AvailabilityElements();
                    availabilityDay.setDay(savedAvailability.getAvailabilityDays().get(i).getDay());
                    availabilityDay.setStartTime(savedAvailability.getAvailabilityDays().get(i).getStartTime());
                    availabilityDay.setEndTime(savedAvailability.getAvailabilityDays().get(i).getStartTime());
                    mSelectedDaysList.add(availabilityDay);
                }
            } else {
                AvailabilityElements availabilityDay = new AvailabilityElements();
                mSelectedDaysList.add(availabilityDay);
            }
        }
        removeDuplicateValues();*/

    }

    void removeDuplicateValues() {
        Log.d(LOG_TAG, " removeDuplicateValues before   :: " + mSelectedDaysList.toString());
        HashSet<AvailabilityElements> selectedElementHashSet = new HashSet<AvailabilityElements>(mSelectedDaysList);
        mSelectedDaysList.clear();
        mSelectedDaysList.addAll(selectedElementHashSet);
        Log.d(LOG_TAG, " removeDuplicateValues after mSelectedDaysList :: " + mSelectedDaysList.toString());

    }

    private void addFlexibleSelectedDay(int day, String startTime, String endTime) {
        AvailabilityElements availabilityDay = new AvailabilityElements();
        availabilityDay.setDay(day);
        availabilityDay.setStartTime(startTime);
        availabilityDay.setEndTime(endTime);

        if (mFlexibleDays.contains(day)) {
            for (int i = 0; i < mFlexibleSelectedDaysList.size(); i++) {
                if (mFlexibleSelectedDaysList.get(i).getDay() == day) {
                    mFlexibleSelectedDaysList.remove(i);
                    mFlexibleDays.remove(i);
                    break;
                }
            }
        } else {
            mFlexibleDays.add(day);
            mFlexibleSelectedDaysList.add(availabilityDay);
        }
        Log.d(LOG_TAG, "Check Flexible List" + mFlexibleSelectedDaysList.toString());
    }


    //T Selected Days
    private void addFixedSelectedDay(int day, String startTime, String endTime) {

        Log.d(LOG_TAG, " addSelectedDay");
        AvailabilityElements availabilityDay = new AvailabilityElements();
        availabilityDay.setDay(day);
        availabilityDay.setStartTime(startTime);
        availabilityDay.setEndTime(endTime);

        if (mDays.contains(day)) {
            for (int i = 0; i < mSelectedDaysList.size(); i++) {
                if (mSelectedDaysList.get(i).getDay() == day) {
                    mSelectedDaysList.remove(i);
                    mDays.remove(i);
                    break;
                }
            }
        } else {
            mDays.add(day);
            mSelectedDaysList.add(availabilityDay);
        }
        Log.d(LOG_TAG, "Check Fixed List" + mSelectedDaysList.toString());
    }

    private void deselectDay(TextView textView) {
        textView.setBackgroundResource(R.drawable.circle_border_unselected);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
    }

    private void selectDay(TextView textView) {
        textView.setBackgroundResource(R.drawable.circle_border);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFixed:
                mButtonSelection = 0;
                binding.btnFlexible.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
                binding.btnFlexible.setTextColor(getApplication().getResources().getColor(R.color.colorBlack));
                binding.btnFixed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.btnFixed.setTextColor(getApplication().getResources().getColor(R.color.colorWhite));
                binding.linFlexible.setVisibility(View.GONE);
                binding.linFixed.setVisibility(View.VISIBLE);
                binding.linAvailabilityHour.setVisibility(View.VISIBLE);
                break;
            case R.id.btnFlexible:
                mButtonSelection = 1;
                binding.btnFixed.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
                binding.btnFixed.setTextColor(getApplication().getResources().getColor(R.color.colorBlack));
                binding.btnFlexible.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                binding.btnFlexible.setTextColor(getApplication().getResources().getColor(R.color.colorWhite));
                binding.linFlexible.setVisibility(View.VISIBLE);
                binding.linFixed.setVisibility(View.GONE);
                binding.linAvailabilityHour.setVisibility(View.GONE);
                break;
            case R.id.imgClear:
                clearSelectedDates();
                break;
            case R.id.textMonF:
                if (MON == 0) {
                    deselectDay(binding.textMonF);
                    MON = 1;
                } else {
                    selectDay(binding.textMonF);
                    MON = 0;

                }
                addFlexibleSelectedDay(1, binding.txtStartMon.getText().toString(), binding.txtEndMon.getText().toString());

                break;
            case R.id.textTuF:
                if (TUE == 0) {
                    deselectDay(binding.textTuF);
                    TUE = 1;
                } else {
                    selectDay(binding.textTuF);
                    TUE = 0;

                }
                addFlexibleSelectedDay(2, binding.txtStartTue.getText().toString(), binding.txtEndTue.getText().toString());
                break;
            case R.id.textWedF:
                if (WED == 0) {
                    deselectDay(binding.textWedF);
                    WED = 1;
                } else {
                    selectDay(binding.textWedF);
                    WED = 0;

                }
                addFlexibleSelectedDay(3, binding.txtStartWedF.getText().toString(), binding.txtEndWedF.getText().toString());
                break;
            case R.id.textThuF:
                if (THU == 0) {
                    deselectDay(binding.textThuF);
                    THU = 1;
                } else {
                    selectDay(binding.textThuF);
                    THU = 0;

                }
                addFlexibleSelectedDay(4, binding.txtStartThuF.getText().toString(), binding.txtEndThuF.getText().toString());
                break;
            case R.id.textFriF:
                if (FRI == 0) {
                    deselectDay(binding.textFriF);
                    FRI = 1;
                } else {
                    selectDay(binding.textFriF);
                    FRI = 0;

                }
                addFlexibleSelectedDay(5, binding.txtStartFriF.getText().toString(), binding.txtEndFriF.getText().toString());
                break;
            case R.id.textSatF:
                if (SAT == 0) {
                    deselectDay(binding.textSatF);
                    SAT = 1;
                } else {
                    selectDay(binding.textSatF);
                    SAT = 0;
                }
                addFlexibleSelectedDay(6, binding.txtStartSatF.getText().toString(), binding.txtEndSatF.getText().toString());

                break;
            case R.id.textSunF:
                if (SUN == 0) {
                    deselectDay(binding.textSunF);
                    SUN = 1;
                } else {
                    selectDay(binding.textSunF);
                    SUN = 0;

                }
                addFlexibleSelectedDay(7, binding.txtStartSunF.getText().toString(), binding.txtEndSunF.getText().toString());
                break;

            // Fixed
            case R.id.textMon:
                if (Mf == 0) {
                    deselectDay(binding.textMon);
                    Mf = 1;
                } else {
                    selectDay(binding.textMon);
                    Mf = 0;

                }
                addFixedSelectedDay(1, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());
                break;
            case R.id.textTu:
                if (Tf == 0) {
                    deselectDay(binding.textTu);
                    Tf = 1;
                } else {
                    selectDay(binding.textTu);
                    Tf = 0;

                }
                addFixedSelectedDay(2, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());
                break;
            case R.id.textWed:
                if (Wf == 0) {
                    deselectDay(binding.textWed);
                    Wf = 1;
                } else {
                    selectDay(binding.textWed);
                    Wf = 0;
                }
                addFixedSelectedDay(3, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());

                break;
            case R.id.textThu:
                if (Thf == 0) {
                    deselectDay(binding.textThu);
                    Thf = 1;
                } else {
                    selectDay(binding.textThu);
                    Thf = 0;
                }
                addFixedSelectedDay(4, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());
                break;
            case R.id.textFri:
                if (Ff == 0) {
                    deselectDay(binding.textFri);
                    Ff = 1;
                } else {
                    selectDay(binding.textFri);
                    Ff = 0;

                }
                addFixedSelectedDay(5, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());
                break;
            case R.id.textSat:
                if (Sf == 0) {
                    deselectDay(binding.textSat);
                    Sf = 1;
                } else {
                    selectDay(binding.textSat);
                    Sf = 0;
                }
                addFixedSelectedDay(6, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());
                break;
            case R.id.textSun:
                if (Suf == 0) {
                    deselectDay(binding.textSun);
                    Suf = 1;
                } else {
                    selectDay(binding.textSun);
                    Suf = 0;
                }
                addFixedSelectedDay(7, binding.txtStart.getText().toString(), binding.txtEnd.getText().toString());
                break;
            default:
                break;
        }
    }

    private void clearSelectedDates() {
        hSelectedDays.clear();
        mDayList.clear();
        selectedDayList.clear();
        binding.txtSelectedDays.setText("");
        binding.imgClear.setVisibility(View.GONE);
        binding.calendarView.setSelectedDates(new ArrayList<>());
    }
}
