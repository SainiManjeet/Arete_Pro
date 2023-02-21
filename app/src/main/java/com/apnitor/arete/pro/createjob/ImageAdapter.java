package com.apnitor.arete.pro.createjob;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.interfaces.ListItemCancelCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ImageUrlReq> providerRes;
    ListItemCancelCallback listItemCancelCallback;
    private ListItemClickWithPositionCallback listItemClickCallback;

    public ImageAdapter(Context context, ArrayList<ImageUrlReq> providerRes, ListItemClickWithPositionCallback listItemClickCallback,ListItemCancelCallback listItemCancelCallback) {
        this.context = context;
        this.providerRes = providerRes;
        this.listItemCancelCallback=listItemCancelCallback;
        this.listItemClickCallback = listItemClickCallback;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);

        return new ImageAdapter.ViewHolder(view);
    }

//    public void setOnFlagClicked(LanguageInterface languageInterface) {
//        this.languageInterface = languageInterface;
//    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        try {
            ImageUrlReq model = providerRes.get(position);
            Log.d("IMAGE URL", " : "+model.imageUrl);
            Glide.with(context).load(model.imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_default_image)
                    .centerCrop()).into(holder.img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override

    public int getItemCount() {
        return providerRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView img;
        ImageView mIvClose;

        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCleaningArea);
            mIvClose = itemView.findViewById(R.id.iv_close_btn);

            itemView.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onListItemWithPositionClick(providerRes.get(getLayoutPosition()),getLayoutPosition());
            });

            if (listItemCancelCallback==null){
                mIvClose.setVisibility(View.GONE);
            }
            mIvClose.setOnClickListener(v -> {
                if (listItemCancelCallback != null)
                    listItemCancelCallback.onListCancelWithPositionClick(providerRes.get(getLayoutPosition()),getLayoutPosition());
            });
        }
    }
}