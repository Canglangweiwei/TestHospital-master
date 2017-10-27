package com.geecity.hospital;

import android.app.Activity;
import android.content.Context;

import com.geecity.hospital.version.AppUpdateInfo;
import com.geecity.hospital.version.UpdateVersionController;
import com.geecity.hospital.version.VersionInfoApiService;
import com.geecity.hospital.xgohttp.WebAPIListener;
import com.jaydenxiao.common.commonwidget.LoadingDialog;

/**
 * <p>
 * 功能说明：版本更新工具类
 * </p>
 * Created by weiwei On 2016-09-06 上午10:27:20
 */
@SuppressWarnings("ALL")
public class VerCheckTools {

    private Context mContext;// 上下文环境

    private AppUpdateInfo appUpdateInfo; // 版本检查

    public VerCheckTools(Context context) {
        this.mContext = context;
    }

    /**
     * 检测是否需要更新版本
     */
    public void checkVersionRequest(final boolean showToast) {
        if (showToast) {
            LoadingDialog.showDialogForLoading((Activity) mContext);
        }
        final VersionInfoApiService apiService = new VersionInfoApiService(mContext);
        apiService.setListener(new WebAPIListener() {
            @Override
            public void onLoadSuccess(int successCode) {
                LoadingDialog.cancelDialogForLoading();
                UpdateVersionController controller = UpdateVersionController.getInstance(mContext, showToast);
                appUpdateInfo = apiService.getAppUpdateInfo();
                controller.normalCheckUpdateInfo(appUpdateInfo);
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }
}
