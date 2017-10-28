package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.caoyouxin.taoke.model.OrderItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class OrderDataSource extends RxDataSource<List<OrderItem>> implements IDataCacheLoader<List<OrderItem>> {

    public static enum FetchType {
        ALL,
        ALL_EFFECTIVE,
        INEFFECTIVE,
        EFFECTIVE_PAYED,
        EFFECTIVE_CONSIGNED,
        EFFECTIVE_SETTLED
    }

    private FetchType type;

    public OrderDataSource(Context context, FetchType type) {
        super(context);
        this.type = type;
    }

    public OrderDataSource changeFetchType(FetchType type) {
        this.type = type;
        return this;
    }

    @Override
    public Observable<List<OrderItem>> refresh() throws Exception {
        return TaoKeApi.getOrderList(this.type);
    }

    @Override
    public Observable<List<OrderItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<OrderItem> loadCache(boolean isEmpty) {
        return null;
    }
}