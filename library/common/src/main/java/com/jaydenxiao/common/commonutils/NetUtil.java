package com.jaydenxiao.common.commonutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;

import com.jaydenxiao.common.base.BaseApplication;

/**
 * des:网络管理工具
 * Created by xsf
 * on 2016.04.10:34
 */
@SuppressWarnings("ALL")
public class NetUtil {

    @IntDef({NETWORK_TYPE_NONE, NETWORK_TYPE_MOBILE, NETWORK_TYPE_WIFI})
    public @interface NetWorkType {

    }

    public static final int NETWORK_TYPE_NONE = 1;
    public static final int NETWORK_TYPE_MOBILE = NETWORK_TYPE_NONE << 1;
    public static final int NETWORK_TYPE_WIFI = NETWORK_TYPE_MOBILE << 1;

    public static int getNetworkType() {
        ConnectivityManager connMgr = (ConnectivityManager) BaseApplication.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    return NETWORK_TYPE_MOBILE;
                case ConnectivityManager.TYPE_WIFI:
                    return NETWORK_TYPE_WIFI;
            }
        }
        return NETWORK_TYPE_NONE;
    }
}
