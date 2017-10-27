package com.geecity.hospital.message;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.geecity.hospital.MainActivity;
import com.jaydenxiao.common.commonutils.JsonUtils;
import com.jaydenxiao.common.commonutils.XgoLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * <p>
 * 自定义接收器(处理极光推送)
 * 如果不定义这个 Receiver，则
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 * </p>
 */
@SuppressWarnings("ALL")
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MessageReceiver] onReceive - " + intent.getAction() + ", extras: "
                + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MessageReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 接收到推送下来的自定义消息: " +
                    bundle.getString(JPushInterface.EXTRA_MESSAGE));

            // 处理自定义消息(跳转至自定义消息处理页)
            processCustomMessage(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 接收到推送下来的通知");
            int notification_Id = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MessageReceiver] 接收到推送下来的通知的ID: " + notification_Id);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 用户点击打开了通知");
            /******************* 打开通知start *************************/
            //打开自定义的Activity
            openNotificationMessage(context, bundle);
            /******************* 打开通知end *************************/

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MessageReceiver] 用户收到到RICH PUSH CALLBACK: " +
                    bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity、 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MessageReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MessageReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:").append(key).append(", value:")
                        .append(bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:").append(key).append(", value: [")
                                .append(myKey).append(" - ")
                                .append(json.optString(myKey))
                                .append("]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:").append(key).append(", value:")
                        .append(bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void processCustomMessage(Context context, Bundle bundle) {

    }

    /**
     * 打开通知栏通知
     *
     * @param context
     * @param bundle
     */
    private void openNotificationMessage(Context context, Bundle bundle) {

        // 预处理
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        MessageBean messageBean = (MessageBean) JsonUtils.fromJson(extra, MessageBean.class);
        if (messageBean == null) {
            return;
        }

        int toType = messageBean.getType();// 获取通知类型
        Class activity = null;
        Bundle targetBundle = null;
        switch (toType) {
            case 5:
                activity = MainActivity.class;
                break;
            default:
                break;
        }
        // 容错处理
        if (activity != null) {
            Intent i = new Intent(context, activity);
            i.putExtra("id", messageBean.getId());// 传入id(任务id和接待id)
            i.putExtra("alert", alert);// 传入通知提醒
            if (targetBundle != null) {
                i.putExtras(targetBundle);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    XgoLog.logd("后台：" + appProcess.processName);
                    return true;
                } else {
                    XgoLog.logd("前台：" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
