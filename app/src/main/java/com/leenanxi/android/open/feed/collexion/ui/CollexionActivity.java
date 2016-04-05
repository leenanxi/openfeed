package com.leenanxi.android.open.feed.collexion.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.broadcast.ui.BroadcastActivity;
import com.leenanxi.android.open.feed.widget.SinglePaneActivity;

/**
 * Created by leenanxi on 16/4/1.
 */
public class CollexionActivity extends SinglePaneActivity{
    private static final String KEY_PREFIX = CollexionActivity.class.getName() + '.';
    public static final String EXTRA_ITEM = KEY_PREFIX + "item";

    @Override
    protected Fragment onCreatePane() {
        return new CollexionFragment();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.main_single_pane_empty_activity;
    }


    public static Intent makeIntent(ItemData data, Context context) {
        return new Intent(context, CollexionActivity.class)
                .putExtra(CollexionActivity.EXTRA_ITEM, data);
    }

}
