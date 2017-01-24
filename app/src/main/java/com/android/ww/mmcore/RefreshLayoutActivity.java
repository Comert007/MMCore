package com.android.ww.mmcore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ww.com.core.ScreenUtil;
import ww.com.core.widget.CustomRecyclerView;
import ww.com.core.widget.CustomSwipeRefreshLayout;


/**
 * Created by fighter on 2016/5/23.
 */
public class RefreshLayoutActivity extends AppCompatActivity {
    private static final int NORMAL_ITEM_COUNT = 20;
    private int itemCount = 0;

    CustomSwipeRefreshLayout refreshLayout;
    CustomRecyclerView rvCustom;
    RvAdapter adapter;

    View viFooter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_layout);
        ScreenUtil.init(this);

        refreshLayout = (CustomSwipeRefreshLayout) findViewById(R.id.srl_custom);
        rvCustom = (CustomRecyclerView) findViewById(R.id.rv_custom);

        // 加载更多的布局
        viFooter = LayoutInflater.from(this)
                .inflate(R.layout.view_more_footer, refreshLayout, false);

        rvCustom.setItemAnimator(new DefaultItemAnimator());

        refreshLayout.setRefreshView(rvCustom);
        refreshLayout.setOnSwipeRefreshListener(new CustomSwipeRefreshLayout
                .OnSwipeRefreshLayoutListener() {
            @Override
            public void onHeaderRefreshing() {
                // 刷新数据
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载成功
                        refreshLayout.setRefreshFinished();
                        adapter.addList(getDataList(0, NORMAL_ITEM_COUNT));
                        itemCount = NORMAL_ITEM_COUNT;
                        // 设置可以上拉
                        refreshLayout.setFooterRefreshAble(true);
                    }
                }, 1000);
            }

            @Override
            public void onFooterRefreshing() {
                // 加载更多数据
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshFinished();
                        adapter.appendList(getDataList(itemCount, NORMAL_ITEM_COUNT));
                        itemCount += NORMAL_ITEM_COUNT;
                        if (itemCount >= 110) {
                            // 没有更多， 关闭上拉
                            refreshLayout.setFooterRefreshAble(false);
                        }
                    }
                }, 3500);
            }
        });


        adapter = new RvAdapter(this);
        // 设置无数据时的提示()
        rvCustom.addEmpty(createEmpty("暂无数据"));

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustom.setLayoutManager(lm);
        rvCustom.setAdapter(adapter);

        // 设置加载更多显示的View
        rvCustom.setMoreFooterView(viFooter);
    }

    private List<DataBean> getDataList(int pos, int itemCount) {
        List<DataBean> strings = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            strings.add(new DataBean("item:" + (pos + i)));
        }
        return strings;
    }

    // 回到顶部
    public void onToUp(View v) {
        rvCustom.smoothScrollToPosition(0);
        refreshLayout.setRefreshing(true);
    }

    private TextView createEmpty(String _str) {
        TextView textView = new TextView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        textView.setText(_str);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    class RvAdapter extends ww.com.core.adapter.RvAdapter<DataBean> {
        public RvAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemLayoutResId(int viewType) {
            return R.layout.item_recycler;
        }

        @Override
        protected ww.com.core.adapter.RvViewHolder<DataBean> getViewHolder(int viewType, View view) {
            return new RvViewHolder(this, view);
        }
    }


    static class RvViewHolder extends ww.com.core.adapter.RvViewHolder<DataBean> {
        TextView txtContent;
        Button btn;

        private RvAdapter rvAdapter;

        public RvViewHolder(final RvAdapter adapter, View itemView) {
            super(itemView);
            this.rvAdapter = adapter;
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    bean.content = "更新" + bean.content;
                    adapter.update(bean);
                    return true;
                }
            });
            txtContent = (TextView) itemView.findViewById(R.id.tv);
            btn = (Button) itemView.findViewById(R.id.btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rvAdapter.removeItem(bean);
                }
            });
        }

        @Override
        public void onBindData(int position, DataBean bean) {
            txtContent.setText("text Content: " + bean.content);
        }
    }

    class DataBean {
        String content;

        public DataBean(String content) {
            this.content = content;
        }
    }
}
