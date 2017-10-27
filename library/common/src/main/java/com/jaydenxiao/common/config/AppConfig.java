package com.jaydenxiao.common.config;


/**
 * ************************************************************************
 * **                              _oo0oo_                               **
 * **                             o8888888o                              **
 * **                             88" . "88                              **
 * **                             (| -_- |)                              **
 * **                             0\  =  /0                              **
 * **                           ___/'---'\___                            **
 * **                        .' \\\|     |// '.                          **
 * **                       / \\\|||  :  |||// \\                        **
 * **                      / _ ||||| -:- |||||- \\                       **
 * **                      | |  \\\\  -  /// |   |                       **
 * **                      | \_|  ''\---/''  |_/ |                       **
 * **                      \  .-\__  '-'  __/-.  /                       **
 * **                    ___'. .'  /--.--\  '. .'___                     **
 * **                 ."" '<  '.___\_<|>_/___.' >'  "".                  **
 * **                | | : '-  \'.;'\ _ /';.'/ - ' : | |                 **
 * **                \  \ '_.   \_ __\ /__ _/   .-' /  /                 **
 * **            ====='-.____'.___ \_____/___.-'____.-'=====             **
 * **                              '=---='                               **
 * ************************************************************************
 * **                        佛祖保佑      镇类之宝                         **
 * ************************************************************************
 */
public class AppConfig {

    /**
     * 是否打印LogCat
     */
    public static final int LOG_DEBUG = 10 /*10*/;           // 是否打印LogCat >=10都允许打印，小于0的都不允许打印

    /**
     * 开发者模式
     */
    public static boolean DEBUG = true;

    /**
     * url
     */
    public static final String BASE_URL = "http://0.89892528.cn:8877/";

    /**
     * 限定城区的最大请求数
     */
    public static final int MAX_REQUEST_CONNECT_NUMBER = 6;
}
