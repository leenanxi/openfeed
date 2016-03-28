package com.leenanxi.android.open.feed.profile.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.api.model.Image;
import com.leenanxi.android.open.feed.api.model.Photo;
import com.leenanxi.android.open.feed.broadcast.ui.BroadcastActivity;
import com.leenanxi.android.open.feed.util.ContentStateLayout;
import com.leenanxi.android.open.feed.util.ImageUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;
import com.leenanxi.android.open.feed.widget.FriendlyCardView;
import com.leenanxi.android.open.feed.widget.TimeActionTextView;

import java.util.List;

public class ProfileBroadcastsLayout extends FriendlyCardView {
    private static final int BROADCAST_COUNT_MAX = 3;
    TextView mTitleText;
    ContentStateLayout mContentStateLayout;
    LinearLayout mBroadcastList;
    TextView mViewAllText;

    public ProfileBroadcastsLayout(Context context) {
        super(context);
        init();
    }

    public ProfileBroadcastsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProfileBroadcastsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.profile_broadcasts_layout, this);
        mTitleText = (TextView) findViewById(R.id.title);
        mContentStateLayout = (ContentStateLayout) findViewById(R.id.contentState);
        mBroadcastList = (LinearLayout) findViewById(R.id.broadcast_list);
        mViewAllText = (TextView) findViewById(R.id.view_all);
    }

    public void setLoading() {
        mContentStateLayout.setLoading();
    }

    public void bind(final String userIdOrUid, List<Broadcast> broadcastList) {
        final Context context = getContext();
        View.OnClickListener viewAllListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(BroadcastListActivity.makeIntent(userIdOrUid));
            }
        };
        mTitleText.setOnClickListener(viewAllListener);
        mViewAllText.setOnClickListener(viewAllListener);
        int i = 0;
        for (final Broadcast broadcast : broadcastList) {
            if (i >= BROADCAST_COUNT_MAX) {
                break;
            }
            if (TextUtils.isEmpty(broadcast.text) || broadcast.isRebroadcasted()) {
                continue;
            }
            if (i >= mBroadcastList.getChildCount()) {
                LayoutInflater.from(context)
                        .inflate(R.layout.profile_broadcast_item, mBroadcastList);
            }
            View broadcastLayout = mBroadcastList.getChildAt(i);
            BroadcastLayoutHolder holder = (BroadcastLayoutHolder) broadcastLayout.getTag();
            if (holder == null) {
                holder = new BroadcastLayoutHolder(broadcastLayout);
                broadcastLayout.setTag(holder);
            }
            // HACK: Should not change on rebind.
            if (holder.boundBroadcastId != broadcast.id) {
                String imageUrl = null;
                if (broadcast.attachment != null) {
                    imageUrl = broadcast.attachment.image;
                }
                if (TextUtils.isEmpty(imageUrl)) {
                    List<Image> images = broadcast.images.size() > 0 ? broadcast.images
                            : Photo.toImageList(broadcast.photos);
                    if (images.size() > 0) {
                        imageUrl = images.get(0).medium;
                    }
                }
                ViewUtils.setVisibleOrGone(holder.image, !TextUtils.isEmpty(imageUrl));
                ImageUtils.loadImage(holder.image, imageUrl, context);
                holder.textText.setText(broadcast.getTextWithEntities(context));
                holder.timeActionText.setDoubanTimeAndAction(broadcast.createdAt, broadcast.action);
                broadcastLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(BroadcastActivity.makeIntent(broadcast, context));
                    }
                });
                holder.boundBroadcastId = broadcast.id;
            }
            ++i;
        }
        mContentStateLayout.setLoaded(i != 0);
        for (int count = mBroadcastList.getChildCount(); i < count; ++i) {
            ViewUtils.setVisibleOrGone(mBroadcastList.getChildAt(i), false);
        }
    }

    public void setError() {
        mContentStateLayout.setError();
    }

    static class BroadcastLayoutHolder {
        public ImageView image;
        public TextView textText;
        public TimeActionTextView timeActionText;

        public long boundBroadcastId;

        public BroadcastLayoutHolder(View itemView) {
            image = (ImageView) itemView.findViewById(R.id.image);
            textText = (TextView) itemView.findViewById(R.id.text);
            timeActionText = (TimeActionTextView) itemView.findViewById(R.id.time_action);
        }
    }
}
