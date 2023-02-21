package com.apnitor.arete.pro.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apnitor.arete.pro.profile.LoginActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.AssignedTo;
import com.apnitor.arete.pro.api.request.CreateJobNewReq;
import com.apnitor.arete.pro.api.request.LogoutReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.createjob.CreateJobActivity;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.profile.ProfileActivity;
import com.apnitor.arete.pro.provider.AvailabilityHourActivity;
import com.apnitor.arete.pro.provider.MyBidsProviderActivity;
import com.apnitor.arete.pro.provider.NotificationsProviderActivity;
import com.apnitor.arete.pro.provider.PastJobsProviderActivity;
import com.apnitor.arete.pro.util.HouseConstants;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.ResultCodes;
import com.apnitor.arete.pro.viewmodel.LoginViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG = "HomeActivity";
    public static String mLoginToken = "";
    public static int mCurrentFragPos = 0;
    public ArrayList<GetJobRes> mJobList = new ArrayList<>();
    JobsFragment jobsFragment;
    BottomNavigationView navigation;
    View mBadgeView;
    ImageView mIvMessages, mIvNotification, mIvFilter;
    private SharedPreferences sharedPreferences;
    private NavigationView navigationView;
    private LoginViewModel loginViewModel;
    private SharedPreferences mPreferences;
    private int mNotificationCount;
    private TextView mTvNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        String userTypes = getUserTypes();

        setContentView(R.layout.activity_home);
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNotificationCount = extras.getInt(HouseConstants.NOTIFICATION_COUNT);
        }
        sharedPreferences = getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolBarItems();
        //
        navigation = findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loginViewModel = getViewModel(LoginViewModel.class);
        loginViewModel.getLogoutResLiveData().observe(this, logInRes -> {
            Toast.makeText(this, "You are successfully logged out.", Toast.LENGTH_SHORT).show();
            logoutSuccessfully();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mPreferences = getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);

        if (savedInstanceState == null) {
            jobsFragment = new JobsFragment();
            startFragment(jobsFragment);
        }
        mIvFilterJobs = (ImageView) findViewById(R.id.imgFilter);
    }

    private String getUserTypes() {
        return mPreferences.getString("userTypes", "");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Todo set user detail in nav drawer*/
        setUserInfo();
        if (mNotificationCount > 0) {
            showBadge(mNotificationCount);
        }
        //   Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        Log.v(LOG_TAG, " onResume ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show();
    }

    private void setToolBarItems() {
        mTvNotificationCount = findViewById(R.id.tv_notification);
        //
        mIvFilter = findViewById(R.id.imgFilter);
        mIvFilter.setVisibility(View.GONE);
        //
        mIvMessages = findViewById(R.id.imgMessage);
        mIvMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Coming Soon");
            }
        });
        //
        mIvNotification = findViewById(R.id.imgNotification);
        mIvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(NotificationsProviderActivity.class);
            }
        });

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class).putExtra("ComingFrom", "Home"));
            startWithAnim();
        } else if (id == R.id.nav_my_bids) {
            startActivity(MyBidsProviderActivity.class);
        } else if (id == R.id.nav_past_jobs) {
            startActivity(PastJobsProviderActivity.class);
        } else if (id == R.id.nav_share) {
            Intent mIntent = new Intent(Intent.ACTION_SEND);
            mIntent.putExtra(Intent.EXTRA_TEXT,
                    getResources().getString(R.string.play_store_link));

            mIntent.setType("text/plain");
            startActivity(mIntent);
        } else if (id == R.id.nav_logout) {
            logout(new LogoutReq(getPrefHelper().getAuthToken()));
        } else if (id == R.id.nav_availability) {
            startActivity(AvailabilityHourActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(LogoutReq logoutReq) {
        loginViewModel.logout(logoutReq);
    }

    public void logoutSuccessfully() {
        getPrefHelper().logOut();
        PreferenceHandler.cleraData(HomeActivity.this);
        finishStartActivity(LoginActivity.class);

    }

    public void onAddClick(View view) {
        startActivityForResult(CreateJobActivity.class, RequestCodes.CREATE_JOB_REQUEST); // W bf
        //startActivityForResult(CreateJobActivity.class, RequestCodes.CONFIRM_JOB_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v(LOG_TAG, " InActiVityResult rqt: " + requestCode + " Result Code " + resultCode);
//        Toast.makeText(HomeActivity.this, "In HOME Result=", Toast.LENGTH_LONG).show();

        if (resultCode == ResultCodes.CREATE_JOB_RESULT) {

//            Toast.makeText(HomeActivity.this, "In HOME Confirm=", Toast.LENGTH_LONG).show();

            assert data != null;
            final Bundle extras = data.getExtras();
            assert extras != null;
            // Get data for img,name n id
            String providerName = (String) extras.get("providerName");
            String providerImg = (String) extras.get("providerImg");
            String providerId = (String) extras.get("providerId");
            //
            Log.e(LOG_TAG, "mProviderName = " + providerName + " providerImage:" + providerImg + " providerId:" + providerId);
            CreateJobNewReq jobReq = (CreateJobNewReq) extras.get("Job");
            String jobId = (String) extras.get("JobId");
            assert jobReq != null;
            GetJobRes getJobRes = new GetJobRes(jobReq, jobId);
            getJobRes.statusOfJob = 1;
            // Set data for img,name and id
            getJobRes.assignedTo=new AssignedTo();
            getJobRes.assignedTo.providerName=new AssignedTo().providerName;
            getJobRes.assignedTo.providerImg=new AssignedTo().providerImg;
            getJobRes.assignedTo.providerId=new AssignedTo().providerId;
            if (getJobRes.assignedTo != null) {
                Log.e(LOG_TAG, "assignedTo != null = ");
                getJobRes.assignedTo.providerName = providerName;
                getJobRes.assignedTo.providerImg = providerImg;
                getJobRes.assignedTo.providerId = providerId;
            }
            mJobList.add(getJobRes);

            if (jobsFragment != null) {
                Log.v(LOG_TAG, " in jobsFragment!=null");
                jobsFragment.update();
            }
        }

       /* if (resultCode == ResultCodes.PAYMENT_RESULT) {
            Toast.makeText(HomeActivity.this, "In HOME Result=", Toast.LENGTH_LONG).show();
        }*/
        if (requestCode == RequestCodes.PAYMENT_REQUEST) {

            String position = data.getStringExtra("position");
            Log.e(LOG_TAG,"Position in H"+position);
        }

        if (resultCode == 5001 || resultCode == ResultCodes.MODIFY_BID_RESULT || resultCode == ResultCodes.PLACE_BID_RESULT) {
            assert data != null;
            GetJobRes job = data.getParcelableExtra("jobRes");
            String price = data.getStringExtra("price");
            Log.d("onActivityResult-", "==" + price);
            //job.bidPlaced = true;
            // Update Bid Price
            //job.bidPrice = price;

            /**
             * TODO
             *
             * Remove try catch
             * It should be handled..
             */

            try {
                if (job.bid.bidStatus != null) {
                    // Update Bid Status
                    job.bid.bidStatus = "Active";

                    /**
                     * TODO
                     *
                     * March 21, 2019
                     *
                     * It should be handled at Mobile end
                     */

                    //(Bid object must not be missing from backend it must be return even with empty values :TO DO at API end)
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

            /**
             * TODO
             *
             * Check whether this object exist in main list or not
             */

            int indexOfObject = mJobList.indexOf(job);


            /**
             * TODO
             * Set price
             * Set description
             */

            if (indexOfObject >= 0) {
                GetJobRes jobRes = mJobList.get(indexOfObject);
                jobRes.bid = job.bid;
                jobRes.bidPlaced = true;
            }

            jobsFragment.update();

        }


    }

    @SuppressLint("SetTextI18n")
    private void setUserInfo() {

        View header = navigationView.getHeaderView(0);
        try {
            String firstname = sharedPreferences.getString("firstName", "");
            String email = sharedPreferences.getString("email", "");
            String lastName = sharedPreferences.getString("lastName", "");
            String phone = sharedPreferences.getString("phone", "");
            String profilePhotoUrl = sharedPreferences.getString("profilePhotoUrl", "");
            Log.e("USER_ID", lastName + ",,,,");
            LogInRes logInRes = getPrefHelper().getLogInRes();
            mLoginToken = logInRes.getAuthToken();
            AppCompatTextView userName = header.findViewById(R.id.userName);
            AppCompatTextView userEmail = header.findViewById(R.id.userEmail);
            AppCompatImageView userImage = header.findViewById(R.id.userImage);
            userName.setText(firstname + " " + lastName);
            assert email != null;
            if (email.isEmpty()) {
                userEmail.setText(phone);
            } else {
                userEmail.setText(email);
            }
            Glide.with(this).load(profilePhotoUrl).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                    .centerCrop()).into(userImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showBadge(int notificationCount) {
        removeBadge();
//        mBadgeView = LayoutInflater.from(HomeProviderActivity.this).inflate(R.layout.layout_notification_badge, navigation, false);
//        TextView mBadgeTextView = mBadgeView.findViewById(R.id.badge_text_view);
        mTvNotificationCount.setVisibility(View.VISIBLE);
        mTvNotificationCount.setText("" + notificationCount);
//        BottomNavigationItemView itemView = navigation.findViewById(R.id.navigation_notification);
//        itemView.addView(mBadgeView);
    }


    public void removeBadge() {
        if (mBadgeView != null) {
            mTvNotificationCount.setVisibility(View.GONE);
        }
    }


}
