package com.apnitor.arete.pro.createjob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ImageUrlReq;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import io.reactivex.annotations.NonNull;


public class ImagesPagerAdapter extends PagerAdapter {

    Context context;
    String imageLink;
    String LOG_TAG = "ImagesPagerAdapter";
    ArrayList<ImageUrlReq> mImagesPojoList;

    public ImagesPagerAdapter(Context context, ArrayList<ImageUrlReq> mImagesPojoList) {
        this.context = context;
        this.mImagesPojoList = mImagesPojoList;
    }


    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cover, null);
        try {
            final ImageView imageCover = (ImageView) view.findViewById(R.id.imageCover);
            imageLink = mImagesPojoList.get(position).imageUrl;
            Glide.with(context)
                    .asBitmap()
                    .load(imageLink)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageCover.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


            container.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImagesPojoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


}