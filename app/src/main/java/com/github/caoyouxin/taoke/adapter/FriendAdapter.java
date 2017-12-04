package com.github.caoyouxin.taoke.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.FriendItem;
import com.github.caoyouxin.taoke.util.SpannedTextUtil;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendAdapter extends RecyclerView.Adapter implements IDataAdapter<List<FriendItem>> {

    private List<FriendItem> data = new ArrayList<>();
    private Context context;

    public FriendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        FriendItem item = data.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.friendName.setText(item.name);
        holder.friendAmount.setText(SpannedTextUtil.buildAmount(this.context, R.string.friends_amount, item.amount.toString().substring(0, item.amount.toString().indexOf('.') + 3), ' ', 1));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void notifyDataChanged(List<FriendItem> data, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public List<FriendItem> getData() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.friend_name)
        TextView friendName;

        @BindView(R.id.friend_amount)
        TextView friendAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}