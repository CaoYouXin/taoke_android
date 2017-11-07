package com.github.caoyouxin.taoke.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.github.caoyouxin.taoke.datasource.CouponDataSource;
import com.github.caoyouxin.taoke.datasource.ProductDataSource;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.ui.activity.DetailActivity;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.mikepenz.iconics.view.IconicsTextView;
import com.shizhefei.mvc.MVCNormalHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jasontsang on 10/24/17.
 */

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

    private int sort;
    private GestureDetector gestureDetector;

    public SearchResultFragment setSearchActivity(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
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

            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.app_not_release_hint, Snackbar.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void initCouponList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponList.setLayoutManager(layoutManager);
        //couponList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(1).build());

        CouponAdapter couponAdapter = new CouponAdapter(getActivity());

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

        CouponDataSource couponDataSource = new CouponDataSource(getActivity());

        //hacky to remove mvchelper loadview loadmoreview
        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        MVCNormalHelper couponListHelper = new MVCNormalHelper(couponList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        couponListHelper.setAdapter(couponAdapter);
        couponListHelper.setDataSource(couponDataSource);

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
    }

}
