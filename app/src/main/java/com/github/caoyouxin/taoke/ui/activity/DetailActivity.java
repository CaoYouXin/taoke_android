package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

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

    @BindView(R.id.detail_thumb)
    SimpleDraweeView detailThumb;

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

    private CouponItem couponItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        couponItem = getIntent().getParcelableExtra(EXTRA_COUPON_ITEM);

        initView();
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
                break;
            case R.id.detail_app:
                break;
        }
    }

    private void initView() {
        title.setText(R.string.detail);

        TaoKeApi.getCouponDetail(couponItem)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(couponItemDetail -> {
                    detailThumb.setImageURI(couponItemDetail.thumb);
                    detailTitle.setText(couponItemDetail.title);
                    priceAfter.setText(couponItemDetail.priceAfter);

                    String text = getResources().getString(R.string.detail_price_before, couponItemDetail.priceBefore);
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                    builder.setSpan(strikethroughSpan, text.indexOf("¥") + 2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    priceBefore.setText(builder);

                    detailSales.setText(getResources().getString(R.string.detail_sales, String.valueOf(couponItemDetail.sales)));

                    text = getResources().getString(R.string.detail_coupon, couponItemDetail.coupon, couponItemDetail.couponRequirement);
                    builder = new SpannableStringBuilder(text);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.orange_800));
                    builder.setSpan(foregroundColorSpan, text.lastIndexOf(" ") + 1, text.indexOf("元"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.grey_500));
                    builder.setSpan(foregroundColorSpan, text.indexOf("（"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    detailCoupon.setText(builder);

                    text = getResources().getString(R.string.detail_commission, couponItemDetail.commissionPercent, couponItemDetail.commission);
                    builder = new SpannableStringBuilder(text);
                    foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.grey_500));
                    builder.setSpan(foregroundColorSpan, text.indexOf("（"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    detailCommission.setText(builder);
                });
    }
}
