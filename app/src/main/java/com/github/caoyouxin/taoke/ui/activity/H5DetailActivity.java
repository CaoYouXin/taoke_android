package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.util.MyWebViewClient;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class H5DetailActivity extends BaseActivity {
    public final static String EXTRA_COUPON_ITEM_ID = "couponItemId";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_h5);

        ButterKnife.bind(this);

        initView();
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }

    private void initView() {
        title.setText(R.string.detail);

        //        showDynamicBoxCustomView(DYNAMIC_BOX_MK_LINESPINNER, DetailActivity.this);

        AlibcBasePage detailPage = new AlibcDetailPage(getIntent().getExtras().getString(EXTRA_COUPON_ITEM_ID));
        AlibcShowParams showParams = new AlibcShowParams(OpenType.H5, false);
        // 淘宝客参数
        AlibcTaokeParams taokeParams = new AlibcTaokeParams();
        taokeParams.setPid(UserData.get().getAliPID());
        // 提供给三方传递配置参数
        Map<String, String> trackParam = new HashMap<>();
        AlibcTrade.show(this, webView, new MyWebViewClient(this), new WebChromeClient(), detailPage, showParams, taokeParams, trackParam, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
//                dismissDynamicBox(DetailActivity.this);
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                dismissDynamicBox(DetailActivity.this);
                Toast.makeText(H5DetailActivity.this, String.format("百川电商组件调用失败, code=%d, msg=%s", code, msg), Toast.LENGTH_LONG).show();
            }
        });
    }
}
