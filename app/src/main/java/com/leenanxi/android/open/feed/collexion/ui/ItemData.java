package com.leenanxi.android.open.feed.collexion.ui;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leenanxi on 16/4/1.
 */
public class ItemData implements Parcelable {
    public static final Parcelable.Creator<ItemData> CREATOR =
            new Parcelable.Creator<ItemData>() {
                public ItemData createFromParcel(Parcel source) {
                    return new ItemData(source);
                }

                public ItemData[] newArray(int size) {
                    return new ItemData[size];
                }
            };

    public String action;

    public String title;

    public ItemData(String action, String title) {
        this.action = action;
        this.title = title;
    }

    protected ItemData(Parcel in) {
        action = in.readString();
        title = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(title);
    }
}
