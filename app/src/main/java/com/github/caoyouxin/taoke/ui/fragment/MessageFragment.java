package com.github.caoyouxin.taoke.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.MessageAdapter;
import com.github.caoyouxin.taoke.datasource.MessageDataSource;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/24/17.
 */

public class MessageFragment extends Fragment {

//    @BindView(R.id.global_msg)
//    TextView globalMsg;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.message_list)
    RecyclerView messageList;

    View rootView;

    private MVCHelper<List<MessageItem>> messageListHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            this.initRefreshLayout();
            this.initMessageList();
        }
        return rootView;
    }

    private void initRefreshLayout() {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            messageListHelper.refresh();
            refreshLayout.finishRefresh(2000);
        });
        smartRefreshLayout.setOnLoadmoreListener(refreshLayout -> {
            messageListHelper.loadMore();
            refreshLayout.finishLoadmore(2000);
        });
    }

    private void initMessageList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageList.setLayoutManager(layoutManager);

        MessageAdapter messageAdapter = new MessageAdapter();
        MessageDataSource messageDataSource = new MessageDataSource(getActivity());

        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        messageListHelper = new MVCNormalHelper<>(messageList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        messageListHelper.setAdapter(messageAdapter);
        messageListHelper.setDataSource(messageDataSource);

        messageListHelper.refresh();
    }

//    private void initAccountId() {
//        SpannableString span = new SpannableString("动态\n淘宝客新模式");
//        span.setSpan(new RelativeSizeSpan(1.36f), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        span.setSpan(new ForegroundColorSpan(Color.DKGRAY), 3, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        globalMsg.setText(span);
//    }
//
//    @OnClick(R.id.global_msg)
//    public void onClick(View view) {
//        Intent intent = new Intent(getActivity(), MessageActivity.class);
//        String text = this.globalMsg.getText().toString();
//        intent.putExtra("title", text.substring(0, text.indexOf('\n')));
//        getActivity().startActivity(intent);
//    }
}
