package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.ApiException;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.util.RegUtils;
import com.github.gnastnosaj.boilerplate.util.keyboard.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 8/21/17.
 */

public class EnrollActivity extends BaseActivity {

    @BindView(R.id.progress)
    View progress;

    @BindView(R.id.enroll)
    TextView enroll;

    @BindView(R.id.alipay)
    EditText alipay;

    @BindView(R.id.qq)
    EditText qq;

    @BindView(R.id.wechat)
    EditText wechat;

    @BindView(R.id.announcement)
    EditText announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enroll);

        ButterKnife.bind(this);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(alipay.getEditableText().toString().trim()) && (
                        !TextUtils.isEmpty(qq.getEditableText().toString().trim()) ||
                                !TextUtils.isEmpty(wechat.getEditableText().toString().trim())
                        ) && !TextUtils.isEmpty(announcement.getEditableText().toString().trim())) {
                    enroll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    enroll.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };

        alipay.addTextChangedListener(textWatcher);
        qq.addTextChangedListener(textWatcher);
        wechat.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.back, R.id.enroll})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.enroll:
                enroll();
                break;
        }
    }

    private void enroll() {
//        String phoneNo = phone.getEditableText().toString().trim();
//        if (TextUtils.isEmpty(phoneNo) || !RegUtils.isMobile(phoneNo)) {
//            phone.requestFocus();
//            return;
//        }
//        String pwd = password.getEditableText().toString().trim();
//        if (pwd.length() < 6) {
//            password.requestFocus();
//            return;
//        }

        alipay.setEnabled(false);
        qq.setEnabled(false);
        wechat.setEnabled(false);
        enroll.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

//        TaoKeApi.signIn(phoneNo, pwd)
//                .timeout(10, TimeUnit.SECONDS)
//                .compose(RxHelper.rxSchedulerHelper())
//                .compose(bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(
//                        taoKeData -> {
//                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//                            if (sharedPreferences.getBoolean(IntroActivity.INTRO_READ, false)) {
//                                startActivity(new Intent(this, TaoKeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                            } else {
//                                startActivity(new Intent(this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                            }
//                        },
//                        throwable -> {
////                            phone.setEnabled(true);
////                            password.setEnabled(true);
////                            signIn.setVisibility(View.VISIBLE);
//                            progress.setVisibility(View.INVISIBLE);
//
//                            if (throwable instanceof TimeoutException) {
//                                Snackbar.make(progress, R.string.fail_timeout, Snackbar.LENGTH_LONG).show();
//                            } else if (throwable instanceof ApiException) {
//                                Snackbar.make(progress, getResources().getString(R.string.fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
//                            } else {
//                                Snackbar.make(progress, R.string.fail_network, Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                );
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        return new int[]{R.id.alipay, R.id.qq, R.id.wechat, R.id.announcement};
    }
}
