package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class MessageDataSource extends RxDataSource<List<MessageItem>> implements IDataCacheLoader<List<MessageItem>> {

    private int pageNo;
    private boolean hasMore;

    public MessageDataSource(Context context) {
        super(context);
        this.pageNo = 1;
        this.hasMore = true;
    }

    @Override
    public Observable<List<MessageItem>> refresh() throws Exception {
        this.pageNo = 1;
        return TaoKeApi.getMessageList(this.pageNo).map(list -> {
            if (list.size() < 10) {
                MessageDataSource.this.hasMore = false;
            }
            return list;
        });
    }

    @Override
    public Observable<List<MessageItem>> loadMore() throws Exception {
        return TaoKeApi.getMessageList(++this.pageNo).map(list -> {
            if (list.size() < 10) {
                MessageDataSource.this.hasMore = false;
            }
            return list;
        });
    }

    @Override
    public boolean hasMore() {
        return this.hasMore;
    }

    @Override
    public List<MessageItem> loadCache(boolean isEmpty) {
        return null;
    }
}