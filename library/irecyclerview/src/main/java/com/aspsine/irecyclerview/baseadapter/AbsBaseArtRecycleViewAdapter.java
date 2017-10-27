package com.aspsine.irecyclerview.baseadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.irecyclerview.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Wei
 * DATE 2016/8/3
 * owspace
 */
@SuppressWarnings("ALL")
public abstract class AbsBaseArtRecycleViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER_TYPE = 1001;
    private static final int CONTENT_TYPE = 1002;
    private List<T> artList = new ArrayList<>();
    protected Context mContext;
    private boolean hasMore = true;
    private boolean error = false;

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public AbsBaseArtRecycleViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_footer, parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(bindAbsViewLayoutResID(), parent, false);
            return getAbsBindViewHolder(mContext, view);
        }
    }

    /**
     * 获取item页面
     *
     * @return
     */
    protected abstract int bindAbsViewLayoutResID();

    /**
     * 获取绑定的viewHolder
     *
     * @param context
     * @param view
     * @return
     */
    protected abstract RecyclerView.ViewHolder getAbsBindViewHolder(Context context, View view);

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        }
        return CONTENT_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position + 1 == getItemCount()) {
            if (artList.size() == 0) {
                return;
            }
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            if (error) {
                error = false;
                footerHolder.avi.setVisibility(View.INVISIBLE);
                footerHolder.noMoreTx.setVisibility(View.INVISIBLE);
                footerHolder.errorTx.setVisibility(View.VISIBLE);
            }
            if (hasMore) {
                footerHolder.avi.setVisibility(View.VISIBLE);
                footerHolder.noMoreTx.setVisibility(View.INVISIBLE);
                footerHolder.errorTx.setVisibility(View.INVISIBLE);
            } else {
                footerHolder.avi.setVisibility(View.INVISIBLE);
                footerHolder.noMoreTx.setVisibility(View.VISIBLE);
                footerHolder.errorTx.setVisibility(View.INVISIBLE);
            }
        } else {
            bindAbsVHolder(holder, position);
        }
    }

    /**
     * 绑定viewHolder中页面的数据
     * @param holder
     * @param position
     */
    protected abstract void bindAbsVHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return artList.size() + 1;
    }

    public List<T> getArtList() {
        return artList;
    }

    public T get(int position) {
        return artList.get(position);
    }

    public void addArtList(List<T> artList) {
        int position = artList.size() - 1;
        this.artList.addAll(artList);
        notifyItemChanged(position);
    }

    public void reset(List<T> artList) {
        this.artList.clear();
        this.artList.addAll(artList);
        notifyDataSetChanged();
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        private AVLoadingIndicatorView avi;
        private TextView noMoreTx;
        private TextView errorTx;

        FooterViewHolder(View itemView) {
            super(itemView);
            avi = (AVLoadingIndicatorView) itemView.findViewById(R.id.avi);
            noMoreTx = (TextView) itemView.findViewById(R.id.nomore_tx);
            errorTx = (TextView) itemView.findViewById(R.id.error_tx);
        }
    }
}
