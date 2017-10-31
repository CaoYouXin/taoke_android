package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.Product;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/2/17.
 */

public class ProductDataSource extends RxDataSource<List<Product>> implements IDataCacheLoader<List<Product>> {

    public final static int SORT_MULTIPLE = 0;
    public final static int SORT_SALES = 1;
    public final static int SORT_PRICE_UP = 2;
    public final static int SORT_PRICE_DOWN = 3;
    public static final int SORT_COMMISSION = 4;

    private BrandItem brandItem;

    private int sort;

    public ProductDataSource(Context context, BrandItem brandItem) {
        super(context);
        this.brandItem = brandItem;
    }

    @Override
    public Observable<List<Product>> refresh() throws Exception {
        return TaoKeApi.getProductList(brandItem);
    }

    @Override
    public Observable<List<Product>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<Product> loadCache(boolean isEmpty) {
        return null;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}