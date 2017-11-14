package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.ShareImage;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableJust;

/**
 * Created by jasontsang on 10/24/17.
 */

public class ShareAppImageDataSource extends RxDataSource<List<ShareImage>> implements IDataCacheLoader<List<ShareImage>> {

    public ShareAppImageDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<ShareImage>> refresh() throws Exception {
        return TaoKeApi.getShareAppImageList();
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