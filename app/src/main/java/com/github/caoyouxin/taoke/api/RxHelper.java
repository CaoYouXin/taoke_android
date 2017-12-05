package com.github.caoyouxin.taoke.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.ui.activity.SplashActivity;
import com.github.gnastnosaj.boilerplate.mvchelper.RxDataSource;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxHelper {

    private static boolean showing = false;

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
                    } else if (t.code.equals(5010)) {
                        throw new VersionLowException();
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

    public static <T> ObservableTransformer<T, T> rxHandleServerExp(Activity context) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread()).doOnError(o -> {
            if (!showing && o instanceof UnAuthException) {
                new AlertDialog.Builder(context).setPositiveButton(R.string.re_login_confirm,
                        (dialog, witch) -> {
                            showing = false;
                            UserData.clear();
                            context.startActivity(new Intent(context, SplashActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }).setMessage(R.string.re_login_hint).show();
                showing = true;
            }

            if (!showing && o instanceof VersionLowException) {
                new AlertDialog.Builder(context).setPositiveButton(R.string.go_to_update,
                        (dialog, witch) -> TaoKeApi.getDownloadUrl().compose(rxSchedulerHelper())
                                .compose(((BaseActivity) context).bindUntilEvent(ActivityEvent.DESTROY))
                                .subscribe((downloadUrl) -> {
                                    showing = false;
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(downloadUrl);
                                    intent.setData(content_url);
                                    context.startActivity(intent);
                                }, (throwable) -> {
                                    showing = false;
                                    Toast.makeText(context, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                })).setMessage(R.string.version_low).show();
                showing = true;
            }

            if (o instanceof ApiException) {
                Toast.makeText(context, o.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        }).subscribeOn(Schedulers.io());
    }

    public static class HandleServerExp implements RxDataSource.Hook {

        @Override
        public Observable hook(Context context, Observable observable) {
            return observable.observeOn(AndroidSchedulers.mainThread()).doOnError(o -> {
                if (!showing && o instanceof UnAuthException) {
                    new AlertDialog.Builder(context).setPositiveButton(R.string.re_login_confirm,
                            (dialog, witch) -> {
                                showing = false;
                                UserData.clear();
                                context.startActivity(new Intent(context, SplashActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }).setMessage(R.string.re_login_hint).show();
                    showing = true;
                }

                if (!showing && o instanceof VersionLowException) {
                    new AlertDialog.Builder(context).setPositiveButton(R.string.go_to_update,
                            (dialog, witch) -> TaoKeApi.getDownloadUrl().compose(rxSchedulerHelper())
                                    .compose(((BaseActivity) context).bindUntilEvent(ActivityEvent.DESTROY))
                                    .subscribe((downloadUrl) -> {
                                        showing = false;
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(downloadUrl);
                                        intent.setData(content_url);
                                        context.startActivity(intent);
                                    }, (throwable) -> {
                                        showing = false;
                                        Toast.makeText(context, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    })).setMessage(R.string.version_low).show();
                    showing = true;
                }
            }).subscribeOn(Schedulers.io());
        }

    }

}
