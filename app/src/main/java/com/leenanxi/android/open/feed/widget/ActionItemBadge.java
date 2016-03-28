package com.leenanxi.android.open.feed.widget;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.CheatSheetUtils;
import com.leenanxi.android.open.feed.util.ViewCompat;
import com.leenanxi.android.open.feed.util.ViewUtils;

public class ActionItemBadge {
    public static void setup(final MenuItem menuItem, Drawable icon, int count,
                             final Activity activity) {
        View actionView = menuItem.getActionView();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, menuItem);
            }
        });
        CharSequence title = menuItem.getTitle();
        if (!TextUtils.isEmpty(title)) {
            CheatSheetUtils.setup(actionView, title);
        }
        ImageView iconImage = (ImageView) actionView.findViewById(R.id.icon);
        iconImage.setImageDrawable(icon);
        TextView badgeText = (TextView) actionView.findViewById(R.id.badge);
        badgeText.setTextColor(ViewUtils.getColorFromAttrRes(R.attr.colorPrimary, 0, activity));
        ViewCompat.setBackground(badgeText, new BadgeDrawable());
        update(badgeText, count);
    }

    public static void setup(MenuItem menuItem, int iconResId, int count, Activity activity) {
        setup(menuItem, ContextCompat.getDrawable(activity, iconResId), count, activity);
    }

    private static void update(TextView badgeText, int count) {
        ViewUtils.setVisibleOrInvisible(badgeText, count != 0);
        badgeText.setText(String.valueOf(count));
    }

    public static void update(MenuItem menuItem, int count) {
        TextView badgeView = (TextView) menuItem.getActionView().findViewById(R.id.badge);

        update(badgeView, count);
    }

    public static class BadgeDrawable extends GradientDrawable {
        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            super.setBounds(left, top, right, bottom);
            setCornerRadius(Math.min(right - left, bottom - top));
        }
    }
}
