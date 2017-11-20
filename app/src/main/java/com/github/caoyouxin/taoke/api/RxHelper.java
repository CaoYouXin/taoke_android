package com.github.caoyouxin.taoke.api;

import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxHelper {
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    static <T extends TaoKeData> ObservableTransformer<T, T> handleResult() {
        return upstream ->
                upstream.flatMap(t -> {
                    System.out.println(t.toString());
                    if (t.code.equals(2000)) {
                        return Observable.just(t);
                    } else if (t.code.equals(4010)) {
                        throw new UnAuthException();
                    } else {
                        String message = (String) t.getMap().get("msg");
                        if (TextUtils.isEmpty(message)) {
                            throw new ApiException();
                        } else {
                            throw new ApiException(message);
                        }
                    }
                });
    }
}
