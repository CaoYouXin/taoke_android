package com.github.caoyouxin.taoke.api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface TaoKeService {
    String API_VERIFICATION = "tbk/phone/verify";
    String API_SIGN_IN = "tbk/user/login";
    String API_SIGN_UP = "tbk/user/register";
    String API_RESET_PASSWORD = "tbk/user/reset/pwd";

    String API_BANNER_LIST = "home/banner/list";
    String API_COUPON_TAB = "home/cate/list";
    String API_BRAND_LIST = "home/group/list";

    String API_HINT_LIST = "tbk/hints/{keyword}";
    String API_SEARCH_LIST = "tbk/search/{keyword}";

    String API_MESSAGE_LIST = "msg/list/{pageNo}";
    String API_REPORT = "msg/feedback";

    String API_FRIENDS_LIST = "tbk/team/list";
    String API_COUPON_LIST = "tbk/coupon/{cid}/{pNo}";
    String API_PRODUCT_LIST = "tbk/fav/{favId}/list/{pageNo}";
    String API_ORDER_LIST = "tbk/order/list/{type}/{pageNo}";
    String API_GET_SHARE_LINK = "tbk/url/trans";

    String API_HELP_LIST = "app/help/list";
    String API_NOVICE_LIST = "app/guide/list";
    String API_SHARE_APP_LIST = "app/share/img/url/list";

    String API_SEND_WITHDRAW = "tbk/withdraw/{amount}";
    String API_USER_AMOUNT = "tbk/candraw";
    String API_THIS_MOUNT_ESTIMATE = "tbk/estimate/this";
    String API_LAST_MOUNT_ESTIMATE = "tbk/estimate/that";

    String API_ENROLL = "tbk/user/apply/4/agent";

    @POST("api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Body Object data, @Header("auth") String auth);

    @Headers("Cache-Control: public, max-age=86400")
    @GET("api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Header("auth") String auth);

    @Headers("Cache-Control: public, max-age=86400")
    @GET("api/{api}")
    Observable<TaoKeData> tao(@Path("api") String api);
}
