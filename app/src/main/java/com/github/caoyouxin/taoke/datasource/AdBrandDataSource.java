package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.AdBrandItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

public class AdBrandDataSource extends RxDataSource<List<AdBrandItem>> implements IDataCacheLoader<List<AdBrandItem>> {

    public AdBrandDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<AdBrandItem>> refresh() throws Exception {
        return TaoKeApi.getBrandItems();
    }

    @Override
    public Observable<List<AdBrandItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<AdBrandItem> loadCache(boolean isEmpty) {
        return null;
    }
}
