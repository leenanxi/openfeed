package com.leenanxi.android.open.feed.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
import com.leenanxi.android.open.feed.util.LogUtils;
import com.leenanxi.android.open.feed.util.TimeUtils;

import java.text.ParseException;
import java.util.Date;

public class TimeTextView extends TextView {
    private static final int UPDATE_TIME_TEXT_INTERVAL_MILLI = 30 * 1000;
    private Date mTime;
    private final Runnable mUpdateTimeTextRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimeText();
        }
    };

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimeTextView(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
        updateTimeText();
    }

    /**
     * Should behave the same as {@link TimeUtils#formatDateTime(String, Context)}.
     */
    public void setTime(String time) {
        try {
            setTime(TimeUtils.parseDateTime(time));
        } catch (ParseException e) {
            LogUtils.e("Unable to parse date time: " + time);
            setText(time);
            e.printStackTrace();
        }
    }

    private void updateTimeText() {
        removeCallbacks(mUpdateTimeTextRunnable);
        if (mTime != null) {
            setTimeText(formatTime(mTime));
            postDelayed(mUpdateTimeTextRunnable, UPDATE_TIME_TEXT_INTERVAL_MILLI);
        }
    }

    protected String formatTime(Date time) {
        return TimeUtils.formatDateTime(time, getContext());
    }

    protected void setTimeText(String timeText) {
        setText(timeText);
    }
}
