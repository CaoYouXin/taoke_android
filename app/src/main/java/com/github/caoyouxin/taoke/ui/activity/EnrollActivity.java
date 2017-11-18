package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.ApiException;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.EnrollSubmit;
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

    @BindView(R.id.name)
    EditText name;

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
                if (!TextUtils.isEmpty(name.getEditableText().toString().trim())
                        && !TextUtils.isEmpty(alipay.getEditableText().toString().trim()) && (
                        !TextUtils.isEmpty(qq.getEditableText().toString().trim()) ||
                                !TextUtils.isEmpty(wechat.getEditableText().toString().trim())
                        ) && !TextUtils.isEmpty(announcement.getEditableText().toString().trim())) {
                    enroll.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    enroll.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };

        name.addTextChangedListener(textWatcher);
        alipay.addTextChangedListener(textWatcher);
        qq.addTextChangedListener(textWatcher);
        wechat.addTextChangedListener(textWatcher);
        announcement.addTextChangedListener(textWatcher);

        EnrollSubmit enrollSubmit = EnrollSubmit.get();
        name.setText(enrollSubmit.realName);
        alipay.setText(enrollSubmit.aliPayId);
        qq.setText(enrollSubmit.qqId);
        wechat.setText(enrollSubmit.weChatId);
        announcement.setText(enrollSubmit.announcement);
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
        String nameStr = name.getEditableText().toString().trim();
        if (TextUtils.isEmpty(nameStr)) {
            name.requestFocus();
            return;
        }
        String alipayId = alipay.getEditableText().toString().trim();
        if (TextUtils.isEmpty(alipayId)) {
            alipay.requestFocus();
            return;
        }
        String qqId = qq.getEditableText().toString().trim();
        String wechatId = wechat.getEditableText().toString().trim();
        if (TextUtils.isEmpty(qqId) && TextUtils.isEmpty(wechatId)) {
            if (TextUtils.isEmpty(qqId)) {
                qq.requestFocus();
            } else {
                wechat.requestFocus();
            }
            return;
        }
        String announcementText = announcement.getEditableText().toString().trim();
        if (TextUtils.isEmpty(announcementText)) {
            announcement.requestFocus();
            return;
        }

        alipay.setEnabled(false);
        qq.setEnabled(false);
        wechat.setEnabled(false);
        announcement.setEnabled(false);
        enroll.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

        final EnrollSubmit enrollSubmit = new EnrollSubmit(nameStr, alipayId, qqId, wechatId, announcementText);
        TaoKeApi.enroll(enrollSubmit)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        taoKeData -> {
                            enrollSubmit.persist();
                            onBackPressed();
                        },
                        throwable -> {
                            alipay.setEnabled(true);
                            qq.setEnabled(true);
                            wechat.setEnabled(true);
                            announcement.setEnabled(true);
                            enroll.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.INVISIBLE);

                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(progress, R.string.fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(progress, getResources().getString(R.string.fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(progress, R.string.fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        return new int[]{R.id.alipay, R.id.qq, R.id.wechat, R.id.announcement};
    }
}
