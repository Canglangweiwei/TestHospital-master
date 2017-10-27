package com.geecity.hospital.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.trello.rxlifecycle.components.support.RxFragment;

import butterknife.ButterKnife;

@SuppressWarnings("ALL")
public abstract class AbsBaseFragment extends RxFragment {

    protected View mRootView;
    protected Gson mBaseFrgGson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(initContentView(), container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseFrgGson = new Gson();
        initUi();
        initDatas();
        initListener();
    }

    /**
     * 页面绑定
     */
    public abstract int initContentView();

    /**
     * 初始化UI
     */
    protected abstract void initUi();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * 初始化监听事件
     */
    protected abstract void initListener();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
