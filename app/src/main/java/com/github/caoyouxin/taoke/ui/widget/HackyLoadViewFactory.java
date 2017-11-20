package com.github.caoyouxin.taoke.ui.widget;

import android.view.View;

import com.shizhefei.mvc.ILoadViewFactory;


public class HackyLoadViewFactory implements ILoadViewFactory {
    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new HackyLoadMoreHelper();
    }

    @Override
    public ILoadView madeLoadView() {
        return new HackyLoadViewHelper();
    }

    private static class HackyLoadMoreHelper implements ILoadMoreView {
        @Override
        public void init(FootViewAdder footViewHolder, View.OnClickListener onClickLoadMoreListener) {

        }

        @Override
        public void showNormal() {

        }

        @Override
        public void showNomore() {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void showFail(Exception e) {

        }
    }

    private static class HackyLoadViewHelper implements ILoadView {
        @Override
        public void init(View switchView, View.OnClickListener onClickRefreshListener) {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void showFail(Exception e) {

        }

        @Override
        public void showEmpty() {

        }

        @Override
        public void tipFail(Exception e) {

        }

        @Override
        public void restore() {

        }
    }
}
