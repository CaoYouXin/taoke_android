package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.ui.widget.HackyTextSliderView;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DetailActivity extends BaseActivity {
    public final static String EXTRA_COUPON_ITEM = "couponItem";

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.slider_layout)
    SliderLayout sliderLayout;

    @BindView(R.id.detail_title)
    TextView detailTitle;

    @BindView(R.id.detail_price_after)
    TextView priceAfter;

    @BindView(R.id.detail_price_before)
    TextView priceBefore;

    @BindView(R.id.detail_sales)
    TextView detailSales;

    @BindView(R.id.detail_coupon)
    TextView detailCoupon;

    @BindView(R.id.detail_commission)
    TextView detailCommission;

    @BindView(R.id.detail_description)
    TextView detailDescription;

    @BindView(R.id.coupon_price_wrapper)
    LinearLayout priceWrapper;

    @BindView(R.id.detail_coupon_wrapper)
    LinearLayout detailCouponWrapper;

    @BindView(R.id.buyer_wrapper)
    LinearLayout buyerWrapper;

    @BindView(R.id.agent_detail_share)
    TextView agentDetailShare;

    @BindView(R.id.detail_commission_wrapper)
    LinearLayout detailCommissionWrapper;

    @BindView(R.id.price_label)
    TextView priceLabel;

    private CouponItem couponItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        couponItem = getIntent().getParcelableExtra(EXTRA_COUPON_ITEM);

        initView();

        if (UserData.get().isBuyer()) {
            agentDetailShare.setVisibility(View.GONE);
            buyerWrapper.setVisibility(View.VISIBLE);
        } else {
            buyerWrapper.setVisibility(View.GONE);
            agentDetailShare.setVisibility(View.VISIBLE);
        }

        if (UserData.get().isBuyer() || couponItem.getEarn() < 0.001) {
            detailCommissionWrapper.setVisibility(View.GONE);
        } else {
            detailCommissionWrapper.setVisibility(View.VISIBLE);
        }

//        createDynamicBox();
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @OnClick({R.id.back, R.id.detail_view, R.id.detail_share, R.id.detail_app, R.id.agent_detail_share})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.detail_view:
                openDetail();
                break;
            case R.id.detail_share:
//                startActivity(new Intent(this, EnrollActivity.class));
//                break;
            case R.id.agent_detail_share:
                startActivity(new Intent(this, ShareActivity.class)
                        .putExtra(ShareActivity.EXTRA_COUPON_ITEM, couponItem));
                break;
            case R.id.detail_app:
                openTaobao();
                break;
        }
    }

    private void openTaobao() {
//        showDynamicBoxCustomView(DYNAMIC_BOX_MK_LINESPINNER, DetailActivity.this);

        String taokeUrl = couponItem.getCouponClickUrl();
        if (null == taokeUrl || taokeUrl.isEmpty()) {
            taokeUrl = couponItem.getTkLink();
        }
        if (null == taokeUrl || taokeUrl.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), R.string.fail_unknown, Snackbar.LENGTH_LONG).show();
            return;
        }
        AlibcBasePage tradePage = new AlibcPage(taokeUrl);// 页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        // 淘宝客参数
        AlibcTaokeParams taokeParams = new AlibcTaokeParams();
        taokeParams.setPid(UserData.get().getAliPID());
        // 提供给三方传递配置参数
        Map<String, String> trackParam = new HashMap<>();
        AlibcTrade.show(this, tradePage, showParams, taokeParams, trackParam, new AlibcTradeCallback() {

            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
//                dismissDynamicBox(DetailActivity.this);
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                dismissDynamicBox(DetailActivity.this);
                Toast.makeText(DetailActivity.this, String.format("百川电商组件调用失败, code=%d, msg=%s", code, msg), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openDetail() {
        Intent intent = new Intent(DetailActivity.this, H5DetailActivity.class);
        intent.putExtra(H5DetailActivity.EXTRA_COUPON_ITEM_ID, "" + couponItem.getNumIid());
        startActivity(intent);
    }

    private void initSlider() {
        DisplayMetrics dMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        int sliderLayoutWidth = dMetrics.widthPixels;
        int sliderLayoutHeight = sliderLayoutWidth * 9 / 10;
        LinearLayout.LayoutParams itemViewParams = new LinearLayout.LayoutParams(sliderLayoutWidth, sliderLayoutHeight);
        sliderLayout.setLayoutParams(itemViewParams);
        updateSlider();
    }

    private void updateSlider() {
        sliderLayout.removeAllSliders();
        List<String> thumbs = new ArrayList<>(couponItem.getSmallImages());
        thumbs.add(0, couponItem.getPictUrl());
        for (String thumb : thumbs) {
            TextSliderView textSliderView = new HackyTextSliderView(this);
            textSliderView.image(thumb).setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderLayout.addSlider(textSliderView);
        }
    }

    private void initView() {
        title.setText(R.string.detail);

        initSlider();

        detailTitle.setText(couponItem.getTitle());

        if (null == couponItem.getCouponPrice() || couponItem.getCouponPrice().isEmpty()) {
            priceWrapper.setVisibility(View.GONE);
            detailCouponWrapper.setVisibility(View.GONE);

            SpannableStringBuilder builder = SpannedTextUtil.buildAmount(this, getResources().getString(R.string.detail_price_before, couponItem.getZkFinalPrice()), '¥', 2);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_800));
            builder.setSpan(foregroundColorSpan, builder.toString().indexOf('¥'), builder.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceBefore.setText(builder);
        } else {
            priceWrapper.setVisibility(View.VISIBLE);
            detailCouponWrapper.setVisibility(View.VISIBLE);

            priceAfter.setText(
                    SpannedTextUtil.buildAmount(this, "¥ " + couponItem.getCouponPrice(), '¥', 2)
            );

            if (!couponItem.isJu()) {
                String text = getResources().getString(R.string.detail_coupon, couponItem.getCouponInfo());
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_800));
                builder.setSpan(foregroundColorSpan, text.indexOf('满'), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(this.getResources().getDimensionPixelSize(R.dimen.font_22));
                builder.setSpan(absoluteSizeSpan, text.indexOf("减"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                detailCoupon.setText(builder);
            }

            String text = getResources().getString(R.string.detail_price_before, couponItem.getZkFinalPrice());
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            builder.setSpan(strikethroughSpan, text.indexOf("¥") + 2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            priceBefore.setText(builder);
        }

        if (couponItem.getVolume() > 0) {
            detailSales.setText(getResources().getString(R.string.detail_sales, String.valueOf(couponItem.getVolume())));
        }

        if (couponItem.isJu()) {
            priceLabel.setText(R.string.detail_price_ju);
            detailCommissionWrapper.setVisibility(View.GONE);
        } else {
            detailCommissionWrapper.setVisibility(View.VISIBLE);
            String text = getResources().getString(R.string.detail_commission, couponItem.getEarnPrice());
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.grey_500));
            builder.setSpan(foregroundColorSpan, text.indexOf("¥"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            int end = text.indexOf('.');
            if (-1 == end) {
                end = text.length();
            }
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(this.getResources().getDimensionPixelSize(R.dimen.font_24));
            builder.setSpan(absoluteSizeSpan, text.indexOf("¥") + 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            detailCommission.setText(builder);
        }

        detailDescription.setText(couponItem.getItemDescription());
    }
}
