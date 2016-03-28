package com.leenanxi.android.open.feed.broadcast.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.util.ViewUtils;

public class SingleBroadcastAdapter
        extends RecyclerView.Adapter<SingleBroadcastAdapter.ViewHolder> {
    private OnActionListener mOnActionListener;
    private Broadcast mBroadcast;

    public SingleBroadcastAdapter(Broadcast broadcast, OnActionListener listener) {
        mBroadcast = broadcast;
        mOnActionListener = listener;
        setHasStableIds(true);
    }

    public Broadcast getBroadcast() {
        return mBroadcast;
    }

    public void setBroadcast(Broadcast broadcast) {
        // Don't check for whether mBroadcast == broadcast because we always want to invalidate.
        Broadcast oldBroadcast = mBroadcast;
        mBroadcast = broadcast;
        if (oldBroadcast == null) {
            notifyItemInserted(0);
        } else if (mBroadcast == null) {
            notifyItemRemoved(0);
        } else {
            notifyItemChanged(0);
        }
    }

    public boolean hasBroadcast() {
        return mBroadcast != null;
    }

    public void notifyBroadcastChanged() {
        notifyItemChanged(0);
    }

    @Override
    public int getItemCount() {
        return mBroadcast != null ? 1 : 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.single_broadcast_item, parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.broadcastLayout.bindBroadcast(mBroadcast);
        holder.broadcastLayout.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnActionListener.onLike(!mBroadcast.liked)) {
                    holder.broadcastLayout.mLikeButton.setEnabled(false);
                }
            }
        });
        holder.broadcastLayout.mRebroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnActionListener.onRebroadcast(!mBroadcast.isRebroadcasted())) {
                    holder.broadcastLayout.mRebroadcastButton.setEnabled(false);
                }
            }
        });
        holder.broadcastLayout.mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnActionListener.onComment();
            }
        });
        holder.viewActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnActionListener.onViewActivity();
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.broadcastLayout.releaseBroadcast();
    }

    public interface OnActionListener {
        boolean onLike(boolean like);

        boolean onRebroadcast(boolean rebroadcast);

        void onComment();

        void onViewActivity();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public BroadcastLayout broadcastLayout;
        public Button viewActivityButton;

        public ViewHolder(View itemView) {
            super(itemView);
            broadcastLayout = (BroadcastLayout) itemView.findViewById(R.id.broadcast);
            viewActivityButton = (Button) itemView.findViewById(R.id.view_activity);
        }
    }
}
