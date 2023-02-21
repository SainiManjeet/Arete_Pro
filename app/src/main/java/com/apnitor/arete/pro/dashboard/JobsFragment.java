package com.apnitor.arete.pro.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.profile.BaseFragment;
import com.apnitor.arete.pro.provider.FindJobsProviderFragment;
import com.apnitor.arete.pro.provider.MyJobsProviderFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class JobsFragment extends BaseFragment {

    HomeTabAdapter adapter;
    private TabLayout mTabLayout;
    private HomeActivity homeActivity;
    private SharedPreferences mPreferences;
    private View mView;

    public static Fragment newInstance(LogInRes getLeagueRes) {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeActivity = (HomeActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (mView == null) {

            // Initialise your layout here
            mView = inflater.inflate(R.layout.fragment_jobs, container, false);
            mTabLayout = (TabLayout) mView.findViewById(R.id.tabProfile);
            ViewPager mViewPager = (ViewPager) mView.findViewById(R.id.homeViewPager);

            mPreferences = getActivity().getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
            String userTypes = getUserTypes();
            if (userTypes.equals("serviceProvider") || userTypes.equals("Service Provider")) {
                providerViewPager(mViewPager);
            } else {
                createClientViewPager(mViewPager);
            }
            // providerViewPager(mViewPager);
            mTabLayout.setupWithViewPager(mViewPager);
        } else {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
        return mView;
    }

    private void createClientViewPager(ViewPager viewPager) {
        adapter = new HomeTabAdapter(getChildFragmentManager());
        adapter.addFrag(new ClientRequestsFragment(), "Requests");
        adapter.addFrag(new ClientPastFragment(), "Past");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        homeActivity.isShowFilter(false);
                        homeActivity.isShowProgress(false);
                        break;
                    case 1:
                        homeActivity.isShowFilter(false);
                        homeActivity.isShowProgress(false);
                        break;
                    case 2:
                        homeActivity.isShowFilter(false);
                        homeActivity.isShowProgress(false);
                        break;
                    case 3:
                        homeActivity.isShowFilter(false);
                        homeActivity.isShowProgress(false);
                        break;
                    default:
                        homeActivity.isShowFilter(false);
                        homeActivity.isShowProgress(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void providerViewPager(ViewPager viewPager) {
        adapter = new HomeTabAdapter(getChildFragmentManager());
        adapter.addFrag(new FindJobsProviderFragment(), "Find Jobs");
        adapter.addFrag(new MyJobsProviderFragment(), "My Jobs");
//        adapter.addFrag(new MyBidsFragment(), "My Bids");//
//        adapter.addFrag(new ProviderPastFragment(), "Past");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        homeActivity.isShowFilter(true);
                        break;
                    case 1:
                        homeActivity.isShowFilter(false);
                        break;
                    case 2:
                        homeActivity.isShowFilter(false);
                        break;
                    case 3:
                        homeActivity.isShowFilter(false);
                        break;
                    default:
                        homeActivity.isShowFilter(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private String getUserTypes() {
        return mPreferences.getString("userTypes", "");
    }


    public void update() {
        adapter.updateFragments();
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.mCurrentFragPos = 0;
    }
}
