package com.leenanxi.android.open.feed.settings.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.leenanxi.android.open.feed.widget.SinglePaneActivity;

public class SettingsActivity extends SinglePaneActivity {


    public static Intent makeIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }


    @Override
    protected Fragment onCreatePane() {
        return new SettingsFragment();
    }


}
