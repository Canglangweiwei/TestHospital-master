package com.geecity.hospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.baseadapter.BaseReclyerViewAdapter;
import com.geecity.hospital.bean.JiedianBean;
import com.geecity.hospital.viewholder.JiedianViewHolder;

/**
 * des: 圈子列表的adapter
 * Created by xsf
 * on 2016.08.14:19
 */
@SuppressWarnings("ALL")
public class JiedianAdapter extends BaseReclyerViewAdapter<JiedianBean> {

    private Context mContext;

    public JiedianAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return JiedianViewHolder.create(mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof JiedianViewHolder) {
            ((JiedianViewHolder) holder).setData(get(position), getItemViewType(position));
        }
    }
}
