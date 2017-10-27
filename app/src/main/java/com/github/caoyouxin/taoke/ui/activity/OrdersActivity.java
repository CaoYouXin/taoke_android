package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
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

    private TextView[] resetSelectViews;
    private View[] resetSelectedViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orders);

        ButterKnife.bind(this);

        resetSelectViews = new TextView[]{selectAll, selectEffective, selectIneffective};
        resetSelectedViews = new View[]{selectedAll, selectedEffective, selectedIneffective};

        title.setText(R.string.orders);

        onClick(selectEffective);
    }

    @OnClick({R.id.back, R.id.select_all, R.id.select_effective, R.id.select_ineffective})
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
        }
    }

    private void resetSelections(final int id) {
        for (int i = 0; i < this.resetSelectViews.length; i++) {
            TextView tv = this.resetSelectViews[i];
            View v = this.resetSelectedViews[i];
            if (tv.getId() == id) {
                tv.setTextColor(this.getResources().getColor(R.color.orange_300));
                v.setBackgroundColor(this.getResources().getColor(R.color.orange_300));
            } else {
                tv.setTextColor(this.getResources().getColor(R.color.black_alpha_176));
                v.setBackgroundColor(this.getResources().getColor(R.color.trans));
            }
        }
    }
}
