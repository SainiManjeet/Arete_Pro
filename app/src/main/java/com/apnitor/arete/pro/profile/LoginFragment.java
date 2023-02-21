package com.apnitor.arete.pro.profile;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.LogInReq;
import com.apnitor.arete.pro.databinding.FragmentLoginBinding;
import com.apnitor.arete.pro.fragments_callback.LoginFragmentBindingCallback;
import com.apnitor.arete.pro.util.ConnectivityHelper;
import com.apnitor.arete.pro.util.UIUtility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

public class LoginFragment extends BaseFragment implements LoginFragmentBindingCallback, GoogleApiClient.OnConnectionFailedListener {

    String fbId, fbFirstName, fbLastName, fbEmail, fbImgUrl;
    String LOG_TAG = "LoginFragment";
    private FragmentLoginBinding binding;
    private LoginActivity loginActivity;
    private CallbackManager callbackManager;
    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount token;
    private String android_id, userType;
    private String email = "";
    private String phone = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.loginActivity = (LoginActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setCallback(this);
        getToken();
        hideKeyboard(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.e("DeviceId:  ", "" + android_id);

        userType = getPrefHelper().getUserType();

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn
                .getClient(getActivity(), gso);
        initFBLogin();
    }

    private void getGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            token = completedTask.getResult(ApiException.class);
            String photoUrl = "";
            assert token != null;
            String id = token.getId();
            String firstName = token.getGivenName();
            String lastName = token.getFamilyName();
            String email = token.getEmail();
            Uri uri = token.getPhotoUrl();
            if (uri != null) {
                photoUrl = token.getPhotoUrl().toString();
            }
            loginActivity.login(new LogInReq(email, "", firstName, lastName, photoUrl, getPrefHelper().getFirebaseToken(), "Android", id, android_id, "serviceProvider"));
            logoutGoogle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFBLogin() {
        callbackManager = CallbackManager.Factory.create();
        FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getBasicDetail(loginResult);
            }

            private void getBasicDetail(LoginResult loginResult) {
                GraphRequest basicDetailRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (basicDetailJsonObject, basicDetailResponse) -> {
                            try {
                                fbId = basicDetailJsonObject.getString("id");
                                Log.e("fbId", "" + fbId);

                                fbFirstName = basicDetailJsonObject.getString("first_name");
                                fbLastName = basicDetailJsonObject.getString("last_name");
                                fbEmail = basicDetailJsonObject.getString("email");
                                Log.e("success", "" + basicDetailJsonObject.toString());
                                try {
                                    JSONObject fbImgUrlObject = basicDetailJsonObject.getJSONObject("picture");
                                    JSONObject UrlObject = fbImgUrlObject.getJSONObject("data");
                                    fbImgUrl = UrlObject.getString("url");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (fbEmail.isEmpty()) {
                                    showDialog();
                                } else {
                                    Log.e("In else==", "Else==");
                                    loginActivity.login(new LogInReq(fbEmail, fbId, fbFirstName, fbLastName, fbImgUrl, getPrefHelper().getFirebaseToken(), "Android", android_id, "serviceProvider"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,picture.type(normal)");
                basicDetailRequest.setParameters(parameters);
                basicDetailRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(loginActivity, "You have canceled the facebook login.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                exception.printStackTrace();
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
                Toast.makeText(loginActivity, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        LoginManager.getInstance().registerCallback(
                callbackManager,
                loginResultFacebookCallback
        );
    }

    private void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_popup_email);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AppCompatEditText mEmailEdt = dialog.findViewById(R.id.etDialogEmail);
        AppCompatButton dialogButton = dialog.findViewById(R.id.btnDialogSubmit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(v -> {
            email = "";
            phone = "";
            String strEmail = UIUtility.getEditTextValue(mEmailEdt);
            if (strEmail.contains("@")) {
                email = strEmail;
            } else {
                phone = strEmail;
            }
            if (!checkUser(mEmailEdt, strEmail, email, phone)) {
            } else if (UIUtility.isNotValidEmail(strEmail)) {
                showErrorOnEditText(mEmailEdt, "Please enter valid email address.");
            } else {
                loginActivity.login(new LogInReq(strEmail, fbId, fbFirstName, fbLastName, fbImgUrl, getPrefHelper().getAuthToken(), "Android", android_id, "serviceProvider"));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1000) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onFacebookLogin() {
        if (ConnectivityHelper.isConnectedToNetwork(getActivity())) {
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("public_profile", "email")
            );
        } else {
            showSnackBar(getResources().getString(R.string.no_connection));
        }
    }

    @Override
    public void onGoogleLogin() {
        if (ConnectivityHelper.isConnectedToNetwork(getActivity())) {
            getGoogle();
        } else {
            showSnackBar(getResources().getString(R.string.no_connection));
        }
    }


    @Override
    public void onLogin() {
        email = "";
        phone = "";
        String username = UIUtility.getEditTextValue(binding.etUsername);
        String password = UIUtility.getEditTextValue(binding.etPassword);
        if (username.contains("@")) {
            email = username;
        } else {
            phone = username;
        }
        if (!checkUser(binding.etUsername, username, email, phone)) {
        } else if (password.isEmpty()) {
            showErrorOnEditText(binding.etPassword, "Please enter password.");
        } else {

            loginActivity.login(new LogInReq(email, password, getPrefHelper().getFirebaseToken(), "Android", android_id, "serviceProvider", phone));

        }
    }

    @Override
    public void onForgotPassword() {
        Fragment mFragment = new ForgotPasswordFragment();
        startFragment(mFragment);
        //navController.navigate(R.id.action_forgot_password);
    }

    @Override
    public void onSignUp() {
        try {
            navController.navigate(R.id.action_sign_up);
        } catch (Exception e) {
            e.printStackTrace();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();

        }

    }


    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    getPrefHelper().saveFirebaseToken(token);
                    // Log and toast

                });
    }


    private void logoutGoogle() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Executor) this, task -> {
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("ERRROR", connectionResult.getErrorMessage());

    }


}
