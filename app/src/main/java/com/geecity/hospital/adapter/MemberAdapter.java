package com.geecity.hospital.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geecity.hospital.R;
import com.geecity.hospital.bean.MemberBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 成员adapter
 * Created by LiuPeng on 2015/11/9.
 */
@SuppressWarnings("ALL")
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context mContext;

    public interface OnSeletedListener {
        void onSelected(MemberBean memberBean, int position);
    }

    private OnSeletedListener onSeletedListener;

    private List<MemberBean> list;

    public MemberAdapter(List<MemberBean> list, OnSeletedListener onSeletedListener) {
        this.onSeletedListener = onSeletedListener;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MemberBean item = list.get(position);
        holder.name.setText(item.getName());
        holder.depart.setText(item.getDepart());
        holder.pos.setText(item.getPosition());
        holder.contact.setText(item.getContactWay());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeletedListener.onSelected(item, position);
            }
        });
        // 判断当前item是否选中，并设置相对应的颜色背景
        switch (getSelect(position)) {
            case 0:
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                break;
            case 1:
                holder.itemView.setBackgroundResource(R.drawable.choose_item_right);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.im_tv_name)
        TextView name;
        @Bind(R.id.im_tv_depart)
        TextView depart;
        @Bind(R.id.im_tv_position)
        TextView pos;
        @Bind(R.id.im_tv_contactWay)
        TextView contact;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public int getSelect(int position) {
        return list.get(position).isSelect();
    }

    public void setSelect(int position, int select) {
        this.list.get(position).setSelect(select);
        this.notifyDataSetChanged();
    }
}
