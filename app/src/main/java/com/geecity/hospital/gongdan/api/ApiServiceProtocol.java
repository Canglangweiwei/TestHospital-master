package com.geecity.hospital.gongdan.api;

import com.geecity.hospital.xgohttp.XgoHttpClient;
import com.geecity.hospital.xgohttp.protocol.BaseJsonProtocol;
import com.jaydenxiao.common.config.AppConfig;

import java.util.Map;

import rx.Observable;

/**
 * 工单接口
 * Created by ljb on 2016/3/23.
 */
public class ApiServiceProtocol extends BaseJsonProtocol {

    private static final String URL_FUNC_Q = AppConfig.BASE_URL + "common/homeFunc.php";

    private static final String URL_GONGDAN_LIST = AppConfig.BASE_URL + "gongdan/list.php";
    private static final String URL_GONGDAN_DETAIL = AppConfig.BASE_URL + "gongdan/detail.php";
    private static final String URL_GONGDAN_DETAIL_NEW_STATE = AppConfig.BASE_URL + "gongdan/lastNewStatus.php";
    private static final String URL_MEMBER_POSITION_LIST = AppConfig.BASE_URL + "task/position.php";

    /**
     * 首页数量统计
     */
    public Observable<String> homeFunc(Map<String, Object> params) {
        return createObservable(URL_FUNC_Q, XgoHttpClient.METHOD_POST, params);
    }

    /**
     * 工单列表请求
     */
    public Observable<String> list(Map<String, Object> params) {
        return createObservable(URL_GONGDAN_LIST, XgoHttpClient.METHOD_POST, params);
    }

    /**
     * 工单详情信息
     */
    public Observable<String> detail(Map<String, Object> params) {
        return createObservable(URL_GONGDAN_DETAIL, XgoHttpClient.METHOD_POST, params);
    }

    /**
     * 工单详情最新信息
     */
    public Observable<String> detailLastNewState(Map<String, Object> params) {
        return createObservable(URL_GONGDAN_DETAIL_NEW_STATE, XgoHttpClient.METHOD_POST, params);
    }

    /**
     * 职位列表
     */
    public Observable<String> positionList() {
        return createObservable(URL_MEMBER_POSITION_LIST, XgoHttpClient.METHOD_GET, null);
    }
}
