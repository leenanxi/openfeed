package com.leenanxi.android.open.feed.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhotoSizes implements Parcelable {
    public static final Creator<PhotoSizes> CREATOR =
            new Creator<PhotoSizes>() {
                public PhotoSizes createFromParcel(Parcel source) {
                    return new PhotoSizes(source);
                }

                public PhotoSizes[] newArray(int size) {
                    return new PhotoSizes[size];
                }
            };
    public ArrayList<Integer> cover = new ArrayList<>();
    public ArrayList<Integer> icon = new ArrayList<>();
    public ArrayList<Integer> image = new ArrayList<>();
    public ArrayList<Integer> large = new ArrayList<>();
    @SerializedName("thumb")
    public ArrayList<Integer> thumbnail = new ArrayList<>();

    public PhotoSizes() {
    }

    protected PhotoSizes(Parcel in) {
        in.readList(cover, ArrayList.class.getClassLoader());
        in.readList(icon, ArrayList.class.getClassLoader());
        in.readList(image, ArrayList.class.getClassLoader());
        in.readList(large, ArrayList.class.getClassLoader());
        in.readList(thumbnail, ArrayList.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cover);
        dest.writeList(icon);
        dest.writeList(image);
        dest.writeList(large);
        dest.writeList(thumbnail);
    }
}
