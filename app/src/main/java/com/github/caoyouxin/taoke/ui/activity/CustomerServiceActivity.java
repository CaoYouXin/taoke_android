package com.github.caoyouxin.taoke.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerServiceActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.qqId)
    TextView qqId;

    @BindView(R.id.weChatId)
    TextView weChatId;

    @BindView(R.id.go_to_qq)
    TextView goToQQ;

    @BindView(R.id.go_to_weChat)
    TextView goToWeChat;

    @BindView(R.id.go_to_qq_progress)
    View goToQQProgress;

    @BindView(R.id.go_to_weChat_progress)
    View goToWeChatProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_service);

        ButterKnife.bind(this);

        title.setText(R.string.customer_serv);

        TaoKeApi.getCustomerService()
                .timeout(10, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxHandleServerExp(this))
                .subscribe(
                        view -> {
                            qqId.setText(view.mqq);
                            weChatId.setText(view.weChat);
                        }
                );
    }

    @OnClick({R.id.back, R.id.go_to_qq, R.id.go_to_weChat})
    protected void onBackClick(View view) {
        ClipboardManager cmb = (ClipboardManager) CustomerServiceActivity.this
                .getSystemService(CustomerServiceActivity.this.CLIPBOARD_SERVICE);
        switch (view.getId()) {
            case R.id.go_to_qq:
                String qq = qqId.getText().toString().trim();
                if (qq.equals("")) {
                    return;
                }

                goToQQ.setVisibility(View.GONE);
                goToQQProgress.setVisibility(View.VISIBLE);
                cmb.setText(qq);

                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");

                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    goToQQ.setVisibility(View.VISIBLE);
                    goToQQProgress.setVisibility(View.GONE);
                    Toast.makeText(this, "文案已复制！检查到您手机没有安装QQ，请安装后方可跳转到QQ。", Toast.LENGTH_LONG).show();
                }
                return;
            case R.id.go_to_weChat:
                String weChat = weChatId.getText().toString().trim();
                if (weChat.equals("")) {
                    return;
                }

                goToWeChat.setVisibility(View.GONE);
                goToWeChatProgress.setVisibility(View.VISIBLE);
                cmb.setText(weChat);

                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");

                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    goToWeChat.setVisibility(View.VISIBLE);
                    goToWeChatProgress.setVisibility(View.GONE);
                    Toast.makeText(this, "文案已复制！检查到您手机没有安装微信，请安装后方可跳转到微信。", Toast.LENGTH_LONG).show();
                }
                return;
            case R.id.back:
                onBackPressed();
        }
    }
}
