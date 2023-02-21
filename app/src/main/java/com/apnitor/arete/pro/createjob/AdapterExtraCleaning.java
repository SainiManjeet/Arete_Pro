package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.interfaces.FindJobClickCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterExtraCleaning extends RecyclerView.Adapter<AdapterExtraCleaning.ViewHolder> {

    private Context context;
    private List<ExtraCleaningRes> extraCleaningRes;

    AdapterExtraCleaning(Context context, List<ExtraCleaningRes> extraCleaningRes, FindJobClickCallback findJobClickCallback) {
        this.context = context;
        this.extraCleaningRes = extraCleaningRes;
        FindJobClickCallback findJobClickCallback1 = findJobClickCallback;
    }

    @NonNull
    @Override
    public AdapterExtraCleaning.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_extra_cleaning, parent, false);
        //String mName = PreferenceHandler.readString(context, PreferenceHandler.PREF_EXTRA_CLEAN, "");
        return new AdapterExtraCleaning.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override

    public void onBindViewHolder(@NonNull AdapterExtraCleaning.ViewHolder holder, int position) {
        try {
            ExtraCleaningRes model = extraCleaningRes.get(position);
            if (model.description.isEmpty()) {
                holder.txtDescription.setVisibility(View.GONE);
            } else {
                holder.txtDescription.setVisibility(View.VISIBLE);
            }
            holder.txtName.setText(model.name);
            holder.txtDescription.setText(model.description);

            if (model.name.equalsIgnoreCase("Laundry")) {
                holder.txtTime.setText(model.extraTime + " Hour per " + model.name);
            } else {
                holder.txtTime.setText(model.extraTime + " Min per " + model.name);
            }
            holder.imgItem.setImageResource(model.isSelected ? model.imageSelected : model.image);
            //holder.imgTime.setImageResource(model.isSelected ? model.imageTime : model.imageTime);//
            holder.txtName.setTextColor(model.isSelected ? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.secondary_text));
            holder.txtDescription.setTextColor(model.isSelected ? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.secondary_text));
            holder.txtTime.setTextColor(model.isSelected ? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.secondary_text));
            if (model.isSelected) {
                holder.imgTime.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.imgTime.setColorFilter(ContextCompat.getColor(context, R.color.secondary_text), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return extraCleaningRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imgItem, imgTime;
        AppCompatTextView txtName;
        AppCompatTextView txtDescription;
        CardView extraItem;
        AppCompatTextView txtTime;

        ViewHolder(View itemView) {
            super(itemView);
            extraItem = itemView.findViewById(R.id.carExtras);
            imgItem = itemView.findViewById(R.id.imgItems);
            imgTime = itemView.findViewById(R.id.imgTime);// Time
            txtName = itemView.findViewById(R.id.tvName);
            txtDescription = itemView.findViewById(R.id.tvDescription);
            txtTime = itemView.findViewById(R.id.tvTime);

            extraItem.setOnClickListener(v -> {
                ExtraCleaningRes model = extraCleaningRes.get(getAdapterPosition());
                model.isSelected = !model.isSelected;
                imgItem.setImageResource(model.isSelected ? model.imageSelected : model.image);

                //  imgTime.setImageResource(model.isSelected ? model.imageTime : model.imageTime);

                txtName.setTextColor(model.isSelected ? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.secondary_text));
                txtDescription.setTextColor(model.isSelected ? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.secondary_text));
                txtTime.setTextColor(model.isSelected ? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.secondary_text));
                if (model.isSelected) {
                    imgTime.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else {
                    imgTime.setColorFilter(ContextCompat.getColor(context, R.color.mdtp_light_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
            /*extraItem = (CardView) itemView.findViewById(R.id.carExtras);
            imgItem = (AppCompatImageView) itemView.findViewById(R.id.imgItems);
            txtName = (AppCompatTextView) itemView.findViewById(R.id.tvName);
            txtDescription =  itemView.findViewById(R.id.tvDescription);
            txtTime =  itemView.findViewById(R.id.tvTime);

*/

                // Log.e("ADAPTER", "ViewHolder****=" + mName+"==Pos="+getAdapterPositi
                //
                // on());
          /*  if (getAdapterPosition()==0&&mName.equalsIgnoreCase("Keep Clean")){
                //holder.imgItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
               txtName.setTextColor(ContextCompat.getColor(context, R.color.colorButton));
            }
            if (getAdapterPosition()==1 && mName.equalsIgnoreCase("Inside Cabinets")){
                //  holder.imgItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlack));
               txtName.setTextColor(ContextCompat.getColor(context, R.color.redColor));
            }*/

            });
        }
    }
}