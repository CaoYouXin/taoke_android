package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/23/17.
 */

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences.getBoolean("intro_read", false)) {
                startActivity(new Intent(this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            } else {
                startActivity(new Intent(this, TaoKeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }
}
