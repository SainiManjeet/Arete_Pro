package com.apnitor.arete.pro.createjob;

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
import com.apnitor.arete.pro.api.response.GetProviderRes;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;
import com.apnitor.arete.pro.interfaces.ListItemClickWithPositionCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSelectProvider extends RecyclerView.Adapter<AdapterSelectProvider.ViewHolder> {

    private static final String TAG = "AdapterSelectProvider";
    public List<GetProviderRes> providerRes;
    private ListItemClickCallback listItemClickCallback;
    private ListItemClickWithPositionCallback listItemClickWithPositionCallback;
    private Context context;
    private String mType;

    public AdapterSelectProvider(Context context, List<GetProviderRes> providerRes, ListItemClickCallback listItemClickCallback, ListItemClickWithPositionCallback listItemClickWithPositionCallback, String mType) {
        this.context = context;
        this.providerRes = providerRes;
        this.listItemClickCallback = listItemClickCallback;
        this.listItemClickWithPositionCallback = listItemClickWithPositionCallback;
        this.mType = mType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (mType.equalsIgnoreCase("Favorite")) {
            view = LayoutInflater.from(context).inflate(R.layout.row_select_provider, parent, false);
        } else if (mType.equalsIgnoreCase("Bids")) {
            view = LayoutInflater.from(context).inflate(R.layout.row_select_bid, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.row_select_favorite_provider, parent, false);
        }


        return new ViewHolder(view);
    }

//    public void setOnFlagClicked(LanguageInterface languageInterface) {
//        this.languageInterface = languageInterface;
//    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            GetProviderRes model = providerRes.get(position);

            if (model.providerName != null && model.providerName.length() > 1) {
                String name = model.providerName.substring(0, 1).toUpperCase() + model.providerName.substring(1);
                holder.txtProviderName.setText(name);
            } else {
                holder.txtProviderName.setText(model.providerName);
            }

            Log.d(TAG, "bidPrice: " + model.bidPrice);
            if (mType.equalsIgnoreCase("Bids")) {
                if (model.bidPrice != null && !model.bidPrice.isEmpty()) {
                    holder.txtBidPrice.setVisibility(View.VISIBLE);
                    holder.txtBidPrice.setText(context.getResources().getString(R.string.bid_price) + model.bidPrice);

                } else {
                    holder.txtBidPrice.setVisibility(View.GONE);
                }
            }

            //else {
            else if(mType.equalsIgnoreCase("Fixed")){
                // Fixed Price Only
                if (model.providerPrice != null && !model.providerPrice.isEmpty()) {
                    holder.txtBidPrice.setVisibility(View.VISIBLE);
                    holder.txtBidPrice.setText(context.getResources().getString(R.string.price_txt) + ": " + model.providerPrice);
                } else {
                    holder.txtBidPrice.setVisibility(View.GONE);
                }
            }
            else{
                holder.txtBidPrice.setVisibility(View.GONE);
            }
            // Bid description
            if (model.bidDescription != null && !model.bidDescription.isEmpty()) {

                Log.d("BidDEscription", model.bidDescription);
                try {
                    holder.txtBidDescription.setVisibility(View.VISIBLE);
                    if (holder.txtBidDescription != null)
                        holder.txtBidDescription.setText(model.bidDescription);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            holder.txtProviderJobDone.setText(model.jobDone + "\nJobs");

            if (model.providerPrice != null && !model.providerPrice.isEmpty()) {
                //  String perHour = model.providerPrice.substring(0, model.providerPrice.length() - 1);
                //holder.txtProviderPrice.setText("$" + perHour + "\nPer Hour");

                holder.txtProviderPrice.setText("$" + model.providerPrice + "\n Price Per Hour");
            }


            Log.e("Adap Rating", "  " + model.rating);
            Float ratingh = Float.valueOf(model.rating);
            holder.mRatingBar.setRating(ratingh);

           /* Log.e("TotalHours==", "=" + model.totalHours);
            if (null == model.totalHours) {
                holder.txtWorkedHours.setText(0 + "\nHours");
            } else {
                holder.txtWorkedHours.setText(model.totalHours + "\nHours");
            }*/


            Glide.with(context).load(model.providerImage).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
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
        AppCompatTextView txtProviderName;
        AppCompatTextView txtProviderPrice;
        AppCompatButton btnHired, btnView;
        AppCompatTextView txtProviderJobDone;
        //  AppCompatTextView txtWorkedHours;
        AppCompatTextView txtBidPrice;
        RatingBar mRatingBar;

        AppCompatTextView txtBidDescription;


        ViewHolder(View itemView) {
            super(itemView);
            mRatingBar = itemView.findViewById(R.id.rating);
            img = itemView.findViewById(R.id.providerImage);
            txtProviderName = itemView.findViewById(R.id.tvName);
            txtProviderPrice = itemView.findViewById(R.id.tvProviderRate);
            // txtWorkedHours = itemView.findViewById(R.id.tvWorkedHours);
            txtBidPrice = itemView.findViewById(R.id.tvBidPrice);
            //tvBidDescription = itemView.findViewById(R.id.tvBidDescription);// Bid description
            btnHired = itemView.findViewById(R.id.btnHire);
            btnView = itemView.findViewById(R.id.btnView);
            txtProviderJobDone = itemView.findViewById(R.id.tvJobPerformed);

            txtBidDescription = itemView.findViewById(R.id.tvBidDescription);

            itemView.setOnClickListener(v -> {
//                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                if (listItemClickWithPositionCallback != null)
                    listItemClickWithPositionCallback.onListItemWithPositionClick(providerRes.get(getLayoutPosition()), getLayoutPosition());

            });
            btnHired.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onHireClick(providerRes.get(getLayoutPosition()));

                // TOdO set result
            });

            btnView.setOnClickListener(v -> {
//                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                if (listItemClickWithPositionCallback != null)
                    listItemClickWithPositionCallback.onListItemWithPositionClick(providerRes.get(getLayoutPosition()), getLayoutPosition());

            });


        }
    }
}
