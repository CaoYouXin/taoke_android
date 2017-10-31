package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.Product;
import com.github.caoyouxin.taoke.ui.widget.RatioImageView;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/29/17.
 */

public class ProductAdapter extends RecyclerView.Adapter implements IDataAdapter<List<Product>> {

    private List<Product> data = new ArrayList<>();

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
        Product item = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(item.thumb)
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
        holder.title.setText(item.title);
        holder.price.setText(context.getResources().getString(R.string.product_price, item.price));
        holder.sales.setText(context.getResources().getString(R.string.product_sales, String.valueOf(item.sales)));
        if (item.isNew) {
            holder.isNew.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void notifyDataChanged(List<Product> data, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<Product> getData() {
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

        @BindView(R.id.product_new)
        View isNew;

        @BindView(R.id.product_price)
        TextView price;

        @BindView(R.id.product_sales)
        TextView sales;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}