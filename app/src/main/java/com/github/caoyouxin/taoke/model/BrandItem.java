package com.github.caoyouxin.taoke.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jasontsang on 10/25/17.
 */

public class BrandItem implements Parcelable {
    public int type;
    public String thumb;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.thumb);
    }

    public BrandItem() {
    }

    protected BrandItem(Parcel in) {
        this.type = in.readInt();
        this.thumb = in.readString();
    }

    public static final Parcelable.Creator<BrandItem> CREATOR = new Parcelable.Creator<BrandItem>() {
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
