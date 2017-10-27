package com.geecity.hospital.version;

import android.content.Context;

import com.geecity.hospital.xgohttp.IHttpPostApi;

import org.json.JSONException;

import java.util.HashMap;

/**
 * <p>
 * 获取版本信息
 * </p>
 * Created by Administrator on 2017/9/4 0004.
 */
public class VersionInfoApiService extends IHttpPostApi {

    private AppUpdateInfo appUpdateInfo;

    public AppUpdateInfo getAppUpdateInfo() {
        return appUpdateInfo;
    }

    /**
     * 构造器
     *
     * @param context 上下文环境
     */
    public VersionInfoApiService(Context context) {
        super(context);
    }

    @Override
    protected void getInputParam(HashMap<String, Object> params) {

    }

    @Override
    protected boolean analysisOutput(String result) throws JSONException {
        appUpdateInfo = mGson.fromJson(result, AppUpdateInfo.class);
        return true;
    }

    @Override
    protected String getMethodName() {
        return "version/findAppVersion.php";
    }
}
