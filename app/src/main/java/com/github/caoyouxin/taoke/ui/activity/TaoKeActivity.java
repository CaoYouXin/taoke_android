package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.TaoKe;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.event.MessageEvent;
import com.github.caoyouxin.taoke.ui.fragment.AccountFragment;
import com.github.caoyouxin.taoke.ui.fragment.ChartFragment;
import com.github.caoyouxin.taoke.ui.fragment.DiscoverFragment;
import com.github.caoyouxin.taoke.ui.fragment.MessageFragment;
import com.github.gnastnosaj.boilerplate.Boilerplate;
import com.github.gnastnosaj.boilerplate.rxbus.RxBus;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.internal.functions.Functions;
import me.leolin.shortcutbadger.ShortcutBadger;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import timber.log.Timber;


public class TaoKeActivity extends BaseActivity {

    @BindView(R.id.tab_discover)
    LinearLayout tabDiscover;

    @BindView(R.id.tab_chart)
    LinearLayout tabChart;

    @BindView(R.id.tab_message)
    LinearLayout tabMessage;

    @BindView(R.id.tab_account)
    LinearLayout tabAccount;

    private Map<Integer, Fragment> pages;

    private Badge tabBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_taoke);

        ButterKnife.bind(this);

        initSystemBar();

        decorateTab(tabDiscover);

        showTabBadge();

        showTabContent(tabDiscover.getId());
    }

    @OnClick({R.id.tab_discover, R.id.tab_chart, R.id.tab_message, R.id.tab_account, R.id.search_btn})
    protected void onTabClick(View view) {
        if (view.getId() == R.id.search_btn) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return;
        }

        LinearLayout[] tabs = new LinearLayout[]{tabDiscover, tabChart, tabMessage, tabAccount};
        for (LinearLayout tab : tabs) {
            tab.animate().scaleX(1.0f).scaleY(1.0f);
            ((TextView) tab.getChildAt(0)).setTextColor(getResources().getColor(R.color.grey_600));
            ((TextView) tab.getChildAt(1)).setTextColor(getResources().getColor(R.color.grey_600));
        }

        decorateTab((LinearLayout) view);
        showTabContent(view.getId());

        //do not use id compare because tabBadge change the view structure
        if (view == tabMessage) {
            ShortcutBadger.removeCount(Boilerplate.getInstance());
            tabBadge.hide(false);
        }
    }

    private void decorateTab(LinearLayout tab) {
        tab.animate().scaleX(1.1f).scaleY(1.1f);
        ((TextView) tab.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView) tab.getChildAt(1)).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void showTabContent(int tabId) {
        if (pages == null) {
            pages = new ArrayMap<>();
            pages.put(tabDiscover.getId(), new DiscoverFragment().setTaokeActivity(this));
            pages.put(tabChart.getId(), new ChartFragment());
            pages.put(tabMessage.getId(), new MessageFragment());
            pages.put(tabAccount.getId(), new AccountFragment());
        }
        FragmentManager fm = getSupportFragmentManager();
        if (pages.containsKey(tabId)) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.tab_content, pages.get(tabId));
            ft.commit();
        }
    }

    private void showTabBadge() {
        tabBadge = new QBadgeView(this).bindTarget(tabMessage)
                .setGravityOffset(60, 15, false)
                .setBadgeGravity(Gravity.END | Gravity.TOP);
        MessageEvent.observable
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(messageEvent -> {
                    if(messageEvent.hide) {
                        ShortcutBadger.removeCount(Boilerplate.getInstance());
                        tabBadge.hide(false);
                    }else {
                        tabBadge.setBadgeNumber((int) messageEvent.count);
                        ShortcutBadger.applyCount(Boilerplate.getInstance(), (int) messageEvent.count);
                    }
                }, throwable -> Timber.e(throwable));

        Observable.interval(1, TimeUnit.MINUTES).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(aLong -> TaoKeApi.getUnreadMessages().subscribe(count -> RxBus.getInstance().post(MessageEvent.class, new MessageEvent(count)), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, Functions.emptyConsumer()));
    }
}
