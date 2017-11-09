package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/2/17.
 */

public class ProductDataSource extends RxDataSource<List<CouponItem>> implements IDataCacheLoader<List<CouponItem>> {

    public final static int SORT_MULTIPLE = 0;
    public final static int SORT_SALES = 1;
    public final static int SORT_PRICE_UP = 2;
    public final static int SORT_PRICE_DOWN = 3;
    public static final int SORT_COMMISSION = 4;

    private BrandItem brandItem;

    private int sort;
    private int pageNo;

    public ProductDataSource(Context context, BrandItem brandItem) {
        super(context);
        this.brandItem = brandItem;
        this.sort = 0;
        this.pageNo = 1;
    }

    @Override
    public Observable<List<CouponItem>> refresh() throws Exception {
        return TaoKeApi.getProductList(brandItem, this.pageNo, this.sort);
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

    public void setSort(int sort) {
        this.sort = sort;
        this.pageNo = 1;
    }
}