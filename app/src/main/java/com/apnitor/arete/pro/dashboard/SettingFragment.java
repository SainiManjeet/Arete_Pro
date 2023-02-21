package com.apnitor.arete.pro.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.LogInRes;
import com.apnitor.arete.pro.profile.BaseFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SettingFragment extends BaseFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
    public static Fragment newInstance(LogInRes getLeagueRes) {
        return null;
    }
}
