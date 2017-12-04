package com.github.caoyouxin.taoke.model;

import android.os.Parcel;
import android.os.Parcelable;


public class BrandItem implements Parcelable {
    public static final Creator<BrandItem> CREATOR = new Creator<BrandItem>() {
        @Override
        public BrandItem createFromParcel(Parcel source) {
            return new BrandItem(source);
        }

        @Override
        public BrandItem[] newArray(int size) {
            return new BrandItem[size];
        }
    };
    public String favId;
    public String title;

    public BrandItem(String title, String favId) {
        this.favId = favId;
        this.title = title;
    }

    protected BrandItem(Parcel in) {
        this.title = in.readString();
        this.favId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.favId);
    }
}
