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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.CouponAdapter;
import com.github.caoyouxin.taoke.datasource.SearchCouponDataSource;
import com.github.caoyouxin.taoke.datasource.SortableCouponDataSource;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.UserData;
import com.github.caoyouxin.taoke.ui.activity.DetailActivity;
import com.github.caoyouxin.taoke.ui.activity.SearchActivity;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.github.caoyouxin.taoke.util.SortHelper;
import com.mikepenz.iconics.view.IconicsTextView;
import com.shizhefei.mvc.IDataAdapter;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.shizhefei.mvc.OnStateChangeListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.dynamicbox.DynamicBox;

import static com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity.DYNAMIC_BOX_MK_LINESPINNER;


public class SearchResultFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView couponList;

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

    @BindView(R.id.sort_commission_up)
    IconicsTextView sortCommissionUp;

    @BindView(R.id.sort_commission_down)
    IconicsTextView sortCommissionDown;

    @BindView(R.id.sort_coupon)
    TextView sortCoupon;

    @BindView(R.id.sort_coupon_up)
    IconicsTextView sortCouponUp;

    @BindView(R.id.sort_coupon_down)
    IconicsTextView sortCouponDown;

    @BindView(R.id.sort_commission_wrapper)
    LinearLayout sortCommissionWrapper;

    View rootView;

    private SortHelper sortHelper;
    private GestureDetector gestureDetector;
    private SearchActivity context;
    private String searchKeyword;
    private boolean isJu;
    private DynamicBox dynamicBox;
    private CouponAdapter couponAdapter;
    private SearchCouponDataSource couponDataSource;
    private MVCHelper<List<CouponItem>> couponListHelper;

    public void setSearchKeyword(String searchKeyword, boolean isJu) {
        this.searchKeyword = searchKeyword;
        this.isJu = isJu;
    }

    public void setSearchKeywordAndUpdate(String searchKeyword, boolean isJu) {
        this.searchKeyword = searchKeyword;
        this.isJu = isJu;
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

            dynamicBox = context.createDynamicBox(context, rootView.findViewById(R.id.recycler_view));

            sortHelper = new SortHelper(getActivity(), couponDataSource, couponListHelper);
            sortHelper.setup(R.id.sort_price_wrapper, sortPrice, Arrays.asList(sortPriceUp, sortPriceDown),
                    Arrays.asList(SortableCouponDataSource.SORT.SORT_PRICE_UP, SortableCouponDataSource.SORT.SORT_PRICE_DOWN));
            sortHelper.setup(R.id.sort_sales_wrapper, sortSales, null,
                    Arrays.asList(SortableCouponDataSource.SORT.SORT_SALES, SortableCouponDataSource.SORT.SORT_SALES));
            sortHelper.setup(R.id.sort_commission_wrapper, sortCommission, Arrays.asList(sortCommissionDown, sortCommissionUp),
                    Arrays.asList(SortableCouponDataSource.SORT.SORT_COMMISSION_DOWN, SortableCouponDataSource.SORT.SORT_COMMISSION_UP));
            sortHelper.setup(R.id.sort_coupon_wrapper, sortCoupon, Arrays.asList(sortCouponDown, sortCouponUp),
                    Arrays.asList(SortableCouponDataSource.SORT.SORT_COUPON_DOWN, SortableCouponDataSource.SORT.SORT_COUPON_UP));
        }
        return rootView;
    }

    private void initCouponList() {
        if (UserData.get().isBuyer()) {
            sortCommissionWrapper.setVisibility(View.GONE);
        }

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
        couponDataSource.setKeyword(this.searchKeyword).setJu(this.isJu);
        sortHelper.handleSortChange(R.id.sort_sales_wrapper, null);
    }

    @OnClick({R.id.sort_coupon_wrapper, R.id.sort_sales_wrapper, R.id.sort_price_wrapper, R.id.sort_commission_wrapper})
    protected void initSortBar(View view) {
        sortHelper.handleSortChange(view.getId(), couponAdapter.getData());
    }

}
