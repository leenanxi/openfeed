package com.leenanxi.android.open.feed.link;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class UriSpan extends ClickableSpan {
    private String mUri;

    public UriSpan(String uri) {
        mUri = uri;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        UriHandler.open(mUri, widget.getContext());
    }
}
