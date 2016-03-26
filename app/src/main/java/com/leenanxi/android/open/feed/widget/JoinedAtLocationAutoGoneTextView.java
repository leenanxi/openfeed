package com.leenanxi.android.open.feed.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.LogUtils;
import com.leenanxi.android.open.feed.util.TimeUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;

import java.text.ParseException;
import java.util.Date;

public class JoinedAtLocationAutoGoneTextView extends TimeTextView {
    private String mLocation;

    public JoinedAtLocationAutoGoneTextView(Context context) {
        super(context);
    }

    public JoinedAtLocationAutoGoneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoinedAtLocationAutoGoneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JoinedAtLocationAutoGoneTextView(Context context, AttributeSet attrs, int defStyleAttr,
                                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setDoubanTime(String doubanTime) {
        throw new UnsupportedOperationException("Use setJoinedAtAndLocation() instead.");
    }

    public void setJoinedAtAndLocation(String doubanTime, String location) {
        mLocation = location;
        try {
            setTime(TimeUtils.parseDoubanDateTime(doubanTime));
        } catch (ParseException e) {
            LogUtils.e("Unable to parse date time: " + doubanTime);
            setTimeText(doubanTime);
            e.printStackTrace();
        }
    }

    @Override
    protected String formatTime(Date time) {
        return TimeUtils.formatDate(time, getContext());
    }

    @Override
    protected void setTimeText(String timeText) {
        String text;
        if (!TextUtils.isEmpty(mLocation)) {
            text = getContext().getString(R.string.profile_joined_at_and_location_format, timeText,
                    mLocation);
        } else {
            text = getContext().getString(R.string.profile_joined_at_format, timeText);
        }
        setText(text);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        ViewUtils.setVisibleOrGone(this, !TextUtils.isEmpty(text));
    }
}
