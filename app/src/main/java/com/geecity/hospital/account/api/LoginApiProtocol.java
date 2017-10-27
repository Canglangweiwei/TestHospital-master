package com.geecity.hospital.account.api;

import com.geecity.hospital.xgohttp.XgoHttpClient;
import com.geecity.hospital.xgohttp.protocol.BaseJsonProtocol;
import com.jaydenxiao.common.config.AppConfig;

import java.util.Map;

import rx.Observable;

/**
 * 测试接口
 * Created by ljb on 2016/3/23.
 */
public class LoginApiProtocol extends BaseJsonProtocol {

    private static final String URL = AppConfig.BASE_URL + "common/login.php";

    /**
     * Delete请求
     */
    public Observable<String> login(Map<String, Object> params) {
        return createObservable(URL, XgoHttpClient.METHOD_POST, params);
    }
}
