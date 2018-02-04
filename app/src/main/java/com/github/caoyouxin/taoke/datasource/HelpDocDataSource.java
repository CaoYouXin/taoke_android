package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.HelpDoc;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;


public class HelpDocDataSource extends RxDataSource<List<HelpDoc>> implements IDataCacheLoader<List<HelpDoc>> {
    public HelpDocDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<HelpDoc>> refresh() throws Exception {
        return TaoKeApi.getHelpDocs();
    }

    @Override
    public Observable<List<HelpDoc>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<HelpDoc> loadCache(boolean isEmpty) {
        return null;
    }
}