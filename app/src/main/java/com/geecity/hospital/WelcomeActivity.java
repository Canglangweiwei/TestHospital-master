package com.geecity.hospital;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.geecity.hospital.account.LoginActivity;
import com.geecity.hospital.account.api.LoginApiProtocol;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.base.AbsBaseApplication;
import com.geecity.hospital.bean.UserBean;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.commonutils.MD5Util;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonutils.XgoLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.TreeMap;

import butterknife.Bind;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 欢迎页面
 */
@SuppressWarnings("ALL")
public class WelcomeActivity extends AbsBaseActivity {

    private static final int MSG_CODE_OK = 0x001;
    private static final int MSG_CODE_ERR = 0x002;

    // 用户名
    private String username;
    // 明文密码
    private String password;

    @Bind(R.id.image_launcherBg)
    ImageView image_launcherBg;// 背景图片

    private LoginApiProtocol protocol;
    private Subscription mSubscription;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void initUi() {
        protocol = new LoginApiProtocol();
    }

    @Override
    protected void initDatas() {
        UserBean userBean = AbsBaseApplication.get(this).getUserInfo();
        if (userBean == null) {
            startNextActivity(null, LoginActivity.class, true);
            return;
        }
        username = userBean.getUserName();
        password = userBean.getPassword();

        if (TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)) {
            startNextActivity(null, LoginActivity.class, true);
            return;
        }
        login();
    }

    @Override
    protected void initListener() {

    }

    /**
     * 登录
     */
    private void login() {
        // 将密码进行md516位加密
        String md5Password = MD5Util.getMD5String(password, 16, false);
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("username", username);
        params.put("password", md5Password);
        mSubscription = protocol.login(params)
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        // 解析用户信息
                        if (TextUtils.isEmpty(dataJson)) {
                            ToastUitl.showShort("用户信息加载失败");
                            mHandler.sendEmptyMessage(MSG_CODE_ERR);
                            return;
                        }
                        // 预处理
                        try {
                            JSONObject json = new JSONObject(dataJson);
                            if (!json.getBoolean("success")) {
                                ToastUitl.showShort(json.getString("message"));
                                mHandler.sendEmptyMessage(MSG_CODE_ERR);
                                return;
                            }
                            String result = json.getString("data");
                            Type typeToken = new TypeToken<UserBean>() {}.getType();
                            UserBean loginInfo = mGson.fromJson(result, typeToken);
                            loginInfo.setPassword(password);// 密码
                            loginInfo.setUserName(username);// 用户名
                            // 保存用户信息
                            AbsBaseApplication.sApp.setUserInfo(loginInfo);
                            XgoLog.logd(loginInfo.toString());
                            // 跳转到主页面
                            mHandler.sendEmptyMessageDelayed(MSG_CODE_OK, 2000);
                        } catch (JSONException e) {
                            mHandler.sendEmptyMessage(MSG_CODE_ERR);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUitl.showShort("请检查网络连接是否正常");
                        mHandler.sendEmptyMessage(MSG_CODE_ERR);
                    }
                });
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CODE_OK:
                    // 跳转到主页面
                    startNextActivity(null, MainActivity.class, true);
                    break;
                case MSG_CODE_ERR:
                    // 跳转到登录页面
                    startNextActivity(null, LoginActivity.class, true);
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mSubscription != null
                && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
