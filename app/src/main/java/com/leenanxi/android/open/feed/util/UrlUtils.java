package com.leenanxi.android.open.feed.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.customtabshelper.CustomTabsHelperFragment;
import com.leenanxi.android.open.feed.util.customtabshelper.customtabsclient.CustomTabsActivityHelper;
import com.leenanxi.android.open.feed.widget.WebViewActivity;

public class UrlUtils {
    private UrlUtils() {
    }

    public static void openWithWebViewActivity(Uri uri, Context context) {
        context.startActivity(WebViewActivity.makeIntent(uri, context));
    }

    public static void openWithIntent(Uri uri, Context context) {
        Intent intent = IntentUtils.makeView(uri);
        AppUtils.startActivity(intent, context);
    }

    public static void openWithIntent(String url, Context context) {
        openWithIntent(Uri.parse(url), context);
    }

    public static void openWithCustomTabs(Uri uri,
                                          CustomTabsActivityHelper.CustomTabsFallback fallback,
                                          Activity activity) {
        CustomTabsHelperFragment.open(activity, makeCustomTabsIntent(activity), uri, fallback);
    }

    private static CustomTabsIntent makeCustomTabsIntent(Context context) {
        return new CustomTabsIntent.Builder()
                .enableUrlBarHiding()
                .setToolbarColor(ViewUtils.getColorFromAttrRes(R.attr.colorPrimary, 0, context))
                .setShowTitle(true)
                // TODO: Add share
                //.addMenuItem()
                .build();
    }
}
