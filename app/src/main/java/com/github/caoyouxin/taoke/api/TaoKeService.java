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
    String API_ANONYMOUS_LOGIN = "tbk/user/anonymous/{hash}";

    String API_BANNER_LIST = "home/banner/list";
    String API_COUPON_TAB = "home/cate/list";
    String API_BRAND_LIST = "home/group/list";
    String API_AD_ZONE_LIST = "home/adZone/list";

    String API_HINT_LIST = "tbk/hints/{keyword}";
    String API_SEARCH_LIST = "tbk/search/{keyword}";
    String API_JU_SEARCH = "tbk/ju/{keyword}";

    String API_MESSAGE_LIST = "msg/list/{pageNo}";
    String API_REPORT = "msg/feedback";
    String API_UNREAD_MSG = "msg/unread/count";
    String API_READ_MSG = "msg/read/{id}";

    String API_FRIENDS_LIST = "tbk/team/list";
    String API_COUPON_LIST = "tbk/coupon/{cid}/{pNo}";
    String API_PRODUCT_LIST = "tbk/fav/{favId}/list/{pageNo}/v2";
    String API_ORDER_LIST = "tbk/order/list/{type}/{pageNo}";
    String API_GET_SHARE_LINK = "tbk/url/trans";
    String API_GET_SHARE_LINK2 = "tbk/share/save";

    String API_HELP_LIST = "app/help/list";
    String API_HELP_DOC_LIST = "blog/helpdoc/list";
    String API_NOVICE_LIST = "app/guide/list/{type}";
    String API_SHARE_APP_LIST = "app/share/img/url/list/{type}";

    String API_SEND_WITHDRAW = "tbk/withdraw/{amount}";
    String API_USER_AMOUNT = "tbk/candraw";
    String API_THIS_MOUNT_ESTIMATE = "tbk/estimate/this";
    String API_LAST_MOUNT_ESTIMATE = "tbk/estimate/that";
    String API_CAN_WITHDRAW = "tbk/user/canWithdraw";

    String API_ENROLL = "tbk/user/apply/4/agent";
    String API_COMPETE_INFO = "tbk/user/competeInfo";
    String API_DOWNLOAD_URL = "app/download/url";
    String API_CUSTOMER_SERVICE = "tbk/user/customerService";

    @Headers({"version: 1.9.1", "platform: android"})
    @POST("{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Body Object data, @Header("auth") String auth);

    @Headers({"version: 1.9.1", "platform: android", "Cache-Control: public, max-age=86400"})
    @GET("{api}")
    Observable<TaoKeData> tao(@Path("api") String api, @Header("auth") String auth);

    @Headers({"version: 1.9.1", "platform: android", "Cache-Control: public, max-age=86400"})
    @GET("{api}")
    Observable<TaoKeData> tao(@Path("api") String api);
}
