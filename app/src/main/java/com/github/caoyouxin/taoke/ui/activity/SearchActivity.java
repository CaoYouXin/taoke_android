package com.github.caoyouxin.taoke.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.caoyouxin.taoke.R;
import com.github.gnastnosaj.boilerplate.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.next.tagview.TagCloudView;

public class SearchActivity extends BaseActivity implements TagCloudView.OnTagClickListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.search_history_list)
    TagCloudView searchHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        title.setText(R.string.search_title);

        initSearchHistoryList();
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
}
