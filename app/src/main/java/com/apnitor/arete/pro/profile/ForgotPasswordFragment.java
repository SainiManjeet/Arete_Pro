package com.apnitor.arete.pro.profile;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.databinding.FragmentForgotPasswordBinding;
import com.apnitor.arete.pro.fragments_callback.ForgotPasswordFragmentBindingCallback;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.viewmodel.ForgotPasswordViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ForgotPasswordFragment extends BaseFragment implements ForgotPasswordFragmentBindingCallback {

    private FragmentForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String email = "";
    private String phone = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LoginActivity loginActivity = (LoginActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        forgotPasswordViewModel = getViewModel(ForgotPasswordViewModel.class);
        forgotPasswordViewModel.getForgotPasswordLiveData().observe(
                this,
                res -> {
                    if (email.isEmpty()) {
                        showSnackBar("A message sent to you to reset password.");
                    } else {
                        showSnackBar("An email sent to you to reset password.");
                    }

                    // Toast.makeText(loginActivity, "An email sent to you to reset password.", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new UpdatePasswordFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("email", email);
                    mBundle.putString("phone", phone);
                    mBundle.putString("otp", res.getOtp());
                    startFragment(fragment, mBundle);
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        binding.setCallback(this);
        return binding.getRoot();
    }

    @Override
    public void onBack() {
       // navController.navigateUp();
    }

    @Override
    public void onResetPassword() {
        phone = "";
        email = "";
        String emlPhn = UIUtility.getEditTextValue(binding.etEmail);

        if (emlPhn.contains("@")) {
            email = emlPhn;
        } else {
            phone = emlPhn;
        }
        if (!checkUser(binding.etEmail, emlPhn, email, phone)) {

        } else {

            forgotPasswordViewModel.forgotPassword(email, phone);

        }
    }

}
