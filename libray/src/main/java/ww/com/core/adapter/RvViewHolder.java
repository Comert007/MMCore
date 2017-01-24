package ww.com.core.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import ww.com.core.ScreenUtil;

/**
 * Created by lenovo on 2016/1/26.
 */
public abstract class RvViewHolder<T> extends RecyclerView.ViewHolder {
    protected int position;
    protected T bean;

    public RvViewHolder(View itemView) {
        super(itemView);
        ScreenUtil.scale(itemView);

        initView();
    }

    public void initView() {
        ButterKnife.bind(this, itemView);
    }

    public final void onAttachData(int position, T bean) {
        this.position = position;
        this.bean = bean;
        onBindData(position, bean);
    }

    public abstract void onBindData(int position, T bean);

    public Object getObj() {
        return null;
    }
}
