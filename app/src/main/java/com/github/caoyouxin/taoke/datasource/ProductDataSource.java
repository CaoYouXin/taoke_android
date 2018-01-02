package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponItem;

import java.util.List;

import io.reactivex.Observable;


public class ProductDataSource extends SortableCouponDataSource {

    private BrandItem brandItem;

    public ProductDataSource(Context context, BrandItem brandItem) {
        super(context);
        this.brandItem = brandItem;
    }

    @Override
    protected Observable<List<CouponItem>> fetchDataUnderlay() throws Exception {
        return TaoKeApi.getProductList(brandItem, 1);
    }

}