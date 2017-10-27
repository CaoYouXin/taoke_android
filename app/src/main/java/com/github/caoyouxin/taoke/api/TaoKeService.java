package com.github.caoyouxin.taoke.api;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jasontsang on 10/24/17.
 */

public interface TaoKeService {

    String API_HELP_LIST = "helpList";
    String API_BRAND_LIST = "brandList";
    String API_COUPON_TAB = "couponTabList";
    String API_COUPON_LIST = "couponList";
    String API_MESSAGE_LIST = "messageList";
    String API_COUPON_DETAIL = "couponDetail";

    @FormUrlEncoded
    @POST("/api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Field("data") String data, @Field("signature") String signature);

    @Headers("Cache-Control: public, max-age=86400")
    @GET("/api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api);
}
