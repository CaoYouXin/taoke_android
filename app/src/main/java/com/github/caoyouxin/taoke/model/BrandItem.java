package com.github.caoyouxin.taoke.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jasontsang on 10/25/17.
 */

public class BrandItem implements Parcelable {
    public int type;
    public String title;
    public String thumb;

    public BrandItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeString(this.thumb);
    }

    protected BrandItem(Parcel in) {
        this.type = in.readInt();
        this.title = in.readString();
        this.thumb = in.readString();
    }

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
}
