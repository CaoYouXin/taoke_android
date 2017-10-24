package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class CouponDataSource extends RxDataSource<List<CouponItem>> implements IDataCacheLoader<List<CouponItem>> {
    public CouponDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<CouponItem>> refresh() throws Exception {
        List<CouponItem> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(new CouponItem());
        }
        return Observable.just(data);
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
}