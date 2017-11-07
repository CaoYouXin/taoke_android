package com.github.caoyouxin.taoke.datasource;

import android.content.Context;

import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.shizhefei.mvc.IDataCacheLoader;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class CouponDataSource extends RxDataSource<List<CouponItem>> implements IDataCacheLoader<List<CouponItem>> {
    public CouponDataSource(Context context) {
        super(context);
    }

    @Override
    public Observable<List<CouponItem>> refresh() throws Exception {
        return TaoKeApi.getCouponList(this.cid, this.pageNo + "");
    }

    @Override
    public Observable<List<CouponItem>> loadMore() throws Exception {
//        this.pageNo++;
//        return TaoKeApi.getCouponList(this.cid, this.pageNo + "");
        return null;
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<CouponItem> loadCache(boolean isEmpty) {
        return null;
    }

    private String cid;
    private Long pageNo;

    public void setCid(String cid) {
        this.cid = cid;
        this.pageNo = 1L;
    }

}