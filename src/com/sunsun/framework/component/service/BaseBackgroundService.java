
package com.sunsun.framework.component.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public abstract class BaseBackgroundService extends BaseService {

    private HandlerThread mHandlerThread;

    protected BackgroundHandler mBackgroundHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("service worker:" + getClass().getSimpleName());
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandlerThread.getLooper() != null) {
            mHandlerThread.getLooper().quit();
        }
    }

    /**
     * 处理后台操作
     */
    protected abstract void handleBackgroundMessage(Message msg);

    /**
     * 发送后台操作
     * 
     * @param msg
     */
    protected void sendBackgroundMessage(Message msg) {
        mBackgroundHandler.sendMessage(msg);
    }

    /**
     * 发送后台操作
     * 
     * @param what
     */
    protected void sendEmptyBackgroundMessage(int what) {
        mBackgroundHandler.sendEmptyMessage(what);
    }

    // 后台Handler
    private class BackgroundHandler extends Handler {

        BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleBackgroundMessage(msg);
        }
    }
}
