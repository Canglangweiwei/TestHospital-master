package com.geecity.hospital.xgohttp;

import com.jaydenxiao.common.commonutils.XgoLog;
import com.jaydenxiao.common.config.AppConfig;

import java.util.ArrayList;

/**
 * 类名：连接管理 作用： 1.将请求添加到等待队列 2.从等待队列中取出请求并添加到活动队列中，执行请求 3.将完成的请求移除活动队列
 */
@SuppressWarnings("ALL")
public class ConnectManager {

    private final ArrayList<Runnable> mActiveQueue = new ArrayList<>();
    private final ArrayList<Runnable> mWaitingQueue = new ArrayList<>();
    public static ConnectManager sConnectManager;

    public static ConnectManager getInstance() {
        if (sConnectManager == null) {
            sConnectManager = new ConnectManager();
        }
        return sConnectManager;
    }

    /**
     * 将请求放入到等待队列中，并开始下一条任务
     */
    public void push(Runnable runnable) {
        this.mWaitingQueue.add(runnable);
        if (this.mActiveQueue.size() < AppConfig.MAX_REQUEST_CONNECT_NUMBER) {
            this.startNext();
        }
    }

    /**
     * 从等待队列中提取一条请求放入到活动队列中并从等待队列中移除当前请求
     */
    private void startNext() {
        if (!this.mWaitingQueue.isEmpty()) {
            Runnable next = this.mWaitingQueue.get(0);
            this.mWaitingQueue.remove(0);
            this.mActiveQueue.add(next);
            int THREAD_NUMBER = 0;
            Thread thread = new Thread(next, "Thread__" + THREAD_NUMBER);
            if (AppConfig.DEBUG) {
                XgoLog.logd(this.mActiveQueue.size() + "个");
            }
            thread.start();
        }
    }

    /**
     * 一条请求结束后，将其从活动队列中移除，并开始下一条请求的处理
     */
    public void didComplete(Runnable runnable) {
        this.mActiveQueue.remove(runnable);
        this.startNext();
    }
}
