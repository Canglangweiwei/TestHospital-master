package com.geecity.hospital.base;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.geecity.hospital.bean.UserBean;
import com.geecity.hospital.message.MessageReceiver;
import com.jaydenxiao.common.base.BaseApplication;
import com.jaydenxiao.common.commonutils.NetUtil;
import com.jaydenxiao.common.commonutils.XgoLog;

import org.litepal.crud.DataSupport;

import java.util.Set;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@SuppressWarnings("ALL")
public class AbsBaseApplication extends BaseApplication {

    public static AbsBaseApplication sApp;

    private Activity curActivity;

    private Stack<Activity> activityStack;// activity栈

    public static AbsBaseApplication get(Context context) {
        return (AbsBaseApplication) context.getApplicationContext();
    }

    private UserBean usInfo;                    // 用户信息

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        // 极光推送初始化
        // 初始化 JPush
        JPushInterface.init(this);
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
    }

    /**
     * 获取用户信息
     **/
    public void setUserInfo(UserBean userBean) {
        this.usInfo = userBean;
        // 清空数据表
        DataSupport.deleteAll(UserBean.class);
        if (userBean == null) {
            // 登录信息为空，表示退出登录，停止推送
            JPushInterface.stopPush(curActivity);
            return;
        }
        // 初始化极光推送工具
        initPush();
        // 保存用户信息
        userBean.save();
    }

    /**
     * 保存用户信息
     **/
    public UserBean getUserInfo() {
        if (usInfo == null) {
            usInfo = DataSupport.findFirst(UserBean.class);
        }
        return usInfo;
    }

    /**
     * 是否已经登录
     */
    public boolean isLogin() {
        return sApp.getUserInfo() != null;
    }

    /**
     * 把一个activity压入栈列中
     */
    public void pushActivityToStack(Activity actvity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        curActivity = actvity;
        activityStack.add(actvity);
    }

    /**
     * 获取栈顶的activity，先进后出原则
     */
    private Activity getLastActivityFromStack() {
        return activityStack.lastElement();
    }

    /**
     * 从栈列中移除一个activity
     */
    private void popActivityFromStack(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activity.finish();
                activityStack.remove(activity);
                activity = null;
            }
        }
    }

    /**
     * 退出所有activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivityFromStack();
                if (activity == null) {
                    break;
                }
                popActivityFromStack(activity);
            }
        }
    }

    // 登录或者注册成功之后，绑定推送
    public void initPush() {
        registerMessageReceiver();
        if (JPushInterface.isPushStopped(curActivity)) {
            JPushInterface.resumePush(curActivity);
        } else {
            JPushInterface.init(curActivity);
        }
        // 绑定推送
        bindPushID();
    }

    // 绑定推送ID
    private void bindPushID() {
        if (sApp.getUserInfo() == null
                || TextUtils.isEmpty(getUserInfo().getUserName())) {
            return;
        }
        XgoLog.logd("绑定极光推送");
        JPushInterface.setAlias(sApp, getUserInfo().getUserName(),
                new TagAliasCallback() {

                    @Override
                    public void gotResult(int code, String alias, Set<String> tags) {
                        String logs;
                        switch (code) {
                            case 0:
                                logs = "设置别名成功：" + alias;
                                break;
                            case 6002:
                                logs =
                                        "Failed to set alias and tags due to timeout. Try again after 60s.";
                                if (NetUtil.getNetworkType() == NetUtil.NETWORK_TYPE_NONE) {
                                    handler.sendMessageDelayed(
                                            handler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                                } else {
                                    logs = "No network";
                                }
                                break;
                            default:
                                logs = "Failed with errorCode = " + code;
                        }
                        XgoLog.logd(logs);
                    }
                });
        JPushInterface.resumePush(sApp);
    }

    // for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MessageReceiver.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    private final static int MSG_SET_ALIAS = 0x1000;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    bindPushID();
                    break;
            }
        }
    };

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(mMessageReceiver);
    }
}
