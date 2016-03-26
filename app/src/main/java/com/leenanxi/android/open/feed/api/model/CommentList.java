package com.leenanxi.android.open.feed.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CommentList implements Parcelable {
    public static final Creator<CommentList> CREATOR =
            new Creator<CommentList>() {
                public CommentList createFromParcel(Parcel source) {
                    return new CommentList(source);
                }

                public CommentList[] newArray(int size) {
                    return new CommentList[size];
                }
            };
    public ArrayList<Comment> comments = new ArrayList<>();
    public int count;
    public int start;

    public CommentList() {
    }

    protected CommentList(Parcel in) {
        this.comments = in.createTypedArrayList(Comment.CREATOR);
        this.count = in.readInt();
        this.start = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(comments);
        dest.writeInt(this.count);
        dest.writeInt(this.start);
    }
}
