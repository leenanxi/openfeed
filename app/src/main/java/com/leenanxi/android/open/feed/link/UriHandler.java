package com.leenanxi.android.open.feed.link;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import com.leenanxi.android.open.feed.settings.contract.Settings;
import com.leenanxi.android.open.feed.util.UrlUtils;
import com.leenanxi.android.open.feed.util.customtabshelper.customtabsclient.CustomTabsActivityHelper;

public class UriHandler {
    private static final CustomTabsActivityHelper.CustomTabsFallback sFallback =
            new CustomTabsActivityHelper.CustomTabsFallback() {
                @Override
                public void openUri(Activity activity, Uri uri) {
                    open(uri, activity, false);
                }
            };

    private UriHandler() {
    }

    private static void open(Uri uri, Context context, boolean enableCustomTabs) {
        if (AppUriHandler.open(uri, context)) {
            return;
        }
        switch (Settings.OPEN_URL_WITH_METHOD.getEnumValue(context)) {
            case CUSTOM_TABS:
                if (enableCustomTabs && context instanceof Activity) {
                    UrlUtils.openWithCustomTabs(uri, sFallback, (Activity) context);
                    break;
                }
                // Fall through!
            case WEBVIEW:
                UrlUtils.openWithWebViewActivity(uri, context);
                break;
            case INTENT:
                UrlUtils.openWithIntent(uri, context);
                break;
        }
    }

    public static void open(String url, Context context) {
        open(Uri.parse(url), context, true);
    }
}
