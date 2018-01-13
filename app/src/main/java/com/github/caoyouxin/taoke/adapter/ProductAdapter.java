package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.ui.widget.RatioImageView;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductAdapter extends RecyclerView.Adapter implements IDataAdapter<List<CouponItem>> {

    private List<CouponItem> data = new ArrayList<>();

    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CouponItem item = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(item.getPictUrl())
                .setOldController(holder.thumb.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        holder.thumb.setOriginalSize(imageInfo.getWidth(), imageInfo.getHeight());
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }
                }).build();
        holder.thumb.setController(draweeController);
        holder.title.setText(item.getTitle());

        if (UserData.get().isBuyer()) {
            holder.shareEarn.setVisibility(View.GONE);
        } else {
            holder.shareEarn.setVisibility(View.VISIBLE);
            holder.shareEarn.setText(context.getResources().getString(R.string.share_earn, item.getEarnPrice()));
        }

        if (null != item.getCouponInfo()) {
            holder.couponWrapper.setVisibility(View.VISIBLE);
            holder.noCouponWrapper.setVisibility(View.GONE);
            holder.coupon.setVisibility(View.VISIBLE);

            holder.couponPriceAfter.setText(context.getResources().getString(R.string.miquaner_money, item.getCouponPrice()));
            String text = context.getResources().getString(R.string.miquaner_money, item.getZkFinalPrice());
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.grey_400));
            builder.setSpan(foregroundColorSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.font_12));
            builder.setSpan(absoluteSizeSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            builder.setSpan(strikethroughSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.couponPriceBefore.setText(builder);

            holder.couponSales.setText(context.getResources().getString(R.string.product_sales, String.valueOf(item.getVolume())));

            holder.coupon.setText(context.getResources().getString(R.string.miquaner_coupon,
                    item.getCouponInfo().substring(item.getCouponInfo().indexOf('减') + 1)));
        } else {
            holder.couponWrapper.setVisibility(View.GONE);
            holder.noCouponWrapper.setVisibility(View.VISIBLE);
            holder.coupon.setVisibility(View.GONE);

            holder.price.setText(context.getResources().getString(R.string.miquaner_money, item.getZkFinalPrice()));

            holder.sales.setText(context.getResources().getString(R.string.product_sales, String.valueOf(item.getVolume())));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void notifyDataChanged(List<CouponItem> data, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<CouponItem> getData() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_thumb)
        RatioImageView thumb;

        @BindView(R.id.product_title)
        TextView title;

        @BindView(R.id.product_price)
        TextView price;

        @BindView(R.id.product_sales)
        TextView sales;

        @BindView(R.id.coupon_value)
        TextView coupon;

        @BindView(R.id.share_earn)
        TextView shareEarn;

        @BindView(R.id.coupon_price_before)
        TextView couponPriceBefore;

        @BindView(R.id.coupon_price_after)
        TextView couponPriceAfter;

        @BindView(R.id.product_sales_coupon)
        TextView couponSales;

        @BindView(R.id.no_coupon_wrapper)
        FrameLayout noCouponWrapper;

        @BindView(R.id.coupon_wrapper)
        FrameLayout couponWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}