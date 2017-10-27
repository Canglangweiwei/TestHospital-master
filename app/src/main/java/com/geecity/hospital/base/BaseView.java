package com.geecity.hospital.base;

@SuppressWarnings("ALL")
public interface BaseView {
    void onFailureCallback(Throwable throwable);

    void onFailureCallback(int errorCode, String errorMsg);
}
