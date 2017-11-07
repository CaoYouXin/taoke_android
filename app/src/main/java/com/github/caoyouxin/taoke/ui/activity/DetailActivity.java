package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.ui.widget.HackyTextSliderView;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/26/17.
 */

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

    private CouponItem couponItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        couponItem = getIntent().getParcelableExtra(EXTRA_COUPON_ITEM);

        initView();
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @OnClick({R.id.back, R.id.detail_view, R.id.detail_share, R.id.detail_app})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.detail_view:
                break;
            case R.id.detail_share:
                Intent intent = new Intent(this, ShareActivity.class).putExtra(ShareActivity.EXTRA_COUPON_ITEM, couponItem);
                startActivity(intent);
                break;
            case R.id.detail_app:
                break;
        }
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
        priceAfter.setText(
                SpannedTextUtil.buildAmount(this, "¥ " + couponItem.getCouponPrice(), '¥', 2)
        );

        String text = getResources().getString(R.string.detail_price_before, couponItem.getZkFinalPrice());
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        builder.setSpan(strikethroughSpan, text.indexOf("¥") + 2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        priceBefore.setText(builder);

        detailSales.setText(getResources().getString(R.string.detail_sales, String.valueOf(couponItem.getVolume())));

        text = getResources().getString(R.string.detail_coupon, couponItem.getCouponInfo());
        builder = new SpannableStringBuilder(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_800));
        builder.setSpan(foregroundColorSpan, text.indexOf('满'), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        detailCoupon.setText(builder);

        text = getResources().getString(R.string.detail_commission, couponItem.getEarnPrice());
        builder = new SpannableStringBuilder(text);
        foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.grey_500));
        builder.setSpan(foregroundColorSpan, text.indexOf("¥"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        detailCommission.setText(builder);

        detailDescription.setText(couponItem.getItemDescription());
    }
}
