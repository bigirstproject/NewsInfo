package com.sunsun.framework.component.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

public abstract class BasePopupWindow extends PopupWindow {

	public BasePopupWindow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(View contentView, int width, int height) {
		super(contentView, width, height);
		// TODO Auto-generated constructor stub
	}

	public BasePopupWindow(View contentView) {
		super(contentView);
		// TODO Auto-generated constructor stub
	}

}
