package com.leenanxi.android.open.feed.notification.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Notification;
import com.leenanxi.android.open.feed.link.UriHandler;
import com.leenanxi.android.open.feed.util.ImageUtils;
import com.leenanxi.android.open.feed.util.RecyclerViewUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;
import com.leenanxi.android.open.feed.widget.SimpleAdapter;
import com.leenanxi.android.open.feed.widget.TextCircleImageView;
import com.leenanxi.android.open.feed.widget.TimeTextView;

import java.util.List;

public class NotificationAdapter extends SimpleAdapter<Notification,
        NotificationAdapter.ViewHolder> {

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        super(notificationList);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getList().get(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.notification_item, parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = RecyclerViewUtils.getContext(holder);
        final Notification notification = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNotificationAsRead(notification);
                UriHandler.open(notification.targetUri, context);
            }
        });
        holder.textText.setText(notification.text);
        holder.timeText.setDoubanTime(notification.time);
        holder.titleText.setText(notification.title);
        ImageUtils.loadAvatar(holder.image,notification.image,context);
    }

    private void markNotificationAsRead(Notification notification) {
        notification.read = true;
        notifyItemChangedById(notification.id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextCircleImageView image;
        public TextView titleText;
        public TextView textText;
        public TimeTextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (TextCircleImageView) itemView.findViewById(R.id.image);
            titleText = (TextView) itemView.findViewById(R.id.title);
            textText = (TextView) itemView.findViewById(R.id.text);
            timeText = (TimeTextView) itemView.findViewById(R.id.time);
        }
    }
}
