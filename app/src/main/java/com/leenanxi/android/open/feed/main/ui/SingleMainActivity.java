package com.leenanxi.android.open.feed.main.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.broadcast.ui.BroadcastListFragment;
import com.leenanxi.android.open.feed.notification.ui.NotificationListFragment;
import com.leenanxi.android.open.feed.settings.ui.SettingsActivity;
import com.leenanxi.android.open.feed.simple.SimpleRecyclerViewFragment;
import com.leenanxi.android.open.feed.widget.ActionItemBadge;
import com.leenanxi.android.open.feed.widget.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SingleMainActivity  extends MainActivity {
    private MenuItem mNotificationMenu;
    private int mUnreadNotificationCount;

    @Override
    protected void setContentViews(Bundle savedInstanceState) {
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (viewPager != null) {
            TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
            adapter.addTab(BroadcastListFragment.newInstance(), "消息");
            adapter.addTab(new SimpleRecyclerViewFragment(), "办公");
            adapter.addTab(new Fragment(), "通讯录");
            adapter.addTab(new Fragment(), "动态");
            viewPager.setAdapter(adapter);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {

                        @Override
                        public boolean onNavigationItemSelected(MenuItem item) {
                            // Handle navigation view item clicks here.
                            int id = item.getItemId();

                            if (id == R.id.navigation_settings) {
                                startActivity(SettingsActivity.makeIntent(SingleMainActivity.this));
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
        super.setContentViews(savedInstanceState);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setDrawerLayout(drawer);
        return;
    }






}
