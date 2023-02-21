package com.apnitor.arete.pro.viewjob;

import android.content.Context;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapMyBids extends RecyclerView.Adapter<AdapMyBids.ViewHolder> {
    private static DecimalFormat df = new DecimalFormat("0.00");
    String LOG_TAG = "AdapMyBids";
    FindJobClickCallback findJobClickCallback;
    private Context context;
    private List<GetJobRes> jobRes;

    public AdapMyBids(Context context, List<GetJobRes> jobRes, FindJobClickCallback findJobClickCallback) {
        this.context = context;
        this.jobRes = jobRes;
        this.findJobClickCallback = findJobClickCallback;
    }

    @Override
    public AdapMyBids.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_view_my_bids, parent, false);
        return new AdapMyBids.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapMyBids.ViewHolder holder, int position) {
        GetJobRes model = jobRes.get(position);
        try {
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

            } else if (mCleaningType.equalsIgnoreCase(context.getResources().getString(R.string.cleaning_carpet))) {
                Log.d(LOG_TAG, " In Carpet" + " ROOMS:" + model.rooms + " BATH:" + model.bath);
                String carpet = "";
                if (model.rooms != null && model.bath != null && model.entry != null && model.stairCase != null) {
                    carpet = context.getResources().getString(R.string.rooms) + " : " + model.rooms.clean + " " + context.getResources().getString(R.string.clean) + context.getResources().getString(R.string.comma) + " " + model.rooms.protect + " " +
                            context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) + " " + model.rooms.deodrize + " " + "Deodorize" + " " + "\n" +
                            "Bath/Laundry" + " : " + model.bath.clean + context.getResources().getString(R.string.clean) + "," + model.bath.protect + " " + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) +
                            model.bath.deodrize + "Deodorize" + " " + "\n" + "Entry/Hall" + " : " + model.entry.clean + context.getResources().getString(R.string.clean) + "," + model.entry.protect + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) +
                            model.entry.deodrize + context.getResources().getString(R.string.deodorize) + " " + "\n" + context.getResources().getString(R.string.staircase) + " : " + model.stairCase.clean + context.getResources().getString(R.string.clean) + context.getResources().getString(R.string.comma) +
                            model.stairCase.protect + context.getResources().getString(R.string.protect) + context.getResources().getString(R.string.comma) + model.stairCase.deodrize + context.getResources().getString(R.string.deodorize) + " ";
                }
                Log.d(LOG_TAG, " CARPET " + carpet);
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

            String time = "";
            time += model.time;
            if (model.date != null)
                time += model.date;

            if (model.when.equals("Now")) {
                holder.txtTime.setText(model.when);
            } else if (model.when.equals("Today")) {
                holder.txtTime.setText(model.when + " at " + model.time);
            } else {
                //  holder.txtTime.setText(model.time + " on " + model.date);
                holder.txtTime.setText(model.time);
            }
            GetAddress mAddress = model.address;
            String address = "";
            address += mAddress.street;
            if (mAddress.zipCode > 0)
                address += ", " + mAddress.zipCode;
            holder.txtAddress.setText(address);

            Double totalTime = Double.valueOf(model.estTime);
            totalTime = Double.valueOf(df.format(totalTime));
            holder.txtEstHour.setText(totalTime + " Hours");

            holder.linPrice.setWeightSum(3);
            holder.priceView2.setVisibility(View.VISIBLE);
            holder.txtClientPrice.setVisibility(View.VISIBLE);

            holder.txtEstPrice.setText(model.estPrice);

            if (model.jobType.equalsIgnoreCase("Fixed Price")) {

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

                        //   holder.btnBid.setVisibility(View.GONE);//
                        holder.btnViewDetails.setText(context.getString(R.string.modify_bid));//

                        holder.btnReject.setVisibility(View.GONE);

                        holder.btnCancelBid.setVisibility(View.VISIBLE);
                        holder.btnView3.setVisibility(View.VISIBLE);


                    } else if (model.bid.bidStatus.equalsIgnoreCase("Cancelled")) {
                        holder.linPrice.setWeightSum(2);
                        holder.txtClientPrice.setVisibility(View.GONE);
                        holder.txtClientPriceVal.setVisibility(View.GONE);
                        holder.priceView2.setVisibility(View.GONE);

                        holder.btnReject.setVisibility(View.GONE);//


                    }
                }


            } else {
                //  holder.btnReject.setVisibility(View.VISIBLE); // co
                //  holder.linPrice.setWeightSum(2);
                holder.txtClientPrice.setVisibility(View.GONE);
                holder.priceView2.setVisibility(View.GONE);


            }

            /**
             * TODO
             * Resolve this crash
             * and
             * Remove this try catch
             */

        } catch (Exception e) {
            e.printStackTrace();
        }

        //}
    }

    public void setItems(List<GetJobRes> items) {
        this.jobRes = items;
        Log.e("setItems===", "here=" + items);
        notifyDataSetChanged();
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
        AppCompatTextView txtClientPriceVal;
        AppCompatButton btnViewDetails;
        AppCompatButton btnReject;
        // AppCompatButton btnBid, btnCancelBid;
        AppCompatButton btnCancelBid;
        LinearLayout linPrice;
        View priceView2, btnView3;
        LinearLayout linButton;
        CardView cardView;


        ViewHolder(View itemView) {
            super(itemView);
            txtTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
            txtCleaningType = itemView.findViewById(R.id.tvCleaningType);
            txtTime = itemView.findViewById(R.id.tvTime);
            txtAddress = itemView.findViewById(R.id.tvAddress);
            txtEstPrice = itemView.findViewById(R.id.estPrice);
            txtEstHour = itemView.findViewById(R.id.estTime);
            txtClientPrice = itemView.findViewById(R.id.clientPriceText);
            txtClientPriceVal = itemView.findViewById(R.id.clientPrice);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnReject = itemView.findViewById(R.id.btnReject);
            //   btnBid = itemView.findViewById(R.id.btnBid);
            btnCancelBid = itemView.findViewById(R.id.btnCancelBid);// Cancel Bid
            linPrice = itemView.findViewById(R.id.linPrice);
            linButton = itemView.findViewById(R.id.linBtn);
            priceView2 = itemView.findViewById(R.id.priceView2);
            btnView3 = itemView.findViewById(R.id.btnView3); // Cancel Bid View
            cardView = itemView.findViewById(R.id.cardView);
            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onListItemClick(jobRes.get(getLayoutPosition()));
                    }
                }
            });

           /* btnBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (findJobClickCallback != null) {
                        findJobClickCallback.onMakeBidClick(jobRes.get(getLayoutPosition()));
                    }
                }
            });*/
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
}