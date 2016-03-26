package com.leenanxi.android.open.feed.settings.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.AppUtils;
import com.leenanxi.android.open.feed.util.ToastUtils;
import com.leenanxi.android.open.feed.widget.KonamiCodeDetector;

public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.container)
    LinearLayout mContainerLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.version)
    TextView mVersionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        // Seems that ScrollView intercepts touch event, so we have to set the onTouchListener on a
        // view inside it.
        mContainerLayout.setOnTouchListener(new KonamiCodeDetector(this) {
            @Override
            public void onDetected() {
                onKonamiCodeDetected();
            }
        });
        mVersionText.setText(getString(R.string.about_version_format,
                AppUtils.getPackageInfo(this).versionName));
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

    private void onKonamiCodeDetected() {
        ToastUtils.show(R.string.about_konami_code_detected, this);
    }
}
