package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.M;
import com.github.caoyouxin.taoke.model.MessageItem;
import com.github.caoyouxin.taoke.model.OrderItem;
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
        holder.orderImageUrl.setImageURI(item.itemImgUrl);
        holder.orderItemName.setText(item.itemName);
        holder.orderItemStoreName.setText("所属店铺: " + item.itemStoreName);
        holder.orderStatus.setText(item.status);
        switch (item.status) {
            case "已支付":
            case "已收货":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.orderStatus.setBackground(this.context.getResources().getDrawable(R.drawable.bg_blue_round_corner));
                } else {
                    holder.orderStatus.setBackgroundColor(this.context.getResources().getColor(R.color.blue_500));
                }
                break;
            case "已结算":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.orderStatus.setBackground(this.context.getResources().getDrawable(R.drawable.bg_green_round_corner));
                } else {
                    holder.orderStatus.setBackgroundColor(this.context.getResources().getColor(R.color.green_500));
                }
                break;
            case "已失效":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.orderStatus.setBackground(this.context.getResources().getDrawable(R.drawable.bg_grey_round_corner));
                } else {
                    holder.orderStatus.setBackgroundColor(this.context.getResources().getColor(R.color.grey_500));
                }
                break;
        }
        String tradePrice = item.itemTradePrice.toString();
        holder.orderTradePrice.setText(this.buildAmount(R.string.order_trade_price, tradePrice.substring(0, tradePrice.indexOf('.') + 3), '¥', 2));
        String commissionPrice = String.valueOf(item.itemTradePrice * item.commission);
        holder.orderCommissionPrice.setText(this.buildAmount(R.string.order_commission_price, commissionPrice.substring(0, commissionPrice.indexOf('.') + 3), '¥', 2));
        String commissionRate = String.valueOf(item.commission * 100);
        holder.orderCommissionRate.setText(this.buildAmount(R.string.order_commission_rate, commissionRate.substring(0, commissionRate.indexOf('.') + 3), '\n', 1));
        holder.orderCreateTime.setText(item.dateStr + " 创建");
    }

    private SpannableStringBuilder buildAmount(final int id, final String textV, final char start, final int offset) {
        String text = this.context.getResources().getString(id, textV);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new AbsoluteSizeSpan(this.context.getResources().getDimensionPixelSize(R.dimen.font_28)), text.indexOf(start) + offset, text.indexOf('.'), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
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

        @BindView(R.id.order_img_url)
        SimpleDraweeView orderImageUrl;

        @BindView(R.id.order_item_name)
        TextView orderItemName;

        @BindView(R.id.order_item_store_name)
        TextView orderItemStoreName;

        @BindView(R.id.order_status)
        TextView orderStatus;

        @BindView(R.id.order_trade_price)
        TextView orderTradePrice;

        @BindView(R.id.order_commission_price)
        TextView orderCommissionPrice;

        @BindView(R.id.order_commission_rate)
        TextView orderCommissionRate;

        @BindView(R.id.order_create_time)
        TextView orderCreateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}