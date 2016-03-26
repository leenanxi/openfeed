package com.leenanxi.android.open.feed.settings.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import com.leenanxi.android.open.feed.main.SinglePaneActivity;

public class SettingsActivity extends SinglePaneActivity {


    public static Intent makeIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }


    @Override
    protected Fragment onCreatePane() {
        return new SettingsFragment();
    }


}
