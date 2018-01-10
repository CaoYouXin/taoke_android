package com.github.caoyouxin.taoke.model;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.felipecsl.asymmetricgridview.AsymmetricItem;

@SuppressLint("ParcelCreator")
public class AdBrandItem implements AsymmetricItem {

    public String thumb;
    public int cSpan;
    public int rSpan;

    public AdBrandItem() {
    }

    public AdBrandItem(Parcel source) {
        thumb = source.readString();
        cSpan = source.readInt();
        rSpan = source.readInt();
    }

    @Override
    public int getColumnSpan() {
        return cSpan;
    }

    @Override
    public int getRowSpan() {
        return rSpan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumb);
        dest.writeInt(cSpan);
        dest.writeInt(rSpan);
    }

    @Override
    public String toString() {
        return "AdBrandItem{" +
                "thumb='" + thumb + '\'' +
                ", cSpan=" + cSpan +
                ", rSpan=" + rSpan +
                '}';
    }
}
