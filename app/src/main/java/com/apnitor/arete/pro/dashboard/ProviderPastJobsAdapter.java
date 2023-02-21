package com.apnitor.arete.pro.dashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProviderPastJobsAdapter extends RecyclerView.Adapter<ProviderPastJobsAdapter.ViewHolder> {

    private FindJobClickCallback findJobClickCallback;
    private Context context;
    private ArrayList<GetJobRes> jobRes;

    public ProviderPastJobsAdapter(Context context, ArrayList<GetJobRes> jobRes, FindJobClickCallback findJobClickCallback) {
        this.context = context;
        this.jobRes = jobRes;
        this.findJobClickCallback = findJobClickCallback;
    }

    @Override
    public ProviderPastJobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_job, parent, false);
        return new ProviderPastJobsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProviderPastJobsAdapter.ViewHolder holder, int position) {
        try {
            GetJobRes model = jobRes.get(position);
            String mCleaningType = model.cleaningType + " Cleaning";
            holder.txtCleaningType.setText(mCleaningType);

            if (mCleaningType.equalsIgnoreCase(context.getResources().getString(R.string.house_cleaning))) {
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
                holder.txtTaskDetail.setText(rooms + ", " + model.sqft + " SQFT ");
            } else if (mCleaningType.equalsIgnoreCase(context.getResources().getString(R.string.carpet_cleaning))) {
                String carpet = "";
                if (model.rooms != null && model.bath != null && model.entry != null && model.stairCase != null) {
                    carpet = context.getResources().getString(R.string.rooms) + " : " + model.rooms.clean + " " + context.getResources().getString(R.string.clean) + context.getResources().getString(R.string.comma) + " " + model.rooms.protect + " " +
                            context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) + " " + model.rooms.deodrize + " " + "Deodorize" + " " + "\n" +
                            "Bath/Laundry" + " : " + model.bath.clean + context.getResources().getString(R.string.clean) + "," + model.bath.protect + " " + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) +
                            model.bath.deodrize + "Deodorize" + " " + "\n" + "Entry/Hall" + " : " + model.entry.clean + context.getResources().getString(R.string.clean) + "," + model.entry.protect + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) +
                            model.entry.deodrize + context.getResources().getString(R.string.deodorize) + " " + "\n" + context.getResources().getString(R.string.staircase) + " : " + model.stairCase.clean + context.getResources().getString(R.string.clean) + context.getResources().getString(R.string.comma) +
                            model.stairCase.protect + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) + model.stairCase.deodrize + context.getResources().getString(R.string.deodorize) + " ";
                }
                holder.txtTaskDetail.setText(carpet);
            } else {
                // Window Cleaning
                String windows = "";

                if (model.firstFloorWindows > 1) {
                    windows += model.firstFloorWindows + " First Floor Windows";
                } else {
                    windows += model.firstFloorWindows + " First Floor Window";
                }
                if (model.secondFloorWindows > 1) {
                    windows += ", " + model.secondFloorWindows + " Second Floor Windows";
                } else {
                    windows += ", " + model.secondFloorWindows + " Second Floor Window";
                }
                if (model.frenchWindows > 1) {
                    windows += ", " + model.frenchWindows + " frenchWindows";
                } else {
                    windows += ", " + model.frenchWindows + " frenchWindow";
                }
                if (model.slidingWindows > 1) {
                    windows += ", " + model.slidingWindows + " sliding Window";
                } else {
                    windows += ", " + model.slidingWindows + " sliding Windows";
                }
                if (model.gardenWindows > 1) {
                    windows += ", " + model.gardenWindows + " garden Windows";
                } else {
                    windows += ", " + model.gardenWindows + " garden Window";
                }
                windows += ", " + model.wardrobeMirrors + " wardrobe Mirrors";
                windows += ", " + model.screens + " screens";
                windows += ", " + model.skylights + " skylights";
                windows += ", " + model.stories + " stories";
                holder.txtTaskDetail.setText(windows);
            }

            Log.d("Rating Given", " Rating " + model.ratingGiven);

            /*if (model.ratingGiven) {
                holder.btnRating.setVisibility(View.GONE);
                holder.linButton.setVisibility(View.GONE);
            } else {
                holder.btnRating.setVisibility(View.VISIBLE);
                holder.linButton.setVisibility(View.VISIBLE);
            }*/

            String time = "";
            time += model.time;
            String mWhen = model.when;
            if (mWhen.equalsIgnoreCase(context.getResources().getString(R.string.when_future))) {
                if (model.date != null && model.endDate != null) {
                    time += " " + model.date + " to " + model.endDate;
                }

            } else {
                if (model.date != null)
                    time += " " + model.date;
            }
            holder.txtTime.setText(time);

            GetAddress mAddress = model.address;
            String address = "";
            address += mAddress.street;
            if (mAddress.zipCode > 0)
                address += ", " + mAddress.zipCode;
            holder.txtAddress.setText(address);
            String estimatedTime = "Hours Worked \n" + model.estTime;
            holder.txtEstHour.setText(estimatedTime);

            // Check if Bid price is available
            if (model.bidPrice != null && !model.bidPrice.isEmpty())
                holder.txtEstPrice.setText("Earning \n$" + model.bidPrice);
            else
                holder.txtEstPrice.setText("Earning \n$" + model.estPrice);


            if (model.jobType.equals("Fixed Price")) {
                holder.txtClientPrice.setText("Client Price \n$" + model.price);
                estimatedTime = "Est. Time (Hours) \n" + model.estTime;
                holder.linPrice.setWeightSum(3);
                holder.priceView2.setVisibility(View.VISIBLE);
                holder.txtClientPrice.setVisibility(View.VISIBLE);
                holder.txtEstPrice.setText("Earning \n$" + model.price);
                //holder.btnBid.setText("I'm Interested");
                // holder.btnReject.setVisibility(View.GONE);
            } else if (model.jobType.equals("Bid")) {
                holder.linPrice.setWeightSum(2);
                // holder.btnBid.setText("Bid");
                holder.txtClientPrice.setVisibility(View.GONE);
                holder.priceView2.setVisibility(View.GONE);
                //holder.btnReject.setVisibility(View.GONE);
            } else {
                // holder.btnBid.setText("Accept");
                // holder.btnReject.setVisibility(View.VISIBLE);
                holder.linPrice.setWeightSum(2);
                holder.txtClientPrice.setVisibility(View.GONE);
                holder.priceView2.setVisibility(View.GONE);
                estimatedTime = "Est. Time (Hours) \n" + model.estTime;
            }


            holder.txtEstHour.setText(estimatedTime);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jobRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView txtTaskDetail;
        AppCompatTextView txtCleaningType;
        AppCompatTextView txtAddress;
        AppCompatTextView txtTime;
        AppCompatTextView txtEstPrice;
        AppCompatTextView txtEstHour;
        AppCompatTextView txtClientPrice;
        AppCompatButton btnViewDetails;
        AppCompatButton btnMessage, btnRating;
        //AppCompatButton btnReject;
        // AppCompatButton btnBid;
        LinearLayout linPrice;
        View priceView2;
        LinearLayout linButton;

        ViewHolder(View itemView) {
            super(itemView);
            txtTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
            txtCleaningType = itemView.findViewById(R.id.tvCleaningType);
            txtTime = itemView.findViewById(R.id.tvTime);
            txtAddress = itemView.findViewById(R.id.tvAddress);
            txtEstPrice = itemView.findViewById(R.id.estPrice);
            txtEstHour = itemView.findViewById(R.id.estTime);
            txtClientPrice = itemView.findViewById(R.id.clientPrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            //btnReject = (AppCompatButton) itemView.findViewById(R.id.btnReject);
            //btnBid = (AppCompatButton) itemView.findViewById(R.id.btnBid);
            linPrice = itemView.findViewById(R.id.linPrice);
            linButton = itemView.findViewById(R.id.linBtn);
            priceView2 = itemView.findViewById(R.id.priceView2);
            btnRating = itemView.findViewById(R.id.btnRating);
           /* btnViewDetails.setOnClickListener(v -> {
                if (findJobClickCallback != null) {
                    findJobClickCallback.onListItemClick(jobRes.get(getLayoutPosition()));
                }
            });*/

            //
            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onListItemClickWithPositionClick(jobRes.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });

            btnMessage.setOnClickListener(v -> Toast.makeText(context, context.getString(R.string.functionality_pending), Toast.LENGTH_SHORT).show());

            btnRating.setOnClickListener(v -> {
                // Navigate to Rating Screen
                if (findJobClickCallback != null) {
                    findJobClickCallback.onRatingClick(jobRes.get(getLayoutPosition()));
                }

            });
        }
    }
}