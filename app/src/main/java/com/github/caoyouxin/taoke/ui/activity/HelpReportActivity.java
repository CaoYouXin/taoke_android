package com.github.caoyouxin.taoke.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.caoyouxin.taoke.R;
import com.github.caoyouxin.taoke.adapter.CouponAdapter;
import com.github.caoyouxin.taoke.adapter.HelpAdapter;
import com.github.caoyouxin.taoke.datasource.CouponDataSource;
import com.github.caoyouxin.taoke.datasource.HelpDataSource;
import com.github.caoyouxin.taoke.model.HelpItem;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;
import com.shizhefei.mvc.MVCHelper;
import com.shizhefei.mvc.MVCNormalHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpReportActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.help_list)
    RecyclerView helpList;

    @BindView(R.id.help_content)
    LinearLayout helpContent;

    @BindView(R.id.help_q)
    TextView helpQ;

    @BindView(R.id.help_a)
    TextView helpA;

    @BindView(R.id.report_entry)
    Button reportEntry;

    private GestureDetector gestureDetector;
    private boolean isReadingHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_report);

        ButterKnife.bind(this);

        title.setText(R.string.help_report);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        initHelpList();
    }

    private void initHelpList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        helpList.setLayoutManager(layoutManager);
        helpList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).size(1).build());

        HelpAdapter helpAdapter = new HelpAdapter();

        helpList.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    View childView = rv.findChildViewUnder(event.getX(), event.getY());
                    int childPosition = rv.getChildAdapterPosition(childView);
                    HelpItem helpItem = helpAdapter.getData().get(childPosition);

                    HelpReportActivity.this.helpList.setVisibility(View.GONE);
                    HelpReportActivity.this.reportEntry.setVisibility(View.GONE);
                    HelpReportActivity.this.helpContent.setVisibility(View.VISIBLE);
                    HelpReportActivity.this.helpQ.setText(helpItem.q);
                    HelpReportActivity.this.helpA.setText(helpItem.a);
                    HelpReportActivity.this.isReadingHelp = true;

                    return true;
                } else {
                    return false;
                }
            }
        });

        HelpDataSource helpDataSource = new HelpDataSource(this);

        MVCHelper helpListHelper = new MVCNormalHelper(helpList);
        helpListHelper.setAdapter(helpAdapter);
        helpListHelper.setDataSource(helpDataSource);

        helpListHelper.refresh();
    }

    @OnClick({R.id.back, R.id.report_entry})
    protected void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (this.isReadingHelp) {
                    this.helpList.setVisibility(View.VISIBLE);
                    this.reportEntry.setVisibility(View.VISIBLE);
                    this.helpContent.setVisibility(View.GONE);
                    this.helpQ.setText("");
                    this.helpA.setText("");
                    this.isReadingHelp = false;
                    return;
                }
                onBackPressed();
                return;
            case R.id.report_entry:
                Intent intent = new Intent(this, ReportActivity.class);
                startActivity(intent);
        }
    }
}
