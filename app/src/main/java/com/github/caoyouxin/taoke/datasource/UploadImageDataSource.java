package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.model.UploadImageItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class UploadImageDataSource extends RxDataSource<List<UploadImageItem>> implements IDataCacheLoader<List<UploadImageItem>> {

    private List<UploadImageItem> data = new ArrayList<>();

    public UploadImageDataSource(Context context) {
        super(context);
        UploadImageItem handle = new UploadImageItem();
        handle.isHandle = true;
        handle.uploaded = true;
        data.add(handle);
    }

    public void addImage(String uri) {
        UploadImageItem item = new UploadImageItem();
        item.uri = uri;
        data.add(0, item);
    }

    public void setImageUploaded(String uri) {
        for (UploadImageItem datum : data) {
            if (datum.uri.equals(uri)) {
                datum.uploaded = true;
                return;
            }
        }
    }

    @Override
    public Observable<List<UploadImageItem>> refresh() throws Exception {
        return Observable.just(data);
    }

    @Override
    public Observable<List<UploadImageItem>> loadMore() throws Exception {
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<UploadImageItem> loadCache(boolean isEmpty) {
        return null;
    }
}
