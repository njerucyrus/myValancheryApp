package com.hudutech.mymanjeri.adapters.majery_adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hudutech.mymanjeri.GetTimeAgo;
import com.hudutech.mymanjeri.R;
import com.hudutech.mymanjeri.majery_activities.CustomNotificationDetailActivity;
import com.hudutech.mymanjeri.models.majery_models.CustomNotification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<CustomNotification> customNotificationList;
    private Context mContext;

    public NotificationAdapter(Context mContext, List<CustomNotification> customNotificationList) {
        this.customNotificationList = customNotificationList;
        this.mContext = mContext;

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CustomNotification customNotification = customNotificationList.get(position);

        long timestamp = customNotification.getDate().getTime();
        String timeAgo = GetTimeAgo.getTimeAgo(timestamp, mContext);

        holder.mDate.setText(timeAgo);
        holder.mNotifContent.setText(customNotification.getNotification());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CustomNotificationDetailActivity.class)
                        .putExtra("customNotification", customNotification)
                );
            }
        });


    }


    @Override
    public int getItemCount() {
        return customNotificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNotifContent;
        TextView mDate;
        View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mNotifContent = itemView.findViewById(R.id.tv_notification_content);
            mDate = itemView.findViewById(R.id.tv_notification_date);

        }


    }

}