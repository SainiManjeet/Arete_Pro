package com.apnitor.arete.pro.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.databinding.FragmentResetPasswordBinding;
import com.apnitor.arete.pro.fragments_callback.ResetPasswordFragmentBindingCallback;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.viewmodel.ForgotPasswordViewModel;


public class UpdatePasswordFragment extends BaseFragment implements ResetPasswordFragmentBindingCallback {

    private FragmentResetPasswordBinding binding;
    private LoginActivity loginActivity;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String email = "", otp = "";
    private String phone = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) context;
    }


    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getArguments().getString("email");
        // otp= getArguments().getString("otp");
        phone = getArguments().getString("phone");
        forgotPasswordViewModel = getViewModel(ForgotPasswordViewModel.class);
        forgotPasswordViewModel.getNewPasswordResLiveData().observe(
                this,
                signUpRes -> {
                    showSnackBar("Your password is reset successfully.");
                    Fragment fragment = new LoginFragment();
                    startFragment(fragment);
                    // Toast.makeText(loginActivity, "Your password is reset successfully.", Toast.LENGTH_SHORT).show();
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        binding.setCallback(this);
        passwordValidations();
        return binding.getRoot();
    }

    @Override
    public void onBack() {
        navController.navigateUp();
    }

    @Override
    public void onResetPassword() {
        String strOtp = UIUtility.getEditTextValue(binding.etOtp);
        String newPassword = UIUtility.getEditTextValue(binding.etNewPassword).trim();
        String confirmPassword = UIUtility.getEditTextValue(binding.etConfirmPassword).trim();
        if (strOtp.isEmpty() || strOtp.length() != 4)
            showErrorOnEditText(binding.etOtp, "Please enter correct OTP");
        else if (newPassword.isEmpty())
            showErrorOnEditText(binding.etNewPassword, "Please enter New Password");
        else if (confirmPassword.isEmpty())
            showErrorOnEditText(binding.etConfirmPassword, "Please enter Confirm Password");
        else if (!UIUtility.isValidPassword(newPassword)) {
            showErrorOnEditText(binding.etNewPassword, "Please enter valid Password.");
        } else if (!newPassword.equals(confirmPassword)) {
            showErrorOnEditText(binding.etConfirmPassword, "New Password and Confirm password do not match");
        } else {
            forgotPasswordViewModel.newPassword(newPassword, phone, email, strOtp);
        }
    }

    private void passwordValidations() {
        binding.etNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    binding.cardPasswordValid.setVisibility(View.VISIBLE);
                } else {
                    binding.cardPasswordValid.setVisibility(View.GONE);
                }

            }
        });


        binding.etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkCharacters(s.toString());
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkCharacters(String s) {
        if (UIUtility.checkSpcChar(s)) {
            binding.tvPasswordSpcChr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        } else {
            binding.tvPasswordSpcChr.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        }

        if (UIUtility.checkSmall(s)) {
            binding.tvPasswordSmall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        } else {
            binding.tvPasswordSmall.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        }

        if (UIUtility.checkCaps(s)) {
            binding.tvPasswordCapital.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        } else {
            binding.tvPasswordCapital.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        }
        if (s.length() < 8) {
            binding.tvPasswordLength.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);
        } else {
            binding.tvPasswordLength.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
        }


    }

}
