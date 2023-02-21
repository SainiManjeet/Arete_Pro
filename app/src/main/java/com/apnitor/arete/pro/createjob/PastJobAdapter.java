package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.interfaces.RateAndPayCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PastJobAdapter extends RecyclerView.Adapter<PastJobAdapter.ViewHolder> {

    private ArrayList<GetJobRes> mGetJobsList;
    private RateAndPayCallback clientJobsClickCallback;
    private Context context;

    public PastJobAdapter(Context context, ArrayList<GetJobRes> mGetJobsList, RateAndPayCallback clientJobsClickCallback) {
        this.context = context;
        this.mGetJobsList = mGetJobsList;
        this.clientJobsClickCallback = clientJobsClickCallback;
    }

    @NonNull
    @Override
    public PastJobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_client_past_job, parent, false);
        return new PastJobAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PastJobAdapter.ViewHolder holder, int position) {
        try {
            GetJobRes model = mGetJobsList.get(position);
            String jobCleaningType = model.cleaningType + " Clean";

            //
            String rooms = "";
            if (model.bedrooms > 0)
                if (model.bedrooms == 1) {
                    rooms += model.bedrooms + " Bedroom";
                } else {
                    rooms += model.bedrooms + " Bedrooms";
                }
            if (model.bathrooms > 0)
                if (model.bathrooms == 1) {
                    rooms += ", " + model.bathrooms + " Bathroom";
                } else {
                    rooms += ", " + model.bathrooms + " Bathrooms";
                }
            if (model.others > 0)
                if (model.others == 1) {
                    rooms += ", " + model.others + " Other";
                } else {
                    rooms += ", " + model.others + " Others";
                }

            holder.tvTaskDetail.setText(rooms);
            holder.estimatedTime.setText("" + model.estTime);

            // Check if Bid price is available
            if (model.assignedTo.bookedPrice != null && !model.assignedTo.bookedPrice.isEmpty()) {
                Double totalTime = Double.valueOf(model.assignedTo.bookedPrice);
                DecimalFormat df = new DecimalFormat("#.##");
                totalTime = Double.valueOf(df.format(totalTime));

                holder.estimatedPrice.setText("$" + totalTime);
            } else {
                holder.estimatedPrice.setText("$" + model.estPrice);
            }


            if (model.jobType.equalsIgnoreCase("Bid")) {
                holder.yourPriceLayout.setVisibility(View.GONE);
                holder.estimationLayout.setWeightSum(2);
                hideViewForYourPrice(holder);

            } else if (model.jobType.equalsIgnoreCase("Fixed Price")) {
                holder.yourPriceLayout.setVisibility(View.VISIBLE);
                holder.yourPrice.setText("$" + model.price);
                holder.estimationLayout.setWeightSum(3);
                holder.estimatedTimeTextView.setText("Hours Paid");
                holder.estimatedPrice.setText("$" + model.price);
                showViewForYouPrice(holder);

            } else {
                holder.yourPriceLayout.setVisibility(View.GONE);
                holder.estimationLayout.setWeightSum(2);
                hideViewForYourPrice(holder);
            }

            if (model.time != null && model.date != null) {
                holder.txtDate.setText(model.time + " on \n" + model.date);

            }


          //  holder.tvAddress.setText(model.address.get(0).street);
            holder.tvAddress.setText(model.address.street);

            if (model.assignedTo.providerImg != null && !model.assignedTo.providerImg.isEmpty()) {
                holder.cardView.setVisibility(View.VISIBLE);
                holder.ivProvider.setVisibility(View.VISIBLE);
                Glide.with(context).load(model.assignedTo.providerImg).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop()).into(holder.ivProvider);
            } else {
                holder.cardView.setVisibility(View.VISIBLE);
                holder.ivProvider.setVisibility(View.VISIBLE);
                Glide.with(context).load(R.drawable.ic_person_black_24dp).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                        .centerCrop()).into(holder.ivProvider);
            }

            if (model.assignedTo.providerName != null && !model.assignedTo.providerName.isEmpty()) {
                holder.txtProviderName.setVisibility(View.VISIBLE);
                holder.txtProviderName.setText(model.assignedTo.providerName);
            } else {
                holder.txtProviderName.setVisibility(View.GONE);
            }

            /*
              Set title in last so that status can be appended to it.
             */

            holder.txtTitle.setText(jobCleaningType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mGetJobsList.size();
    }

    private void showViewForYouPrice(ViewHolder holder) {
        holder.priceView2.setVisibility(View.VISIBLE);
    }

    private void hideViewForYourPrice(ViewHolder holder) {
        holder.priceView2.setVisibility(View.GONE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView txtTitle, txtProviderName;
        AppCompatTextView txtDate;
        AppCompatTextView tvTaskDetail;
        AppCompatTextView tvAddress;
        AppCompatTextView estimatedTime;
        AppCompatTextView estimatedPrice;
        AppCompatTextView yourPrice;
        AppCompatButton btnViewBids;
        AppCompatButton btnPayment;
        AppCompatButton btnEditJob;
        AppCompatButton btnMessage;
        View viewBids, priceView2;
        View viewEditJob;
        LinearLayout estimationLayout;
        LinearLayout yourPriceLayout;
        AppCompatTextView estimatedTimeTextView;
        CardView cardView;
        ImageView ivProvider;
        //  RelativeLayout jobTypeLayout;

        Boolean mRatingType = false;

        ViewHolder(View itemView) {
            super(itemView);
            ivProvider = itemView.findViewById(R.id.ivProvider);
            txtProviderName = itemView.findViewById(R.id.txtProviderName);
            cardView = itemView.findViewById(R.id.cardView);
            txtTitle = itemView.findViewById(R.id.tvCleaningType);
            txtDate = itemView.findViewById(R.id.tvTime);
            tvTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnViewBids = itemView.findViewById(R.id.btnViewBid);
            estimatedTime = itemView.findViewById(R.id.estJobTime);

            estimatedPrice = itemView.findViewById(R.id.estJobPrice);
            //
            //  txtCompletedBy = itemView.findViewById(R.id.txtCompletedBy);

            yourPrice = itemView.findViewById(R.id.yourJobPrice);

            btnPayment = itemView.findViewById(R.id.btnPay);
            btnEditJob = itemView.findViewById(R.id.btnEditJob);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            viewBids = itemView.findViewById(R.id.view_viewbids);
            priceView2 = itemView.findViewById(R.id.priceView2);
            viewEditJob = itemView.findViewById(R.id.viewEditJob);

            estimationLayout = itemView.findViewById(R.id.estimationLayout);
            yourPriceLayout = itemView.findViewById(R.id.yourPriceLayout);

            estimatedTimeTextView = itemView.findViewById(R.id.estimatedTimeTextView);

        }
    }
}
