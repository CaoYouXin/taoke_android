package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.HelpAdapter;
import com.github.caoyouxin.taoke.adapter.MessageAdapter;
import com.github.caoyouxin.taoke.datasource.HelpDataSource;
import com.github.caoyouxin.taoke.datasource.MessageDataSource;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.message_list)
    RecyclerView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        this.title.setText(getIntent().getStringExtra("title"));

        this.initMessageList(getIntent().getStringExtra("title"));

        Snackbar.make(findViewById(android.R.id.content), R.string.app_not_release_hint, Snackbar.LENGTH_LONG).show();
    }

    private void initMessageList(String type) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(layoutManager);
//        messageList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).size(1).build());

        MessageAdapter messageAdapter = new MessageAdapter();
        MessageDataSource messageDataSource = new MessageDataSource(this, type);

        MVCHelper messageListHelper = new MVCNormalHelper(messageList);
        messageListHelper.setAdapter(messageAdapter);
        messageListHelper.setDataSource(messageDataSource);

        messageListHelper.refresh();
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }
}
