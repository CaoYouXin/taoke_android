package com.github.caoyouxin.taoke.api;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeTestService implements TaoKeService {
    @Override
    public Observable<List<CouponTab>> getCouponTabData() {
        String[] titles = new String[]{"精选", "女装", "家居家装", "数码家电", "母婴", "食品", "鞋包配饰", "美妆个护", "男装", "内衣", "户外运动"};
        List<CouponTab> tabs = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CouponTab tab = new CouponTab();
            tab.type = i;
            tab.title = titles[i];
            tabs.add(tab);
        }
        return Observable.just(tabs);
    }
}
