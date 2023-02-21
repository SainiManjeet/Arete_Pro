package com.apnitor.arete.pro.profile;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.databinding.FragmentSignUpBinding;
import com.apnitor.arete.pro.fragments_callback.SignUpFragmentBindingCallback;
import com.apnitor.arete.pro.util.ConnectivityHelper;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.viewmodel.SignUpViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends BaseFragment implements SignUpFragmentBindingCallback {
    private String email = "";
    private FragmentSignUpBinding binding;
    private SignUpViewModel signUpViewModel;
    private LoginActivity loginActivity;
    private boolean isPasswordVisible = false;
    private String userType;
    private String phone = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signUpViewModel = getViewModel(SignUpViewModel.class);

        signUpViewModel.getSignUpResLiveData().observe(
                this,
                signUpRes -> {
                    getPrefHelper().setUserID(signUpRes.getUserId());
                    //showSnackBar("Sign up successfully.");
                    //navController.navigate(R.id.action_verify_User);
                    //  Toast.makeText(loginActivity, "Sign up successfully.", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new VerifyUserFragment();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("email", email);
                    mBundle.putString("screen", "signUp");
                    mBundle.putString("phone", phone);

                    //
                    mBundle.putString("password", binding.etPassword.toString());
                    mBundle.putString("firstName", binding.etFirstName.toString());
                    mBundle.putString("lastName", binding.etLastName.toString());

                    startFragment(fragment, mBundle);


                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        binding.setCallback(this);
        passwordValidations();
        return binding.getRoot();
    }

    @Override
    public void onSignUp() {
        email = "";
        phone = "";
        String firstName = UIUtility.getEditTextValue(binding.etFirstName);


        String strEmlPhn = UIUtility.getEditTextValue(binding.etEmail);

        String password = UIUtility.getEditTextValue(binding.etPassword);
        String lastName = UIUtility.getEditTextValue(binding.etLastName);

        if (strEmlPhn.contains("@")) {
            email = strEmlPhn;
        } else {
            phone = strEmlPhn;
        }
        if (firstName.isEmpty()) {
            showErrorOnEditText(binding.etFirstName, "Please enter First Name.");
        } else if (lastName.isEmpty()) {
            showErrorOnEditText(binding.etLastName, "Please enter Last Name.");
        } else if (!checkUser(binding.etEmail, strEmlPhn, email, phone)) {
        } else if (password.isEmpty()) {
            showErrorOnEditText(binding.etPassword, "Please enter Password.");
        } else if (!UIUtility.isValidPassword(password)) {
            showErrorOnEditText(binding.etPassword, "Please enter valid Password.");
        } else {
            if (ConnectivityHelper.isConnectedToNetwork(getActivity())) {
                hideKeyboard(getActivity());
                signUpViewModel.signUp(firstName, lastName, email, phone, password, "serviceProvider");
            } else {
                showSnackBar(getResources().getString(R.string.no_connection));
            }

        }
    }

    @Override
    public void onProfilePhotoClick() {
    }

    @Override
    public void onLogInClicked() {
        navController.navigateUp();
        /*Fragment fragment = new LoginFragment();
        Bundle mBundle = new Bundle();
        startFragment(fragment, mBundle);*/
    }

    @Override
    public void onBack() {
        navController.navigateUp();
    }

    @Override
    public void onVisiblePassword() {
        if (isPasswordVisible) {
            isPasswordVisible = false;
            binding.ivPasswordVisibility.setImageResource(R.drawable.ic_visibility_off);
            binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        } else {
            isPasswordVisible = true;
            binding.ivPasswordVisibility.setImageResource(R.drawable.ic_visibility);
            binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        }
    }

    private void passwordValidations() {
        binding.etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.cardPasswordValid.setVisibility(View.VISIBLE);
            } else {
                binding.cardPasswordValid.setVisibility(View.GONE);
            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
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
