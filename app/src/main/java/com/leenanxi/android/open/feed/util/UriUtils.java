package com.leenanxi.android.open.feed.util;

import android.net.Uri;

public class UriUtils {
    public static long parseId(Uri uri) {
        String last = uri.getLastPathSegment();
        return last == null ? -1 : Long.parseLong(last);
    }

    public static Uri.Builder appendId(Uri.Builder builder, long id) {
        return builder.appendEncodedPath(String.valueOf(id));
    }

    public static Uri withAppendedId(Uri contentUri, long id) {
        return appendId(contentUri.buildUpon(), id).build();
    }
}
