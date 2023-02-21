package com.apnitor.arete.pro.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.DecimalFormat;
import java.util.List;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class ProviderJobsAdapter extends RecyclerView.Adapter<ProviderJobsAdapter.ViewHolder> {

    public static final String LOG_TAG = "ProviderJobsAdapter";
    private static DecimalFormat df = new DecimalFormat("0.00");
    FindJobClickCallback findJobClickCallback;
    private Context context;
    private List<GetJobRes> jobRes;

    public ProviderJobsAdapter(Context context, List<GetJobRes> jobRes, FindJobClickCallback findJobClickCallback) {
        this.context = context;
        this.jobRes = jobRes;
        this.findJobClickCallback = findJobClickCallback;
    }

    @Override
    public ProviderJobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.provider_jobs, parent, false);
        return new ProviderJobsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProviderJobsAdapter.ViewHolder holder, int position) {
        try {
            GetJobRes model = jobRes.get(position);
            String jobCleaningType = model.cleaningType + " Cleaning";
            holder.txtCleaningType.setText(jobCleaningType);
            if (jobCleaningType.equalsIgnoreCase("House Cleaning")) {
                String rooms = "";
                if (model.bedrooms != 0 && model.bedrooms > 0)
                    if (model.bedrooms == 1) {
                        rooms += model.bedrooms + " Bedroom";
                    } else {
                        rooms += model.bedrooms + " Bedrooms";
                    }
                if (model.bathrooms != 0 && model.bathrooms > 0)
                    if (model.bathrooms == 1) {
                        rooms += ", " + model.bathrooms + " Bathroom";
                    } else {
                        rooms += ", " + model.bathrooms + " Bathrooms";
                    }
                if (model.others != 0 && model.others > 0)
                    if (model.others == 1) {
                        rooms += ", " + model.others + " Other";
                    } else {
                        rooms += ", " + model.others + " Others";
                    }

                holder.txtTaskDetail.setText(rooms + ", " + model.sqft + " SQFT ");
            } else if (jobCleaningType.equalsIgnoreCase("Window Cleaning")) {
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
                    windows += ", " + model.frenchWindows + " french Windows";
                } else {
                    windows += ", " + model.frenchWindows + " french Window";
                }

                if (model.slidingWindows > 1) {
                    windows += ", " + model.slidingWindows + " sliding Window";
                } else {
                    windows += ", " + model.slidingWindows + " sliding Windows";
                }
                // }
                //if (model.gardenWindows != 0 && model.gardenWindows > 0) {
                if (model.gardenWindows > 1) {
                    windows += ", " + model.gardenWindows + " garden Windows";
                } else {
                    windows += ", " + model.gardenWindows + " garden Window";
                }
                //  }
                // if (model.wardrobeMirrors != 0 && model.wardrobeMirrors > 0) {
                windows += ", " + model.wardrobeMirrors + " wardrobe Mirrors";
                // }
                // if (model.screens != 0 && model.screens > 0) {
                windows += ", " + model.screens + " screens";
                // }
                // if (model.skylights != 0 && model.skylights > 0) {
                windows += ", " + model.skylights + " skylights";
                // }
                //  if (model.stories != 0 && model.stories > 0) {
                windows += ", " + model.stories + " stories";
                //  }
                holder.txtTaskDetail.setText(windows);

            } else {
                String carpet = "";
                if (model.rooms != null && model.bath != null && model.entry != null && model.stairCase != null) {

                    String rooms = context.getResources().getString(R.string.rooms) + " : " + model.rooms.clean + " " + context.getResources().getString(R.string.clean) + context.getResources().getString(R.string.comma) + " " + model.rooms.protect + " " +
                            context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) + " " + model.rooms.deodrize + " " + "Deodorize" + " " + "\n";

                    String bathLaundry = "Bath/Laundry" + " : " + model.bath.clean + " " + context.getResources().getString(R.string.clean) + ", " + model.bath.protect + " " + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) +
                            " " + model.bath.deodrize + " Deodorize" + " " + "\n";

                    String entryHall = "Entry/Hall" + " : " + model.entry.clean + " " + context.getResources().getString(R.string.clean) + ", " + model.entry.protect + " " + context.getResources().getString(R.string.protect) + ", " +
                            model.entry.deodrize + " " + context.getResources().getString(R.string.deodorize) + " " + "\n";

                    String stairCase = context.getResources().getString(R.string.staircase) + " : " + model.stairCase.clean + " " + context.getResources().getString(R.string.clean) + ", " +
                            model.stairCase.protect + " " + context.getResources().getString(R.string.protect) + ", " + model.stairCase.deodrize + " " + context.getResources().getString(R.string.deodorize) + " ";

                    carpet = rooms + bathLaundry
                            + entryHall + stairCase;
                }
                holder.txtTaskDetail.setText(carpet);
            }
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


            if (model.address != null) {
                GetAddress mAddress = model.address;
                String address = "";
                address += mAddress.street;
                if (mAddress.zipCode > 0)
                    address += ", " + mAddress.zipCode;
                holder.txtAddress.setText(address);
            }
            String estimatedTime = "Estimated Time (Hours) \n" + df.format(model.estTime);
            holder.txtEstHour.setText(estimatedTime);
            holder.txtEstPrice.setText("Estimated Price \n$" + model.estPrice);

            if (model.bidPrice != null && !model.bidPrice.isEmpty()) {
                showBidPRice(holder, model.bidPrice);
            } else {
                hideBidPRice(holder);
            }

            SharedPreferences mSharedPreferenceHelper = context.getSharedPreferences("MaidPickerPrefs", Context.MODE_PRIVATE);
            String userId = mSharedPreferenceHelper.getString("userId", "");

            if (model.statusOfJob == 1) {
                if (model.providerId.equalsIgnoreCase(userId) && model.jobType.equalsIgnoreCase("Service Provider")) {
                    showViewAndStartJobButton(holder, null);
                    holder.btnStartJob.setText("Accept");
                    showViewDetailButton(holder, "Reject");
                    holder.btnViewDetails.setBackgroundColor(context.getResources().getColor(R.color.reject));
                    //
                    hideCancelJobButton(holder);
                    // do hide msg button as well
                    hideMessageButton(holder);
                } else {
                    showViewAndStartJobButton(holder, null);
                    hideCancelJobButton(holder);
                    hideMessageButton(holder);
                }
            } else if (model.statusOfJob == 2) {
                showViewAndStartJobButton(holder, null);
                //
                showCancelJobButton(holder);
                showMessageButton(holder);
                hideViewDetailButton(holder, "");

            } else if (model.statusOfJob == 3) {
                holder.btnViewDetails.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                showViewDetailButton(holder, "View Detail");
                showViewAndStartJobButton(holder, "Complete Job");
                //
                hideCancelJobButton(holder);
                hideMessageButton(holder);
            }

           else if (model.statusOfJob == 4) {
                holder.btnViewDetails.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnViewDetails.setText("View Details");
                holder.btnStartJob.setVisibility(View.GONE);
                showViewDetailButton(holder, "View Details");
                //
                hideCancelJobButton(holder);
                hideMessageButton(holder);

            }

            else {
                    holder.btnViewDetails.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.btnViewDetails.setText("View Details");
                    holder.btnStartJob.setText("Start Job");
                    hideCancelJobButton(holder);
                    hideMessageButton(holder);

            }

            if (model.jobType.equals("Fixed Price")) {
                holder.txtClientPrice.setText("Client Price \n$" + model.price);
                estimatedTime = "Est. Time (Hours) \n" + model.estTime;
                holder.linPrice.setWeightSum(3);
                holder.priceView2.setVisibility(View.VISIBLE);
                holder.txtClientPrice.setVisibility(View.VISIBLE);
            } else if (model.jobType.equals("Bid")) {
                holder.linPrice.setWeightSum(2);
                holder.txtClientPrice.setVisibility(View.GONE);
                holder.priceView2.setVisibility(View.GONE);
            } else {
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

    private void showViewDetailButton(ViewHolder holder, String s) {
        holder.btnViewDetails.setVisibility(View.VISIBLE);
        holder.btnViewDetails.setText(s);
    }

    private void hideViewDetailButton(ViewHolder holder, String s) {
        holder.btnViewDetails.setVisibility(View.GONE);
    }

    private void showViewAndStartJobButton(ViewHolder holder, String text) {
        holder.btnView2.setVisibility(View.VISIBLE);
        holder.btnStartJob.setVisibility(View.VISIBLE);
        if (text != null) {
            holder.btnStartJob.setText(text);
        } else {
            holder.btnStartJob.setText("Start Job");
        }
    }

    //
    private void showCancelJobButton(ViewHolder holder) {
        Log.v(LOG_TAG, "showCancelJobButton");
        holder.btnViewCancelJob.setVisibility(View.VISIBLE);
        holder.btnCancelJob.setVisibility(View.VISIBLE);
        //
        holder.btnStartJob.setTextSize(13);
    }

    private void hideCancelJobButton(ViewHolder holder) {
        holder.btnViewCancelJob.setVisibility(View.GONE);
        holder.btnCancelJob.setVisibility(View.GONE);
    }

    //
    private void hideMessageButton(ViewHolder holder) {
        holder.btnMessage.setVisibility(View.GONE);
    }

    private void showMessageButton(ViewHolder holder) {
        holder.btnMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return jobRes.size();
    }

    private void showBidPRice(ViewHolder holder, String bidPrice) {
        holder.txtBidrice.setVisibility(View.VISIBLE);
        holder.txtBidrice.setText(context.getResources().getString(R.string.bid_price_with_colon) + " " + context.getResources().getString(R.string.doller) + bidPrice);
    }

    private void hideBidPRice(ViewHolder holder) {
        holder.txtBidrice.setVisibility(View.GONE);
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
        AppCompatButton btnMessage;
        AppCompatButton btnRating;
        AppCompatButton btnStartJob;
        AppCompatTextView txtBidrice;
        AppCompatButton btnCancelJob;
        //AppCompatButton btnReject;
        // AppCompatButton btnBid;
        LinearLayout linPrice;
        View priceView2, btnView2, btnViewCancelJob;
        LinearLayout linButton;

        ViewHolder(View itemView) {
            super(itemView);
            txtTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
            txtBidrice = itemView.findViewById(R.id.bidPrice);
            txtCleaningType = itemView.findViewById(R.id.tvCleaningType);
            txtTime = itemView.findViewById(R.id.tvTime);
            txtAddress = itemView.findViewById(R.id.tvAddress);
            txtEstPrice = itemView.findViewById(R.id.estPrice);
            txtEstHour = itemView.findViewById(R.id.estTime);
            txtClientPrice = itemView.findViewById(R.id.clientPrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            btnRating = itemView.findViewById(R.id.btnRating);
            btnStartJob = itemView.findViewById(R.id.btnStartJob);
            btnCancelJob = itemView.findViewById(R.id.btnCancelJob);
            //btnReject = (AppCompatButton) itemView.findViewById(R.id.btnReject);
            //btnBid = (AppCompatButton) itemView.findViewById(R.id.btnBid);
            linPrice = itemView.findViewById(R.id.linPrice);
            linButton = itemView.findViewById(R.id.linBtn);
            priceView2 = itemView.findViewById(R.id.priceView2);
            btnView2 = itemView.findViewById(R.id.btnView2);
            btnViewCancelJob = itemView.findViewById(R.id.btnViewCancel);
            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (btnViewDetails.getText().toString().equalsIgnoreCase("Reject")) {
                        //  Rejected
                        if (findJobClickCallback != null) {
                            findJobClickCallback.onCancelBidClick(jobRes.get(getLayoutPosition()));
                        }
                    } else {
                        if (findJobClickCallback != null) {
                            findJobClickCallback.onListItemClick(jobRes.get(getLayoutPosition()));
                        }
                    }
                }
            });

            btnMessage.setOnClickListener(v -> Toast.makeText(context, context.getString(R.string.functionality_pending), Toast.LENGTH_SHORT).show());
            btnRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // Navigate to Rating Screen
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onRatingClick(jobRes.get(getLayoutPosition()));
                    }
                }
            });

            btnStartJob.setOnClickListener(v -> {
                if (btnStartJob.getText().toString().equalsIgnoreCase("Accept")) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onMakeBidClick(jobRes.get(getLayoutPosition()));
                    }
                } else {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onStartJobWithPosition(jobRes.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });

            btnCancelJob.setOnClickListener(v -> {
                if (findJobClickCallback != null) {
                    findJobClickCallback.onCancelBidOnlyClick(jobRes.get(getLayoutPosition()));
                }
            });

        }
    }
}