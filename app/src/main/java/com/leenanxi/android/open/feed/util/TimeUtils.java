package com.leenanxi.android.open.feed.util;

import android.content.Context;
import com.leenanxi.android.open.feed.R;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    private static final Locale DEFAULT_LOCAL = Locale.CHINA;
    private static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCAL),
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", DEFAULT_LOCAL)
    };
    
    public static Date parseDateTime(String source) throws ParseException {
        if (source.contains("T")) {
            return ISO8601Utils.parse(source, new ParsePosition(0));
        }
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {
            try {
                return format.parse(source);
            } catch (ParseException ex) {
                continue;
            }
        }
        throw new ParseException("Unsupported date format: \"" + source + "\"",
                0);
    }
    
    

    public static String formatDateTime(String source, Context context) {
        try {
            Date dateTime = parseDateTime(source);
            return  formatDateTime(dateTime,context);
        } catch (Exception e) {
            return source;
        }
    }


    public static String formatDateTime(Date source, Context context) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeInMillis(source.getTime());
        int date = calendar.get(Calendar.DATE);
        int nowDate = now.get(Calendar.DATE);
        if (date == nowDate) {
            int duration = now.get(Calendar.SECOND) - calendar.get(Calendar.SECOND);
            if (duration > 0) {
                if (duration - 5 * 60 < 0) {
                    return context.getString(R.string.just_now);
                } else if (duration - 3600 < 0) {
                    return context.getString(R.string.minute_format,
                            Math.round((float) duration / 60));
                } else if (duration - 7200 < 0) {
                    return context.getString(R.string.hour_format,
                            Math.round((float) duration / 3600));
                }
            }
            return (new SimpleDateFormat(context.getString(R.string.today_hour_minute_pattern))).format(source);
        }
        if (nowDate - date == 1) {
            return (new SimpleDateFormat(context.getString(R.string.yesterday_hour_minute_pattern))).format(source);
        } else if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return (new SimpleDateFormat(context.getString(R.string.month_day_hour_minute_pattern))).format(source);
        } else {
            return (new SimpleDateFormat(context.getString(R.string.date_hour_minute_pattern))).format(source);
        }
    }



    public static String formatDate(Date date, Locale locale, Context context) {
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeInMillis(date.getTime());
        if (calendar.get(Calendar.DATE) == now.get(Calendar.DATE)) {
            return context.getString(R.string.today);
        }
        if (now.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1) {
            return context.getString(R.string.yesterday);
        } else if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return (new SimpleDateFormat(context.getString(R.string.month_day_pattern))).format(date);
        } else {
            return (new SimpleDateFormat(context.getString(R.string.date_pattern))).format(date);
        }
    }

    public static String formatDate(Date source, Context context) {
        return formatDate(source, DEFAULT_LOCAL, context);
    }

    public static String formatDate(String source, Context context) {
        try {
            Date dateTime = parseDateTime(source);
            return formatDate(dateTime, DEFAULT_LOCAL, context);
        } catch (Exception e) {
            return source;
        }
    }
}
