package com.github.caoyouxin.taoke.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.ui.activity.SplashActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxHelper {
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T extends TaoKeData> ObservableTransformer<T, T> handleResult() {
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

    public static <T> ObservableTransformer<T, T> rxHandlerUnAuth(Activity context) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread()).onErrorReturn(o -> {
            if (o instanceof UnAuthException) {
                new AlertDialog.Builder(context).setPositiveButton(R.string.re_login_confirm,
                        (dialog, witch) -> {
                            UserData.clear();
                            context.startActivity(new Intent(context, SplashActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }).setMessage(R.string.re_login_hint).show();
            }
            return null;
        }).subscribeOn(Schedulers.io());
    }
}
