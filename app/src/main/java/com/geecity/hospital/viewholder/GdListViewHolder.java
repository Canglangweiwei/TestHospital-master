package com.geecity.hospital.viewholder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.geecity.hospital.R;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.bean.GongdanDetailBean;
import com.geecity.hospital.gongdan.GongdanDetailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * des：节点viewholder
 * Created by weiwei on 2017/06/14
 */
@SuppressWarnings("ALL")
public class GdListViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private View itemView;

    /**
     * 订单号
     */
    @Bind(R.id.gd_list_ddh)
    TextView ddh;

    /**
     * 时间
     */
    @Bind(R.id.gd_list_datetime)
    TextView timeTv;

    /**
     * 描述
     */
    @Bind(R.id.gd_list_miaoshu)
    TextView msTv;

    /**
     * 类别
     */
    @Bind(R.id.gd_list_type)
    TextView typeTv;

    /**
     * 科室
     */
    @Bind(R.id.gd_list_ks)
    TextView ksTv;

    /**
     * 运送物品
     */
    @Bind(R.id.gd_list_yswp)
    TextView yswpTv;

    public static GdListViewHolder create(Context context) {
        GdListViewHolder imageViewHolder =
                new GdListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gongdan_list, null), context);
        return imageViewHolder;
    }

    public GdListViewHolder(View itemView, final Context context) {
        super(itemView);
        this.itemView = itemView;
        this.mContext = context;
        ButterKnife.bind(this, itemView);
    }

    /**
     * 设置数据
     */
    public void setData(final GongdanDetailBean bean) {
        if (bean == null) {
            return;
        }

        ddh.setText(bean.getCode());
        timeTv.setText(bean.getTjtime());
        msTv.setText(bean.getSm());

        typeTv.setText(bean.getZt());
        ksTv.setText(bean.getKs());
        yswpTv.setText(bean.getWp());

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(bean.getId()));
                ((AbsBaseActivity) mContext).startNextActivity(bundle, GongdanDetailActivity.class);
            }
        });
    }
}
