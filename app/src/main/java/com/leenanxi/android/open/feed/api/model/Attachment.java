package com.leenanxi.android.open.feed.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Attachment implements Parcelable {
    public static final Creator<Attachment> CREATOR =
            new Creator<Attachment>() {
                public Attachment createFromParcel(Parcel source) {
                    return new Attachment(source);
                }

                public Attachment[] newArray(int size) {
                    return new Attachment[size];
                }
            };
    @SerializedName("desc")
    public String description;
    public String href;
    public String image;
    public String title;
    public String type;

    public Attachment() {
    }

    protected Attachment(Parcel in) {
        description = in.readString();
        href = in.readString();
        image = in.readString();
        title = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(href);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(type);
    }
}
