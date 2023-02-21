package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.interfaces.ClientJobsClickCallback;
import com.apnitor.arete.pro.util.FragmentObserver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RequestJobsAdapter extends RecyclerView.Adapter<RequestJobsAdapter.ViewHolder> {

    private static final String LOG_TAG = "RequestJobsAdapter";
    DecimalFormat df = new DecimalFormat("#.##");
    private Observable mObservers = new FragmentObserver();
    private ArrayList<GetJobRes> mGetJobsList;
    private ClientJobsClickCallback clientJobsClickCallback;
    private Context context;

    public RequestJobsAdapter(Context context, ArrayList<GetJobRes> mGetJobsList, ClientJobsClickCallback clientJobsClickCallback) {
        this.context = context;
        this.mGetJobsList = mGetJobsList;
        this.clientJobsClickCallback = clientJobsClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_req_jobs, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            GetJobRes model = mGetJobsList.get(position);
            String jobCleaningType = model.cleaningType + " Clean";
            /*
              Set title in first so when job is just created it will be visible.
             */
            holder.txtTitle.setText(jobCleaningType);

            if (jobCleaningType.equalsIgnoreCase("House Clean")) {
                String rooms = "";
                if (model.bedrooms != 0 && model.bedrooms > 0)
                    rooms += model.bedrooms + " Bedrooms";
                if (model.bathrooms != 0 && model.bathrooms > 0)
                    rooms += ", " + model.bathrooms + " Bathrooms";
                if (model.kitchen != 0 && model.kitchen > 0)
                    rooms += ", " + model.kitchen + " Kitchen";
                if (model.others != 0 && model.others > 0)
                    rooms += ", " + model.others + " Others";
                if (model.sqft > 0)
                    rooms += ", " + model.sqft + " SQFT";
                holder.tvTaskDetail.setText(rooms);
            } else if (jobCleaningType.equalsIgnoreCase("Window Clean")) {
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

                if (model.gardenWindows > 1) {
                    windows += ", " + model.gardenWindows + " garden Windows";
                } else {
                    windows += ", " + model.gardenWindows + " garden Window";
                }

                windows += ", " + model.wardrobeMirrors + " wardrobe Mirrors";


                windows += ", " + model.screens + " screens";


                windows += ", " + model.skylights + " skylights";


                windows += ", " + model.stories + " stories";

                holder.tvTaskDetail.setText(windows);
                //
            }
            /*  Cleaning type : Carpet */
            else {
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
                holder.tvTaskDetail.setText(carpet);

            }

            holder.estimatedTime.setText("" + Double.valueOf(df.format(model.estTime)));
            //  holder.estimatedPrice.setText("$" + model.estPrice); // W bf

           /* if (model.ratingGiven) {
                holder.btnRating.setVisibility(View.VISIBLE);
            }*/

            if (model.jobType.equalsIgnoreCase("Bid")) {
                holder.yourPriceLayout.setVisibility(View.GONE);
                holder.viewYourPrice.setVisibility(View.GONE);
                holder.estimationLayout.setWeightSum(2);

                if (model.assignedTo != null)
                    if (model.assignedTo.bookedPrice != null && !model.assignedTo.bookedPrice.isEmpty()) {
                        String mPrice = "$" + model.assignedTo.bookedPrice;
                        setBidBookedPrice(holder, mPrice);
                    } else {
                        setPrice(holder, "$" + model.estPrice);
                    }
                else setPrice(holder, "$" + model.estPrice);


            } else if (model.jobType.equalsIgnoreCase("Fixed Price")) {
                holder.yourPriceLayout.setVisibility(View.VISIBLE);
                holder.viewYourPrice.setVisibility(View.VISIBLE);
                holder.yourPrice.setText(model.price);
                holder.estimationLayout.setWeightSum(3);
                holder.estimatedTimeTextView.setText("Est");

                setPrice(holder, "$" + model.estPrice);


            } else {
                holder.yourPriceLayout.setVisibility(View.GONE);
                holder.viewYourPrice.setVisibility(View.GONE);//5
                holder.estimationLayout.setWeightSum(2);
                setPrice(holder, "$" + model.estPrice);

            }

            String howOften;
            if (!model.howOften.equalsIgnoreCase("Just Once"))
                howOften = model.howOften + ", " + model.time;
            else
                howOften = model.time;
            holder.txtDate.setText(howOften);

            if (model.when.equals(context.getString(R.string.now))) {
                holder.txtDate.setText(model.when);
            } else if (model.when.equals(context.getString(R.string.today))) {
                holder.txtDate.setText(model.when + " at " + model.time);
            } else {
                //binding.when.setText(jobRes.time + " on " + jobRes.date);
                holder.txtDate.setText(model.date);
            }

            if (model.address != null)
                // holder.tvAddress.setText(model.address.get(0).street);
                holder.tvAddress.setText(model.address.street);

            if (model.bidAvailable && model.statusOfJob == 1) {
                holder.assignedRelativeLayout.setVisibility(View.GONE);
                holder.btnViewBids.setVisibility(View.VISIBLE);
                holder.viewBids.setVisibility(View.VISIBLE);
                holder.btnEditJob.setVisibility(View.VISIBLE);
                holder.btnCancelJob.setVisibility(View.VISIBLE);
                holder.jobTypeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                setTextColorBlack(holder);
                // Change Bg of buttons
                holder.btnViewBids.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnViewBids.setTextColor(context.getResources().getColor(R.color.colorWhite));
                //holder.btnMessage.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnCancelJob.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                //holder.btnMessage.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.btnCancelJob.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.btnMessage.setVisibility(View.GONE);
                hidePaymentButton(holder);
                if (model.jobType.equalsIgnoreCase("Fixed Price")) {
                    holder.estimatedTimeTextView.setText("Est");
                } else {
                    holder.estimatedTimeTextView.setText("Estimated Time");
                }
            }
            // For Create Job
            else if (model.statusOfJob == 1) {

                holder.assignedRelativeLayout.setVisibility(View.GONE);

                if (model.jobType.equalsIgnoreCase("Fixed Price")) {
                    holder.estimatedTimeTextView.setText("Est");
                } else {
                    holder.estimatedTimeTextView.setText("Estimated Time");
                }

                holder.jobTypeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                setTextColorBlack(holder);
                holder.btnCancelJob.setVisibility(View.VISIBLE);
                holder.btnEditJob.setVisibility(View.VISIBLE);
                //  holder.btnMessage.setVisibility(View.VISIBLE);
                holder.viewEditJob.setVisibility(View.VISIBLE);
                holder.viewMsg.setVisibility(View.VISIBLE);

                //ch
                holder.btnMessage.setVisibility(View.GONE);
                hideBids(holder);

                // Change Bg of buttons
                holder.btnMessage.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnCancelJob.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnEditJob.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnCancelJob.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.btnMessage.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.btnEditJob.setTextColor(context.getResources().getColor(R.color.colorWhite));

                holder.txtAssigned.setVisibility(View.GONE);
                holder.txtProviderName.setVisibility(View.GONE);
                holder.ivProvider.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.GONE);

                /**
                 * Hide Payment Button
                 */

                hidePaymentButton(holder);
//TODO (show data from api:Done already) Problem: when job is created there is null value for provider Name,img so assigned data is not shown due to this there is an crash in Payment module.The problem is pic n name is not updating when push notification comes
                // Show Offered To UI
                if (model.assignedTo.providerName != null) {
                    holder.assignedRelativeLayout.setVisibility(View.VISIBLE);
                    showAssignedTo(holder, model, "Offered To");
                    if (model.assignedTo.providerImg != null && !model.assignedTo.providerImg.isEmpty()) {
                        holder.cardView.setVisibility(View.VISIBLE);
                        holder.ivProvider.setVisibility(View.VISIBLE);
                        Glide.with(context).load(model.assignedTo.providerImg).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                                .centerCrop()).into(holder.ivProvider);
                    } else {
                        Glide.with(context).load(R.drawable.ic_person_black_24dp).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                                .centerCrop()).into(holder.ivProvider);
                    }
                }

            }
            // Job Assigned or Accepted
            else if (model.statusOfJob == 2) {
                holder.assignedRelativeLayout.setVisibility(View.VISIBLE);
                // Change the text
                showAssignedTo(holder, model, "Assigned To");
                holder.btnEditJob.setVisibility(View.GONE);
                holder.viewEditJob.setVisibility(View.VISIBLE);
                holder.btnMessage.setVisibility(View.VISIBLE);
                //
                holder.btnPay.setVisibility(View.GONE);
                holder.estimatedTimeTextView.setText("Booked Time (Hours)");
                holder.btnCancelJob.setVisibility(View.VISIBLE);
                //  holder.viewYourPrice.setVisibility(View.GONE);
                holder.btnMessage.setOnClickListener(v -> Toast.makeText(context, context.getString(R.string.functionality_pending), Toast.LENGTH_SHORT).show());
                /**
                 * Hide Payment Button
                 */
                hidePaymentButton(holder);

            } else if (model.statusOfJob == 3) {
                holder.assignedRelativeLayout.setVisibility(View.VISIBLE);
                showAssignedTo(holder, model, "Started By");
                holder.estimatedTimeTextView.setText("Booked Time (Hours)");

                holder.jobTypeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));

                holder.btnEditJob.setVisibility(View.VISIBLE);
                setTextColorBlack(holder);
                showBids(holder);
                // Change Bg of buttons
                holder.btnViewBids.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnViewBids.setTextColor(context.getResources().getColor(R.color.colorWhite));

                /**
                 *
                 *
                 * 1. HIde Edit Button
                 */
                holder.btnEditJob.setVisibility(View.GONE);

                /**
                 *
                 *
                 * Hide view bid button
                 */

                holder.btnViewBids.setVisibility(View.GONE);

                /**
                 *
                 * Hide Cancel Job button
                 */

                holder.btnCancelJob.setVisibility(View.GONE);


                /**
                 *
                 *
                 * 5. Show mMessage button
                 */

                holder.btnMessage.setVisibility(View.VISIBLE);

                holder.btnMessage.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnMessage.setTextColor(context.getResources().getColor(R.color.colorWhite));

                /**
                 * Hide Payment Button
                 */

                hidePaymentButton(holder);

            }
            // Status 4 is for job completed(Show Message and Payment buttons)
            else if (model.statusOfJob == 4) {
                holder.assignedRelativeLayout.setVisibility(View.VISIBLE);
                showAssignedTo(holder, model, "Completed By");
                holder.estimatedTimeTextView.setText("Booked Time (Hours)");
                // Show Payment option here
                holder.btnPay.setVisibility(View.VISIBLE);
                holder.viewPay.setVisibility(View.VISIBLE);
                holder.jobTypeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                setTextColorBlack(holder);

                holder.btnEditJob.setVisibility(View.GONE);
                holder.viewEditJob.setVisibility(View.GONE);
                holder.btnMessage.setVisibility(View.VISIBLE);
                holder.btnCancelJob.setVisibility(View.GONE);
                hideBids(holder);


                // Change Bg of buttons
                holder.btnViewBids.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.viewEditJob.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnMessage.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.btnPay.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                // holder.btnViewBids.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.btnPay.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.btnMessage.setTextColor(context.getResources().getColor(R.color.colorWhite));

            } else {
                holder.jobTypeLayout.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                setTextColorBlack(holder);
                hideBids(holder);

            }

            /*
              Set title in last so that status can be appended to it.
             */
            holder.txtTitle.setText(jobCleaningType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBidBookedPrice(ViewHolder holder, String mPrice) {
        holder.estimatedPrice.setText(mPrice);
    }

    private void setPrice(ViewHolder holder, String mPrice) {
        holder.estimatedPrice.setText(mPrice);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }


    private void hidePaymentButton(@NonNull ViewHolder holder) {
        /**
         * Hide Payment Button
         */

        holder.btnPay.setVisibility(View.GONE);
        holder.viewPay.setVisibility(View.GONE);
    }

    private void showAssignedTo(@NonNull ViewHolder holder, GetJobRes model, String text) {
        holder.estPrice.setText("Booked Price");
        hideBids(holder);

        // jobCleaningType = jobCleaningType + " (Assigned)"; // W bf
        //jobCleaningType = jobCleaningType;
        holder.txtAssigned.setVisibility(View.VISIBLE);

        if (text != null) {
            holder.txtAssigned.setText(text);
        } else {
            holder.txtAssigned.setText("Assigned To");
        }


        Log.d("NAMe==", "==" + model.assignedTo.providerName);

        if (model.assignedTo.providerName != null && !model.assignedTo.providerName.isEmpty()) {
            holder.txtProviderName.setVisibility(View.VISIBLE);
            holder.txtProviderName.setText(model.assignedTo.providerName);
        }
        if (model.assignedTo.providerImg != null && !model.assignedTo.providerImg.isEmpty()) {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.ivProvider.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.assignedTo.providerImg).apply(new RequestOptions().placeholder(R.drawable.ic_person_black_24dp)
                    .centerCrop()).into(holder.ivProvider);
        } else {

            /*
              TODO
              This will never reach here

              Test it and remove
             */

            if (model.assignedTo.providerName != null && !model.assignedTo.providerName.isEmpty()) {
                holder.cardView.setVisibility(View.VISIBLE);
                holder.ivProvider.setVisibility(View.VISIBLE);
            }
        }
    }


    private void setTextColorBlack(ViewHolder holder) {
        holder.txtTitle.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        holder.tvTaskDetail.setTextColor(Color.parseColor("#000000"));
        holder.tvAddress.setTextColor(Color.parseColor("#000000"));
        holder.estimatedTimeTextView.setTextColor(Color.parseColor("#000000"));
        holder.estimatedTime.setTextColor(Color.parseColor("#000000"));
        holder.estimatedPrice.setTextColor(Color.parseColor("#000000"));
        holder.estPrice.setTextColor(Color.parseColor("#000000"));
        holder.imgAddress.setColorFilter(ContextCompat.getColor(context, R.color.primary_text));
    }

    private void hideBids(ViewHolder holder) {
        holder.btnViewBids.setVisibility(View.GONE);
        holder.viewBids.setVisibility(View.GONE);
    }

    private void showBids(ViewHolder holder) {
        holder.btnViewBids.setVisibility(View.VISIBLE);
        holder.viewBids.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return mGetJobsList.size();
    }


    public void updateFragments() {
        mObservers.notifyObservers();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView txtTitle, txtAssigned, txtProviderName;
        // AppCompatTextView txtMessage;
        AppCompatTextView txtDate;
        AppCompatTextView tvTaskDetail;
        AppCompatTextView tvAddress;
        AppCompatTextView estimatedTime, estPrice;
        AppCompatTextView estimatedPrice;
        AppCompatTextView yourPrice;
        AppCompatButton btnViewBids;
        AppCompatButton btnCancelJob;
        AppCompatButton btnEditJob;
        AppCompatButton btnMessage;
        AppCompatButton btnPay;
        //AppCompatButton btnRating;
        View viewBids;
        View viewEditJob, viewPay, viewMsg, viewYourPrice;
        LinearLayout estimationLayout;
        LinearLayout yourPriceLayout;
        AppCompatTextView estimatedTimeTextView;
        RelativeLayout jobTypeLayout;
        ImageView imgAddress, ivProvider;
        CardView cardView;
        CardView assignedRelativeLayout;

        ViewHolder(View itemView) {
            super(itemView);

            jobTypeLayout = itemView.findViewById(R.id.jobTypeLayout);
            imgAddress = itemView.findViewById(R.id.imgAddress);
            ivProvider = itemView.findViewById(R.id.ivProvider);
            cardView = itemView.findViewById(R.id.cardView);

            txtTitle = itemView.findViewById(R.id.tvCleaningType);
            txtAssigned = itemView.findViewById(R.id.txtAssigned);// txtAssigned
            txtProviderName = itemView.findViewById(R.id.txtProviderName);// Provider Name
            txtDate = itemView.findViewById(R.id.tvTime);
            tvTaskDetail = itemView.findViewById(R.id.tvTaskDetail);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnViewBids = itemView.findViewById(R.id.btnViewBid);


            estimatedTime = itemView.findViewById(R.id.estJobTime);
            estPrice = itemView.findViewById(R.id.estPrice);//M1

            estimatedPrice = itemView.findViewById(R.id.estJobPrice);

            yourPrice = itemView.findViewById(R.id.yourJobPrice);

            btnCancelJob = itemView.findViewById(R.id.btnCancelJob);
            btnEditJob = itemView.findViewById(R.id.btnEditJob);
            btnMessage = itemView.findViewById(R.id.btnMessage);
            viewBids = itemView.findViewById(R.id.view_viewbids);
            viewEditJob = itemView.findViewById(R.id.viewEditJob);
            viewMsg = itemView.findViewById(R.id.viewMsg);
            viewYourPrice = itemView.findViewById(R.id.priceView2);//

            btnPay = itemView.findViewById(R.id.btnPay);
            // btnRating = itemView.findViewById(R.id.btnRating);

            estimationLayout = itemView.findViewById(R.id.estimationLayout);
            yourPriceLayout = itemView.findViewById(R.id.yourPriceLayout);


            viewPay = itemView.findViewById(R.id.viewPay);//


            estimatedTimeTextView = itemView.findViewById(R.id.estimatedTimeTextView);

            assignedRelativeLayout = itemView.findViewById(R.id.relAssigned);

            /*Rating*/
           /* btnRating.setOnClickListener(v ->
                    clientJobsClickCallback.onRatingClick(mGetJobsList.get(getLayoutPosition())));*/


            /* Payment*/
           /* btnPay.setOnClickListener(v ->
                    clientJobsClickCallback.onPayJobClick(mGetJobsList.get(getLayoutPosition())));*/
            btnPay.setOnClickListener(v ->
                    clientJobsClickCallback.onPayJobClickWithPosition(mGetJobsList.get(getLayoutPosition()), getLayoutPosition()));

            /* View Bids */
            btnViewBids.setOnClickListener(v ->

                    clientJobsClickCallback.onViewBids(mGetJobsList.get(getLayoutPosition())));

            btnCancelJob.setOnClickListener(v -> clientJobsClickCallback.onCancelJobClick(mGetJobsList.get(getLayoutPosition())));

            btnEditJob.setOnClickListener(v -> clientJobsClickCallback.onEditJobClickWithPosition(mGetJobsList.get(getLayoutPosition()), getLayoutPosition()));

        }
    }
}


