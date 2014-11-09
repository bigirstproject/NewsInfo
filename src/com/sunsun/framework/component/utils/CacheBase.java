package com.sunsun.framework.component.utils;

import com.sunsun.framework.component.debug.KGLog;

public abstract class CacheBase {

	protected String TAG = "CacheManager";

	protected void errorFilePath(String filePath) {
		KGLog.e(TAG, "param invalid, filePath: " + filePath);
	}

	protected void commonLog(String msg) {
		KGLog.d(TAG, msg);
	}

}
