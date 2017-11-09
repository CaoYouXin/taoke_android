package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.OrderItem;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter implements IDataAdapter<List<OrderItem>> {

    private List<OrderItem> data = new ArrayList<>();
    private Context context;

    public OrderAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        OrderItem item = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.orderItemName.setText(item.itemName);
        holder.orderItemStoreName.setText("所属店铺: " + item.itemStoreName);
        holder.orderStatus.setText(item.status);
        switch (item.status) {
            case "订单付款":
            case "订单收货":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.orderStatus.setBackground(this.context.getResources().getDrawable(R.drawable.bg_blue_round_corner));
                } else {
                    holder.orderStatus.setBackgroundColor(this.context.getResources().getColor(R.color.blue_500));
                }
                break;
            case "订单结算":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.orderStatus.setBackground(this.context.getResources().getDrawable(R.drawable.bg_green_round_corner));
                } else {
                    holder.orderStatus.setBackgroundColor(this.context.getResources().getColor(R.color.green_500));
                }
                break;
            case "订单失效":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.orderStatus.setBackground(this.context.getResources().getDrawable(R.drawable.bg_grey_round_corner));
                } else {
                    holder.orderStatus.setBackgroundColor(this.context.getResources().getColor(R.color.grey_500));
                }
                break;
        }
        String tradePrice = item.itemTradePrice.toString();
        holder.orderTradePrice.setText(SpannedTextUtil.buildAmount(this.context, R.string.order_trade_price, tradePrice.substring(0, tradePrice.indexOf('.') + 3), '¥', 2));
        holder.orderEstimateEffect.setText(SpannedTextUtil.buildAmount(this.context, R.string.order_estimate_effect, item.estimateEffect, '¥', 2));
        holder.orderEstimateIncome.setText(SpannedTextUtil.buildAmount(this.context, R.string.order_estimate_income, item.estimateIncome, '¥', 2));
        holder.orderCreateTime.setText(item.dateStr + " 创建");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void notifyDataChanged(List<OrderItem> data, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<OrderItem> getData() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_item_name)
        TextView orderItemName;

        @BindView(R.id.order_item_store_name)
        TextView orderItemStoreName;

        @BindView(R.id.order_status)
        TextView orderStatus;

        @BindView(R.id.order_trade_price)
        TextView orderTradePrice;

        @BindView(R.id.order_estimate_effect)
        TextView orderEstimateEffect;

        @BindView(R.id.order_estimate_income)
        TextView orderEstimateIncome;

        @BindView(R.id.order_create_time)
        TextView orderCreateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}