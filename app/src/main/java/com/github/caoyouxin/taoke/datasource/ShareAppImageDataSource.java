package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.ShareImage;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;


public class ShareAppImageDataSource extends RxDataSource<List<ShareImage>> implements IDataCacheLoader<List<ShareImage>> {

    private int type;

    public ShareAppImageDataSource(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public Observable<List<ShareImage>> refresh() throws Exception {
        return TaoKeApi.getShareAppImageList(type);
    }

    @Override
    public Observable<List<ShareImage>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<ShareImage> loadCache(boolean isEmpty) {
        return null;
    }
}