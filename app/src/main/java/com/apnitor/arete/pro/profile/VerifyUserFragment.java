package com.apnitor.arete.pro.profile;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.databinding.FragmentVerifyUserBinding;
import com.apnitor.arete.pro.fragments_callback.VerifyUserFragmentBindingCallback;
import com.apnitor.arete.pro.viewmodel.ForgotPasswordViewModel;
import com.apnitor.arete.pro.viewmodel.SignUpViewModel;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VerifyUserFragment extends BaseFragment implements VerifyUserFragmentBindingCallback {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private FragmentVerifyUserBinding binding;
    private LoginActivity loginActivity;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String enteredOTP = "", email = "", phone = "", screen = "", firstName = "", lastName = "", password = "";

    private SignUpViewModel signUpViewModel;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // Log.e("OTP", "==*****");
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.d("OTP", "==" + message);
                binding.lineField.setText(message);
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivity = (LoginActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signUpViewModel = getViewModel(SignUpViewModel.class);//

//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
//        if (checkAndRequestPermissions()) {
//            // carry on the normal flow, as the case of  permissions  granted.
//        }

        // TRY
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            MySMSBroadCastReceiver smsReceiver = new MySMSBroadCastReceiver();
//            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//            intentFilter.addAction(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
//            getActivity().registerReceiver(smsReceiver, intentFilter);
//        }

        //


        email = getArguments().getString("email");
        phone = getArguments().getString("phone");
        screen = getArguments().getString("screen");
        forgotPasswordViewModel = getViewModel(ForgotPasswordViewModel.class);
        forgotPasswordViewModel.getVerifyUserResLiveData().observe(
                this,
                signUpRes -> {
                    showSnackBar("Account verified successfully, Please login to continue.");
                    //Toast.makeText(loginActivity, "OTP matched Successfully.", Toast.LENGTH_SHORT).show();
                    Fragment fragment = new LoginFragment();
                    startFragment(fragment);
                }
        );

     /*   forgotPasswordViewModel.getForgotPasswordLiveData().observe(
                this,
                signUpRes ->{
                    Toast.makeText(loginActivity, "OTP resent to "+email+phone, Toast.LENGTH_SHORT).show();
                });*/
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVerifyUserBinding.inflate(inflater, container, false);
        binding.setCallback(this);

        if (!email.isEmpty()) {
            binding.tvVerifyTitle.setText(R.string.email_verify);
            binding.tvMessage.setText(R.string.email_message);
            binding.imgLogo.setImageResource(R.drawable.ic_message);
        } else {
            binding.imgLogo.setImageResource(R.drawable.ic_phone);
            binding.tvVerifyTitle.setText(R.string.phone_verify);
            binding.tvMessage.setText(R.string.phone_message);
        }

        binding.lineField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete(@NotNull String enteredText) {
                enteredOTP = enteredText;
                if (enteredOTP.isEmpty() || enteredOTP.length() != 4) {
                    showErrorOnEditText(binding.lineField, "Please enter valid OTP");
                } else {
                    hitService();
                }
                return false; // Return true to keep the keyboard open else return false to close the keyboard
            }
        });
        return binding.getRoot();
    }

    private void hitService() {
        if (phone.isEmpty()) {
            forgotPasswordViewModel.verifyUserOnEmail(getPrefHelper().getUserId(), enteredOTP);
        } else {
            forgotPasswordViewModel.verifyUserOnPhone(getPrefHelper().getUserId(), enteredOTP);
        }

    }

    @Override
    public void onDoneClick() {


        if (enteredOTP.isEmpty() || enteredOTP.length() != 4)
            showErrorOnEditText(binding.lineField, "Please enter OTP.");
        else {
            hitService();
        }
    }

    @Override
    public void onResendCode() {
        showToast(getString(R.string.functionality_pending));


        // To Do : Call Resend code API here

        // signUpViewModel.signUp(firstName, lastName, email, phone, password, getPrefHelper().getUserType());

        //  forgotPasswordViewModel.forgotPassword(email,phone);

       /* signUpViewModel.getSignUpResLiveData().observe(
                this,
                signUpRes -> {
                    getPrefHelper().setUserID(signUpRes.getUserId());
                    showSnackBar("Sign up successfully****.");

                }
        );*/

    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
}
