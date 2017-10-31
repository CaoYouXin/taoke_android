package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.FriendAdapter;
import com.github.caoyouxin.taoke.adapter.HelpAdapter;
import com.github.caoyouxin.taoke.datasource.FriendDataSource;
import com.github.caoyouxin.taoke.datasource.HelpDataSource;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.friends_list)
    RecyclerView friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friends);

        ButterKnife.bind(this);

        title.setText(R.string.friends);

        this.initFriendsList();

        Snackbar.make(findViewById(android.R.id.content), R.string.app_not_release_hint, Snackbar.LENGTH_LONG).show();
    }

    private void initFriendsList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendsList.setLayoutManager(layoutManager);
        friendsList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).size(1).build());

        FriendAdapter friendAdapter = new FriendAdapter(this);
        FriendDataSource friendDataSource = new FriendDataSource(this);

        MVCHelper friendsListHelper = new MVCNormalHelper(friendsList);
        friendsListHelper.setAdapter(friendAdapter);
        friendsListHelper.setDataSource(friendDataSource);

        friendsListHelper.refresh();
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }
}
