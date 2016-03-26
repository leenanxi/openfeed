package com.leenanxi.android.open.feed.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewUtils {
    private RecyclerViewUtils() {
    }

    public static Context getContext(RecyclerView.ViewHolder holder) {
        return holder.itemView.getContext();
    }

    public static boolean hasFirstChildReachedTop(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        View firstChild = layoutManager.findViewByPosition(0);
        if (firstChild != null) {
            return firstChild.getTop() <= 0;
        } else {
            return layoutManager.getChildCount() > 0;
        }
    }
}
