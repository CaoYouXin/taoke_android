package com.github.caoyouxin.taoke.api;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.caoyouxin.taoke.datasource.OrderDataSource;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.CouponTab;
import com.github.caoyouxin.taoke.model.FriendItem;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.caoyouxin.taoke.model.HomeBtn;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.caoyouxin.taoke.model.Product;
import com.github.caoyouxin.taoke.model.OrderItem;
import com.github.caoyouxin.taoke.model.SearchHintItem;
import com.github.caoyouxin.taoke.model.ShareSubmit;
import com.github.caoyouxin.taoke.model.UserLoginSubmit;
import com.github.caoyouxin.taoke.model.UserRegisterSubmit;
import com.github.caoyouxin.taoke.util.StringUtils;
import com.github.gnastnosaj.boilerplate.Boilerplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeApi {

    private final static String PREF_ACCESS_TOKEN = "token";
    private final static String CDN_HOST = "http://192.168.1.102:8070/";

    private static String accessToken;

    // **** user apis below *******************************************

    public static Observable<TaoKeData> verification(String phone) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_VERIFICATION, phone, null)
                .compose(RxHelper.handleResult());
    }

    public static Observable<TaoKeData> signUp(String phone, String verificationCode, String password) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_SIGN_UP,
                new UserRegisterSubmit(verificationCode, phone, StringUtils.toMD5HexString(password)), null)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    accessToken = (String) taoKeData.getMap().get(PREF_ACCESS_TOKEN);
                    cacheToken();
                    return taoKeData;
                });
    }

    public static Observable<TaoKeData> signIn(String phone, String password) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_SIGN_IN,
                new UserLoginSubmit(phone, StringUtils.toMD5HexString(password)), null)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    accessToken = (String) taoKeData.getMap().get(PREF_ACCESS_TOKEN);
                    cacheToken();
                    return taoKeData;
                });
    }

    public static Observable<TaoKeData> resetPassword(String phone, String verificationCode, String password) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_RESET_PASSWORD, null, null)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    accessToken = (String) taoKeData.getMap().get(PREF_ACCESS_TOKEN);
                    cacheToken();
                    return taoKeData;
                });
    }

    private static void cacheToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static boolean restoreToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        if (sharedPreferences.contains(PREF_ACCESS_TOKEN)) {
            accessToken = sharedPreferences.getString(PREF_ACCESS_TOKEN, null);
            return true;
        } else {
            return false;
        }
    }

    public static void clearToken() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Boilerplate.getInstance());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        accessToken = null;
    }

    // **** user apis above *******************************************

    public static Observable<List<HomeBtn>> getBannerList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_BANNER_LIST)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<HomeBtn> items = new ArrayList<>();
                    for (Map rec : taoKeData.getList()) {
                        HomeBtn item = new HomeBtn();
                        item.imgUrl = CDN_HOST + rec.get("imgUrl");
                        item.name = (String) rec.get("name");
                        item.openType = ((Double) rec.get("openType")).intValue();
                        item.ext = (String) rec.get("ext");
                        items.add(item);
                    }
                    return items;
                });
    }

    public static Observable<List<BrandItem>> getBrandList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_BRAND_LIST)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<BrandItem> items = new ArrayList<>();
//                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
//                    if (recs != null) {
//                        for (Map rec : recs) {
//                            BrandItem item = new BrandItem();
//                            item.type = (int) rec.get("type");
//                            item.title = (String) rec.get("title");
//                            item.thumb = (String) rec.get("thumb");
//                            items.add(item);
//                        }
//                    }
                    return items;
                });
    }

    public static Observable<List<CouponItem>> getProductList(BrandItem brandItem, int pageNo, int sort) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_PRODUCT_LIST.replace("{favId}", brandItem.favId).replace("{pageNo}", "" + pageNo), accessToken)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<CouponItem> items = new ArrayList<>();
                    List<Map> recs = taoKeData.getList();
                    for (Map rec : recs) {
                        CouponItem item = new CouponItem();

                        item.setTkLink((String) rec.get("clickUrl"));
                        item.setCategory(((Double) rec.get("category")).longValue());
                        item.setUserType(((Double) rec.get("userType")).longValue());
                        item.setNumIid(((Double) rec.get("numIid")).longValue());
                        item.setSellerId(((Double) rec.get("sellerId")).longValue());
                        item.setVolume(((Double) rec.get("volume")).longValue());
                        item.setSmallImages((List<String>) rec.get("smallImages"));

                        item.setCommissionRate((String) rec.get("tkRate"));
//                        if (null == item.getCommissionRate() || Double.parseDouble(item.getCommissionRate()) < 0.001) {
//                            item.setCommissionRate((String) rec.get("commissionRate"));
//                        }

                        item.setZkFinalPrice((String) rec.get("zkFinalPriceWap"));
//                        if (null == item.getZkFinalPrice() || Double.parseDouble(item.getZkFinalPrice()) < 0.001) {
//                            item.setZkFinalPrice((String) rec.get("zkFinalPrice"));
//                        }

                        item.setItemUrl((String) rec.get("itemUrl"));
                        item.setNick((String) rec.get("nick"));
                        item.setPictUrl((String) rec.get("pictUrl"));
                        item.setTitle((String) rec.get("title"));
                        item.setShopTitle((String) rec.get("shopTitle"));
                        item.setItemDescription((String) rec.get("itemDescription"));

                        item.setCouponInfo((String) rec.get("couponInfo"));
                        if (null != item.getCouponInfo()) {
                            item.setCouponClickUrl((String) rec.get("couponClickUrl"));
                            item.setCouponRemainCount(((Double) rec.get("couponRemainCount")).longValue());
                            item.setCouponTotalCount(((Double) rec.get("couponTotalCount")).longValue());
                            item.setCouponEndTime((String) rec.get("couponEndTime"));
                            item.setCouponStartTime((String) rec.get("couponStartTime"));

                            int start = item.getCouponInfo().indexOf('减') + 1;
                            int end = item.getCouponInfo().indexOf('元', start);
                            double couponPrice = Double.parseDouble(item.getZkFinalPrice()) - Double.parseDouble(item.getCouponInfo().substring(start, end));
                            item.setCouponPrice(String.format(Locale.ENGLISH, "%.2f", couponPrice));

                            item.setEarnPrice(String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(item.getCommissionRate()) * couponPrice / 100));
                        } else {
                            item.setEarnPrice(String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(item.getZkFinalPrice()) * Double.parseDouble(item.getCommissionRate()) / 100));
                        }

                        System.out.println(item.getTkLink());
                        items.add(item);
                    }
                    return items;
                });
    }

    public static Observable<List<CouponTab>> getCouponTab() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_COUPON_TAB)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<CouponTab> tabs = new ArrayList<>();
                    List<Map> recs = (List<Map>) taoKeData.body;
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

    public static Observable<List<CouponItem>> getCouponList(String cid, String pageNo) {
        return TaoKeRetrofit.getService().tao(
                TaoKeService.API_COUPON_LIST.replace("{cid}", cid).replace("{pNo}", pageNo), accessToken
        )
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<CouponItem> items = new ArrayList<>();
                    List<Map> recs = taoKeData.getList();
                    for (Map rec : recs) {
                        CouponItem item = new CouponItem();

                        item.setCategory(((Double) rec.get("category")).longValue());
                        item.setCouponRemainCount(((Double) rec.get("couponRemainCount")).longValue());
                        item.setCouponTotalCount(((Double) rec.get("couponTotalCount")).longValue());
                        item.setUserType(((Double) rec.get("userType")).longValue());
                        item.setNumIid(((Double) rec.get("numIid")).longValue());
                        item.setSellerId(((Double) rec.get("sellerId")).longValue());
                        item.setVolume(((Double) rec.get("volume")).longValue());
                        item.setSmallImages((List<String>) rec.get("smallImages"));
                        item.setCommissionRate((String) rec.get("commissionRate"));
                        item.setCouponClickUrl((String) rec.get("couponClickUrl"));
                        item.setCouponEndTime((String) rec.get("couponEndTime"));
                        item.setCouponInfo((String) rec.get("couponInfo"));
                        item.setCouponStartTime((String) rec.get("couponStartTime"));
                        item.setZkFinalPrice((String) rec.get("zkFinalPrice"));
                        item.setItemUrl((String) rec.get("itemUrl"));
                        item.setNick((String) rec.get("nick"));
                        item.setPictUrl((String) rec.get("pictUrl"));
                        item.setTitle((String) rec.get("title"));
                        item.setShopTitle((String) rec.get("shopTitle"));
                        item.setItemDescription((String) rec.get("itemDescription"));

                        int start = item.getCouponInfo().indexOf('减') + 1;
                        int end = item.getCouponInfo().indexOf('元', start);
                        double couponPrice = Double.parseDouble(item.getZkFinalPrice()) - Double.parseDouble(item.getCouponInfo().substring(start, end));
                        item.setCouponPrice(String.format(Locale.ENGLISH, "%.2f", couponPrice));
                        item.setEarnPrice(String.format(Locale.ENGLISH, "%.2f", Double.parseDouble((String) rec.get("commissionRate")) * couponPrice / 100));

                        items.add(item);
                    }
                    return items;
                });
    }

    public static Observable<List<HelpItem>> getHelpList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_HELP_LIST)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<HelpItem> items = new ArrayList<>();
//                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
//                    if (recs != null) {
//                        for (Map rec : recs) {
//                            HelpItem item = new HelpItem();
//                            item.q = "Q: " + (String) rec.get("q");
//                            item.a = (String) rec.get("a");
//                            items.add(item);
//                        }
//                    }
                    return items;
                });
    }

    public static Observable<List<MessageItem>> getMessageList(String type) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_MESSAGE_LIST, type, "")
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<MessageItem> items = new ArrayList<>();
//                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
//                    if (recs != null) {
//                        for (Map rec : recs) {
//                            MessageItem item = new MessageItem();
//                            item.title = (String) rec.get("title");
//                            item.dateStr = (String) rec.get("date");
//                            item.content = (String) rec.get("content");
//                            items.add(item);
//                        }
//                    }
//                    Collections.sort(items);
                    return items;
                });
    }

    public static Observable<List<OrderItem>> getOrderList(OrderDataSource.FetchType type, Integer pageNo) {
        return TaoKeRetrofit.getService().tao(
                TaoKeService.API_ORDER_LIST.replace("{type}", "" + type.getType())
                        .replace("{pageNo}", "" + pageNo),
                accessToken)
                .compose(RxHelper.handleResult())
                .map(taoKeData -> {
                    List<OrderItem> items = new ArrayList<>();
                    for (Map rec : taoKeData.getList()) {
                        OrderItem item = new OrderItem();
                        item.itemName = (String) rec.get("itemTitle");
                        item.itemStoreName = (String) rec.get("shopTitle");
                        item.dateStr = (String) rec.get("createTime");
                        item.status = (String) rec.get("orderStatus");
                        item.itemTradePrice = (String) rec.get("payedAmount");
                        item.commission = (String) rec.get("commissionRate");
                        item.estimateIncome = (String) rec.get("estimateIncome");
                        item.estimateEffect = (String) rec.get("estimateEffect");
                        items.add(item);
                    }
                    return items;
                });
    }

    public static Observable<List<FriendItem>> getFriendsList() {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_FRIENDS_LIST)
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> {
                    List<FriendItem> items = new ArrayList<>();
//                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
//                    if (recs != null) {
//                        for (Map rec : recs) {
//                            FriendItem item = new FriendItem();
//                            item.amount = (Double) rec.get("amount");
//                            item.name = (String) rec.get("name");
//                            items.add(item);
//                        }
//                    }
                    return Observable.just(items);
                });
    }

    public static Observable<List<SearchHintItem>> getSearchHintList(String inputNow) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_SEARCH_HINT_LIST, inputNow, "")
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> {
                    List<SearchHintItem> items = new ArrayList<>();
//                    List<Map> recs = (List<Map>) taoKeData.body.get("recs");
//                    if (recs != null) {
//                        for (Map rec : recs) {
//                            SearchHintItem item = new SearchHintItem();
//                            item.hint = (String) rec.get("hint");
//                            items.add(item);
//                        }
//                    }
                    return Observable.just(items);
                });
    }

    public static Observable<String> getLink(String couponClickUrl, String title) {
        return TaoKeRetrofit.getService().tao(TaoKeService.API_GET_SHARE_LINK, new ShareSubmit(title, couponClickUrl), accessToken)
                .compose(RxHelper.handleResult())
                .flatMap(taoKeData -> Observable.just(taoKeData.body.toString()));
    }
}
