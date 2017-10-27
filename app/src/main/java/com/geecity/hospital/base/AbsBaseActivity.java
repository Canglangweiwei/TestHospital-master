package com.geecity.hospital.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.geecity.hospital.R;
import com.geecity.hospital.bean.UserBean;
import com.google.gson.Gson;
import com.jaydenxiao.common.commonwidget.LoadingDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * <p>
 * AbsBaseActivity
 * </p>
 * Created by Administrator on 2017/9/25 0025.
 */
@SuppressWarnings("ALL")
public abstract class AbsBaseActivity extends RxAppCompatActivity {

    protected Gson mGson;
    protected UserBean mBaseUserBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AbsBaseApplication.get(getApplicationContext()).pushActivityToStack(this);
        setContentView(initLayoutResID());
        ButterKnife.bind(this);
        mGson = new Gson();
        mBaseUserBean = AbsBaseApplication.get(getApplicationContext()).getUserInfo();
        parseIntent();
        initUi();
        initDatas();
        initListener();
    }

    /**
     * 页面绑定
     */
    protected abstract int initLayoutResID();

    /**
     * 解析页面间传递的数据
     */
    protected abstract void parseIntent();

    /**
     * 初始化UI
     */
    protected abstract void initUi();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * 初始化监听事件
     */
    protected abstract void initListener();

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(this);
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg 提示信息
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(this, msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }

    /**
     * 启动新一个activity
     */
    public void startNextActivity(Bundle bundle, Class<?> tClass) {
        Intent intent = new Intent(this, tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    /**
     * 最后一个参数为true表示finish当前activity
     */
    public void startNextActivity(Bundle bundle, Class<?> tClass, boolean allowedFinish) {
        Intent intent = new Intent(this, tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
        if (allowedFinish) {
            super.finish();
        }
    }

    /**
     * 请求activity for reslut
     */
    public void startNextActivityForResult(Bundle bundle, Class<?> tClass, int resquestCode) {
        Intent intent = new Intent(this, tClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, resquestCode);
        overridePendingTransition(R.anim.enter_righttoleft, R.anim.exit_righttoleft);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
    }

    @Override
    public void finish() {
        this.overridePendingTransition(R.anim.enter_lefttoright, R.anim.exit_lefttoright);
        super.finish();
    }

    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定是否需要隐藏软键盘
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
