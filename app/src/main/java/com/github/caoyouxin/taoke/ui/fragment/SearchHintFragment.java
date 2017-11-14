package com.github.caoyouxin.taoke.ui.fragment;

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

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.SearchHintAdapter;
import com.github.caoyouxin.taoke.datasource.SearchHintDataSource;
import com.github.caoyouxin.taoke.model.SearchHintItem;
import com.github.caoyouxin.taoke.ui.activity.SearchActivity;
import com.github.caoyouxin.taoke.ui.widget.HackyLoadViewFactory;
import com.shizhefei.mvc.MVCNormalHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jasontsang on 10/24/17.
 */

public class SearchHintFragment extends Fragment {

    View rootView;

    @BindView(R.id.search_hint_list)
    RecyclerView searchHintList;

    private SearchHintDataSource searchHintDataSource;
    private MVCNormalHelper<List<SearchHintItem>> searchHintListHelper;

    private String inputNow;
    private SearchActivity context;
    private GestureDetector gestureDetector;

    public SearchHintFragment setSearchActivity(SearchActivity context, GestureDetector gestureDetector) {
        this.context = context;
        this.gestureDetector = gestureDetector;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_hint, container, false);
            ButterKnife.bind(this, rootView);

            if (savedInstanceState != null) {
                //restore
            }

            initSearchHistoryList();
        }
        return rootView;
    }

    private void initSearchHistoryList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchHintList.setLayoutManager(layoutManager);
        searchHintList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).size(1).build());

        SearchHintAdapter searchHintAdapter = new SearchHintAdapter();

        searchHintList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    SearchHintItem searchHintItem = searchHintAdapter.getData().get(childPosition);
                    SearchHintFragment.this.context.setText(searchHintItem.hint);
                    return true;
                } else {
                    return false;
                }
            }
        });

        searchHintDataSource = new SearchHintDataSource(getActivity(), this.inputNow);

        HackyLoadViewFactory hackyLoadViewFactory = new HackyLoadViewFactory();
        searchHintListHelper = new MVCNormalHelper<>(searchHintList, hackyLoadViewFactory.madeLoadView(), hackyLoadViewFactory.madeLoadMoreView());
        searchHintListHelper.setAdapter(searchHintAdapter);
        searchHintListHelper.setDataSource(searchHintDataSource);
        this.searchHintListHelper.refresh();
    }

    public SearchHintFragment setInputNow(String inputNow) {
        this.inputNow = inputNow;
        return this;
    }

    public void refresh(String inputNow) {
        this.searchHintDataSource.changeInputNow(inputNow);
        this.searchHintListHelper.refresh();
    }

}
