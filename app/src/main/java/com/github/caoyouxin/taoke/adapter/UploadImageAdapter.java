package com.github.caoyouxin.taoke.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.UploadImageItem;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadImageAdapter extends RecyclerView.Adapter implements IDataAdapter<List<UploadImageItem>> {

    private List<UploadImageItem> data = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_upload_image_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;

        UploadImageItem uploadImageItem = data.get(position);

        holder.mask.setVisibility(uploadImageItem.uploaded ? View.GONE : View.VISIBLE);
        holder.thumb.setVisibility(uploadImageItem.isHandle ? View.GONE : View.VISIBLE);

        if (null != uploadImageItem.uri) {
            holder.thumb.setImageURI(uploadImageItem.uri);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void notifyDataChanged(List<UploadImageItem> uploadImageItems, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(uploadImageItems);
        notifyDataSetChanged();
    }

    @Override
    public List<UploadImageItem> getData() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumb)
        SimpleDraweeView thumb;

        @BindView(R.id.mask)
        TextView mask;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
