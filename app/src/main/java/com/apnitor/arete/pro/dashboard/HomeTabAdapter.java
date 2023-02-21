package com.apnitor.arete.pro.dashboard;

import com.apnitor.arete.pro.provider.FindJobsProviderFragment;
import com.apnitor.arete.pro.util.FragmentObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomeTabAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Observable mObservers = new FragmentObserver();


    public HomeTabAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {

        final Fragment fragment = mFragmentList.get(position);

        if (fragment instanceof ClientRequestsFragment && fragment instanceof Observer) {
            mObservers.addObserver((Observer) fragment);
        }

        if (fragment instanceof FindJobsProviderFragment && fragment instanceof Observer) {
            mObservers.addObserver((Observer) fragment);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


    public void updateFragments() {
        mObservers.notifyObservers();
    }

}
