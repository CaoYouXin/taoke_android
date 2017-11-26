package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.UserData;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class CouponAdapter extends RecyclerView.Adapter implements IDataAdapter<List<CouponItem>> {

    private Context context;

    private List<CouponItem> data = new ArrayList<>();

    private Interpolator interpolator = new DecelerateInterpolator();

    public CouponAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_discover_coupon_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CouponItem item = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.thumb.setImageURI(item.getPictUrl());
        holder.title.setText(item.getTitle());
        holder.priceBefore.setText(context.getResources().getString(R.string.discover_coupon_price_before, item.getZkFinalPrice()));
        holder.salesStatus.setText(context.getResources().getString(R.string.discover_coupon_sales_status, String.valueOf(item.getVolume())));

        String text = context.getResources().getString(R.string.discover_coupon_price_after, item.getCouponPrice());
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.grey_900));
        builder.setSpan(foregroundColorSpan, text.indexOf("¥"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.font_16)), text.indexOf("¥"), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.priceAfter.setText(builder);

        if (UserData.get().isBuyer()) {
            holder.earn.setVisibility(View.GONE);
        } else {
            holder.earn.setText(context.getResources().getString(R.string.discover_coupon_earn, item.getEarnPrice()));
        }


        holder.value.setText(context.getResources().getString(R.string.discover_coupon_value, item.getCouponInfo(), String.valueOf(item.getCouponRemainCount())));

        if (holder.progressDisposable != null && !holder.progressDisposable.isDisposed()) {
            holder.progressDisposable.dispose();
        }
        holder.progressDisposable = Observable.intervalRange(0, 20, 200, 50, TimeUnit.MILLISECONDS)
                .map(aLong -> {
                    float result = interpolator.getInterpolation((float) aLong / 20);
                    float progress = result * ((float) item.getCouponRemainCount() * 100) / item.getCouponTotalCount();
                    return progress;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> {
                    if (holder.progress.getProgress() < progress) {
                        holder.progress.setProgress(progress);
                    }
                }, throwable -> {
                });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        ViewHolder holder = (ViewHolder) viewHolder;
        if (holder.progressDisposable != null && !holder.progressDisposable.isDisposed()) {
            holder.progressDisposable.dispose();
        }
        holder.progress.setProgress(0);
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

        @BindView(R.id.coupon_thumb)
        SimpleDraweeView thumb;

        @BindView(R.id.coupon_title)
        TextView title;

        @BindView(R.id.coupon_price_before)
        TextView priceBefore;

        @BindView(R.id.coupon_sales_status)
        TextView salesStatus;

        @BindView(R.id.coupon_price_after)
        TextView priceAfter;

        @BindView(R.id.coupon_earn)
        TextView earn;

        @BindView(R.id.coupon_value)
        TextView value;

        @BindView(R.id.coupon_progress)
        RoundCornerProgressBar progress;

        Disposable progressDisposable;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}