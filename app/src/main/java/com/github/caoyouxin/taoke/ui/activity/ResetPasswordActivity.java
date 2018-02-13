package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.util.RegUtils;
import com.github.gnastnosaj.boilerplate.util.keyboard.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ResetPasswordActivity extends BaseActivity {
    @BindView(R.id.back)
    View back;

    @BindView(R.id.phone)
    EditText phone;

    @BindView(R.id.verification_code)
    EditText verificationCode;

    @BindView(R.id.verification_code_send)
    TextView verificationCodeSend;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.password_visiable)
    TextView passwordVisiable;

    @BindView(R.id.reset_password)
    TextView resetPassword;

    @BindView(R.id.progress)
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);

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
                if (verificationCode.getEditableText().toString().trim().length() == 6 && password.getEditableText().toString().trim().length() >= 6) {
                    resetPassword.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    resetPassword.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };

        verificationCode.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.back, R.id.verification_code_send, R.id.password_visiable, R.id.reset_password})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.verification_code_send:
                verificationCodeSend();
                break;
            case R.id.password_visiable:
                if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordVisiable.setTextColor(Color.BLACK);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordVisiable.setTextColor(getResources().getColor(R.color.grey_400));
                }
                break;
            case R.id.reset_password:
                resetPassword();
                break;
        }
    }

    private void verificationCodeSend() {
        String phoneNo = phone.getEditableText().toString().trim();
        if (TextUtils.isEmpty(phoneNo) || !RegUtils.isMobile(phoneNo)) {
            phone.requestFocus();
            return;
        }
        if (verificationCodeSend.getText().equals(getResources().getString(R.string.verification_code_send))) {
            verificationCodeSend.setTextColor(getResources().getColor(R.color.grey_400));
            Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(aLong -> {
                        if (aLong == 60) {
                            verificationCodeSend.setText(getResources().getString(R.string.verification_code_send));
                            verificationCodeSend.setTextColor(Color.BLACK);
                        } else {
                            verificationCodeSend.setText(60 - aLong + "s");
                        }
                    });
            TaoKeApi.verification(phoneNo)
                    .timeout(10, TimeUnit.SECONDS)
                    .compose(RxHelper.rxSchedulerHelper())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(RxHelper.rxHandleServerExp(this))
                    .subscribe(
                            homeBasedCareData -> {
                            }
                    );
        }
    }

    private void resetPassword() {
        String phoneNo = phone.getEditableText().toString().trim();
        if (TextUtils.isEmpty(phoneNo) || !RegUtils.isMobile(phoneNo)) {
            phone.requestFocus();
            return;
        }
        String vcode = verificationCode.getEditableText().toString().trim();
        if (vcode.length() != 6) {
            verificationCode.requestFocus();
            return;
        }
        String pwd = password.getEditableText().toString().trim();
        if (pwd.length() < 6) {
            password.requestFocus();
            return;
        }

        verificationCode.setEnabled(false);
        password.setEnabled(false);
        resetPassword.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

        TaoKeApi.resetPassword(phoneNo, vcode, pwd)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(this))
                .subscribe(
                        taoKeData -> {
                            startActivity(new Intent(this, TaoKeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        },
                        throwable -> {
                            verificationCode.setEnabled(true);
                            password.setEnabled(true);
                            resetPassword.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.INVISIBLE);
                        }
                );
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        int[] ids = {R.id.phone, R.id.verification_code, R.id.password};
        return ids;
    }
}
