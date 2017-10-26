package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/24/17.
 */

public class CouponAdapter extends RecyclerView.Adapter implements IDataAdapter<List<CouponItem>> {

    private Context context;

    private List<CouponItem> data = new ArrayList<>();

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
        holder.thumb.setImageURI(item.thumb);
        holder.title.setText(item.title);
        holder.priceBefore.setText(context.getResources().getString(R.string.discover_coupon_price_before, item.priceBefore, String.valueOf(item.sales)));
        holder.priceAfter.setText(context.getResources().getString(R.string.discover_coupon_price_after, item.priceAfter));
        holder.earn.setText(context.getResources().getString(R.string.discover_coupon_earn, item.earn));
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

        @BindView(R.id.coupon_thumb)
        SimpleDraweeView thumb;

        @BindView(R.id.coupon_title)
        TextView title;

        @BindView(R.id.coupon_price_before)
        TextView priceBefore;

        @BindView(R.id.coupon_price_after)
        TextView priceAfter;

        @BindView(R.id.coupon_earn)
        TextView earn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}