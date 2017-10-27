package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class MessageDataSource extends RxDataSource<List<MessageItem>> implements IDataCacheLoader<List<MessageItem>> {

    private String type;

    public MessageDataSource(Context context, String type) {
        super(context);
        this.type = type;
    }

    @Override
    public Observable<List<MessageItem>> refresh() throws Exception {
        return TaoKeApi.getMessageList(this.type);
    }

    @Override
    public Observable<List<MessageItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<MessageItem> loadCache(boolean isEmpty) {
        return null;
    }
}