package com.github.caoyouxin.taoke.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class CouponItem implements Parcelable {
    public Long id;
    public String thumb;
    public String title;
    public String priceBefore;
    public Long sales;
    public String priceAfter;
    public String value;
    public Long total;
    public Long left;
    public String earn;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.thumb);
        dest.writeString(this.title);
        dest.writeString(this.priceBefore);
        dest.writeLong(this.sales);
        dest.writeString(this.priceAfter);
        dest.writeString(this.value);
        dest.writeLong(this.total);
        dest.writeLong(this.left);
        dest.writeString(this.earn);
    }

    public CouponItem() {
    }

    private CouponItem(Parcel in) {
        this.id = in.readLong();
        this.thumb = in.readString();
        this.title = in.readString();
        this.priceBefore = in.readString();
        this.sales = in.readLong();
        this.priceAfter = in.readString();
        this.value = in.readString();
        this.total = in.readLong();
        this.left = in.readLong();
        this.earn = in.readString();
    }

    public static final Parcelable.Creator<CouponItem> CREATOR = new Parcelable.Creator<CouponItem>() {
        @Override
        public CouponItem createFromParcel(Parcel source) {
            return new CouponItem(source);
        }

        @Override
        public CouponItem[] newArray(int size) {
            return new CouponItem[size];
        }
    };
}
