package com.github.caoyouxin.taoke.model;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.felipecsl.asymmetricgridview.AsymmetricItem;

@SuppressLint("ParcelCreator")
public class AdBrandItem implements AsymmetricItem {

    public String thumb;
    public int cSpan;
    public int rSpan;
    public int openType;
    public String name;
    public String ext;

    public AdBrandItem() {
    }

    public AdBrandItem(Parcel source) {
        thumb = source.readString();
        cSpan = source.readInt();
        rSpan = source.readInt();
        openType = source.readInt();
        name = source.readString();
        ext = source.readString();
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
        dest.writeInt(openType);
        dest.writeString(name);
        dest.writeString(ext);
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
