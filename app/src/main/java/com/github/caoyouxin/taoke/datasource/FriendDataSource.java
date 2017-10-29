package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.FriendItem;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class FriendDataSource extends RxDataSource<List<FriendItem>> implements IDataCacheLoader<List<FriendItem>> {
    public FriendDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<FriendItem>> refresh() throws Exception {
        return TaoKeApi.getFriendsList();
    }

    @Override
    public Observable<List<FriendItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<FriendItem> loadCache(boolean isEmpty) {
        return null;
    }
}