package com.leenanxi.android.open.feed.main.ui;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements NotificationListFragment.UnreadNotificationCountListener {
    private MenuItem mNotificationMenu;
    private int mUnreadNotificationCount;
    private NotificationListFragment mNotificationListFragment;


    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public NotificationListFragment getNotificationListFragment() {
        return mNotificationListFragment;
    }

    private DrawerLayout mDrawerLayout;

    protected void setNotificationListFragment(NotificationListFragment mNotificationListFragment) {
        this.mNotificationListFragment = mNotificationListFragment;
    }

    protected void setDrawerLayout(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
        if (!AccountUtils.ensureAccountAvailability(this)) {
            return;
        }
        setContentViews(savedInstanceState);
    }

    protected void setContentViews(@Nullable Bundle savedInstanceState) {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
            ((ImageView) headerView.findViewById(R.id.avatar)).setVisibility(View.VISIBLE);
            ((TextView) headerView.findViewById(R.id.name)).setText(AccountUtils.getUserName(this));
           // ((TextView) headerView.findViewById(R.id.tag)).setText(AccountUtils.getActiveAccount(this).toString());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mNotificationMenu = menu.findItem(R.id.action_notification);
        ActionItemBadge.setup(mNotificationMenu, R.drawable.md_ic_notifications_white_24dp,
                mUnreadNotificationCount, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                mNotificationListFragment.refresh();
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onUnreadNotificationUpdate(int count) {
        mUnreadNotificationCount = count;
        if (mNotificationMenu != null) {
            ActionItemBadge.update(mNotificationMenu, mUnreadNotificationCount);
        }
    }

    public void refreshNotificationList() {
        mNotificationListFragment.refresh();
    }
}

