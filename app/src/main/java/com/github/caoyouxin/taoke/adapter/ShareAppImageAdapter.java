package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.graphics.drawable.Animatable;
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
import com.github.caoyouxin.taoke.model.ShareImage;
import com.github.caoyouxin.taoke.util.RatioImageView;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/24/17.
 */

public class ShareAppImageAdapter extends RecyclerView.Adapter implements IDataAdapter<List<ShareImage>> {

    private Context context;

    private List<ShareImage> data = new ArrayList<>();

    public ShareAppImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_share_app_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ShareImage shareImage = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(shareImage.thumb)
                .setOldController(holder.thumb.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                        holder.thumb.setOriginalSizeAccordingToHeight(imageInfo.getWidth(), imageInfo.getHeight());
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }
                }).build();
        holder.thumb.setController(draweeController);

        holder.select.setTextColor(context.getResources().getColor(shareImage.selected ? R.color.orange_900 : R.color.black_alpha_128));
        holder.itemView.setOnClickListener(v -> {
            for (int i = 0; i < data.size(); i++) {
                if (i != position) {
                    data.get(i).selected = false;
                }
            }
            shareImage.selected = !shareImage.selected;
            holder.select.setTextColor(context.getResources().getColor(shareImage.selected ? R.color.orange_900 : R.color.black_alpha_128));
            notifyDataChanged(new ArrayList<>(), false);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void notifyDataChanged(List<ShareImage> data, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<ShareImage> getData() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.share_image_thumb)
        RatioImageView thumb;

        @BindView(R.id.share_image_select)
        TextView select;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}