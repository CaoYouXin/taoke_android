package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.HomeBtn;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class BrandDataSource extends RxDataSource<List<HomeBtn>> implements IDataCacheLoader<List<HomeBtn>> {
    public BrandDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<HomeBtn>> refresh() throws Exception {
        return TaoKeApi.getBrandList();
    }

    @Override
    public Observable<List<HomeBtn>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<HomeBtn> loadCache(boolean isEmpty) {
        return null;
    }
}