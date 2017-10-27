package com.geecity.hospital.base;

import android.support.annotation.NonNull;

@SuppressWarnings("ALL")
public interface BasePresenter<T extends BaseView> {

    /**
     * 绑定view，这个方法将会在activity中调用
     */
    void attachView(@NonNull T view);

    /**
     * 解绑
     */
    void detachView();
}

