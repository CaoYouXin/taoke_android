package com.github.caoyouxin.taoke.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class H5BlogActivity extends BaseActivity {

    public static final String HELP_DOC_LINK = "HELP_DOC_LINK";

    @BindView(R.id.title)
    TextView textView;

    @BindView(R.id.web_view)
    WebView webView;

    private String helpDocLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_h5);

        ButterKnife.bind(this);

        helpDocLink = getIntent().getStringExtra(HELP_DOC_LINK);

        initView();
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                textView.setText(title);
            }
        };
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(wvcc);

        // 创建WebViewClient对象
        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                webView.loadUrl(url);
                // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
                return true;
            }
        };
        webView.setWebViewClient(wvc);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(helpDocLink);
    }
}
