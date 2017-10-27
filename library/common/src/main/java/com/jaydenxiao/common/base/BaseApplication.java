package com.jaydenxiao.common.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.jaydenxiao.common.commonutils.XgoLog;
import com.jaydenxiao.common.config.AppConfig;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * APPLICATION
 */
@SuppressWarnings("ALL")
public class BaseApplication extends LitePalApplication {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
        LitePal.initialize(this);
        // 是否允许打印日志
        XgoLog.logInit(AppConfig.LOG_DEBUG);
    }

    public static Context get() {
        return baseApplication;
    }

    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    /**
     * 分包
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
