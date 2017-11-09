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

    public enum FetchType {
        ALL(1),
        ALL_EFFECTIVE(2),
        INEFFECTIVE(6),
        EFFECTIVE_PAYED(3),
        EFFECTIVE_CONSIGNED(4),
        EFFECTIVE_SETTLED(5);

        int type;

        FetchType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }
    }

    private FetchType type;
    private Integer pageNo;

    public OrderDataSource(Context context, FetchType type) {
        super(context);
        this.type = type;
        this.pageNo = 1;
    }

    public OrderDataSource changeFetchType(FetchType type) {
        this.type = type;
        this.pageNo = 1;
        return this;
    }

    @Override
    public Observable<List<OrderItem>> refresh() throws Exception {
        return TaoKeApi.getOrderList(this.type, this.pageNo);
    }

    @Override
    public Observable<List<OrderItem>> loadMore() throws Exception {
        return TaoKeApi.getOrderList(this.type, ++this.pageNo);
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