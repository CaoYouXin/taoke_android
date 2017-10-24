package com.github.caoyouxin.taoke.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by jasontsang on 10/24/17.
 */

public interface TaoKeService {
    @Headers("Cache-Control: public, max-age=86400")
    @GET("/api/coupontab")
    Observable<List<CouponTab>> getCouponTabData();
}
