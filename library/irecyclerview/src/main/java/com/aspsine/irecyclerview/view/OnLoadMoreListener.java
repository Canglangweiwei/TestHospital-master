package com.aspsine.irecyclerview.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * <p>
 * 功能描述：用于RecyclerView加载更多的监听器
 * </p>
 * Created by weiwei on 2017/6/21.
 */
@SuppressWarnings("ALL")
public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {

    private int lastVisibleItem;
    private RecyclerView mRecyclerView;

    /**
     * 重写此抽象方法，实现加载更多
     */
    protected abstract void onLoadMore();

    public OnLoadMoreListener(RecyclerView recyclerView) {
        super();
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItem + 1 >= mRecyclerView.getAdapter().getItemCount()) {
            onLoadMore();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        lastVisibleItem =
                ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
    }
}
