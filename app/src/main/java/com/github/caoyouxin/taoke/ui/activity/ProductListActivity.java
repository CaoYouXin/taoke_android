package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.mikepenz.iconics.view.IconicsTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/26/17.
 */

public class ProductListActivity extends BaseActivity {
    public final static String EXTRA_BRAND_ITEM = "brandItem";

    public final static int SORT_MULTIPLE = 0;
    public final static int SORT_SALES = 1;
    public final static int SORT_PRICE_UP = 2;
    public final static int SORT_PRICE_DOWN = 3;

    @BindView(R.id.sort_multiple)
    TextView sortMultiple;

    @BindView(R.id.sort_sales)
    TextView sortSales;

    @BindView(R.id.sort_price)
    TextView sortPrice;

    @BindView(R.id.sort_price_up)
    IconicsTextView sortPriceUp;

    @BindView(R.id.sort_price_down)
    IconicsTextView sortPriceDown;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private BrandItem brandItem;

    private int sort;

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

    @OnClick({R.id.sort_multiple_wrapper, R.id.sort_sales_wrapper, R.id.sort_price_wrapper})
    protected void initSortBar(View view) {
        sortMultiple.setTextColor(getResources().getColor(R.color.grey_400));
        sortSales.setTextColor(getResources().getColor(R.color.grey_400));
        sortPrice.setTextColor(getResources().getColor(R.color.grey_400));
        sortPriceUp.setTextColor(getResources().getColor(R.color.grey_400));
        sortPriceDown.setTextColor(getResources().getColor(R.color.grey_400));

        switch (view.getId()) {
            case R.id.sort_multiple_wrapper:
                sort = SORT_MULTIPLE;
                sortMultiple.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_sales_wrapper:
                sort = SORT_SALES;
                sortSales.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_price_wrapper:
                if (sort == SORT_PRICE_UP) {
                    sort = SORT_PRICE_DOWN;
                    sortPriceDown.setTextColor(getResources().getColor(R.color.grey_900));
                } else {
                    sort = SORT_PRICE_UP;
                    sortPriceUp.setTextColor(getResources().getColor(R.color.grey_900));
                }
                sortPrice.setTextColor(getResources().getColor(R.color.grey_900));
                break;
        }

        sort();
    }

    private void sort() {

    }

    private void initRefreshRecyclerView() {

    }
}
