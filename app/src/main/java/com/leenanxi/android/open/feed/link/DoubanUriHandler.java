package com.leenanxi.android.open.feed.link;

import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import com.leenanxi.android.open.feed.broadcast.ui.BroadcastActivity;
import com.leenanxi.android.open.feed.profile.ui.ProfileActivity;
import com.leenanxi.android.open.feed.util.UriUtils;

public class DoubanUriHandler {
    private static final String AUTHORITY = "www.douban.com";
    private static final String AUTHORITY_FRODO = "douban.com";
    private static final UriMatcher MATCHER;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        for (UriType uriType : UriType.values()) {
            MATCHER.addURI(uriType.getAuthority(), uriType.getPath(), uriType.ordinal());
        }
    }

    private DoubanUriHandler() {
    }

    public static boolean open(Uri uri, Context context) {
        int code = MATCHER.match(uri);
        if (code == UriMatcher.NO_MATCH) {
            return false;
        }
        UriType uriType = UriType.values()[code];
        Intent intent;
        switch (uriType) {
            case BROADCAST:
            case BROADCAST_FRODO:
                intent = BroadcastActivity.makeIntent(UriUtils.parseId(uri), context);
                break;
            case USER:
                intent = ProfileActivity.makeIntent(uri.getLastPathSegment(), context);
                break;
            default:
                return false;
        }
        context.startActivity(intent);
        return true;
    }

    public static boolean open(String uri, Context context) {
        return open(Uri.parse(uri), context);
    }

    private enum UriType {
        BROADCAST("people/*/status/#"),
        BROADCAST_FRODO(AUTHORITY_FRODO, "status/#"),
        USER("people/*");
        String mAuthority;
        String mPath;

        UriType(String authority, String path) {
            mAuthority = authority;
            mPath = path;
        }

        UriType(String path) {
            this(AUTHORITY, path);
        }

        public String getAuthority() {
            return mAuthority;
        }

        public String getPath() {
            return mPath;
        }
    }
}
