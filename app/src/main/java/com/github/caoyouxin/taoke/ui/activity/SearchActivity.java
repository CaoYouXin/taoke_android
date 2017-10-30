package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.CouponAdapter;
import com.github.caoyouxin.taoke.adapter.SearchHintAdapter;
import com.github.caoyouxin.taoke.datasource.CouponDataSource;
import com.github.caoyouxin.taoke.datasource.SearchHintDataSource;
import com.github.caoyouxin.taoke.model.CouponItem;
import com.github.caoyouxin.taoke.model.SearchHintItem;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.next.tagview.TagCloudView;

public class SearchActivity extends BaseActivity implements TagCloudView.OnTagClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.search_history_list)
    TagCloudView searchHistoryList;

    @BindView(R.id.search_hint_list)
    RecyclerView searchHintList;

    @BindView(R.id.coupon_list)
    RecyclerView couponList;

    @BindView(R.id.search_text)
    EditText searchText;

    @BindView(R.id.search_result_wrapper)
    LinearLayout searchResultWrapper;

    @BindView(R.id.search_hint_wrapper)
    FrameLayout searchHintWrapper;

    private GestureDetector gestureDetector;
    private SearchHintDataSource searchHintDataSource;
    private MVCNormalHelper searchHintListHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        title.setText(R.string.search_title);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        this.searchText.setOnEditorActionListener(this);

        initSearchHistoryList();
        initSearchHintList();
        initCouponList();
    }

    private void initCouponList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponList.setLayoutManager(layoutManager);
        //couponList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(1).build());

        CouponAdapter couponAdapter = new CouponAdapter(this);

        couponList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    if (-1 < childPosition && childPosition < couponAdapter.getData().size()) {
                        CouponItem couponItem = couponAdapter.getData().get(childPosition);
                        Intent intent = new Intent(SearchActivity.this, DetailActivity.class).putExtra(DetailActivity.EXTRA_COUPON_ITEM, couponItem);
                        SearchActivity.this.startActivity(intent);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        CouponDataSource couponDataSource = new CouponDataSource(this);

        //hacky to remove mvchelper loadview loadmoreview
        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        MVCNormalHelper couponListHelper = new MVCNormalHelper(couponList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        couponListHelper.setAdapter(couponAdapter);
        couponListHelper.setDataSource(couponDataSource);

        couponListHelper.refresh();
    }

    private void initSearchHintList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchHintList.setLayoutManager(layoutManager);
        searchHintList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).size(1).build());

        SearchHintAdapter searchHintAdapter = new SearchHintAdapter();

        searchHintList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    SearchHintItem searchHintItem = searchHintAdapter.getData().get(childPosition);
                    SearchActivity.this.searchText.setText(searchHintItem.hint);
                    return true;
                } else {
                    return false;
                }
            }
        });

        searchHintDataSource = new SearchHintDataSource(this, "");

        searchHintListHelper = new MVCNormalHelper(searchHintList);
        searchHintListHelper.setAdapter(searchHintAdapter);
        searchHintListHelper.setDataSource(searchHintDataSource);
    }

    private void initSearchHistoryList() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tags.add("搜词" + i);
        }

        searchHistoryList.setTags(tags);
        searchHistoryList.setOnTagClickListener(this);
        searchHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "TagView onClick",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTagClick(int position) {
        if (position == -1) {
            Toast.makeText(getApplicationContext(), "点击末尾文字",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "点击 position : " + position,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }

    @OnTextChanged(R.id.search_text)
    public void onTextChanged(CharSequence text) {
        this.searchHintDataSource.changeInputNow(text.toString());
        this.searchHintListHelper.refresh();
        this.searchResultWrapper.setVisibility(View.INVISIBLE);
        this.searchHintWrapper.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH){
            this.searchResultWrapper.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}
