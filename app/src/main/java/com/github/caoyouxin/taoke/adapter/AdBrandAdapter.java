package com.github.caoyouxin.taoke.adapter;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.AdBrandItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdBrandAdapter extends BaseAdapter implements IDataAdapter<List<AdBrandItem>> {

    private final BaseActivity context;
    private final List<AdBrandItem> data = new ArrayList<>();

    public AdBrandAdapter(BaseActivity context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if (convertView != null) {
            v = convertView;
        } else {
            v = LayoutInflater.from(context.getApplicationContext()).inflate(
                    R.layout.layout_discover_adbrand_item, null);
        }

        AdBrandItem adBrandItem = data.get(position);

        SimpleDraweeView brandThumb = v.findViewById(R.id.brand_thumb);
        brandThumb.setImageURI(adBrandItem.thumb);

        return v;
    }

    @Override
    public void notifyDataChanged(List<AdBrandItem> adBrandItems, boolean isRefresh) {
        if (isRefresh) {
            this.data.clear();
        }
        this.data.addAll(adBrandItems);
        notifyDataSetChanged();
    }

    @Override
    public List<AdBrandItem> getData() {
        return data;
    }
}
