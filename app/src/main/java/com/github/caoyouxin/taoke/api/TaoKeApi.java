package com.github.caoyouxin.taoke.api;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.caoyouxin.taoke.datasource.OrderDataSource;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.CouponItemDetail;
import com.github.caoyouxin.taoke.model.CouponTab;
import com.github.caoyouxin.taoke.model.FriendItem;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.caoyouxin.taoke.model.Product;
import com.github.caoyouxin.taoke.model.OrderItem;
import com.github.caoyouxin.taoke.model.SearchHintItem;
import com.github.caoyouxin.taoke.model.UserLoginSubmit;
import com.github.gnastnosaj.boilerplate.Boilerplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeApi {
    private final static String PREF_ACCESS_TOKEN = "token";

    private static String accessToken;

    public static Observable<TaoKeData> verification(String phone) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_VERIFICATION, null, null)
                .compose(RxHelper.handleResult());
    }

    public static Observable<TaoKeData> signUp(String phone, String verificationCode, String password) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_SIGN_UP, null, null)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    accessToken = (String) taoKeData.body.get(PREF_ACCESS_TOKEN);
                    cacheCustInfo();
                    return taoKeData;
                });
    }

    public static Observable<TaoKeData> signIn(String phone, String password) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_SIGN_IN, new UserLoginSubmit(phone, password), null)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    accessToken = (String) taoKeData.body.get(PREF_ACCESS_TOKEN);
                    cacheCustInfo();
                    return taoKeData;
                });
    }

    public static Observable<TaoKeData> resetPassword(String phone, String verificationCode, String password) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_RESET_PASSWORD, null, null)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    accessToken = (String) taoKeData.body.get(PREF_ACCESS_TOKEN);
                    cacheCustInfo();
                    return taoKeData;
                });
    }

    public static void cacheCustInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static boolean restoreCustInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        if (sharedPreferences.contains(PREF_ACCESS_TOKEN)) {
            accessToken = sharedPreferences.getString(PREF_ACCESS_TOKEN, null);
            return true;
        } else {
            return false;
        }
    }

    public static void clearCustInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        accessToken = null;
    }

    public static Observable<List<BrandItem>> getBrandList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_BRAND_LIST)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<BrandItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            BrandItem item = new BrandItem();
                            item.type = (int) rec.get("type");
                            item.title = (String) rec.get("title");
                            item.thumb = (String) rec.get("thumb");
                            items.add(item);
                        }
                    }
                    return items;
                });
    }

    public static Observable<List<Product>> getProductList(BrandItem brandItem) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_PRODUCT_LIST + "/" + brandItem.type)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<Product> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            Product item = new Product();
                            item.id = (int) rec.get("id");
                            item.thumb = (String) rec.get("thumb");
                            item.title = (String) rec.get("title");
                            item.isNew = (boolean) rec.get("isNew");
                            item.price = (String) rec.get("price");
                            item.sales = (int) rec.get("sales");
                            items.add(item);
                        }
                    }
                    return items;
                });
    }

    public static Observable<List<CouponTab>> getCouponTab() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_COUPON_TAB)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<CouponTab> tabs = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            CouponTab tab = new CouponTab();
                            tab.cid = (String) rec.get("cid");
                            tab.name = (String) rec.get("name");
                            tabs.add(tab);
                        }
                    }
                    return tabs;
                });
    }

    public static Observable<List<CouponItem>> getCouponList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_COUPON_LIST)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<CouponItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            CouponItem item = new CouponItem();
                            item.id = (int) rec.get("id");
                            item.thumb = (String) rec.get("thumb");
                            item.title = (String) rec.get("title");
                            item.priceBefore = (String) rec.get("priceBefore");
                            item.sales = (int) rec.get("sales");
                            item.priceAfter = (String) rec.get("priceAfter");
                            item.value = (String) rec.get("value");
                            item.total = (int) rec.get("total");
                            item.left = (int) rec.get("left");
                            item.earn = (String) rec.get("earn");
                            items.add(item);
                        }
                    }
                    return items;
                });
    }

    public static Observable<List<HelpItem>> getHelpList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_HELP_LIST)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<HelpItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            HelpItem item = new HelpItem();
                            item.q = "Q: " + (String) rec.get("q");
                            item.a = (String) rec.get("a");
                            items.add(item);
                        }
                    }
                    return items;
                });
    }

    public static Observable<CouponItemDetail> getCouponDetail(CouponItem couponItem) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_COUPON_DETAIL + "/" + couponItem.id)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    CouponItemDetail couponItemDetail = new CouponItemDetail();
                    couponItemDetail.thumb = (String) taoKeData.body.get("thumb");
                    couponItemDetail.title = (String) taoKeData.body.get("title");
                    couponItemDetail.priceAfter = (String) taoKeData.body.get("priceAfter");
                    couponItemDetail.priceBefore = (String) taoKeData.body.get("priceBefore");
                    couponItemDetail.sales = (int) taoKeData.body.get("sales");
                    couponItemDetail.coupon = (String) taoKeData.body.get("coupon");
                    couponItemDetail.couponRequirement = (String) taoKeData.body.get("couponRequirement");
                    couponItemDetail.commissionPercent = (String) taoKeData.body.get("commissionPercent");
                    couponItemDetail.commission = (String) taoKeData.body.get("commission");
                    return couponItemDetail;
                });
    }

    public static Observable<List<String>> getCouponShareImageList(CouponItem couponItem) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_COUPON_SHARE_IMAGE_LIST + "/" + couponItem.id)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> (List<String>) taoKeData.body.get("images"));
    }

    public static Observable<List<MessageItem>> getMessageList(String type) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_MESSAGE_LIST, type, "")
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<MessageItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            MessageItem item = new MessageItem();
                            item.title = (String) rec.get("title");
                            item.dateStr = (String) rec.get("date");
                            item.content = (String) rec.get("content");
                            items.add(item);
                        }
                    }
                    Collections.sort(items);
                    return items;
                });
    }

    public static Observable<List<OrderItem>> getOrderList(OrderDataSource.FetchType type) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_ORDER_LIST, type.toString(), "")
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<OrderItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            OrderItem item = new OrderItem();
                            item.itemName = (String) rec.get("item_name");
                            item.itemStoreName = (String) rec.get("item_store");
                            item.dateStr = (String) rec.get("date");
                            item.itemImgUrl = (String) rec.get("thumb");
                            item.status = (String) rec.get("status");
                            item.itemTradePrice = (Double) rec.get("price");
                            item.commission = (Double) rec.get("commission");
                            items.add(item);
                        }
                    }
                    Collections.sort(items);
                    return items;
                });
    }

    public static Observable<List<FriendItem>> getFriendsList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_FRIENDS_LIST)
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> {
                    List<FriendItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            FriendItem item = new FriendItem();
                            item.amount = (Double) rec.get("amount");
                            item.name = (String) rec.get("name");
                            items.add(item);
                        }
                    }
                    return Observable.just(items);
                });
    }

    public static Observable<List<SearchHintItem>> getSearchHintList(String inputNow) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_SEARCH_HINT_LIST, inputNow, "")
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> {
                    List<SearchHintItem> items = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
                    if (recs != null) {
                        for (Map rec : recs) {
                            SearchHintItem item = new SearchHintItem();
                            item.hint = (String) rec.get("hint");
                            items.add(item);
                        }
                    }
                    return Observable.just(items);
                });
    }
}
