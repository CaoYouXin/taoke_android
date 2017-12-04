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


public class SignInActivity extends BaseActivity {
    @BindView(R.id.phone)
    EditText phone;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.password_visiable)
    TextView passwordVisiable;

    @BindView(R.id.sign_in)
    TextView signIn;

    @BindView(R.id.progress)
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

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
                if (!TextUtils.isEmpty(phone.getEditableText().toString().trim()) && RegUtils.isMobile(phone.getEditableText().toString().trim()) && password.getEditableText().toString().trim().length() >= 6) {
                    signIn.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    signIn.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        };

        phone.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.password_visiable, R.id.sign_in, R.id.sign_up, R.id.has_problem})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_visiable:
                if (password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordVisiable.setTextColor(Color.BLACK);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordVisiable.setTextColor(getResources().getColor(R.color.grey_400));
                }
                break;
            case R.id.sign_in:
                signIn();
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.has_problem:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }

    private void signIn() {
        String phoneNo = phone.getEditableText().toString().trim();
        if (TextUtils.isEmpty(phoneNo) || !RegUtils.isMobile(phoneNo)) {
            phone.requestFocus();
            return;
        }
        String pwd = password.getEditableText().toString().trim();
        if (pwd.length() < 6) {
            password.requestFocus();
            return;
        }

        phone.setEnabled(false);
        password.setEnabled(false);
        signIn.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);

        TaoKeApi.signIn(phoneNo, pwd)
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(this))
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
                            phone.setEnabled(true);
                            password.setEnabled(true);
                            signIn.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.INVISIBLE);

                            if (throwable instanceof TimeoutException) {
                                Snackbar.make(progress, R.string.sign_in_fail_timeout, Snackbar.LENGTH_LONG).show();
                            } else if (throwable instanceof ApiException) {
                                Snackbar.make(progress, getResources().getString(R.string.sign_in_fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(progress, R.string.sign_in_fail_network, Snackbar.LENGTH_LONG).show();
                            }
                        }
                );
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        int[] ids = {R.id.phone, R.id.password};
        return ids;
    }
}
