package com.leenanxi.android.open.feed.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Image;
import com.leenanxi.android.open.feed.util.systemuihelper.SystemUiHelper;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private static final String KEY_PREFIX = GalleryActivity.class.getSimpleName() + '.';
    public static final String EXTRA_IMAGE_LIST = KEY_PREFIX + "image_list";
    public static final String EXTRA_POSITION = KEY_PREFIX + "position";
    int mToolbarHideDuration;
    Toolbar mToolbar;
    ViewPager mViewPager;
    private SystemUiHelper mSystemUiHelper;

    public static Intent makeIntent(ArrayList<Image> imageList, int position, Context context) {
        return new Intent(context, GalleryActivity.class)
                .putParcelableArrayListExtra(EXTRA_IMAGE_LIST, imageList)
                .putExtra(EXTRA_POSITION, position);
    }

    private void initViews() {
        mToolbarHideDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        initViews();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSystemUiHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE,
                SystemUiHelper.FLAG_IMMERSIVE_STICKY,
                new SystemUiHelper.OnVisibilityChangeListener() {
                    @Override
                    public void onVisibilityChange(boolean visible) {
                        if (visible) {
                            mToolbar.animate()
                                    .alpha(1)
                                    .translationY(0)
                                    .setDuration(mToolbarHideDuration)
                                    .setInterpolator(new FastOutSlowInInterpolator())
                                    .start();
                        } else {
                            mToolbar.animate()
                                    .alpha(0)
                                    .translationY(-mToolbar.getBottom())
                                    .setDuration(mToolbarHideDuration)
                                    .setInterpolator(new FastOutSlowInInterpolator())
                                    .start();
                        }
                    }
                });
        // This will set up window flags.
        mSystemUiHelper.show();
        ArrayList<Image> imageList = getIntent().getParcelableArrayListExtra(EXTRA_IMAGE_LIST);
        mViewPager.setAdapter(new GalleryAdapter(imageList, new GalleryAdapter.OnTapListener() {
            @Override
            public void onTap() {
                mSystemUiHelper.toggle();
            }
        }));
        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        mViewPager.setCurrentItem(position);
        mViewPager.setPageTransformer(true, new ViewPagerTransformers.Depth());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
