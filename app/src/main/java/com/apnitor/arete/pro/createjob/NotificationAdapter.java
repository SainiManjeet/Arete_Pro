package com.apnitor.arete.pro.createjob;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.GetNotificationRes;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public List<GetNotificationRes> providerRes;
    private ListItemClickCallback listItemClickCallback;
    private Context context;

    public NotificationAdapter(Context context, List<GetNotificationRes> providerRes, ListItemClickCallback listItemClickCallback) {
        this.context = context;
        this.providerRes = providerRes;
        this.listItemClickCallback = listItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custom_notification, parent, false);

        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            GetNotificationRes model = providerRes.get(position);
            holder.mMessage.setText(model.message);

            String[] separated = model.createdOn.split("T");
            String mDate = separated[0];
            holder.mDate.setText(mDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return providerRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView mMessage, mDate;

        ViewHolder(View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.txtMessage);
            mDate = itemView.findViewById(R.id.txtDate);
            itemView.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onListItemClick(providerRes.get(getLayoutPosition()));
            });


        }
    }
}
