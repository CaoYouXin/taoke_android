package com.github.caoyouxin.taoke.api;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.ui.activity.SplashActivity;
import com.github.gnastnosaj.boilerplate.Boilerplate;

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

    static <T extends TaoKeData> ObservableTransformer<T, T> handleResult() {
        return upstream ->
                upstream.flatMap(t -> {
                    System.out.println(t.toString());
                    if (t.code.equals(2000)) {
                        return Observable.just(t);
                    } else if (t.code.equals(4010)) {
                        new AlertDialog.Builder(Boilerplate.getInstance())
                                .setPositiveButton(R.string.sign_out, (DialogInterface dialog, int which) -> {
                            Boilerplate.getInstance().startActivity(
                                    new Intent(Boilerplate.getInstance(), SplashActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            );
                            TaoKeApi.clearToken();
                        }).show();
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
