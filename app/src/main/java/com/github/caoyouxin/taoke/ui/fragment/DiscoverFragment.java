package com.github.caoyouxin.taoke.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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
import com.github.caoyouxin.taoke.adapter.CouponAdapter;
import com.github.caoyouxin.taoke.api.CouponTab;
import com.github.caoyouxin.taoke.api.RxHelper;
import com.github.caoyouxin.taoke.api.TaoKeRetrofit;
import com.github.caoyouxin.taoke.datasource.CouponDataSource;
import com.github.caoyouxin.taoke.ui.widget.HackyTextSliderView;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    View rootView;

    MVCHelper mvcHelper;

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

            initCouponTab();

            initRecyclerView();

            initRefreshLayout();
        }
        return rootView;
    }

    @OnClick({R.id.tool_collection, R.id.tool_coupon, R.id.tool_11, R.id.tool_convert, R.id.tool_novice})
    protected void onToolClick(View view) {

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

    private void initCouponTab() {
        TaoKeRetrofit.getService().getCouponTabData()
                .compose(((BaseActivity) getActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(tabs -> {
                    for (CouponTab tab : tabs) {
                        tabLayout.addTab(tabLayout.newTab().setText(tab.title));
                    }
                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            appBarLayout.setExpanded(false, true);
                            mvcHelper.refresh();
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(1).build());

        CouponAdapter couponAdapter = new CouponAdapter();

        GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    return true;
                } else {
                    return false;
                }
            }
        });

        CouponDataSource couponDataSource = new CouponDataSource(getActivity());

        mvcHelper = new MVCNormalHelper(recyclerView);
        mvcHelper.setAdapter(couponAdapter);
        mvcHelper.setDataSource(couponDataSource);

        mvcHelper.refresh();
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
