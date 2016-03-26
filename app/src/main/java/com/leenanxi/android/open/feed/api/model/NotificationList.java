package com.leenanxi.android.open.feed.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.leenanxi.android.open.feed.api.Frodo;

import java.util.ArrayList;

@Frodo
public class NotificationList implements Parcelable {
    public static final Creator<NotificationList> CREATOR = new Creator<NotificationList>() {
        public NotificationList createFromParcel(Parcel source) {
            return new NotificationList(source);
        }

        public NotificationList[] newArray(int size) {
            return new NotificationList[size];
        }
    };
    public int count;
    public ArrayList<Notification> notifications = new ArrayList<>();
    public int start;

    public NotificationList() {
    }

    protected NotificationList(Parcel in) {
        count = in.readInt();
        notifications = in.createTypedArrayList(Notification.CREATOR);
        start = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeTypedList(notifications);
        dest.writeInt(start);
    }
}
