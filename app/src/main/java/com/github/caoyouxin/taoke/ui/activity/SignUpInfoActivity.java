package com.github.caoyouxin.taoke.ui.activity;

import android.app.AlertDialog;
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
import com.github.gnastnosaj.boilerplate.util.keyboard.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class SignUpInfoActivity extends BaseActivity {
    public final static String EXTRA_PHONE = "phone";

    @BindView(R.id.verification_code)
    EditText verificationCode;

    @BindView(R.id.verification_code_resend)
    TextView verificationCodeResend;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.password_visiable)
    TextView passwordVisiable;

    @BindView(R.id.nick_name)
    EditText nick;

    @BindView(R.id.invitation_code)
    EditText invitationCode;

    @BindView(R.id.sign_up_finish)
    TextView signUpFinish;

    @BindView(R.id.progress)
    View progress;

    private String phone;

    @OnClick({R.id.back, R.id.verification_code_resend, R.id.password_visiable, R.id.sign_up_finish})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.verification_code_resend:
                verificationCodeResend();
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
            case R.id.sign_up_finish:
                signUpFinish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up_info);

        ButterKnife.bind(this);

        phone = getIntent().getStringExtra(EXTRA_PHONE);
        countDown();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (verificationCode.getEditableText().toString().trim().length() == 6
                        && password.getEditableText().toString().trim().length() >= 6) {
                    signUpFinish.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    signUpFinish.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };
        verificationCode.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        nick.addTextChangedListener(textWatcher);
    }

    private void verificationCodeResend() {
        if (verificationCodeResend.getText().equals(getResources().getString(R.string.verification_code_resend))) {
            countDown();
            TaoKeApi.verification(phone)
                    .timeout(10, TimeUnit.SECONDS)
                    .compose(RxHelper.rxSchedulerHelper())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            taoKeData -> {
                            },
                            throwable -> {
                                if (throwable instanceof TimeoutException) {
                                    Snackbar.make(verificationCodeResend, R.string.sign_up_fail_timeout, Snackbar.LENGTH_LONG).show();
                                } else if (throwable instanceof ApiException) {
                                    Snackbar.make(verificationCodeResend, getResources().getString(R.string.sign_up_fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(verificationCodeResend, R.string.sign_up_fail_network, Snackbar.LENGTH_LONG).show();
                                }
                            }
                    );
        }
    }

    private void countDown() {
        verificationCodeResend.setTextColor(getResources().getColor(R.color.grey_400));
        Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(aLong -> {
                    if (aLong == 60) {
                        verificationCodeResend.setText(getResources().getString(R.string.verification_code_resend));
                        verificationCodeResend.setTextColor(Color.BLACK);
                    } else {
                        verificationCodeResend.setText(60 - aLong + "s");
                    }
                });
    }

    private void signUpFinish() {
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
        String name = nick.getEditableText().toString().trim();
        String invitation = invitationCode.getEditableText().toString().trim();
        if (TextUtils.isEmpty(invitation)) {
            new AlertDialog.Builder(this).setPositiveButton(R.string.to_register_directly, (dialog, which) -> {
                signUp(vcode, pwd, name, invitation);
            }).setNegativeButton(R.string.to_edit_invation_code, (dialog, which) -> {
                invitationCode.requestFocus();
            }).setMessage(R.string.lack_of_invitation_hint).show();
            return;
        }

        signUp(vcode, pwd, name, invitation);
    }

    private void signUp(String vcode, String pwd, String name, String invitation) {
        verificationCode.setEnabled(false);
        password.setEnabled(false);
        nick.setEnabled(false);
        invitationCode.setEnabled(false);
        signUpFinish.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

        TaoKeApi.signUp(phone, vcode, pwd, name, invitation)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(
                        taoKeData -> {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                            if (sharedPreferences.getBoolean(IntroActivity.INTRO_READ, false)) {
                                startActivity(new Intent(this, TaoKeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            } else {
                                startActivity(new Intent(this, IntroActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        },
                        throwable -> {
                            verificationCode.setEnabled(true);
                            password.setEnabled(true);
                            nick.setEnabled(true);
                            invitationCode.setEnabled(true);
                            signUpFinish.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.INVISIBLE);

                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(progress, R.string.sign_up_fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(progress, getResources().getString(R.string.sign_up_fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(progress, R.string.sign_up_fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        return new int[]{R.id.password, R.id.nick_name, R.id.invitation_code};
    }
}
