package com.sunsun.framework.component.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.view.View;

public abstract class BaseDialog extends Dialog {

	public BaseDialog(Activity activity) {
		super(activity);
		setOwnerActivity(activity);
	}

	/**
	 * 设置位置
	 * 
	 * @param rect
	 * @param contentView
	 */
	public void onMeasureAndLayout(Rect rect, View contentView) {
	}

}
