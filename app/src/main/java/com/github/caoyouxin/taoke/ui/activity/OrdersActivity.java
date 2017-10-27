package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

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

    private TextView[] resetSelectViews;
    private View[] resetSelectedViews;
    private TextView[] resetSubSelectViews;
    private int selectedSubId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders);

        ButterKnife.bind(this);

        resetSelectViews = new TextView[]{selectAll, selectEffective, selectIneffective};
        resetSelectedViews = new View[]{selectedAll, selectedEffective, selectedIneffective};
        resetSubSelectViews = new TextView[]{selectPayed, selectConsigned, selectSettled};

        title.setText(R.string.orders);

        onClick(selectEffective);
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
                break;
            case R.id.select_payed:
            case R.id.select_consigned:
            case R.id.select_settled:
                this.resetSubSelections(view.getId());
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
    }

    private void resetSubSelections(final int id) {
        for (TextView resetSubSelectView : this.resetSubSelectViews) {
            if (id == resetSubSelectView.getId()) {

                if (0 == this.selectedSubId || id != selectedSubId) {
                    resetSubSelectView.setTextColor(this.getResources().getColor(R.color.orange_600));
                    this.selectedSubId = id;
                } else {
                    resetSubSelectView.setTextColor(this.getResources().getColor(R.color.black_alpha_176));
                    this.selectedSubId = 0;
                }
            } else {
                resetSubSelectView.setTextColor(this.getResources().getColor(R.color.black_alpha_176));
            }
        }
    }

}
