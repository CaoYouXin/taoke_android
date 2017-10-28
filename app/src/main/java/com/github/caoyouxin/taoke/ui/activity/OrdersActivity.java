package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.HelpAdapter;
import com.github.caoyouxin.taoke.adapter.OrderAdapter;
import com.github.caoyouxin.taoke.datasource.HelpDataSource;
import com.github.caoyouxin.taoke.datasource.OrderDataSource;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrdersActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.select_all)
    TextView selectAll;

    @BindView(R.id.select_effective)
    TextView selectEffective;

    @BindView(R.id.select_ineffective)
    TextView selectIneffective;

    @BindView(R.id.selected_all)
    View selectedAll;

    @BindView(R.id.selected_effective)
    View selectedEffective;

    @BindView(R.id.selected_ineffective)
    View selectedIneffective;

    @BindView(R.id.orders_sub_category)
    FrameLayout orderSubCategory;

    @BindView(R.id.select_payed)
    TextView selectPayed;

    @BindView(R.id.select_consigned)
    TextView selectConsigned;

    @BindView(R.id.select_settled)
    TextView selectSettled;

    @BindView(R.id.order_list)
    RecyclerView orderList;

    private TextView[] resetSelectViews;
    private View[] resetSelectedViews;
    private TextView[] resetSubSelectViews;
    private int selectedSubId = 0;

    private MVCHelper orderListHelper;
    private OrderDataSource orderDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders);

        ButterKnife.bind(this);

        this.resetSelectViews = new TextView[]{this.selectAll, this.selectEffective, this.selectIneffective};
        this.resetSelectedViews = new View[]{this.selectedAll, this.selectedEffective, this.selectedIneffective};
        this.resetSubSelectViews = new TextView[]{this.selectPayed, this.selectConsigned, this.selectSettled};

        this.title.setText(R.string.orders);

        this.initOrderList();

        this.onClick(selectEffective);
    }

    private void initOrderList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.orderList.setLayoutManager(layoutManager);
        this.orderList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).size(15).build());

        OrderAdapter orderAdapter = new OrderAdapter(this);
        this.orderDataSource = new OrderDataSource(this, OrderDataSource.FetchType.ALL_EFFECTIVE);

        this.orderListHelper = new MVCNormalHelper(this.orderList);
        this.orderListHelper.setAdapter(orderAdapter);
        this.orderListHelper.setDataSource(this.orderDataSource);

        this.orderListHelper.refresh();
    }

    @OnClick({R.id.back, R.id.select_all, R.id.select_effective, R.id.select_ineffective, R.id.select_payed, R.id.select_consigned, R.id.select_settled})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.select_all:
            case R.id.select_effective:
            case R.id.select_ineffective:
                this.resetSelections(view.getId());
                this.orderList.scrollToPosition(0);
                break;
            case R.id.select_payed:
            case R.id.select_consigned:
            case R.id.select_settled:
                this.resetSubSelections(view.getId());
                this.orderList.scrollToPosition(0);
                break;
        }
    }

    private void resetSelections(final int id) {
        for (int i = 0; i < this.resetSelectViews.length; i++) {
            TextView tv = this.resetSelectViews[i];
            View v = this.resetSelectedViews[i];
            if (tv.getId() == id) {
                tv.setTextColor(this.getResources().getColor(R.color.orange_600));
                v.setBackgroundColor(this.getResources().getColor(R.color.orange_600));
            } else {
                tv.setTextColor(this.getResources().getColor(R.color.black_alpha_176));
                v.setBackgroundColor(this.getResources().getColor(R.color.trans));
            }
        }

        if (id == this.selectEffective.getId()) {
            this.orderSubCategory.setVisibility(View.VISIBLE);
            this.resetSubSelections(0);
        } else {
            this.orderSubCategory.setVisibility(View.GONE);
        }

        switch (id) {
            case R.id.select_all:
                this.orderDataSource.changeFetchType(OrderDataSource.FetchType.ALL);
                break;
            case R.id.select_effective:
                this.orderDataSource.changeFetchType(OrderDataSource.FetchType.ALL_EFFECTIVE);
                break;
            case R.id.select_ineffective:
                this.orderDataSource.changeFetchType(OrderDataSource.FetchType.INEFFECTIVE);
                break;
        }
        this.orderListHelper.refresh();
    }

    private void resetSubSelections(final int id) {
        for (TextView resetSubSelectView : this.resetSubSelectViews) {
            if (id == resetSubSelectView.getId()) {

                if (0 == this.selectedSubId || id != selectedSubId) {
                    resetSubSelectView.setTextColor(this.getResources().getColor(R.color.orange_600));
                    this.selectedSubId = id;
                    switch (id) {
                        case R.id.select_payed:
                            this.orderDataSource.changeFetchType(OrderDataSource.FetchType.EFFECTIVE_PAYED);
                            break;
                        case R.id.select_consigned:
                            this.orderDataSource.changeFetchType(OrderDataSource.FetchType.EFFECTIVE_CONSIGNED);
                            break;
                        case R.id.select_settled:
                            this.orderDataSource.changeFetchType(OrderDataSource.FetchType.EFFECTIVE_SETTLED);
                            break;
                    }
                    this.orderListHelper.refresh();
                } else {
                    resetSubSelectView.setTextColor(this.getResources().getColor(R.color.black_alpha_176));
                    this.selectedSubId = 0;
                    this.orderDataSource.changeFetchType(OrderDataSource.FetchType.ALL_EFFECTIVE);
                    this.orderListHelper.refresh();
                }
            } else {
                resetSubSelectView.setTextColor(this.getResources().getColor(R.color.black_alpha_176));
            }
        }
    }

}
