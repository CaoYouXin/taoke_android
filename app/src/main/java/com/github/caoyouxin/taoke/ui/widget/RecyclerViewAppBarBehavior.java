package com.github.caoyouxin.taoke.ui.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.github.gnastnosaj.boilerplate.rxbus.RxBus;

import java.lang.ref.WeakReference;
import java.util.Map;

import io.reactivex.Observable;


public final class RecyclerViewAppBarBehavior extends AppBarLayout.Behavior {

    private Map<RecyclerView, RecyclerViewScrollListener> scrollListenerMap = new ArrayMap<>(); //keep scroll listener map, the custom scroll listener also keep the current scroll Y position.

    public RecyclerViewAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        if (target instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) target;
            if (scrollListenerMap.get(recyclerView) == null) {
                RecyclerViewScrollListener recyclerViewScrollListener = new RecyclerViewScrollListener(coordinatorLayout, child, this);
                scrollListenerMap.put(recyclerView, recyclerViewScrollListener);
                recyclerView.addOnScrollListener(recyclerViewScrollListener);
            }
            scrollListenerMap.get(recyclerView).setVelocity(velocityY);
            consumed = scrollListenerMap.get(recyclerView).getScrolledY() > 0; //recyclerView only consume the fling when it's not scrolled to the top
        }

        if (!consumed) {
            RxBus.getInstance().post(RecyclerViewScrollEvent.class, new RecyclerViewScrollEvent(RecyclerViewScrollEvent.TYPE_NESTED_FLING));
        }

        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    public static class RecyclerViewScrollEvent {
        public final static Observable<RecyclerViewScrollEvent> observable = RxBus.getInstance().register(RecyclerViewScrollEvent.class, RecyclerViewScrollEvent.class);

        public final static int TYPE_REST = 0;
        public final static int TYPE_NESTED_FLING = 1;
        public final static int TYPE_SCROLLED = 2;

        private int type;

        public RecyclerViewScrollEvent() {
        }

        public RecyclerViewScrollEvent(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    private static class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        private int scrolledY;
        private boolean dragging;
        private float velocity;
        private WeakReference<CoordinatorLayout> coordinatorLayoutRef;
        private WeakReference<AppBarLayout> childRef;
        private WeakReference<RecyclerViewAppBarBehavior> behaviorWeakReference;

        public RecyclerViewScrollListener(CoordinatorLayout coordinatorLayout, AppBarLayout child, RecyclerViewAppBarBehavior barBehavior) {
            RecyclerViewScrollEvent.observable.subscribe(recyclerViewScrollEvent -> {
                if (recyclerViewScrollEvent.type == RecyclerViewScrollEvent.TYPE_REST) {
                    scrolledY = 0;
                    dragging = false;
                    velocity = 0;
                }
            });
            coordinatorLayoutRef = new WeakReference<>(coordinatorLayout);
            childRef = new WeakReference<>(child);
            behaviorWeakReference = new WeakReference<>(barBehavior);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            dragging = newState == RecyclerView.SCROLL_STATE_DRAGGING;
        }

        public void setVelocity(float velocity) {
            this.velocity = velocity;
        }

        public int getScrolledY() {
            return scrolledY;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrolledY += dy;

            if (scrolledY <= 0 && !dragging && childRef.get() != null && coordinatorLayoutRef.get() != null && behaviorWeakReference.get() != null) {
                //manually trigger the fling when it's scrolled at the top
                behaviorWeakReference.get().onNestedFling(coordinatorLayoutRef.get(), childRef.get(), recyclerView, 0, velocity, false);
            } else {
                RxBus.getInstance().post(RecyclerViewScrollEvent.class, new RecyclerViewScrollEvent(RecyclerViewScrollEvent.TYPE_SCROLLED));
            }
        }
    }
}