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
    private boolean isHasMore;

    public OrderDataSource(Context context, FetchType type) {
        super(context);
        this.type = type;
        this.pageNo = 1;
        this.isHasMore = true;
    }

    public OrderDataSource changeFetchType(FetchType type) {
        this.type = type;
        this.pageNo = 1;
        this.isHasMore = true;
        return this;
    }

    @Override
    public Observable<List<OrderItem>> refresh() throws Exception {
        this.pageNo = 1;
        this.isHasMore = true;
        return TaoKeApi.getOrderList(this.type, this.pageNo).map(list -> {
            if (list.size() < 10) {
                isHasMore = false;
            }
            return list;
        });
    }

    @Override
    public Observable<List<OrderItem>> loadMore() throws Exception {
        if (!isHasMore) {
            return null;
        }
        return TaoKeApi.getOrderList(this.type, ++this.pageNo).map(list -> {
            if (list.size() < 10) {
                isHasMore = false;
            }
            return list;
        });
    }

    @Override
    public boolean hasMore() {
        return this.isHasMore;
    }

    @Override
    public List<OrderItem> loadCache(boolean isEmpty) {
        return null;
    }
}