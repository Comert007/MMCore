package ww.com.core.listener;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * 暂时先不使用改类做RecyclerView的item 点击监听事件.<br/>
 * 存在bug，它会将item view上的 例如 button 的Click监听事件给拦截.
 * Created by fighter on 2016/9/23.
 */
@Deprecated
public abstract class SimpleItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {
    private GestureDetectorCompat mGestureDetector;
    MyOnGestureListener gestureListener;

    public SimpleItemTouchListener(Context context) {
        gestureListener = new MyOnGestureListener();
        this.mGestureDetector = new GestureDetectorCompat(context, gestureListener);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        this.gestureListener.setRecyclerView(rv);
        this.mGestureDetector.onTouchEvent(e);
        return false;
    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder, int position);

    public void onItemLongClick(RecyclerView.ViewHolder viewHolder, int position) {

    }

    private class MyOnGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

        RecyclerView recyclerView;

        public void setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(childView);
                onItemClick(vh, recyclerView.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (childView != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(childView);
                onItemLongClick(vh, recyclerView.getChildAdapterPosition(childView));
            }
        }
    }
}
