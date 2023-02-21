package com.apnitor.arete.pro.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.google.android.material.snackbar.Snackbar;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.application.MaidPickerApplication;
import com.apnitor.arete.pro.application.SharedPreferenceHelper;
import com.apnitor.arete.pro.util.PreferenceHandler;
import com.apnitor.arete.pro.viewmodel.BaseViewModel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected BaseViewModel baseViewModel;
    private Toast toast;
    private MaidPickerApplication application;
    ProgressDialog progressDialog;
    public ImageView mIvFilterJobs;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (MaidPickerApplication) getApplication();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        AWSMobileClient.getInstance().initialize(this).execute();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        //
        mIvFilterJobs = (ImageView) findViewById(R.id.imgFilter);
    }

    void setupToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setTitle(title);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
            //showSnackBar(getWindow().getDecorView().getRootView(),apiError.getErrorMessage());
            if (apiError.getErrorCode()==403){
                //
                getPrefHelper().logOut();
                PreferenceHandler.cleraData(this);
                //
               startActivity(new Intent(this,SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
               //
                finish();
//                finishStartActivity(SplashActivity.class);
            }
            showToast(apiError.getErrorMessage());
            Log.d(TAG, " BaseActivity : "+apiError.toString());

        });
    }

    private void handleProgress() {

        baseViewModel.getApiProgressLiveData().observe(this, apiProgress -> {

            if (apiProgress.isInProgress()) {
                progressDialog.setMessage(apiProgress.getProgressMessage());
                progressDialog.show();
            } else {
                progressDialog.setMessage("");
                progressDialog.dismiss();
            }
        });
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void showToast(String message) {
        toast.setText(message);
        toast.show();
    }

    public SharedPreferenceHelper getPrefHelper() {
        return application.getSharedPreferenceHelper();
    }

    public <T> void startActivity(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
        startWithAnim();
    }

    public <T> void startActivityForResult(Class<T> tClass, int requestCode) {
        Intent intent = new Intent(this, tClass);
        startActivityForResult(intent, requestCode);
        startWithAnim();
    }

    public <T> void startActivityForResultWithExtra(Class<T> tClass, int requestCode, Bundle mBundle) {
        Intent intent = new Intent(this, tClass).putExtras(mBundle);
        startActivityForResult(intent, requestCode);
        startWithAnim();
    }
//    <T> void startActivity(Class<T> tClass, Bundle bundle) {
//        Intent intent = new Intent(this, tClass);
//        startActivity(intent, bundle);
//    }

    public <T> void startActivity(Class<T> tClass, Bundle bundle) {
        Intent intent = new Intent(this, tClass);
        intent.putExtras(bundle);
        startActivity(intent);
        startWithAnim();
    }

    public <T> void finishStartActivity(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
        finish();
        //
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    <T> void finishStartActivity(Class<T> tClass, Bundle bundle) {
        Intent intent = new Intent(this, tClass);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    protected <T extends ViewDataBinding> T bindContentView(int layoutId) {
        return DataBindingUtil.setContentView(this, layoutId);
    }

    public void showErrorOnEditText(EditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }

    void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    <T> void startFragment(Fragment tClass, Bundle bundle, Integer id) {
        tClass.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, tClass);
        fragmentTransaction.commit();
    }

    public void startFragment(Fragment tClass, Bundle mBundle) {
        tClass.setArguments(mBundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, tClass);
        fragmentTransaction.commit();
    }

    public void startFragment(Fragment tClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, tClass);
        fragmentTransaction.commit();
    }

    public void setToolBar(String title) {
        try {
            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            mToolbar.setTitle("");
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            TextView mTextView = (TextView) findViewById(R.id.toolbar_title);
            mTextView.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpToolBar(String title) {
        try {
            Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            mToolbar.setTitle(title);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setToolBarTitle(String title) {
        try {
            getSupportActionBar().setTitle("");
            TextView mTextView = (TextView) findViewById(R.id.toolbar_title);
            mTextView.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isShowProgress(boolean show) {
        try {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.actionProgressBar);
            if (show)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show Filter
    public void isShowFilter(boolean show) {
        try {
            if (show)
                mIvFilterJobs.setVisibility(View.VISIBLE);
            else
                mIvFilterJobs.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }

    protected void startWithAnim() {
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
}
