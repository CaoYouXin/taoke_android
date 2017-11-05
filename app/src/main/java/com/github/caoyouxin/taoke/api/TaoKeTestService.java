package com.github.caoyouxin.taoke.api;

import android.support.v4.util.ArrayMap;

import com.github.caoyouxin.taoke.model.M;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Path;

import static com.github.caoyouxin.taoke.datasource.OrderDataSource.FetchType.*;

/**
 * Created by jasontsang on 10/24/17.
 */

public class TaoKeTestService implements TaoKeService {
    @Override
    public Observable<TaoKeData> tao(@Path("api") String api, @Body Object data, @Header("auth") String auth) {
        TaoKeData taoKeData = new TaoKeData();
        taoKeData.header = new ArrayMap<>();
        taoKeData.body = new ArrayMap<>();
        switch (api) {
            case API_VERIFICATION:
                taoKeData.header.put("ResultCode", "0000");
                return Observable.just(taoKeData);
            case API_SIGN_IN:
                taoKeData.header.put("ResultCode", "0000");
                taoKeData.body.put("access_token", "sadfasdfd");
                taoKeData.body.put("cust_id", "asdfasdf");
                return Observable.just(taoKeData);
            case API_SIGN_UP:
                taoKeData.header.put("ResultCode", "0000");
                taoKeData.body.put("access_token", "sadfasdfd");
                taoKeData.body.put("cust_id", "asdfasdf");
                return Observable.just(taoKeData);
            case API_RESET_PASSWORD:
                taoKeData.header.put("ResultCode", "0000");
                taoKeData.body.put("access_token", "sadfasdfd");
                taoKeData.body.put("cust_id", "asdfasdf");
                return Observable.just(taoKeData);
            case API_MESSAGE_LIST:
                taoKeData.header.put("ResultCode", "0000");

                List<Map> recs2 = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                for (int i = 0; i < 10; i++) {
                    Map item = new ArrayMap();
                    item.put("title", data + "标题" + i);

                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    item.put("date", M.DF.format(calendar.getTime()));

                    item.put("content", Math.random() > 0.5 ? "1. 消息\n2. 消息2\n\n诚至为您服务" : "如此如此\n\n诚至为您服务");
                    recs2.add(item);
                }
                taoKeData.body.put("recs", recs2);

                return Observable.just(taoKeData);
            case API_ORDER_LIST:
                taoKeData.header.put("ResultCode", "0000");

                String[] thumbs = new String[]{"http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                };

                String[] randomCandidates = new String[0];
                switch (valueOf(data.toString())) {
                    case ALL:
                        randomCandidates = new String[]{"已支付", "已结算", "已收货", "已失效"};
                        break;
                    case ALL_EFFECTIVE:
                        randomCandidates = new String[]{"已支付", "已结算", "已收货"};
                        break;
                    case EFFECTIVE_PAYED:
                        randomCandidates = new String[]{"已支付"};
                        break;
                    case EFFECTIVE_CONSIGNED:
                        randomCandidates = new String[]{"已收货"};
                        break;
                    case EFFECTIVE_SETTLED:
                        randomCandidates = new String[]{"已结算"};
                        break;
                    case INEFFECTIVE:
                        randomCandidates = new String[]{"已失效"};
                        break;
                }

                List<Map> recs = new ArrayList<>();
                Calendar calendar2 = Calendar.getInstance();
                for (int i = 0; i < thumbs.length; i++) {
                    Map item = new ArrayMap();
                    item.put("item_name", "（买就送5双棉袜共10双）秋冬保暖羊毛袜男女中筒袜冬季保暖袜");
                    item.put("item_store", "123服装店");
                    calendar2.add(Calendar.HOUR_OF_DAY, (int) (Math.random() * 5 + 1));
                    item.put("date", M.DF.format(calendar2.getTime()));
                    item.put("thumb", thumbs[i]);
                    item.put("price", Math.random() * 100);
                    item.put("commission", Math.random());
                    item.put("status", randomCandidates[(int) (randomCandidates.length * Math.random())]);
                    recs.add(item);
                }
                taoKeData.body.put("recs", recs);

                return Observable.just(taoKeData);
            case API_SEARCH_HINT_LIST:
                taoKeData.header.put("ResultCode", "0000");

                List<Map> recs3 = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    Map item = new ArrayMap();
                    item.put("hint", "与" + data + "相关的搜索词" + i);
                    recs3.add(item);
                }
                taoKeData.body.put("recs", recs3);

                return Observable.just(taoKeData);
        }

        return Observable.empty();
    }

    @Override
    public Observable<TaoKeData> tao(@Path("api") String api) {
        TaoKeData taoKeData = new TaoKeData();
        taoKeData.header = new ArrayMap<>();
        taoKeData.body = new ArrayMap<>();

        switch (api) {
            case API_BRAND_LIST:
                taoKeData.header.put("ResultCode", "0000");

                String[] brandThumbs = new String[]{"http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg"};
                String[] brandTitles = new String[]{"今日上新", "聚划算", "品牌券"};
                //String[] thumbs = new String[]{"", "", ""};
                List<Map> brandItems = new ArrayList<>();
                for (int i = 0; i < brandThumbs.length; i++) {
                    Map item = new ArrayMap();
                    item.put("type", i);
                    item.put("title", brandTitles[i]);
                    item.put("thumb", brandThumbs[i]);
                    brandItems.add(item);
                }
                taoKeData.body.put("recs", brandItems);
                return Observable.just(taoKeData);
            case API_COUPON_TAB:
                taoKeData.header.put("ResultCode", "0000");

                String[] couponTitles = new String[]{"精选", "女装", "家居家装", "数码家电", "母婴", "食品", "鞋包配饰", "美妆个护", "男装", "内衣", "户外运动"};
                List<Map> tabs = new ArrayList<>();
                for (int i = 0; i < couponTitles.length; i++) {
                    Map tab = new ArrayMap();
                    tab.put("cid", i + "");
                    tab.put("name", couponTitles[i]);
                    tabs.add(tab);
                }
                taoKeData.body.put("recs", tabs);
                return Observable.just(taoKeData);
            case API_COUPON_LIST:
                taoKeData.header.put("ResultCode", "0000");

                String[] couponThumbs = new String[]{"http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg"};
                List<Map> couponItems = new ArrayList<>();
                for (int i = 0; i < couponThumbs.length; i++) {
                    Map item = new ArrayMap();
                    item.put("id", i);
                    item.put("thumb", couponThumbs[i]);
                    item.put("title", "（买就送5双棉袜共10双）秋冬保暖羊毛袜男女中筒袜冬季保暖袜");
                    item.put("priceBefore", "34.90");
                    item.put("sales", 10);
                    item.put("priceAfter", "14.90");
                    item.put("value", "20");
                    item.put("total", 130000);
                    if (i % 3 == 0) {
                        item.put("left", 59036);
                    } else if (i % 3 == 1) {
                        item.put("left", 63036);
                    } else {
                        item.put("left", 98036);
                    }
                    item.put("earn", "0.33");
                    couponItems.add(item);
                }
                taoKeData.body.put("recs", couponItems);
                return Observable.just(taoKeData);
            case API_HELP_LIST:
                taoKeData.header.put("ResultCode", "0000");

                List<Map> recs = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    Map item = new ArrayMap();
                    item.put("q", Math.random() > 0.5 ? "问题" + i : "这是一个很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的问题");
                    item.put("a", Math.random() > 0.5 ? "1. 方案1\n2. 方案2" : "如此如此");
                    recs.add(item);
                }
                taoKeData.body.put("recs", recs);

                return Observable.just(taoKeData);
            case API_FRIENDS_LIST:
                taoKeData.header.put("ResultCode", "0000");

                List<Map> recs2 = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    Map item = new ArrayMap();
                    item.put("amount", Math.random() * 1000);
                    item.put("name", "淘客" + ("" + Math.random()).substring(2, 8));
                    recs2.add(item);
                }
                taoKeData.body.put("recs", recs2);

                return Observable.just(taoKeData);
        }

        if (api.startsWith(API_PRODUCT_LIST)) {
            taoKeData.header.put("ResultCode", "0000");

            String[] thumbs = new String[]{"https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fitcjyruajj20u011h412.jpg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171012073213_p4H630_joycechu_syc_12_10_2017_7_32_7_433.jpeg", "https://ws1.sinaimg.cn/large/610dc034ly1fk05lf9f4cj20u011h423.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjppsiclufj20u011igo5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fjfae1hjslj20u00tyq4x.jpg"};
            //String[] thumbs = new String[]{"", "", ""};
            List<Map> items = new ArrayList<>();
            for (int i = 0; i < thumbs.length; i++) {
                Map item = new ArrayMap();
                item.put("id", i);
                item.put("thumb", thumbs[i]);
                item.put("title", "冬季毛绒沙发垫加厚保暖简约法兰绒坐垫布艺防滑沙发套沙发罩全盖");
                if (i % 2 == 0) {
                    item.put("isNew", true);
                } else {
                    item.put("isNew", false);
                }
                item.put("price", "328");
                item.put("sales", 711);
                items.add(item);
            }
            taoKeData.body.put("recs", items);
            return Observable.just(taoKeData);
        } else if (api.startsWith(API_COUPON_DETAIL)) {
            taoKeData.header.put("ResultCode", "0000");
            taoKeData.body.put("id", 0);
            taoKeData.body.put("thumb", "http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg");
            taoKeData.body.put("title", "冬季毛绒沙发垫加厚保暖简约法兰绒坐垫布艺防滑沙发套沙发罩全盖");
            taoKeData.body.put("priceAfter", "99.00");
            taoKeData.body.put("priceBefore", "399.00");
            taoKeData.body.put("sales", 3580);
            taoKeData.body.put("coupon", "300.0");
            taoKeData.body.put("couponRequirement", "398.0");
            taoKeData.body.put("commissionPercent", "5.50%");
            taoKeData.body.put("commission", "5.45");
            return Observable.just(taoKeData);
        } else if (api.startsWith(API_COUPON_SHARE_IMAGE_LIST)) {
            taoKeData.header.put("ResultCode", "0000");

            String[] thumbs = new String[]{"http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171024083526_Hq4gO6_bluenamchu_24_10_2017_8_34_28_246.jpeg", "http://7xi8d6.com1.z0.glb.clouddn.com/20171018091347_Z81Beh_nini.nicky_18_10_2017_9_13_35_727.jpeg"};
            taoKeData.body.put("images", Arrays.asList(thumbs));

            return Observable.just(taoKeData);
        }

        return Observable.empty();
    }
}
