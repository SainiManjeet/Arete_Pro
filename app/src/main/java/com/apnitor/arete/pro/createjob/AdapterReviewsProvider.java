package com.apnitor.arete.pro.createjob;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.ReviewRes;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterReviewsProvider extends RecyclerView.Adapter<AdapterReviewsProvider.ViewHolder> {

    private Context context;
    private List<ReviewRes> providerRes;

    private static final String LOG_TAG = "AdapterReviewsProvider";

    public AdapterReviewsProvider(Context context, ArrayList<ReviewRes> providerRes) {
        this.context = context;
        this.providerRes = providerRes;

    }

    @NonNull
    @Override
    public AdapterReviewsProvider.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.sp_hire_review, parent, false);

        return new AdapterReviewsProvider.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReviewsProvider.ViewHolder holder, int position) {
        try {
            ReviewRes model = providerRes.get(position);
            holder.providerName.setText(model.providerName);
            holder.comment.setText(model.comment);

            if (model.imageUrl != null && !model.imageUrl.isEmpty()) {
                Glide.with(context).load(model.imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop()).into(holder.providerImg);
            } else {
                Glide.with(context).load(R.drawable.ic_person_black_24dp).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop()).into(holder.providerImg);
            }
            Float ratingh = Float.valueOf(model.rating);
            Log.v(LOG_TAG," Rating ish="+ratingh);
            holder.ratingBar.setRating(ratingh);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return providerRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView providerName, comment;
        AppCompatImageView providerImg;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);

            providerName = itemView.findViewById(R.id.providerName);
            comment = itemView.findViewById(R.id.comment);
            providerImg = itemView.findViewById(R.id.imgProvider);
            ratingBar = itemView.findViewById(R.id.rating);


        }
    }
}