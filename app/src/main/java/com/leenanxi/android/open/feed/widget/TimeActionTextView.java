package com.leenanxi.android.open.feed.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.util.LogUtils;
import com.leenanxi.android.open.feed.util.TimeUtils;

import java.text.ParseException;
import java.util.Date;

public class TimeActionTextView extends TimeTextView {
    private String mAction;

    public TimeActionTextView(Context context) {
        super(context);
    }

    public TimeActionTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeActionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeActionTextView(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setTime(String time) {
        throw new UnsupportedOperationException("Use setTimeAndAction() instead.");
    }

    /**
     * Should behave the same as {@link Broadcast#getActionWithTime(Context)}.
     */
    public void setTimeAndAction(String time, String action) {
        mAction = action;
        try {
            Date date = TimeUtils.parseDateTime(time);
            setTime(date);
        } catch (ParseException e) {
            LogUtils.e("Unable to parse date time: " + time);
            e.printStackTrace();
            setTimeText(time);
        }
    }

    @Override
    protected void setTimeText(String timeText) {
        setText(getContext().getString(R.string.broadcast_time_action_format, timeText, mAction));
    }
}
