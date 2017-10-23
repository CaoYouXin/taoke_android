package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/23/17.
 */

public class TaoKeActivity extends BaseActivity {

    @BindView(R.id.tab_discover)
    LinearLayout tabDiscover;

    @BindView(R.id.tab_chat)
    LinearLayout tabChat;

    @BindView(R.id.tab_message)
    LinearLayout tabMessage;

    @BindView(R.id.tab_account)
    LinearLayout tabAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_taoke);

        ButterKnife.bind(this);

        initSystemBar();

        decorateTab(tabDiscover);
    }

    @OnClick({R.id.tab_discover, R.id.tab_chat, R.id.tab_message, R.id.tab_account})
    protected void onTabClick(View view) {
        LinearLayout[] tabs = new LinearLayout[]{tabDiscover, tabChat, tabMessage, tabAccount};
        for (LinearLayout tab : tabs) {
            tab.animate().scaleX(1.0f).scaleY(1.0f);
            ((TextView) tab.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey_600));
            ((TextView) tab.getChildAt(1)).setTextColor(getResources().getColor(R.color.grey_600));
        }

        decorateTab((LinearLayout) view);
    }

    private void decorateTab(LinearLayout tab) {
        tab.animate().scaleX(1.1f).scaleY(1.1f);
        ((TextView) tab.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) tab.getChildAt(1)).setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
