package com.github.caoyouxin.taoke.api;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.Path;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeTestService implements TaoKeService {
    @Override
    public Observable<TaoKeData> tao(@Path("api") String api, @Field("data") String data, @Field("signature") String signature) {
        return Observable.empty();
    }

    @Override
    public Observable<TaoKeData> tao(@Path("api") String api) {
        TaoKeData taoKeData = new TaoKeData();
        taoKeData.header = new ArrayMap<>();
        taoKeData.body = new ArrayMap<>();
        switch (api) {
            case API_COUPON_TAB:
                taoKeData.header.put("ResultCode", "0000");

                String[] titles = new String[]{"精选", "女装", "家居家装", "数码家电", "母婴", "食品", "鞋包配饰", "美妆个护", "男装", "内衣", "户外运动"};
                List<Map> tabs = new ArrayList<>();
                for (int i = 0; i < titles.length; i++) {
                    Map tab = new ArrayMap();
                    tab.put("type", i);
                    tab.put("title", titles[i]);
                    tabs.add(tab);
                }
                taoKeData.body.put("recs", tabs);
                return Observable.just(taoKeData);
        }
        return Observable.empty();
    }
}
