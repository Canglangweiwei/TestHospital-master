package com.geecity.hospital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geecity.hospital.R;
import com.geecity.hospital.bean.KeyValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能描述：显示简单k-v对的的Adapter，重用房间联动的layout
 * </p>
 * Created by Liu Peng on 2015/7/26.
 */
public class KeyValueAdapter extends BaseAdapter {

    private List<KeyValueBean> mList = new ArrayList<>();

    public List<KeyValueBean> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_member_filter, parent, false);

        TextView text = (TextView) convertView.findViewById(R.id.irf_tv_text);
        text.setText(mList.get(position).getValue());

        return convertView;
    }
}
