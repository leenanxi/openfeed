package com.leenanxi.android.open.feed.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.*;
import android.webkit.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.ClipboardUtils;
import com.leenanxi.android.open.feed.util.ToastUtils;
import com.leenanxi.android.open.feed.util.UrlUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;

public class WebViewActivity extends AppCompatActivity {

    Toolbar mToolbar;
    ProgressBar mProgress;
    WebView mWebView;
    TextView mErrorText;
    int mToolbarHeight;
    private boolean mProgressVisible;

    public static Intent makeIntent(Uri uri, Context context) {
        return new Intent(context, WebViewActivity.class)
                .setData(uri);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
        mWebView = (WebView) findViewById(R.id.web);
        mErrorText = (TextView) findViewById(R.id.error);
        mToolbarHeight = getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        initViews();
        setupToolbar();
        setupWebView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initViews();
        setupToolbar();
        ViewUtils.setVisibleOrGone(mProgress, mProgressVisible);
        ((ViewGroup.MarginLayoutParams) mWebView.getLayoutParams()).topMargin = mToolbarHeight;
        mWebView.requestLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_reload) {
            reloadWebView();
            return true;
        } else if (id == R.id.action_copy_url) {
            copyUrl();
            return true;
        } else if (id == R.id.action_open_in_browser) {
            openInBrowser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    protected void onLoadUri(WebView webView) {
        String url = getIntent().getData().toString();
        setTitle(url);
        webView.loadUrl(url);
    }

    protected void onPageStared(WebView webView, String url, Bitmap favicon) {
    }

    protected void onPageFinished(WebView webView, String url) {
    }

    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }

    protected void reloadWebView() {
        hideError();
        mWebView.reload();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setProgressVisible(boolean visible) {
        if (mProgressVisible != visible) {
            mProgressVisible = visible;
            ViewUtils.setVisibleOrGone(mProgress, visible);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        // NOTE: This gives double tap zooming.
        webSettings.setUseWideViewPort(true);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new ViewClient());
        onLoadUri(mWebView);
    }

    private void showError(String error) {
        mWebView.setVisibility(View.INVISIBLE);
        mErrorText.setText(error);
        mErrorText.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        mErrorText.setVisibility(View.INVISIBLE);
        mErrorText.setText(null);
        mWebView.setVisibility(View.VISIBLE);
    }

    private void copyUrl() {
        String url = mWebView.getUrl();
        if (!TextUtils.isEmpty(url)) {
            ClipboardUtils.copyUrl(mWebView.getTitle(), url, this);
        } else {
            ToastUtils.show(R.string.webview_copy_url_empty, this);
        }
    }

    private void openInBrowser() {
        String url = mWebView.getUrl();
        if (!TextUtils.isEmpty(url)) {
            UrlUtils.openWithIntent(url, this);
        } else {
            ToastUtils.show(R.string.webview_copy_url_empty, this);
        }
    }

    private class ChromeClient extends WebChromeClient {
        // NOTE: WebView can be trying to show an AlertDialog after the activity is finished, which
        // will result in a WindowManager$BadTokenException.
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsAlert(view, url, message,
                    result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsConfirm(view, url, message,
                    result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                  JsPromptResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsPrompt(view, url, message,
                    defaultValue, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return WebViewActivity.this.isFinishing() || super.onJsBeforeUnload(view, url, message,
                    result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setProgressVisible(newProgress != 100);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
        }
    }

    private class ViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebViewActivity.this.onPageStared(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            WebViewActivity.this.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            showError(description);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return WebViewActivity.this.shouldOverrideUrlLoading(view, url);
        }
    }
}
