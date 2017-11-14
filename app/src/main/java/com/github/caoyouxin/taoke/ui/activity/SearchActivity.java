package com.github.caoyouxin.taoke.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.model.SearchHistory;
import com.github.caoyouxin.taoke.ui.fragment.SearchHintFragment;
import com.github.caoyouxin.taoke.ui.fragment.SearchHistoryFragment;
import com.github.caoyouxin.taoke.ui.fragment.SearchResultFragment;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.search_text)
    EditText searchText;

    private SearchHistoryFragment searchHistoryFragment;
    private SearchHintFragment searchHintFragment;
    private SearchResultFragment searchResultFragment;
    private GestureDetector gestureDetector;
    private SearchHistory searchHistory = new SearchHistory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        title.setText(R.string.search_title);

        this.searchText.setOnEditorActionListener(this);

        this.showSearchHistory();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.searchHistory.persist();
    }

    @OnClick(R.id.back)
    protected void onBackClick(View view) {
        onBackPressed();
    }

    @OnTextChanged(R.id.search_text)
    public void onTextChanged(CharSequence text) {
        this.showSearchHint(text.toString());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_GO){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            String keyword = v.getText().toString();
            this.showSearchResult(keyword);
            this.searchHistory.add(keyword);
            return true;
        }
        return false;
    }

    private void showSearchHistory() {
        if (null == this.searchHistoryFragment) {
            this.searchHistoryFragment = new SearchHistoryFragment().setSearchActivity(this);
        }

        this.searchHistoryFragment.setHistory(searchHistory.get());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.search_content, this.searchHistoryFragment);
        ft.commit();
    }

    private void showSearchResult(String inputNow) {
        if (null == this.searchResultFragment) {
            this.searchResultFragment = new SearchResultFragment().setSearchActivity(gestureDetector, this);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentById = fm.findFragmentById(R.id.search_content);

        if (null != fragmentById && fragmentById instanceof SearchResultFragment) {

            this.searchResultFragment.setSearchKeywordAndUpdate(inputNow);
        } else {

            this.searchResultFragment.setSearchKeyword(inputNow);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.search_content, this.searchResultFragment);
            ft.commit();
        }
    }

    private void showSearchHint(String inputNow) {
        if (null == this.searchHintFragment) {
            this.searchHintFragment = new SearchHintFragment().setSearchActivity(this, gestureDetector);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentById = fm.findFragmentById(R.id.search_content);

        if (null != fragmentById && fragmentById instanceof SearchHintFragment) {

            this.searchHintFragment.refresh(inputNow);
        } else {

            this.searchHintFragment.setInputNow(inputNow);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.search_content, this.searchHintFragment);
            ft.commit();
        }
    }

    public void setText(String searchText) {
        this.searchText.setText(searchText);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this.searchText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void clearHistory() {
        this.searchHistory.clear();
        this.searchHistory.persist();
    }
}
