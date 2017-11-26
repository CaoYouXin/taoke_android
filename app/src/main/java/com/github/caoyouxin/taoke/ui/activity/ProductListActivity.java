package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.ProductAdapter;
import com.github.caoyouxin.taoke.datasource.ProductDataSource;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.mikepenz.iconics.view.IconicsTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.shizhefei.mvc.OnStateChangeListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductListActivity extends BaseActivity {
    public final static String EXTRA_BRAND_ITEM = "brandItem";

    @BindView(R.id.title)
    TextView title;

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

    @BindView(R.id.sort_commission)
    TextView sortCommission;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private BrandItem brandItem;

    private int sort;

    private MVCHelper<List<CouponItem>> mvcHelper;
    private ProductDataSource productDataSource;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        ButterKnife.bind(this);

        brandItem = getIntent().getParcelableExtra(EXTRA_BRAND_ITEM);
        title.setText(brandItem.title);

        initRefreshRecyclerView();

        createDynamicBox(findViewById(R.id.recycler_view));
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }

    @OnClick({R.id.sort_multiple_wrapper, R.id.sort_sales_wrapper, R.id.sort_price_wrapper, R.id.sort_commission_wrapper})
    protected void initSortBar(View view) {
        sortMultiple.setTextColor(getResources().getColor(R.color.grey_400));
        sortSales.setTextColor(getResources().getColor(R.color.grey_400));
        sortPrice.setTextColor(getResources().getColor(R.color.grey_400));
        sortPriceUp.setTextColor(getResources().getColor(R.color.grey_400));
        sortPriceDown.setTextColor(getResources().getColor(R.color.grey_400));
        sortCommission.setTextColor(getResources().getColor(R.color.grey_400));

        switch (view.getId()) {
            case R.id.sort_multiple_wrapper:
                sort = ProductDataSource.SORT_MULTIPLE;
                sortMultiple.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_sales_wrapper:
                sort = ProductDataSource.SORT_SALES;
                sortSales.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_price_wrapper:
                if (sort == ProductDataSource.SORT_PRICE_UP) {
                    sort = ProductDataSource.SORT_PRICE_DOWN;
                    sortPriceDown.setTextColor(getResources().getColor(R.color.grey_900));
                } else {
                    sort = ProductDataSource.SORT_PRICE_UP;
                    sortPriceUp.setTextColor(getResources().getColor(R.color.grey_900));
                }
                sortPrice.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_commission_wrapper:
                sort = ProductDataSource.SORT_COMMISSION;
                sortCommission.setTextColor(getResources().getColor(R.color.grey_900));
                break;
        }

        sort();
    }

    private void sort() {
        productDataSource.setSort(sort).setCache(productAdapter.getData());
        mvcHelper.refresh();
        smartRefreshLayout.finishRefresh(2000);
    }

    private void initRefreshRecyclerView() {
        productAdapter = new ProductAdapter(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    if (-1 < childPosition && childPosition < productAdapter.getData().size()) {
                        CouponItem couponItem = productAdapter.getData().get(childPosition);
                        Intent intent = new Intent(ProductListActivity.this, DetailActivity.class).putExtra(DetailActivity.EXTRA_COUPON_ITEM, couponItem);
                        ProductListActivity.this.startActivity(intent);
                    }
                    return false;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });

        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        mvcHelper = new MVCNormalHelper<>(recyclerView, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());

        productDataSource = new ProductDataSource(this, brandItem);
        mvcHelper.setAdapter(productAdapter);
        mvcHelper.setDataSource(productDataSource);
        mvcHelper.setOnStateChangeListener(new OnStateChangeListener<List<CouponItem>>() {
            @Override
            public void onStartLoadMore(IDataAdapter<List<CouponItem>> adapter) {

            }

            @Override
            public void onEndLoadMore(IDataAdapter<List<CouponItem>> adapter, List<CouponItem> result) {

            }

            @Override
            public void onStartRefresh(IDataAdapter<List<CouponItem>> adapter) {
                showDynamicBoxCustomView(DYNAMIC_BOX_MK_LINESPINNER, ProductListActivity.this);
            }

            @Override
            public void onEndRefresh(IDataAdapter<List<CouponItem>> adapter, List<CouponItem> result) {
                dismissDynamicBox(ProductListActivity.this);
            }
        });
        mvcHelper.refresh();

        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mvcHelper.refresh();
            refreshLayout.finishRefresh(2000);
        });
    }
}
