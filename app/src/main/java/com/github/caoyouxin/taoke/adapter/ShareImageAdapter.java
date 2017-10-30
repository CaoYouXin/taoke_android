package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.ShareImage;
import com.github.gnastnosaj.boilerplate.rxbus.RxBus;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * Created by jasontsang on 10/24/17.
 */

public class ShareImageAdapter extends RecyclerView.Adapter implements IDataAdapter<List<ShareImage>> {

    private Context context;

    private List<ShareImage> data = new ArrayList<>();

    public ShareImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_share_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ShareImage shareImage = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.thumb.setImageURI(shareImage.thumb);
        holder.select.setTextColor(context.getResources().getColor(shareImage.selected ? R.color.orange_900 : R.color.black_alpha_128));
        holder.itemView.setOnClickListener(v -> {
            shareImage.selected = !shareImage.selected;
            holder.select.setTextColor(context.getResources().getColor(shareImage.selected ? R.color.orange_900 : R.color.black_alpha_128));
            RxBus.getInstance().post(ShareImageEvent.class, new ShareImageEvent());
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
        RxBus.getInstance().post(ShareImageEvent.class, new ShareImageEvent());
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
        SimpleDraweeView thumb;

        @BindView(R.id.share_image_select)
        TextView select;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ShareImageEvent {
        public final static Observable<ShareImageEvent> observable = RxBus.getInstance().register(ShareImageEvent.class, ShareImageEvent.class);
    }
}