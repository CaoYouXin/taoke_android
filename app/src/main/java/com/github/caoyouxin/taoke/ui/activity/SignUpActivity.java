package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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


public class SignUpActivity extends BaseActivity {
    @BindView(R.id.phone)
    EditText phone;

    @BindView(R.id.sign_up)
    TextView signUp;

    @BindView(R.id.progress)
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNo = s.toString().trim();
                signUp.setTextColor((!TextUtils.isEmpty(phoneNo) && RegUtils.isMobile(phoneNo)) ? getResources().getColor(R.color.colorPrimaryDark) : getResources().getColor(R.color.colorPrimary));
            }
        });
    }


    @OnClick({R.id.back, R.id.sign_up})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.sign_up:
                signUp();
                break;
        }
    }

    private void signUp() {
        String phoneNo = phone.getEditableText().toString().trim();
        if (TextUtils.isEmpty(phoneNo) || !RegUtils.isMobile(phoneNo)) {
            phone.requestFocus();
        } else {
            phone.setEnabled(false);
            progress.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.INVISIBLE);

            TaoKeApi.verification(phoneNo)
                    .timeout(10, TimeUnit.SECONDS)
                    .compose(RxHelper.rxSchedulerHelper())
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(RxHelper.rxHandleServerExp(this))
                    .subscribe(
                            taoKeData -> {
                                phone.setEnabled(true);
                                progress.setVisibility(View.INVISIBLE);
                                signUp.setVisibility(View.VISIBLE);

                                startActivity(new Intent(this, SignUpInfoActivity.class).putExtra(SignUpInfoActivity.EXTRA_PHONE, phoneNo));
                            },
                            throwable -> {
                                phone.setEnabled(true);
                                progress.setVisibility(View.INVISIBLE);
                                signUp.setVisibility(View.VISIBLE);
                            }
                    );
        }
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        int[] ids = {R.id.phone};
        return ids;
    }
}
