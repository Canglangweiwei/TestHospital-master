package com.geecity.hospital.account.api;

import com.geecity.hospital.bean.UserBean;
import com.geecity.hospital.xgohttp.XgoHttpClient;
import com.geecity.hospital.xgohttp.protocol.BaseObjProtocol;
import com.jaydenxiao.common.config.AppConfig;

import java.util.Map;

import rx.Observable;

/**
 * 测试接口
 * Created by ljb on 2016/3/23.
 */
public class LoginObjProtocol extends BaseObjProtocol {

    private static final String URL = AppConfig.BASE_URL + "common/login.php";

    /**
     * Delete请求
     */
    public Observable<UserBean> login(Map<String, Object> params) {
        return createObservable(URL, XgoHttpClient.METHOD_POST, params, UserBean.class);
    }
}
