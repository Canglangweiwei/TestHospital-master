package com.jaydenxiao.common.commonwidget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaydenxiao.common.R;


/**
 * description:弹窗浮动加载进度条
 * Created by weiwei
 * on 2017.07.17:22
 */
public class LoadingDialog {

    /**
     * 加载数据对话框
     */
    private static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param mActivity  上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showDialogForLoading(Activity mActivity, String msg, boolean cancelable) {
        View mRootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) mRootView.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);

        mLoadingDialog = new Dialog(mActivity, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(mRootView,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        if (!mActivity.isFinishing())
            mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 显示加载框
     *
     * @param mActivity 上下文环境
     * @return 加载框
     */
    public static Dialog showDialogForLoading(Activity mActivity) {
        View mRootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) mRootView.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("数据加载中，请稍后...");

        mLoadingDialog = new Dialog(mActivity, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(mRootView,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        if (!mActivity.isFinishing())
            mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelDialogForLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.cancel();
    }
}
