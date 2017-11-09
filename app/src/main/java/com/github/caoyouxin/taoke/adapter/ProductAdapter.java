package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.ui.widget.RatioImageView;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/29/17.
 */

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
        holder.sales.setText(context.getResources().getString(R.string.product_sales, String.valueOf(item.getVolume())));
        holder.shareEarn.setText(context.getResources().getString(R.string.share_earn, item.getEarnPrice()));

        if (null != item.getCouponInfo()) {
            holder.couponPrice.setText(context.getResources().getString(R.string.product_price, item.getCouponPrice()));
            holder.coupon.setText(context.getResources().getString(R.string.discover_coupon_value, item.getCouponInfo(), String.valueOf(item.getCouponRemainCount())));
            holder.couponPrice.setVisibility(View.VISIBLE);
            holder.coupon.setVisibility(View.VISIBLE);

            String text = context.getResources().getString(R.string.product_price, item.getZkFinalPrice());
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.grey_400));
            builder.setSpan(foregroundColorSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.font_12));
            builder.setSpan(absoluteSizeSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
            holder.price.setText(builder);
        } else {
            holder.couponPrice.setVisibility(View.GONE);
            holder.coupon.setVisibility(View.GONE);

            holder.price.getPaint().setFlags(0);
            holder.price.setText(context.getResources().getString(R.string.product_price, item.getZkFinalPrice()));
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

        @BindView(R.id.coupon_price_after)
        TextView couponPrice;

        @BindView(R.id.share_earn)
        TextView shareEarn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}