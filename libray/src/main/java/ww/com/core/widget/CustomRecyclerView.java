package ww.com.core.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by fighter on 2016/1/29.
 */
public class CustomRecyclerView extends FrameLayout {
    WWRecyclerView recyclerView;
    FrameLayout flEmpty;
    RecyclerView.Adapter adapter;

    private View moreFooterView;  // 加载更多的FooterView

    private RecyclerView.AdapterDataObserver emptyObserver =
            new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (adapter == null) {
                        return;
                    }
                    if (adapter.getItemCount() <= 0) {
                        flEmpty.setVisibility(View.VISIBLE);
                    } else {
                        flEmpty.setVisibility(View.GONE);
                    }
                }
            };

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);

        flEmpty = new FrameLayout(context);
        recyclerView = new WWRecyclerView(context);

        addView(flEmpty, lp);
        addView(recyclerView, lp);
    }

    public void addEmpty(View emptyView) {
        flEmpty.removeAllViews();
        flEmpty.addView(emptyView);
    }

    public RecyclerView getInnerRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            this.adapter = adapter;
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        recyclerView.setAdapter(adapter);
        emptyObserver.onChanged();
    }

    public void addHeadView(View v) {
        recyclerView.addHeadView(v);
    }

    public void removeHeadView(View v) {
        recyclerView.removeHeadView(v);
    }


    public void showMoreFooterView() {
        if (this.moreFooterView == null) {
            return;
        }

        this.addFooterView(this.moreFooterView);
        smoothScrollToPosition(getAdapter().getItemCount());
    }

    public void hideMoreFooterView() {
        if (this.moreFooterView == null) {
            return;
        }
        this.removeFooterView(this.moreFooterView);
    }

    /**
     * 设置加载更多的View
     *
     * @param moreFooterView
     */
    public void setMoreFooterView(View moreFooterView) {
        this.moreFooterView = moreFooterView;
    }

    public void addFooterView(View v) {
        recyclerView.addFooterView(v);
    }

    public void removeFooterView(View v) {
        recyclerView.removeFooterView(v);
    }


    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        recyclerView.addItemDecoration(decor, index);
    }


    public void addOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener listener) {
        recyclerView.addOnChildAttachStateChangeListener(listener);
    }


    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        recyclerView.addOnItemTouchListener(listener);
    }


    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.addOnScrollListener(listener);
    }

    public void clearOnChildAttachStateChangeListeners() {
        recyclerView.clearOnChildAttachStateChangeListeners();
    }

    public void clearOnScrollListeners() {
        recyclerView.clearOnScrollListeners();
    }

    public View findChildViewUnder(float x, float y) {
        return recyclerView.findChildViewUnder(x, y);
    }

    @Nullable
    public View findContainingItemView(View view) {
        return recyclerView.findContainingItemView(view);
    }

    @Nullable
    public RecyclerView.ViewHolder findContainingViewHolder(View view) {
        return recyclerView.findContainingViewHolder(view);
    }

    public RecyclerView.ViewHolder findViewHolderForAdapterPosition(int position) {
        return recyclerView.findViewHolderForAdapterPosition(position);
    }

    public RecyclerView.ViewHolder findViewHolderForItemId(long id) {
        return recyclerView.findViewHolderForItemId(id);
    }

    public RecyclerView.ViewHolder findViewHolderForLayoutPosition(int position) {
        return recyclerView.findViewHolderForLayoutPosition(position);
    }

    public RecyclerView.ViewHolder findViewHolderForPosition(int position) {
        return recyclerView.findViewHolderForPosition(position);
    }

    public boolean fling(int velocityX, int velocityY) {
        return recyclerView.fling(velocityX, velocityY);
    }


    public RecyclerView.Adapter getAdapter() {
        return recyclerView.getAdapter();
    }

    public long getChildItemId(View child) {
        return recyclerView.getChildItemId(child);
    }

    public int getChildLayoutPosition(View child) {
        return recyclerView.getChildLayoutPosition(child);
    }

    public int getChildPosition(View child) {
        return recyclerView.getChildPosition(child);
    }

    public RecyclerView.ViewHolder getChildViewHolder(View child) {
        return recyclerView.getChildViewHolder(child);
    }

    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return recyclerView.getCompatAccessibilityDelegate();
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        return recyclerView.getItemAnimator();
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return recyclerView.getLayoutManager();
    }

    public int getMaxFlingVelocity() {
        return recyclerView.getMaxFlingVelocity();
    }

    public int getMinFlingVelocity() {
        return recyclerView.getMinFlingVelocity();
    }

    public RecyclerView.RecycledViewPool getRecycledViewPool() {
        return recyclerView.getRecycledViewPool();
    }

    public int getScrollState() {
        return recyclerView.getScrollState();
    }

    public boolean hasFixedSize() {
        return recyclerView.hasFixedSize();
    }

    public boolean hasPendingAdapterUpdates() {
        return recyclerView.hasPendingAdapterUpdates();
    }

    public void invalidateItemDecorations() {
        recyclerView.invalidateItemDecorations();
    }

    public boolean isAnimating() {
        return recyclerView.isAnimating();
    }

    public boolean isComputingLayout() {
        return recyclerView.isComputingLayout();
    }

    public boolean isLayoutFrozen() {
        return recyclerView.isLayoutFrozen();
    }

    public void offsetChildrenHorizontal(int dx) {
        recyclerView.offsetChildrenHorizontal(dx);
    }

    public void offsetChildrenVertical(int dy) {
        recyclerView.offsetChildrenVertical(dy);
    }

    public void onChildAttachedToWindow(View child) {
        recyclerView.onChildAttachedToWindow(child);
    }

    public void onChildDetachedFromWindow(View child) {
        recyclerView.onChildDetachedFromWindow(child);
    }

    public void onScrolled(int dx, int dy) {
        recyclerView.onScrolled(dx, dy);
    }

    public void onScrollStateChanged(int state) {
        recyclerView.onScrollStateChanged(state);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.removeItemDecoration(decor);
    }

    public void removeOnChildAttachStateChangeListener(RecyclerView.OnChildAttachStateChangeListener listener) {
        recyclerView.removeOnChildAttachStateChangeListener(listener);
    }

    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        recyclerView.removeOnItemTouchListener(listener);
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        recyclerView.removeOnScrollListener(listener);
    }

    public void scrollToPosition(int position) {
        recyclerView.scrollToPosition(position);
    }

    public void setAccessibilityDelegateCompat(RecyclerViewAccessibilityDelegate accessibilityDelegate) {
        recyclerView.setAccessibilityDelegateCompat(accessibilityDelegate);
    }

    public void setChildDrawingOrderCallback(RecyclerView.ChildDrawingOrderCallback childDrawingOrderCallback) {
        recyclerView.setChildDrawingOrderCallback(childDrawingOrderCallback);
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        recyclerView.setItemAnimator(animator);
    }


    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool pool) {
        recyclerView.setRecycledViewPool(pool);
    }

    public void setRecyclerListener(RecyclerView.RecyclerListener listener) {
        recyclerView.setRecyclerListener(listener);
    }

    public void setScrollingTouchSlop(int slopConstant) {
        recyclerView.setScrollingTouchSlop(slopConstant);
    }

    public void setViewCacheExtension(RecyclerView.ViewCacheExtension extension) {
        recyclerView.setViewCacheExtension(extension);
    }

    public void smoothScrollBy(int dx, int dy) {
        recyclerView.smoothScrollBy(dx, dy);
    }

    public void smoothScrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    public void stopScroll() {
        recyclerView.stopScroll();
    }

    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        recyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
    }


    private static class WWRecyclerView extends RecyclerView {
        private HeaderRecyclerAdapter hfAdapter;

        public WWRecyclerView(Context context) {
            super(context);
        }

        public WWRecyclerView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public WWRecyclerView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void setAdapter(Adapter adapter) {
            hfAdapter = new HeaderRecyclerAdapter(adapter);
            super.setAdapter(hfAdapter);
        }

        public void addHeadView(View v) {
            if (hfAdapter != null) {
                hfAdapter.addHeaderView(v);
            }
        }

        public void removeHeadView(View v) {
            if (hfAdapter != null) {
                hfAdapter.removeHeaderView(v);
            }
        }

        public void addFooterView(View v) {
            if (hfAdapter != null) {
                hfAdapter.addFooterView(v);
            }
        }

        public void removeFooterView(View v) {
            if (hfAdapter != null) {
                hfAdapter.removeFooterView(v);
            }
        }
    }


    private static class HeaderRecyclerAdapter
            <VH extends HeaderRecyclerAdapter.ViewHolder>
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
        private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 1;

        /**
         * RecyclerView使用的，真正的Adapter
         */
        private RecyclerView.Adapter<RecyclerView.ViewHolder> innerAdapter;

        private ArrayList<View> mHeaderViews = new ArrayList<>();
        private ArrayList<View> mFooterViews = new ArrayList<>();

        private RecyclerView.AdapterDataObserver dataObserver
                = new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                notifyItemRangeChanged(positionStart + getHeaderViewsCount(), itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                notifyItemRangeInserted(positionStart + getHeaderViewsCount(), itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                notifyItemRangeRemoved(positionStart + getHeaderViewsCount(), itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                int headerViewsCountCount = getHeaderViewsCount();
                notifyItemRangeChanged(fromPosition + headerViewsCountCount,
                        toPosition + headerViewsCountCount + itemCount);
            }
        };

        public HeaderRecyclerAdapter() {
        }

        public HeaderRecyclerAdapter(RecyclerView.Adapter innerAdapter) {
            setAdapter(innerAdapter);
        }

        /**
         * 设置adapter
         *
         * @param adapter
         */
        public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {

            if (adapter != null) {
                if (!(adapter instanceof RecyclerView.Adapter))
                    throw new RuntimeException("your adapter must be a RecyclerView.Adapter");
            }

            if (innerAdapter != null) {
                notifyItemRangeRemoved(getHeaderViewsCount(), innerAdapter.getItemCount());
                innerAdapter.unregisterAdapterDataObserver(dataObserver);
            }

            this.innerAdapter = adapter;
            innerAdapter.registerAdapterDataObserver(dataObserver);
            notifyItemRangeInserted(getHeaderViewsCount(), innerAdapter.getItemCount());
        }

        public RecyclerView.Adapter getInnerAdapter() {
            return innerAdapter;
        }

        public void addHeaderView(View header) {

            if (header == null) {
                throw new RuntimeException("header is null");
            }

            if (!mHeaderViews.contains(header)) {
                mHeaderViews.add(header);
            }

            this.notifyDataSetChanged();
        }

        public void addFooterView(View footer) {
            if (footer == null) {
                throw new RuntimeException("footer is null");
            }

            if (!mFooterViews.contains(footer)) {
                mFooterViews.add(footer);
            }

            this.notifyItemInserted(getItemCount() - 1);
        }

        /**
         * 返回第一个FoView
         *
         * @return
         */
        public View getFooterView() {
            return getFooterViewsCount() > 0 ? mFooterViews.get(0) : null;
        }

        /**
         * 返回第一个HeaderView
         *
         * @return
         */
        public View getHeaderView() {
            return getHeaderViewsCount() > 0 ? mHeaderViews.get(0) : null;
        }

        public void removeHeaderView(View view) {
            mHeaderViews.remove(view);
            this.notifyDataSetChanged();
        }

        public void removeFooterView(View view) {
            int index = mFooterViews.indexOf(view);
            if (index > -1) {
                mFooterViews.remove(index);
            }
            this.notifyItemRemoved(getHeaderViewsCount() +
                    innerAdapter.getItemCount() + index);
        }

        public int getHeaderViewsCount() {
            return mHeaderViews.size();
        }

        public int getFooterViewsCount() {
            return mFooterViews.size();
        }

        public boolean isHeader(int position) {
            return getHeaderViewsCount() > 0 && position == 0;
        }

        public boolean isFooter(int position) {
            int lastPosition = getItemCount() - 1;
            return getFooterViewsCount() > 0 && position == lastPosition;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int headerViewsCountCount = getHeaderViewsCount();
            if (viewType < TYPE_HEADER_VIEW + headerViewsCountCount) {
                return new ViewHolder(mHeaderViews.get(viewType - TYPE_HEADER_VIEW));
            } else if (viewType >= TYPE_FOOTER_VIEW && viewType < Integer.MAX_VALUE / 2) {
                return new ViewHolder(mFooterViews.get(viewType - TYPE_FOOTER_VIEW));
            } else {
                return innerAdapter.onCreateViewHolder(parent, viewType - Integer.MAX_VALUE / 2);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int headerViewsCountCount = getHeaderViewsCount();
            if (position >= headerViewsCountCount &&
                    position < headerViewsCountCount + innerAdapter.getItemCount()) {
                innerAdapter.onBindViewHolder(holder, position - headerViewsCountCount);
            } else {
                ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                }
            }
        }

        @Override
        public int getItemCount() {
            return getHeaderViewsCount() + getFooterViewsCount() + innerAdapter.getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            int innerCount = innerAdapter.getItemCount();
            int headerViewsCountCount = getHeaderViewsCount();
            if (position < headerViewsCountCount) {
                return TYPE_HEADER_VIEW + position;
            } else if (headerViewsCountCount <= position && position < headerViewsCountCount + innerCount) {

                int innerItemViewType = innerAdapter.getItemViewType(position - headerViewsCountCount);
                if (innerItemViewType >= Integer.MAX_VALUE / 2) {
                    throw new IllegalArgumentException
                            ("your adapter's return value of getViewTypeCount() must < Integer.MAX_VALUE / 2");
                }
                return innerItemViewType + Integer.MAX_VALUE / 2;
            } else {
                return TYPE_FOOTER_VIEW + position - headerViewsCountCount - innerCount;
            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
