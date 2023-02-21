package com.apnitor.arete.pro.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.application.MaidPickerApplication;
import com.apnitor.arete.pro.application.SharedPreferenceHelper;
import com.apnitor.arete.pro.util.UIUtility;
import com.apnitor.arete.pro.viewmodel.BaseViewModel;
import java.util.Objects;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


public class BaseFragment extends Fragment {

    //public final static String LEAGUE_INFO = "league_info";
    private static final String TAG = BaseFragment.class.getSimpleName();
    NavController navController;
    private SharedPreferences mSharedPreferenceHelper;
    private BaseViewModel baseViewModel;
    private Toast toast;
    private ProgressDialog progressDialog;
    private Context context;
    private MaidPickerApplication application;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("ShowToast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        mSharedPreferenceHelper = Objects.requireNonNull(getActivity()).getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            application = (MaidPickerApplication) Objects.requireNonNull(getActivity()).getApplication();
            navController = NavHostFragment.findNavController(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public <T extends BaseViewModel> T getViewModel(Class<T> tClass) {
        T viewModel = ViewModelProviders.of(this).get(tClass);
        this.baseViewModel = viewModel;
        handleProgress();
        handleError();
        return viewModel;
    }

    private void handleError() {
        baseViewModel.getApiErrorLiveData().observe(this, apiError -> {
            showSnackBar(apiError.getErrorMessage());
            // showToast(apiError.getErrorMessage());


            Log.d(TAG, "Here=="+apiError.toString() + " msg="+apiError.getErrorMessage() + " CODE="+apiError.getError());
        });
    }

    private void handleProgress() {
        try {
            baseViewModel.getApiProgressLiveData().observe(this, apiProgress -> {

                if (apiProgress.isInProgress()) {
                    //progressDialog.setMessage(apiProgress.getProgressMessage());
                    if (apiProgress.getProgressMessage() != null) {
                        progressDialog.setMessage(apiProgress.getProgressMessage());
                    } else {
                        progressDialog.setMessage("Please wait...");
                    }
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                } else {
                    progressDialog.setMessage("");
                    progressDialog.dismiss();
                }
            });
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showToast(String message) {
        toast.setText(message);
        toast.show();
    }

    void showErrorOnEditText(EditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }

    void startFragment(Fragment tClass, Bundle bundle) {
        tClass.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, tClass);
        fragmentTransaction.commit();
    }

    void startFragment(Fragment tClass) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, tClass);
        fragmentTransaction.commit();
    }


    boolean checkUser(AppCompatEditText editText, String username, String email, String phone) {
        Boolean validUserbool = true;
        if (!username.isEmpty()) {
            //showErrorOnEditText(binding.etUsername, "Please enter 'username'.");
            if (username.contains("@")) {
                email = username;
                if (UIUtility.isNotValidEmail(email)) {
                    validUserbool = false;
                    showErrorOnEditText(editText, "Please enter valid Email.");
                } else {
                    validUserbool = true;
                }
            } else {
                phone = username;
                if (UIUtility.isNotValidPhone(phone)) {
                    validUserbool = false;
                    showErrorOnEditText(editText, "Please enter valid Email Id.");
                } else {
                    validUserbool = true;
                }
            }
        } else {
            showErrorOnEditText(editText, "Please enter Email or Phone Number.");
            validUserbool = false;
        }
        return validUserbool;
    }

    protected SharedPreferenceHelper getPrefHelper() {
        return application.getSharedPreferenceHelper();
    }

    void showSnackBar(String message) {
        Snackbar snackbar = Snackbar
                .make(Objects.requireNonNull(getView()), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public <T> void startActivity(Class<T> tClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), tClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public String getAuthToken() {
        return mSharedPreferenceHelper.getString("authToken", "");
    }

}
