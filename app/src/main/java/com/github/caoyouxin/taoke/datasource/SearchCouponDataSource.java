package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class SearchCouponDataSource extends RxDataSource<List<CouponItem>> implements IDataCacheLoader<List<CouponItem>> {

    public final static int SORT_MULTIPLE = 0;
    public final static int SORT_SALES = 1;
    public final static int SORT_PRICE_UP = 2;
    public final static int SORT_PRICE_DOWN = 3;
    public static final int SORT_COMMISSION = 4;
    private String keyword;
    private int sort;
    private List<CouponItem> cache;
    private Comparator<? super CouponItem> sortFunction;

    public SearchCouponDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<CouponItem>> refresh() throws Exception {
        if (null != this.cache) {
            Collections.sort(this.cache, sortFunction);
            Observable<List<CouponItem>> just = Observable.just(new ArrayList<>(this.cache));
            this.cache = null;
            return just;
        }
        return TaoKeApi.getSearchList(this.keyword).map(list -> {
            Collections.sort(list, sortFunction);
            return list;
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

    public SearchCouponDataSource setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public SearchCouponDataSource setSort(int sort) {
        this.sort = sort;

        switch (this.sort) {
            case SORT_MULTIPLE:
                this.sortFunction = (item1, item2) -> {
                    double v = Double.parseDouble(item2.getZkFinalPrice())
                            + Double.parseDouble(item2.getEarnPrice()) + item2.getVolume() - item1.getVolume()
                            - Double.parseDouble(item1.getZkFinalPrice()) - Double.parseDouble(item1.getEarnPrice());
                    return v > 0 ? 1 : v < 0 ? -1 : 0;
                };
                break;
            case SORT_COMMISSION:
                this.sortFunction = (item1, item2) -> {
                    double v = Double.parseDouble(item2.getEarnPrice()) - Double.parseDouble(item1.getEarnPrice());
                    return v > 0 ? 1 : v < 0 ? -1 : 0;
                };
                break;
            case SORT_SALES:
                this.sortFunction = (item1, item2) -> {
                    long l = item2.getVolume() - item1.getVolume();
                    return l > 0 ? 1 : l < 0 ? -1 : 0;
                };
                break;
            case SORT_PRICE_DOWN:
                this.sortFunction = (item1, item2) -> {
                    double v = Double.parseDouble(item2.getZkFinalPrice()) - Double.parseDouble(item1.getZkFinalPrice());
                    return v > 0 ? 1 : v < 0 ? -1 : 0;
                };
                break;
            case SORT_PRICE_UP:
                this.sortFunction = (item1, item2) -> {
                    double v = Double.parseDouble(item1.getZkFinalPrice()) - Double.parseDouble(item2.getZkFinalPrice());
                    return v > 0 ? 1 : v < 0 ? -1 : 0;
                };
                break;
        }
        return this;
    }

    public SearchCouponDataSource setCache(List<CouponItem> cache) {
        this.cache = cache;
        return this;
    }

}