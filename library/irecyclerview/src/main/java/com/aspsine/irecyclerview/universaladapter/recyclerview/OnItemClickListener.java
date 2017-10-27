package com.aspsine.irecyclerview.universaladapter.recyclerview;

import android.view.View;
import android.view.ViewGroup;

@SuppressWarnings("ALL")
public interface OnItemClickListener<T> {

    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}