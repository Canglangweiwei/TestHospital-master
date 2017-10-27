package com.jaydenxiao.common.commonutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.jaydenxiao.common.base.BaseApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 判断网址是否有效
     */
    public static String isLinkAvailable(String link) {
        if (TextUtils.isEmpty(link))
            return "";
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return link;
        }
        return "";
    }
}
