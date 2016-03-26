package com.leenanxi.android.open.feed.widget;

public interface FlexibleSpaceHeaderView {
    int getScroll();

    int getScrollExtent();

    void scrollTo(int scroll);

    void scrollBy(int delta);
}
