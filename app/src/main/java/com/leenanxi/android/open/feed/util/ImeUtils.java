package com.leenanxi.android.open.feed.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ImeUtils {
    private ImeUtils() {
    }

    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void showIme(View view) {
        getInputMethodManager(view.getContext()).showSoftInput(view, 0);
    }

    public static void hideIme(View view) {
        getInputMethodManager(view.getContext()).hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
