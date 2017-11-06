package com.github.caoyouxin.taoke.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.BrandAdapter;
import com.github.caoyouxin.taoke.adapter.CouponAdapter;
import com.github.caoyouxin.taoke.api.ApiException;
import com.github.caoyouxin.taoke.datasource.BrandDataSource;
import com.github.caoyouxin.taoke.model.BrandItem;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.CouponTab;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeApi;
import com.github.caoyouxin.taoke.datasource.BrandDataSource;
import com.github.caoyouxin.taoke.datasource.CouponDataSource;
import com.github.caoyouxin.taoke.model.CouponTab;
import com.github.caoyouxin.taoke.ui.activity.DetailActivity;
import com.github.caoyouxin.taoke.ui.activity.NoviceActivity;
import com.github.caoyouxin.taoke.ui.activity.ProductListActivity;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.github.caoyouxin.taoke.ui.widget.HackyTextSliderView;
import com.github.caoyouxin.taoke.ui.widget.RecyclerViewAppBarBehavior;
import com.github.gnastnosaj.boilerplate.rxbus.RxBus;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by jasontsang on 10/24/17.
 */

public class DiscoverFragment extends Fragment {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.slider_layout)
    SliderLayout sliderLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.brand_list)
    RecyclerView brandList;

    @BindView(R.id.coupon_tab)
    TabLayout couponTab;

    @BindView(R.id.coupon_list)
    RecyclerView couponList;

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    View rootView;

    private MVCHelper brandListHelper;
    private MVCHelper couponListHelper;
    private CouponDataSource couponDataSource;

    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_discover, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            initSlider();

            initBrandList();

            initCouponTab();

            initCouponList();

            initFloatingActionButton();

            initRefreshLayout();

        }
        sliderLayout.setDuration(4000);
        return rootView;
    }

    @OnClick({R.id.tool_collection, R.id.tool_coupon, R.id.tool_11, R.id.tool_convert, R.id.tool_novice})
    protected void onToolClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tool_novice:
                intent = new Intent(getActivity(), NoviceActivity.class);
                break;
        }
        if (intent != null) {
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    private void initSlider() {
        DisplayMetrics dMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        int sliderLayoutWidth = dMetrics.widthPixels;
        int sliderLayoutHeight = sliderLayoutWidth * 9 / 25;
        LinearLayout.LayoutParams itemViewParams = new LinearLayout.LayoutParams(sliderLayoutWidth, sliderLayoutHeight);
        sliderLayout.setLayoutParams(itemViewParams);
        updateSlider();
    }

    private void updateSlider() {
        sliderLayout.removeAllSliders();
        TextSliderView textSliderView = new HackyTextSliderView(getActivity());
        textSliderView.image(R.mipmap.splash).setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderLayout.addSlider(textSliderView);
        textSliderView = new HackyTextSliderView(getActivity());
        textSliderView.image(R.mipmap.splash).setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderLayout.addSlider(textSliderView);
        textSliderView = new HackyTextSliderView(getActivity());
        textSliderView.image(R.mipmap.splash).setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderLayout.addSlider(textSliderView);
        textSliderView = new HackyTextSliderView(getActivity());
        textSliderView.image(R.mipmap.splash).setScaleType(BaseSliderView.ScaleType.CenterCrop);
        sliderLayout.addSlider(textSliderView);
    }

    private void initBrandList() {
        brandList.setLayoutManager(new GridLayoutManager(getActivity(), 3) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
//        brandList.addItemDecoration(dividerItemDecoration);
        brandList.addItemDecoration(new Y_DividerItemDecoration(getActivity()) {
            @Override
            public Y_Divider getDivider(int itemPosition) {
                Y_Divider divider;
                switch (itemPosition % 3) {
                    case 0:
                        divider = new Y_DividerBuilder()
                                .setRightSideLine(true, getResources().getColor(R.color.grey_300), 1, 0, 0)
                                .create();
                        break;
                    case 2:
                        divider = new Y_DividerBuilder()
                                .setLeftSideLine(true, getResources().getColor(R.color.grey_300), 1, 0, 0)
                                .create();
                        break;
                    default:
                        divider = new Y_DividerBuilder().create();
                        break;
                }
                return divider;
            }
        });

        BrandAdapter brandAdapter = new BrandAdapter();

        brandList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    if (-1 < childPosition && childPosition < brandAdapter.getData().size()) {
                        BrandItem brandItem = brandAdapter.getData().get(childPosition);
                        Intent intent = new Intent(getActivity(), ProductListActivity.class).putExtra(ProductListActivity.EXTRA_BRAND_ITEM, brandItem);
                        getActivity().startActivity(intent);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        BrandDataSource brandDataSource = new BrandDataSource(getActivity());

        //hacky to remove mvchelper loadview loadmoreview
        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        brandListHelper = new MVCNormalHelper(brandList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        brandListHelper.setAdapter(brandAdapter);
        brandListHelper.setDataSource(brandDataSource);

        brandListHelper.refresh();
    }

    private void initCouponTab() {
        TaoKeApi.getCouponTab()
                .compose(((BaseActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(tabs -> {
                    for (CouponTab tab : tabs) {
                        couponTab.addTab(couponTab.newTab().setText(tab.name));
                    }
                    couponTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            CouponTab couponTab = tabs.get(DiscoverFragment.this.couponTab.getSelectedTabPosition());
                            appBarLayout.setExpanded(false, true);
                            couponList.scrollToPosition(0);
                            RxBus.getInstance().post(RecyclerViewAppBarBehavior.RecyclerViewScrollEvent.class, new RecyclerViewAppBarBehavior.RecyclerViewScrollEvent());

                            couponDataSource.setCid(couponTab.cid);
                            couponListHelper.refresh();
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                }, throwable -> {
                    if (throwable instanceof TimeoutException) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.sign_in_fail_timeout, Snackbar.LENGTH_LONG).show();
                    } else if (throwable instanceof ApiException) {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.sign_in_fail_message, throwable.getMessage()), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.sign_in_fail_network, Snackbar.LENGTH_LONG).show();
                    }
                });
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
                        Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(DetailActivity.EXTRA_COUPON_ITEM, couponItem);
                        getActivity().startActivity(intent);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        couponDataSource = new CouponDataSource(getActivity());

        //hacky to remove mvchelper loadview loadmoreview
        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        couponListHelper = new MVCNormalHelper(couponList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        couponListHelper.setAdapter(couponAdapter);
        couponListHelper.setDataSource(couponDataSource);

        couponListHelper.refresh();
    }

    private void initFloatingActionButton() {
        floatingActionButton.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(MaterialDesignIconic.Icon.gmi_format_valign_top)
                .color(getResources().getColor(R.color.grey_800))
                .sizeDp(18)
                .paddingDp(4));

        floatingActionButton.setOnClickListener(v -> {
            couponList.scrollToPosition(0);
            RxBus.getInstance().post(RecyclerViewAppBarBehavior.RecyclerViewScrollEvent.class, new RecyclerViewAppBarBehavior.RecyclerViewScrollEvent());
            appBarLayout.setExpanded(true, true);
        });

        RecyclerViewAppBarBehavior.RecyclerViewScrollEvent.observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recyclerViewScrollEvent -> {
                    switch (recyclerViewScrollEvent.getType()) {
                        case RecyclerViewAppBarBehavior.RecyclerViewScrollEvent.TYPE_NESTED_FLING:
                            floatingActionButton.setVisibility(View.INVISIBLE);
                            break;
                        case RecyclerViewAppBarBehavior.RecyclerViewScrollEvent.TYPE_SCROLLED:
                            floatingActionButton.setVisibility(View.VISIBLE);
                            break;
                    }
                });
    }

    private void initRefreshLayout() {
        smartRefreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.finishRefresh(2000);
        });
        smartRefreshLayout.setOnLoadmoreListener(refreshlayout -> {
            refreshlayout.finishLoadmore(2000);
        });
    }
}
