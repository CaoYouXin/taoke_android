package com.github.caoyouxin.taoke.api;

import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeApi {
    public static Observable<List<BrandItem>> getBrandList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_BRAND_LIST)
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> {
                    List<BrandItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            BrandItem item = new BrandItem();
                            item.type = (int) rec.get("type");
                            item.thumb = (String) rec.get("thumb");
                            items.add(item);
                        }
                    }
                    return Observable.just(items);
                });
    }

    public static Observable<List<CouponTab>> getCouponTab() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_COUPON_TAB)
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> {
                    List<CouponTab> tabs = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            CouponTab tab = new CouponTab();
                            tab.type = (int) rec.get("type");
                            tab.title = (String) rec.get("title");
                            tabs.add(tab);
                        }
                    }
                    return Observable.just(tabs);
                });
    }
}
