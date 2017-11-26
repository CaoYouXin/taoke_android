package com.github.caoyouxin.taoke.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.CouponAdapter;
import com.github.caoyouxin.taoke.datasource.SearchCouponDataSource;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.ui.activity.DetailActivity;
import com.github.caoyouxin.taoke.ui.activity.SearchActivity;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.mikepenz.iconics.view.IconicsTextView;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCNormalHelper;
import com.shizhefei.mvc.OnStateChangeListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.dynamicbox.DynamicBox;

import static com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity.DYNAMIC_BOX_MK_LINESPINNER;


public class SearchResultFragment extends Fragment {

    @BindView(R.id.coupon_list)
    RecyclerView couponList;

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

    View rootView;

    private int sort = SearchCouponDataSource.SORT_MULTIPLE;
    private GestureDetector gestureDetector;
    private SearchActivity context;
    private String searchKeyword;
    private DynamicBox dynamicBox;
    private CouponAdapter couponAdapter;
    private SearchCouponDataSource couponDataSource;
    private MVCNormalHelper<List<CouponItem>> couponListHelper;

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public void setSearchKeywordAndUpdate(String searchKeyword) {
        this.searchKeyword = searchKeyword;
        initSearchResult();
    }

    public SearchResultFragment setSearchActivity(GestureDetector gestureDetector, SearchActivity context) {
        this.gestureDetector = gestureDetector;
        this.context = context;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            initCouponList();

            dynamicBox = context.createDynamicBox(context, rootView.findViewById(R.id.coupon_list));

        }
        return rootView;
    }

    private void initCouponList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponList.setLayoutManager(layoutManager);

        couponAdapter = new CouponAdapter(getActivity());

        couponList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    if (-1 < childPosition && childPosition < couponAdapter.getData().size()) {
                        CouponItem couponItem = couponAdapter.getData().get(childPosition);
                        Intent intent = new Intent(SearchResultFragment.this.getActivity(), DetailActivity.class).putExtra(DetailActivity.EXTRA_COUPON_ITEM, couponItem);
                        SearchResultFragment.this.getActivity().startActivity(intent);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        couponDataSource = new SearchCouponDataSource(getActivity());

        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        couponListHelper = new MVCNormalHelper<>(couponList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        couponListHelper.setAdapter(couponAdapter);
        couponListHelper.setDataSource(couponDataSource);
        couponListHelper.setOnStateChangeListener(new OnStateChangeListener<List<CouponItem>>() {
            @Override
            public void onStartLoadMore(IDataAdapter<List<CouponItem>> adapter) {

            }

            @Override
            public void onEndLoadMore(IDataAdapter<List<CouponItem>> adapter, List<CouponItem> result) {

            }

            @Override
            public void onStartRefresh(IDataAdapter<List<CouponItem>> adapter) {
                dynamicBox.showCustomView(DYNAMIC_BOX_MK_LINESPINNER);
            }

            @Override
            public void onEndRefresh(IDataAdapter<List<CouponItem>> adapter, List<CouponItem> result) {
                dynamicBox.hideAll();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        initSearchResult();
    }

    private void initSearchResult() {
        sortMultiple.setTextColor(getResources().getColor(R.color.grey_900));
        sortSales.setTextColor(getResources().getColor(R.color.grey_400));
        sortPrice.setTextColor(getResources().getColor(R.color.grey_400));
        sortPriceUp.setTextColor(getResources().getColor(R.color.grey_400));
        sortPriceDown.setTextColor(getResources().getColor(R.color.grey_400));
        sortCommission.setTextColor(getResources().getColor(R.color.grey_400));

        couponDataSource.setKeyword(this.searchKeyword).setSort(SearchCouponDataSource.SORT_MULTIPLE).setCache(null);
        couponListHelper.refresh();
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
                sort = SearchCouponDataSource.SORT_MULTIPLE;
                sortMultiple.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_sales_wrapper:
                sort = SearchCouponDataSource.SORT_SALES;
                sortSales.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_price_wrapper:
                if (sort == SearchCouponDataSource.SORT_PRICE_UP) {
                    sort = SearchCouponDataSource.SORT_PRICE_DOWN;
                    sortPriceDown.setTextColor(getResources().getColor(R.color.grey_900));
                } else {
                    sort = SearchCouponDataSource.SORT_PRICE_UP;
                    sortPriceUp.setTextColor(getResources().getColor(R.color.grey_900));
                }
                sortPrice.setTextColor(getResources().getColor(R.color.grey_900));
                break;
            case R.id.sort_commission_wrapper:
                sort = SearchCouponDataSource.SORT_COMMISSION;
                sortCommission.setTextColor(getResources().getColor(R.color.grey_900));
                break;
        }

        couponDataSource.setSort(sort).setCache(couponAdapter.getData());
        couponListHelper.refresh();
    }

}
