package com.leenanxi.android.open.feed.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioFrameLayout extends FrameLayout {
    private float mRatio;

    public RatioFrameLayout(Context context) {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public float getRatio() {
        return mRatio;
    }

    public void setRatio(float ratio) {
        if (mRatio != ratio) {
            mRatio = ratio;
            requestLayout();
            invalidate();
        }
    }

    public void setRatio(float width, float height) {
        setRatio(width / height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRatio > 0) {
            int width;
            int height;
            if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
                height = getMeasuredHeight();
                width = Math.round(mRatio * height);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    width = Math.max(width, getMinimumWidth());
                }
            } else {
                width = getMeasuredWidth();
                height = Math.round(width / mRatio);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    height = Math.max(height, getMinimumHeight());
                }
            }
            setMeasuredDimension(width, height);
        }
    }
}
