package com.github.caoyouxin.taoke.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jasontsang on 10/24/17.
 */

public interface TaoKeService {
    String API_VERIFICATION = "/tbk/phone/verify";
    String API_SIGN_IN = "tbk/user/login";
    String API_SIGN_UP = "tbk/user/register";
    String API_RESET_PASSWORD = "resetPassword";

    String API_HELP_LIST = "helpList";
    String API_BRAND_LIST = "brandList";
    String API_COUPON_TAB = "home/cate/list";
    String API_COUPON_LIST = "tbk/coupon/{cid}/{pNo}";
    String API_MESSAGE_LIST = "messageList";
    String API_PRODUCT_LIST = "productList";
    String API_ORDER_LIST = "orderList";
    String API_FRIENDS_LIST = "friendsList";
    String API_SEARCH_HINT_LIST = "searchHintList";
    String API_GET_SHARE_LINK = "tbk/url/trans";

    @POST("api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Body Object data, @Header("auth") String auth);

    @Headers("Cache-Control: public, max-age=86400")
    @GET("api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Header("auth") String auth);

    @Headers("Cache-Control: public, max-age=86400")
    @GET("api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api);
}
