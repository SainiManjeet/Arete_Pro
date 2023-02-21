package com.apnitor.arete.pro.createjob;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.interfaces.ListItemCancelCallback;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapterPost extends RecyclerView.Adapter<ImageAdapterPost.ViewHolder> {

    private Context context;
    private ArrayList<ImageUrlReq> providerRes;
    private ListItemCancelCallback listItemClickCallback;


    public ImageAdapterPost(Context context, ArrayList<ImageUrlReq> providerRes,ListItemCancelCallback listItemClickCallback) {
        this.context = context;
        this.providerRes = providerRes;
        this.listItemClickCallback = listItemClickCallback;
    }

    @NonNull
    @Override
    public ImageAdapterPost.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);

        return new ImageAdapterPost.ViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull ImageAdapterPost.ViewHolder holder, int position) {
        try {
            ImageUrlReq model = providerRes.get(position);
            Uri mUri = Uri.parse(model.imageUrl);
            holder.img.setImageURI(mUri);
//            Glide.with(context).load(model.imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
//                    .centerCrop()).into(holder.img);
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


        ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCleaningArea);

            itemView.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onListCancelWithPositionClick(providerRes.get(getLayoutPosition()),getLayoutPosition());
            });
        }
    }
}