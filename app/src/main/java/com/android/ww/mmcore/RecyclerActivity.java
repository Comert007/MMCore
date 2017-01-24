package com.android.ww.mmcore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ww.com.core.widget.CustomRecyclerView;

/**
 * Created by fighter on 2016/5/23.
 */
public class RecyclerActivity extends AppCompatActivity {
    CustomRecyclerView rvCustom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        rvCustom = (CustomRecyclerView) findViewById(R.id.rv_custom);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustom.setLayoutManager(lm);

        rvCustom.addEmpty(createTxtVi("EmptyView"));


        RvAdapter adapter = new RvAdapter(this);
        rvCustom.setAdapter(adapter);

        // 必须执行在设置adapter 之后
        // add  header
        rvCustom.addHeadView(createTxtVi("HeadView"));
        // add footer
        rvCustom.addFooterView(createTxtVi("Footer View "));
    }

    private TextView createTxtVi(String _str) {
        TextView textView = new TextView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setText(_str);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    static class RvAdapter extends RecyclerView.Adapter<RvViewHolder> {
        Context context;

        public RvAdapter(Context _context) {
            context = _context;
        }

        @Override
        public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RvViewHolder(LayoutInflater.from(context)
                    .inflate(getViewResId(viewType), null));
        }

        @Override
        public void onBindViewHolder(RvViewHolder holder, int position) {
            holder.txtContent.setText("text content : " + position);
        }

        public int getViewResId(int viewType) {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


    static class RvViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;

        public RvViewHolder(View itemView) {
            super(itemView);

            txtContent = (TextView) itemView.findViewById(android.R.id.text1);
        }

    }
}
