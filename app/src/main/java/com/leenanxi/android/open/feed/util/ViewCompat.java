package com.leenanxi.android.open.feed.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class ViewCompat {
    private ViewCompat() {
    }

    public static void setBackground(View view, Drawable background) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            view.setBackgroundDrawable(background);
        } else {
            view.setBackground(background);
        }
    }
}
