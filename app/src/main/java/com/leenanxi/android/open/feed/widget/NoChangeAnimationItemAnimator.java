package com.leenanxi.android.open.feed.widget;

import android.support.v7.widget.DefaultItemAnimator;

/**
 * A DefaultItemAnimator with setSupportsChangeAnimations(false).
 */
public class NoChangeAnimationItemAnimator extends DefaultItemAnimator {
    public NoChangeAnimationItemAnimator() {
        setSupportsChangeAnimations(false);
    }
}
