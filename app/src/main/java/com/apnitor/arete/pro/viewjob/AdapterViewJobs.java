package com.apnitor.arete.pro.viewjob;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.GetAddress;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;

import java.text.DecimalFormat;
import java.util.List;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterViewJobs extends RecyclerView.Adapter<AdapterViewJobs.ViewHolder> {

    public static final String LOG_TAG = "AdapterViewJobs";
    private static DecimalFormat df = new DecimalFormat("0.00");
    FindJobClickCallback findJobClickCallback;
    private Context context;
    private List<GetJobRes> jobRes;

    public AdapterViewJobs(Context context, List<GetJobRes> jobRes, FindJobClickCallback findJobClickCallback) {
        this.context = context;
        this.jobRes = jobRes;
        this.findJobClickCallback = findJobClickCallback;
    }

    @Override
    public AdapterViewJobs.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_view_jobs, parent, false);
        return new AdapterViewJobs.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewJobs.ViewHolder holder, int position) {
        GetJobRes model = jobRes.get(position);
        try {
            String jobCleaningType = model.cleaningType + " Cleaning";
            holder.txtCleaningType.setText(jobCleaningType);

            if (jobCleaningType.equalsIgnoreCase("House Cleaning")) {

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

            } else if (jobCleaningType.equalsIgnoreCase("Window Cleaning")) {
                String windows = "";
                // if (model.firstFloorWindows > 0) {
                if (model.firstFloorWindows > 1) {
                    windows += model.firstFloorWindows + " First Floor Windows";
                } else {
                    windows += model.firstFloorWindows + " First Floor Window";
                }
                //  }
                // if ( model.secondFloorWindows > 0) {
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
            } else {
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
            }

           setDate(model,holder);

            String address = "";
            GetAddress mAddress = null;
            if (model.address != null) {
                mAddress = model.address;

                address += mAddress.street;
                if (mAddress.zipCode > 0)
                    address += ", " + mAddress.zipCode;
            }

            //  holder.txtEstHour.setText("Estimated Time \n" + model.estTime + " Hours");
            holder.txtEstHour.setText(df.format(model.estTime) + " Hours");
            //holder.txtEstPrice.setText("Estimated Price \n$" + model.estPrice);

            //
            holder.linPrice.setWeightSum(3);
            holder.priceView2.setVisibility(View.VISIBLE);
            holder.txtClientPrice.setVisibility(View.VISIBLE);

            holder.txtEstPrice.setText("$" + model.estPrice);
            if (model.jobType.equalsIgnoreCase("Fixed Price")) {
                holder.txtClientPrice.setText("Client Price");
                // holder.txtClientPriceVal.setText("$" + model.price);

                double clientPrice = Double.parseDouble(model.price);

                holder.txtClientPriceVal.setText("$" + df.format(clientPrice));

                holder.linPrice.setWeightSum(3);
                holder.priceView2.setVisibility(View.VISIBLE);
                holder.txtClientPrice.setVisibility(View.VISIBLE);
                holder.btnBid.setText("I'm Interested");
                holder.btnReject.setVisibility(View.GONE);
                holder.linButton.setWeightSum(2);

                if (model.bidPlaced) {
                    Log.e(LOG_TAG, " If");
                    holder.btnBid.setEnabled(false);
                    holder.btnBid.setText("Interest Sent");
                    holder.btnBid.setBackgroundColor(context.getResources().getColor(R.color.colorDisabledButton));
                    holder.btnBid.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                } /*else {
                    Log.e(LOG_TAG," Else");
                    holder.btnBid.setEnabled(true);
                    holder.btnBid.setText("I'm Interested");
                    holder.btnBid.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.btnBid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                }*/

            } else if (model.jobType.equalsIgnoreCase(context.getString(R.string.bid))) {

                /**
                 * There is a bid
                 */
                if (model.bid != null && model.bid.bidId != null) {

                    if (model.bid.bidStatus.equalsIgnoreCase("Active")) {
                        holder.txtClientPrice.setVisibility(View.VISIBLE);
                        holder.txtClientPriceVal.setVisibility(View.VISIBLE);
                        holder.priceView2.setVisibility(View.VISIBLE);
                        holder.txtClientPrice.setText("Bid Price");
                        holder.txtClientPriceVal.setText("$ " + model.bid.bidPrice);
                        holder.btnBid.setVisibility(View.GONE);// 12
                        holder.btnViewDetails.setText(context.getString(R.string.modify_bid));//
                        holder.btnReject.setVisibility(View.GONE);
                        holder.btnCancelBid.setVisibility(View.VISIBLE);
                        holder.btnView3.setVisibility(View.VISIBLE);
                    } else if (model.bid.bidStatus.equalsIgnoreCase("Cancelled")) {
                        holder.linPrice.setWeightSum(2);
                        holder.txtClientPrice.setVisibility(View.GONE);
                        holder.txtClientPriceVal.setVisibility(View.GONE);
                        holder.priceView2.setVisibility(View.GONE);

                        // Code will not reach here : as the job is removed from the list)
                        // problem is here : Bid and View Detail must not Visible
                        holder.btnBid.setEnabled(true);
                        holder.btnBid.setText(context.getString(R.string.bid));
                        holder.btnBid.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        holder.btnBid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.btnBid.setVisibility(View.VISIBLE);
                        holder.btnReject.setVisibility(View.GONE);//
                        holder.btnViewDetails.setVisibility(View.VISIBLE);
                        holder.btnViewDetails.setText(context.getString(R.string.view_details));//
                        holder.btnCancelBid.setVisibility(View.GONE);
                    }
                }
                /**
                 * There is no bid
                 */
                else if (model.bid == null || model.bid.bidId == null) {
                    holder.btnBid.setEnabled(true);
                    holder.btnBid.setText(context.getString(R.string.bid));
                    holder.btnBid.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.btnBid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    holder.btnReject.setVisibility(View.GONE);//
                    holder.linPrice.setWeightSum(2);
                    holder.btnViewDetails.setVisibility(View.VISIBLE);
                }
                holder.btnReject.setVisibility(View.GONE);

            } else {
                holder.btnBid.setText("Accept");
                //holder.linButton.setWeightSum(3);
                holder.btnReject.setVisibility(View.VISIBLE);
                //  holder.linPrice.setWeightSum(2);
                holder.txtClientPrice.setVisibility(View.GONE);
                holder.priceView2.setVisibility(View.GONE);
            }

            if (model.statusOfJob == 2) {
                if (address != null && !address.isEmpty()) {
                    showAddress(holder, address);
                }
            } else {
                if (mAddress != null) {
                    if (mAddress.latitude > 0 && mAddress.longitude > 0) {
                        String mZipCode = getAddressFromLocation(mAddress.latitude, mAddress.longitude);
                        hideAddress(holder, mZipCode);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
    }

    public void setItems(List<GetJobRes> items) {
        this.jobRes = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return jobRes.size();
    }

    // Hide Address and show only zip code
    private void hideAddress(ViewHolder holder, String mZipCode) {
        if (mZipCode != null && !mZipCode.isEmpty()) {
            holder.txtAddress.setVisibility(View.VISIBLE);
            holder.imgAddress.setVisibility(View.VISIBLE);
            holder.txtAddress.setText(mZipCode);
        } else {
            holder.txtAddress.setVisibility(View.GONE);
            holder.imgAddress.setVisibility(View.GONE);
        }
    }

    private void showAddress(ViewHolder holder, String address) {
        holder.txtAddress.setVisibility(View.VISIBLE);
        holder.txtAddress.setText(address);
        holder.imgAddress.setVisibility(View.VISIBLE);

    }

    /*Get Zip code from address*/
    private String getAddressFromLocation(Double latitude, Double longitude) {
        String zipCode = "";
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addr = geocoder.getFromLocation(latitude, longitude, 1);
            if (addr.size() > 0) {
                zipCode = addr.get(0).getPostalCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zipCode;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView txtTaskDetail;
        AppCompatTextView txtCleaningType;
        AppCompatTextView txtAddress;
        AppCompatTextView txtTime;
        AppCompatTextView txtEstPrice;
        AppCompatTextView txtEstHour;
        AppCompatTextView txtClientPrice;
        AppCompatTextView txtClientPriceVal;
        AppCompatButton btnViewDetails;
        AppCompatButton btnReject;
        AppCompatButton btnBid, btnCancelBid;
        LinearLayout linPrice;
        View priceView2, btnView3;
        LinearLayout linButton;
        CardView cardView;
        AppCompatImageView imgAddress;


        ViewHolder(View itemView) {
            super(itemView);
            txtTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
            imgAddress = itemView.findViewById(R.id.imgAddress);
            txtCleaningType = itemView.findViewById(R.id.tvCleaningType);
            txtTime = itemView.findViewById(R.id.tvTime);
            txtAddress = itemView.findViewById(R.id.tvAddress);
            txtEstPrice = itemView.findViewById(R.id.estPrice);
            txtEstHour = itemView.findViewById(R.id.estTime);
            txtClientPrice = itemView.findViewById(R.id.clientPriceText);
            txtClientPriceVal = itemView.findViewById(R.id.clientPrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnBid = itemView.findViewById(R.id.btnBid);
            btnCancelBid = itemView.findViewById(R.id.btnCancelBid);// Cancel Bid
            linPrice = itemView.findViewById(R.id.linPrice);
            linButton = itemView.findViewById(R.id.linBtn);
            priceView2 = itemView.findViewById(R.id.priceView2);
            btnView3 = itemView.findViewById(R.id.btnView3); // Cancel Bid View
            cardView = itemView.findViewById(R.id.cardView);
         /*   btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onListItemClick(jobRes.get(getLayoutPosition()));
                    }
                }
            });*/

            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onListItemClickWithPositionClick(jobRes.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });


            btnBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onMakeBidWithPositionClick(jobRes.get(getLayoutPosition()), getLayoutPosition());
                    }
                }
            });


            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null)
                        findJobClickCallback.onCancelBidClick(jobRes.get(getLayoutPosition()));
                }
            });

            // Cancel Bid
            btnCancelBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null)
                        findJobClickCallback.onCancelBidOnlyClick(jobRes.get(getLayoutPosition()));
                }
            });
        }
    }

    void setDate(GetJobRes model, ViewHolder holder){
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
    }
}