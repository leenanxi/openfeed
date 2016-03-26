package com.leenanxi.android.open.feed.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.leenanxi.android.open.feed.api.Frodo;

@Frodo
public class Notification implements Parcelable {
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
    public long id;
    @SerializedName(value = "is_read")
    public boolean read;
    @SerializedName(value = "target_uri")
    public String targetUri;
    public String text;
    public String time;

    public Notification() {
    }

    protected Notification(Parcel in) {
        id = in.readLong();
        read = in.readByte() != 0;
        targetUri = in.readString();
        text = in.readString();
        time = in.readString();
    }

    private void fixText() {
        int index = text.lastIndexOf("回复");
        if (index == text.length() - "回复".length()) {
            text = text.substring(0, index) + "回应";
        }
    }

    private void fixTime() {
        if (time.startsWith("\"") && time.endsWith("\"")) {
            time = time.substring(1, time.length() - 1);
        }
    }

    public void fix() {
        fixText();
        fixTime();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeByte(read ? (byte) 1 : (byte) 0);
        dest.writeString(targetUri);
        dest.writeString(text);
        dest.writeString(time);
    }
}
