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
    public void setDoubanTime(String doubanTime) {
        throw new UnsupportedOperationException("Use setDoubanTimeAndAction() instead.");
    }

    /**
     * Should behave the same as {@link Broadcast#getActionWithTime(Context)}.
     */
    public void setDoubanTimeAndAction(String doubanTime, String action) {
        mAction = action;
        try {
            Date date = TimeUtils.parseDoubanDateTime(doubanTime);
            setTime(date);
        } catch (ParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            e.printStackTrace();
            setTimeText(doubanTime);
        }
    }

    @Override
    protected void setTimeText(String timeText) {
        setText(getContext().getString(R.string.broadcast_time_action_format, timeText, mAction));
    }
}
