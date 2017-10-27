package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/26/17.
 */

public class ProductListActivity extends BaseActivity {
    public final static String EXTRA_BRAND_ITEM = "brandItem";

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private BrandItem brandItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        ButterKnife.bind(this);

        brandItem = getIntent().getParcelableExtra(EXTRA_BRAND_ITEM);
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }

    private void initSortBar() {

    }

    private void initRefreshRecyclerView() {

    }
}
