package com.apnitor.arete.pro.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.response.NotificationRes;
import com.apnitor.arete.pro.interfaces.ListItemClickCallback;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {

    private Context context;
    private List<NotificationRes> notificationRes;
    private ListItemClickCallback listItemClickCallback;

    AdapterNotification(Context context, List<NotificationRes> notificationRes, ListItemClickCallback listItemClickCallback) {
        this.context = context;
        this.notificationRes = notificationRes;
        this.listItemClickCallback = listItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_view_jobs, parent, false);
        return new AdapterNotification.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.ViewHolder holder, int position) {
        try {
            NotificationRes model = notificationRes.get(position);


            holder.txtTitle.setText(model.title);
            holder.txtMessage.setText(model.message);
            holder.txtDate.setText(model.date);
            holder.txtTime.setText(model.message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notificationRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView txtTitle;
        AppCompatTextView txtMessage;
        AppCompatTextView txtDate;
        AppCompatTextView txtTime;
        ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTaskDetail);

            txtDate = itemView.findViewById(R.id.tvDate);
            txtTime = itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(v -> {
                if (listItemClickCallback != null)
                    listItemClickCallback.onListItemClick(notificationRes.get(getLayoutPosition()));

            });

        }
    }
}