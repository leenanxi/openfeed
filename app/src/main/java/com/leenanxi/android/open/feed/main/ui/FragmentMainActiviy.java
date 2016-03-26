package com.leenanxi.android.open.feed.main.ui;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.leenanxi.android.open.feed.BuildConfig;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.account.util.AccountUtils;
import com.leenanxi.android.open.feed.notification.ui.NotificationListFragment;
import com.leenanxi.android.open.feed.settings.ui.SettingsActivity;
import com.leenanxi.android.open.feed.widget.ActionItemBadge;

/**
 * Created by leenanxi on 16/3/26.
 */
public class FragmentMainActiviy extends MainActivity {

    @Override
    protected void setContentViews(Bundle savedInstanceState) {
        setContentView(R.layout.main_activity_fragment_frame);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setDrawerLayout(drawerLayout);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {

                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            int id = item.getItemId();
                            if (id == R.id.navigation_settings) {
                                startActivity(SettingsActivity.makeIntent(FragmentMainActiviy.this));

                            } else if (id == R.id.navigation_exit) {

                            } else if (id == R.id.navigation_feedback) {

                            }
                            getDrawerLayout().closeDrawer(GravityCompat.START);
                            return true;
                        }
                    });
        }

        NotificationListFragment notificationListFragment = (NotificationListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.notification_list_fragment);
        notificationListFragment.setUnreadNotificationCountListener(this);
        setNotificationListFragment(notificationListFragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }

        super.setContentViews(savedInstanceState);
    }




}
