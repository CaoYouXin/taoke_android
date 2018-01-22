package com.github.caoyouxin.taoke.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.ApiException;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.EnrollSubmit;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CompleterActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.real_name)
    EditText realName;

    @BindView(R.id.aliPay_id)
    EditText aliPayId;

    @BindView(R.id.phone_number)
    EditText phoneNumber;

    @BindView(R.id.verification_code)
    EditText verificationCode;

    @BindView(R.id.verification_code_btn)
    TextView verificationCodeBtn;

    @BindView(R.id.submit_btn)
    TextView submitBtn;

    @BindView(R.id.progress)
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_completer);

        ButterKnife.bind(this);

        title.setText("用户信息补全");

        realName.setText(UserData.get().getRealName());
        phoneNumber.setText(UserData.get().getPhone());
    }

    @OnClick({R.id.back, R.id.verification_code_btn, R.id.submit_btn})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                return;
            case R.id.verification_code_btn:
                verificationCodeResend();
                return;
            case R.id.submit_btn:
                String aliPay = aliPayId.getEditableText().toString().trim();
                if (TextUtils.isEmpty(aliPay)) {
                    new AlertDialog.Builder(this)
                            .setMessage("请您输入一个绑定以上姓名的支付宝账号，谢谢！")
                            .setPositiveButton("知道了",
                                    (dialog, which) -> aliPayId.requestFocus()
                            ).show();
                    return;
                }
                String vCode = verificationCode.getEditableText().toString().trim();
                if (TextUtils.isEmpty(vCode)) {
                    new AlertDialog.Builder(this)
                            .setMessage("请您输入验证码，谢谢！")
                            .setPositiveButton("知道了",
                                    (dialog, which) -> verificationCode.requestFocus()
                            ).show();
                    return;
                }

                aliPayId.setEnabled(false);
                verificationCode.setEnabled(false);
                submitBtn.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);

                TaoKeApi.complete(vCode, phoneNumber.getText().toString().trim(), aliPay)
                        .timeout(10, TimeUnit.SECONDS)
                        .compose(RxHelper.rxSchedulerHelper())
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .compose(RxHelper.rxHandleServerExp(this))
                        .subscribe(
                                taoKeData -> {
                                    onBackPressed();
                                },
                                throwable -> {
                                    aliPayId.setEnabled(true);
                                    verificationCode.setEnabled(true);
                                    submitBtn.setVisibility(View.VISIBLE);
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

    }

    private void verificationCodeResend() {
        if (verificationCodeBtn.getText().equals(getResources().getString(R.string.verification_code_resend)) ||
                verificationCodeBtn.getText().equals(getResources().getString(R.string.verification_code_send))) {
            countDown();
            TaoKeApi.verification(phoneNumber.getText().toString())
                    .timeout(10, TimeUnit.SECONDS)
                    .compose(RxHelper.rxSchedulerHelper())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(RxHelper.rxHandleServerExp(this))
                    .subscribe(
                            taoKeData -> {
                            },
                            throwable -> {
                                if (throwable instanceof TimeoutException) {
                                    Snackbar.make(verificationCodeBtn, R.string.sign_up_fail_timeout, Snackbar.LENGTH_LONG).show();
                                } else if (throwable instanceof ApiException) {
                                    Snackbar.make(verificationCodeBtn, getResources().getString(R.string.sign_up_fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(verificationCodeBtn, R.string.sign_up_fail_network, Snackbar.LENGTH_LONG).show();
                                }
                            }
                    );
        }
    }

    private void countDown() {
        verificationCodeBtn.setTextColor(getResources().getColor(R.color.grey_400));
        Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aLong -> {
                    if (aLong == 60) {
                        verificationCodeBtn.setText(getResources().getString(R.string.verification_code_resend));
                        verificationCodeBtn.setTextColor(Color.BLACK);
                    } else {
                        verificationCodeBtn.setText(60 - aLong + "s");
                    }
                });
    }
}
