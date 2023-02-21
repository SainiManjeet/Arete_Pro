package com.apnitor.arete.pro.viewjob;

import android.os.Bundle;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.databinding.ActivityViewJobImageBinding;
import com.apnitor.arete.pro.profile.BaseActivity;

public class ViewJobImageActivity extends BaseActivity {
private GetJobRes mJobRes;
private ActivityViewJobImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_image);
        binding=bindContentView(R.layout.activity_view_job_image);
        mJobRes=(GetJobRes)getIntent().getParcelableExtra("jobRes");
       /* ViewJobImageAdapter jobImageAdapter=new ViewJobImageAdapter(ViewJobImageActivity.this,mJobRes.images);
        binding.viewPager.setAdapter(jobImageAdapter);*/
    }
}
