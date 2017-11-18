package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.SearchHintItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class SearchHintDataSource extends RxDataSource<List<SearchHintItem>> implements IDataCacheLoader<List<SearchHintItem>> {

    private String inputNow;

    public SearchHintDataSource(Context context, String inputNow) {
        super(context);
        this.inputNow = inputNow;
    }

    public SearchHintDataSource changeInputNow(String inputNow) {
        this.inputNow = inputNow;
        return this;
    }

    @Override
    public Observable<List<SearchHintItem>> refresh() throws Exception {
        return TaoKeApi.getSearchHintList(this.inputNow);
    }

    @Override
    public Observable<List<SearchHintItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<SearchHintItem> loadCache(boolean isEmpty) {
        return null;
    }
}