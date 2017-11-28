package com.github.caoyouxin.taoke.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;


public class CouponItem implements Parcelable {

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
    private Long category;
    private String commissionRate;
    private String couponClickUrl;
    private String couponEndTime;
    private String couponInfo;
    private Long couponRemainCount;
    private String couponStartTime;
    private Long couponTotalCount;
    private String itemDescription;
    private String itemUrl;
    private String nick;
    private Long numIid;
    private String pictUrl;
    private Long sellerId;
    private String shopTitle;
    private List<String> smallImages;
    private String title;
    private Long userType;
    private Long volume;
    private String zkFinalPrice;
    private String couponPrice;
    private String earnPrice;
    private String tkLink;
    private Double coupon;

    public CouponItem() {
    }

    private CouponItem(Parcel source) {
        this.category = source.readLong();
        this.couponRemainCount = source.readLong();
        this.couponTotalCount = source.readLong();
        this.userType = source.readLong();
        this.numIid = source.readLong();
        this.sellerId = source.readLong();
        this.volume = source.readLong();
        this.smallImages = source.readArrayList(getClass().getClassLoader());
        this.commissionRate = source.readString();
        this.couponClickUrl = source.readString();
        this.couponEndTime = source.readString();
        this.couponInfo = source.readString();
        this.couponStartTime = source.readString();
        this.zkFinalPrice = source.readString();
        this.itemUrl = source.readString();
        this.nick = source.readString();
        this.pictUrl = source.readString();
        this.title = source.readString();
        this.shopTitle = source.readString();
        this.itemDescription = source.readString();
        this.couponPrice = source.readString();
        this.earnPrice = source.readString();
        this.tkLink = source.readString();
        this.coupon = source.readDouble();
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCouponClickUrl() {
        return couponClickUrl;
    }

    public void setCouponClickUrl(String couponClickUrl) {
        this.couponClickUrl = couponClickUrl;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public Long getCouponRemainCount() {
        return couponRemainCount;
    }

    public void setCouponRemainCount(Long couponRemainCount) {
        this.couponRemainCount = couponRemainCount;
    }

    public String getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(String couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public Long getCouponTotalCount() {
        return couponTotalCount;
    }

    public void setCouponTotalCount(Long couponTotalCount) {
        this.couponTotalCount = couponTotalCount;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public List<String> getSmallImages() {
        return smallImages;
    }

    public void setSmallImages(List<String> smallImages) {
        this.smallImages = smallImages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public String getZkFinalPrice() {
        return zkFinalPrice;
    }

    public void setZkFinalPrice(String zkFinalPrice) {
        this.zkFinalPrice = zkFinalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.category);
        dest.writeLong(null != this.couponRemainCount ? this.couponRemainCount : 0L);
        dest.writeLong(null != this.couponTotalCount ? this.couponTotalCount : 0L);
        dest.writeLong(null != this.userType ? this.userType : 0L);
        dest.writeLong(this.numIid);
        dest.writeLong(null != this.sellerId ? this.sellerId : 0L);
        dest.writeLong(this.volume);
        dest.writeArray(null != this.smallImages ? this.smallImages.toArray() : new String[0]);
        dest.writeString(null != this.commissionRate ? this.commissionRate : "");
        dest.writeString(null != this.couponClickUrl ? this.couponClickUrl : "");
        dest.writeString(null != this.couponEndTime ? this.couponEndTime : "");
        dest.writeString(null != this.couponInfo ? this.couponInfo : "");
        dest.writeString(null != this.couponStartTime ? this.couponStartTime : "");
        dest.writeString(this.zkFinalPrice);
        dest.writeString(this.itemUrl);
        dest.writeString(null != this.nick ? this.nick : "");
        dest.writeString(this.pictUrl);
        dest.writeString(this.title);
        dest.writeString(null != this.shopTitle ? this.shopTitle : "");
        dest.writeString(this.itemDescription);
        dest.writeString(null != this.couponPrice ? this.couponPrice : "");
        dest.writeString(null != this.earnPrice ? this.earnPrice : "");
        dest.writeString(null != this.tkLink ? this.tkLink : "");
        dest.writeDouble(this.coupon);
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getEarnPrice() {
        return earnPrice;
    }

    public void setEarnPrice(String earnPrice) {
        this.earnPrice = earnPrice;
    }

    public String getTkLink() {
        return tkLink;
    }

    public void setTkLink(String tkLink) {
        this.tkLink = tkLink;
    }

    public String getLink() {
        return null == couponClickUrl || couponClickUrl.isEmpty() ? tkLink : couponClickUrl;
    }

    private Double price;

    public Double getPrice() {
        if (null == price) {
            price = null == couponPrice || couponPrice.isEmpty() ? Double.parseDouble(zkFinalPrice) : Double.parseDouble(couponPrice);
        }
        return price;
    }

    public Double getCoupon() {
        return coupon;
    }

    public void setCoupon(Double coupon) {
        this.coupon = coupon;
    }

    private Double earn;

    public Double getEarn() {
        if (null == earn) {
            earn = Double.parseDouble(earnPrice);
        }
        return earn;
    }

    public boolean isJu() {
        return null == commissionRate || commissionRate.isEmpty();
    }

    @Override
    public String toString() {
        return "CouponItem{" +
//                "category=" + category +
//                ", commissionRate='" + commissionRate + '\'' +
//                ", couponClickUrl='" + couponClickUrl + '\'' +
//                ", couponEndTime='" + couponEndTime + '\'' +
//                ", couponInfo='" + couponInfo + '\'' +
//                ", couponRemainCount=" + couponRemainCount +
//                ", couponStartTime='" + couponStartTime + '\'' +
//                ", couponTotalCount=" + couponTotalCount +
//                ", itemDescription='" + itemDescription + '\'' +
//                ", itemUrl='" + itemUrl + '\'' +
//                ", nick='" + nick + '\'' +
//                ", numIid=" + numIid +
//                ", pictUrl='" + pictUrl + '\'' +
//                ", sellerId=" + sellerId +
//                ", shopTitle='" + shopTitle + '\'' +
//                ", smallImages=" + smallImages +
//                ", title='" + title + '\'' +
//                ", userType=" + userType +
//                ", volume=" + volume +
//                ", zkFinalPrice='" + zkFinalPrice + '\'' +
//                ", couponPrice='" + couponPrice + '\'' +
//                ", earnPrice='" + earnPrice + '\'' +
//                ", tkLink='" + tkLink + '\'' +
                ", coupon=" + coupon +
                ", price=" + price +
                ", earn=" + earn +
                '}';
    }
}
