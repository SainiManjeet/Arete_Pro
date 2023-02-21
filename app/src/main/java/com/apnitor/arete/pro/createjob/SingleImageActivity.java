package com.apnitor.arete.pro.createjob;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.profile.BaseActivity;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by jitenderdev on 28/4/17.
 */

public class SingleImageActivity extends BaseActivity {

    String mDirPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "FootBuzz/Wallpapers";
    private ViewPager viewpagerTop;
    File mFile;
    boolean isSuccess;
    Bitmap mBitmap;
    String LOG_TAG = "SingleImageActivity";
    ProgressDialog mProgressDialog;
    int height;
    int width;
    int mPosition = 0;
    private GetJobRes jobRes;
    ArrayList<ImageUrlReq> mImagesList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getIntentFromPreviousActivity();
        //  setUpToolbar();
        setUpLayout();
        setupViewPager();
    }

    private void getIntentFromPreviousActivity() {
        mPosition = getIntent().getIntExtra("position", 0);
        jobRes = getIntent().getParcelableExtra("jobRes");
        mImagesList = jobRes.images;
    }


    public void setUpLayout() {
        viewpagerTop = (ViewPager) findViewById(R.id.viewpagerTop);
        viewpagerTop.setClipChildren(false);
        viewpagerTop.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        viewpagerTop.setOffscreenPageLimit(3);
    }


    public void setDataInViewObjects() {
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in,
                R.anim.anim_right_out);
    }


    /**
     * Setup viewpager and it's events
     */
    private void setupViewPager() {
        try {
            // Set Top ViewPager Adapter
            if (mImagesList == null) mImagesList = new ArrayList<>();
            ImagesPagerAdapter adapter = new ImagesPagerAdapter(this, mImagesList);
            viewpagerTop.setAdapter(adapter);
            viewpagerTop.setCurrentItem(mPosition);

            viewpagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                private int index = 0;

                @Override
                public void onPageSelected(int position) {
                    index = position;
                }

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }


}
