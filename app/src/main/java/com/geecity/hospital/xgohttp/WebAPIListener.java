package com.geecity.hospital.xgohttp;

/**
 * Web接口监听接口，如WebService或Http请求的类来实现此接口，提供给给客户端统一的操作规范。
 * Created by LiuPeng on 15/8/6.
 */
public interface WebAPIListener {

    /**
     * 加载成功
     *
     * @param successCode 成功代码
     */
    void onLoadSuccess(int successCode);

    /**
     * 加载失败
     *
     * @param errorCode     失败代码
     * @param errorMessage 失败信息
     */
    void onLoadFail(int errorCode, String errorMessage);
}
