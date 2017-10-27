package com.geecity.hospital.account;

import android.text.TextUtils;
import android.widget.EditText;

import com.geecity.hospital.MainActivity;
import com.geecity.hospital.R;
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
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>
 * 用户登录
 * </p>
 * Created by Administrator on 2017/7/6 0006.
 */
//@SuppressWarnings("ALL")
public class LoginActivity extends AbsBaseActivity {

    @Bind(R.id.al_et_username)
    EditText mEditUsername;// 账号
    @Bind(R.id.al_et_password)
    EditText mEditPassword;// 密码

    // 用户名
    private String username;
    // 明文密码
    private String password;

    private LoginApiProtocol protocol;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_login;
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
        UserBean usInfo = AbsBaseApplication.get(this).getUserInfo();
        if (usInfo != null) {
            mEditUsername.setText(usInfo.getUserName());
            mEditPassword.setText(usInfo.getPassword());
        }
    }

    @Override
    protected void initListener() {

    }

    /**
     * 开始登录
     */
    @OnClick({R.id.al_btn_login})
    void btnLogin() {
        username = mEditUsername.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)) {
            startNextActivity(null, LoginActivity.class, true);
            return;
        }
        startProgressDialog();
        login();
    }

    private Subscription mSubscription;

    // 登录方法
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
                        stopProgressDialog();
                        // 解析用户信息
                        if (TextUtils.isEmpty(dataJson)) {
                            ToastUitl.showShort("用户信息加载失败");
                            return;
                        }
                        // 预处理
                        try {
                            JSONObject json = new JSONObject(dataJson);
                            if (!json.getBoolean("success")) {
                                ToastUitl.showShort(json.getString("message"));
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
                            startNextActivity(null, MainActivity.class, true);
                        } catch (JSONException e) {
                            ToastUitl.showShort("用户信息加载失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        stopProgressDialog();
                        ToastUitl.showShort("请检查网络连接是否正常");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (mSubscription != null
                && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
