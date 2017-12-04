package com.github.caoyouxin.taoke.util;

import android.content.Context;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.datasource.SortableCouponDataSource;
import com.mikepenz.iconics.view.IconicsTextView;
import com.shizhefei.mvc.MVCHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortHelper {

    private Context context;
    private SortableCouponDataSource dataSource;
    private MVCHelper mvcHelper;
    private Map<Integer, TextView> mainBar = new HashMap<>();
    private Map<Integer, List<IconicsTextView>> directionBar = new HashMap<>();
    private Map<Integer, List<SortableCouponDataSource.SORT>> sortType = new HashMap<>();

    private SortableCouponDataSource.SORT sort;

    public SortHelper(Context context, SortableCouponDataSource dataSource, MVCHelper mvcHelper) {
        this.context = context;
        this.dataSource = dataSource;
        this.mvcHelper = mvcHelper;
    }

    public boolean setup(int id, TextView main, List<IconicsTextView> direction, List<SortableCouponDataSource.SORT> types) {
        if (null != direction && direction.size() != 2 || null != types && types.size() != 2) {
            return false;
        }

        mainBar.put(id, main);
        directionBar.put(id, direction);
        sortType.put(id, types);
        return true;
    }

    public void handleSortChange(int id, List cache) {
        clear();

        mainBar.get(id).setTextColor(context.getResources().getColor(R.color.grey_900));
        List<SortableCouponDataSource.SORT> sorts = sortType.get(id);
        int idx;
        if (sorts.get(0).equals(sort)) {
            sort = sorts.get(1);
            idx = 1;
        } else {
            sort = sorts.get(0);
            idx = 0;
        }

        List<IconicsTextView> textViews = directionBar.get(id);
        if (null != textViews) {
            textViews.get(idx).setTextColor(context.getResources().getColor(R.color.grey_900));
        }

        dataSource.setSort(sort).setCache(cache);
        mvcHelper.refresh();
    }

    public void clear() {
        for (TextView textView : mainBar.values()) {
            textView.setTextColor(context.getResources().getColor(R.color.grey_400));
        }
        for (List<IconicsTextView> textViews : directionBar.values()) {
            if (null == textViews) {
                continue;
            }
            for (IconicsTextView textView : textViews) {
                textView.setTextColor(context.getResources().getColor(R.color.grey_400));
            }
        }
    }
}
