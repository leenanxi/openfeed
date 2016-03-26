package com.leenanxi.android.open.feed.util;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtils {
    public static void show(View view, CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void show(View view, int textRes) {
        show(view, view.getResources().getText(textRes));
    }
}
