package com.github.caoyouxin.taoke.datasource;


import android.content.Context;

import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;

public abstract class SortableCouponDataSource extends RxDataSource<List<CouponItem>> implements IDataCacheLoader<List<CouponItem>> {

    protected List<CouponItem> cache;
    protected SORT sort;
    public SortableCouponDataSource(Context context) {
        super(context);
        this.sort = SORT.SORT_SALES;
    }

    public SortableCouponDataSource setSort(SORT sort) {
        this.sort = sort;
        return this;
    }

    public SortableCouponDataSource setCache(List<CouponItem> cache) {
        this.cache = cache;
        return this;
    }

    protected abstract Observable<List<CouponItem>> fetchDataUnderlay() throws Exception;

    @Override
    public Observable<List<CouponItem>> refresh() throws Exception {
        Observable<List<CouponItem>> observable;
        if (null != cache) {
            observable = Observable.just(new ArrayList<>(cache));
        } else {
            observable = fetchDataUnderlay();
        }

        return observable.map(data -> {
            if (null != sort) {
                Collections.sort(data, sort.comparator);
            }
            return data;
        });
    }

    @Override
    public Observable<List<CouponItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<CouponItem> loadCache(boolean isEmpty) {
        return null;
    }

    public enum SORT {
        SORT_SALES((item1, item2) -> format(item2.getVolume() - item1.getVolume())),
        SORT_PRICE_UP((item1, item2) -> format(item1.getPrice() - item2.getPrice())),
        SORT_PRICE_DOWN((item1, item2) -> format(item2.getPrice() - item1.getPrice())),
        SORT_COUPON_UP((item1, item2) -> format(item1.getCoupon() - item2.getCoupon())),
        SORT_COUPON_DOWN((item1, item2) -> format(item2.getCoupon() - item1.getCoupon())),
        SORT_COMMISSION_UP((item1, item2) -> format(item1.getEarn() - item2.getEarn())),
        SORT_COMMISSION_DOWN((item1, item2) -> format(item2.getEarn() - item1.getEarn()));

        private Comparator<CouponItem> comparator;

        SORT(Comparator<CouponItem> comparator) {
            this.comparator = comparator;
        }

        private static int format(double result) {
            return result > 0 ? 1 : result < 0 ? -1 : 0;
        }

        private static int format(long result) {
            return result > 0 ? 1 : result < 0 ? -1 : 0;
        }
    }
}
