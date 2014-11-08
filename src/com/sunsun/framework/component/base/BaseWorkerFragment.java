package com.sunsun.framework.component.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * 描述:可处理耗时操作的fragment
 * 
 * @author xuhaichao
 * @since 2013-8-12 上午11:51:02
 */
public abstract class BaseWorkerFragment extends BaseFragment {

	protected HandlerThread mHandlerThread;

	protected BackgroundHandler mBackgroundHandler;

	@Override
	@SuppressLint("HandlerLeak")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHandlerThread = new HandlerThread("activity worker:"
				+ getClass().getSimpleName());
		mHandlerThread.start();
		mBackgroundHandler = new BackgroundHandler(mHandlerThread.getLooper());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mBackgroundHandler != null
				&& mBackgroundHandler.getLooper() != null) {
			mBackgroundHandler.getLooper().quit();
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
		if (mBackgroundHandler != null) {

			mBackgroundHandler.sendMessage(msg);
		}
	}

	/**
	 * 发送后台操作
	 * 
	 * @param what
	 */
	protected void sendEmptyBackgroundMessage(int what) {
		if (mBackgroundHandler != null) {

			mBackgroundHandler.sendEmptyMessage(what);
		}
	}

	// 后台Handler
	@SuppressLint("HandlerLeak")
	public class BackgroundHandler extends Handler {

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
