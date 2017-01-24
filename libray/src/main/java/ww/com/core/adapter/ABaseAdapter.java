package ww.com.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * AbListView BaseAdapter
 *
 * @param <T>
 */
public abstract class ABaseAdapter<T> extends BaseAdapter {
    private static final int _ID = 0x7f010000;
    private Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> datas;

    public ABaseAdapter(Context context) {
        super();
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.datas = new ArrayList<T>();
    }


    public List<T> getList() {
        return datas;
    }

    public void appendList(List<T> list) {
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void appendListTop(List<T> list) {
        this.datas.addAll(0, list);
        notifyDataSetChanged();
    }


    public void addList(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.datas.clear();
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T bean) {
        this.datas.add(bean);
        notifyDataSetChanged();
    }

    public void addFirstItem(T bean) {
        this.datas.add(0, bean);
        notifyDataSetChanged();
    }

    public void delItem(T bean) {
        this.datas.remove(bean);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return _ID + position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    protected abstract int getItemLayoutResId(int viewType);

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RvViewHolder<T> viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(getItemLayoutResId(getItemViewType(position)), null);
            viewHolder = getViewHolder(position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RvViewHolder<T>) convertView.getTag();
        }
        viewHolder.onBindData(position, getItem(position));
        return convertView;
    }

    protected abstract RvViewHolder<T> getViewHolder(int position);

    public Context getContext() {
        return mContext;
    }

}
