package com.leenanxi.android.open.feed.util;

import com.leenanxi.android.open.feed.api.model.User;

public class DoubanUtils {
    private DoubanUtils() {
    }

    public static String getAtUserString(String idOrUid) {
        return '@' + idOrUid + ' ';
    }

    public static String getAtUserString(User user) {
        return getAtUserString(user.uid);
    }
}
