package com.leenanxi.android.open.feed.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.leenanxi.android.open.feed.util.ViewUtils;

public class StaticAdapter extends RecyclerView.Adapter<StaticAdapter.ViewHolder> {
    private int[] mViewReses;

    public StaticAdapter(int... viewReses) {
        mViewReses = viewReses;
        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return mViewReses.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(mViewReses[viewType], parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
