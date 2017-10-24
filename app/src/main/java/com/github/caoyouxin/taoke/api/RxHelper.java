package com.github.caoyouxin.taoke.api;

import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jasontsang on 10/24/17.
 */

public class RxHelper {
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T extends TaoKeData> ObservableTransformer<T, T> handleResult() {
        return upstream ->
                upstream.flatMap(t -> {
                    String resultCode = (String) t.header.get("ResultCode");
                    if (!TextUtils.isEmpty(resultCode) && resultCode.equals("0000")) {
                        return Observable.just(t);
                    } else {
                        String message = (String) t.header.get("Message");
                        if (TextUtils.isEmpty(message)) {
                            throw new ApiException();
                        } else {
                            throw new ApiException(message);
                        }
                    }
                });
    }
}
