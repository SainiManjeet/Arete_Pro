package com.apnitor.arete.pro.viewjob;

import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.GetJobRes;

import java.util.List;

public class ViewJobImageAdapter extends PagerAdapter {


    private List<GetJobRes> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;


    public ViewJobImageAdapter(Context context, List<GetJobRes> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.row_view_job_image, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.jobImage);

       // Glide.with(context).load(imageModelArrayList.get(position).images).into(imageView);


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}