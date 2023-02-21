/*
package com.apnitor.arete.pro.profile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.GetMyRatingRes;
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapRatingReview extends RecyclerView.Adapter<AdapRatingReview.ViewHolder> {

    private ListItemClickCallback listItemClickCallback;
    private Context context;
    public List<GetMyRatingRes> providerRes;

   public AdapRatingReview(Context context, List<GetMyRatingRes> providerRes) {
        this.context = context;
        this.providerRes = providerRes;
        this.listItemClickCallback = listItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_rating_review, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            GetMyRatingRes model = providerRes.get(position);
           // holder.txtProviderName.setText(model.providerName);


            Float ratingh = Float.valueOf(model.);
            holder.mRatingBar.setRating(ratingh);



            Glide.with(context).load(model.).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                    .centerCrop().circleCrop()).into(holder.img);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //
    public void setItems(List<GetProviderRes> items) {
        this.providerRes = items;
        Log.e("here===", "here=" + items);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return providerRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView img;
        RatingBar mRatingBar;

        ViewHolder(View itemView) {
            super(itemView);
            mRatingBar = itemView.findViewById(R.id.rating);
            img = itemView.findViewById(R.id.providerImage);


           */
/* itemView.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onListItemWithPositionClick(providerRes.get(getLayoutPosition()));

            });
            btnHired.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onHireClick(providerRes.get(getLayoutPosition()));
            });

            btnView.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onListItemWithPositionClick(providerRes.get(getLayoutPosition()));

            });*//*




        }
    }
}
*/
