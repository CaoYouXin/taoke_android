package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;


public class HelpDataSource extends RxDataSource<List<HelpItem>> implements IDataCacheLoader<List<HelpItem>> {
    public HelpDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<HelpItem>> refresh() throws Exception {
        return TaoKeApi.getHelpList();
    }

    @Override
    public Observable<List<HelpItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<HelpItem> loadCache(boolean isEmpty) {
        return null;
    }
}