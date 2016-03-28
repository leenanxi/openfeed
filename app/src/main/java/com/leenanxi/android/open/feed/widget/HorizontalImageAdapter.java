package com.leenanxi.android.open.feed.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Image;
import com.leenanxi.android.open.feed.util.ViewUtils;

public class HorizontalImageAdapter
        extends SimpleAdapter<Image, HorizontalImageAdapter.ViewHolder> {
    private OnImageClickListener mOnImageClickListener;

    public HorizontalImageAdapter() {
        setHasStableIds(true);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mOnImageClickListener = listener;
    }

    @Override
    public long getItemId(int position) {
        // Deliberately using plain hash code to identify only this instance.
        return getItem(position).hashCode();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.horizontal_image_item, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageLayout.loadImage(getItem(position));
        holder.imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onImageClick(position);
                }
            }
        });
        ViewUtils.setVisibleOrGone(holder.dividerSpace, position != getItemCount() - 1);
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageLayout imageLayout;
        public Space dividerSpace;

        public ViewHolder(View itemView) {
            super(itemView);
            imageLayout = (ImageLayout) itemView.findViewById(R.id.image);
            dividerSpace = (Space) itemView.findViewById(R.id.divider);
        }
    }
}
