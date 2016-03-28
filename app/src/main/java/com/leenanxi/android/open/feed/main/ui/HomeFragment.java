package com.leenanxi.android.open.feed.main.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.broadcast.ui.BroadcastListFragment;
import com.leenanxi.android.open.feed.link.UriHandler;
import com.leenanxi.android.open.feed.simple.SimpleRecyclerViewFragment;
import com.leenanxi.android.open.feed.widget.TabFragmentPagerAdapter;

public class HomeFragment extends Fragment {
    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    private TabFragmentPagerAdapter mTabAdapter;
    private FloatingActionButton mFab;

    public HomeFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_coordinator_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        MainActivity activity = (MainActivity) getActivity();
        activity.setToolbar(mToolbar);
        mTabAdapter = new TabFragmentPagerAdapter(getChildFragmentManager());
        mTabAdapter.addTab(new SimpleRecyclerViewFragment(), getString(R.string.home_broadcast));
        mTabAdapter.addTab(new Fragment(), getString(R.string.home_nine_and_quater));
        mTabAdapter.addTab(new Fragment(), getString(R.string.home_discover));
        mTabAdapter.addTab(BroadcastListFragment.newInstance(), getString(R.string.home_online));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendBroadcast();
            }
        });
    }


    private void onSendBroadcast() {
        // FIXME: Create a SendBroadcastActivity.
        UriHandler.open("https://www.douban.com/#isay-cont", getActivity());
    }
}
