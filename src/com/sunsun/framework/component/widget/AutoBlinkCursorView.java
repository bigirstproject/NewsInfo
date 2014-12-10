/**
 * Copyright (C) 2004 KuGou-Inc.All Rights Reserved
 */
package com.sunsun.framework.component.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.ImageView;

/**
 * 可以自动闪烁的文字光标
 * 
 */
public class AutoBlinkCursorView extends ImageView {

	class BlinkInterpolator implements Interpolator {

		public BlinkInterpolator() {
		}

		public BlinkInterpolator(Context context, AttributeSet attrs) {
		}

		public float getInterpolation(float input) {
			if (input > .5f)
				return input;
			return 0;
		}
	}

	private boolean isBlinking = false;
	private Animation animation;

	public AutoBlinkCursorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoBlinkCursorView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void startBlink() {
		if (!isBlinking && getWidth() > 0 && getVisibility() == View.VISIBLE) {
			isBlinking = true;
			if (animation == null) {
				animation = new AlphaAnimation(0.f, 1.f);
				animation.setDuration(1000);
				animation.setInterpolator(new BlinkInterpolator());
				animation.setRepeatCount(Animation.INFINITE);
			}
			startAnimation(animation);
		}
	}

	public void stopBlink() {
		if (isBlinking) {
			isBlinking = false;
			setAnimation(null);
			animation = null;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		startBlink();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopBlink();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w > 0)
			startBlink();
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.INVISIBLE || visibility == View.GONE) {
			stopBlink();
		}
		startBlink();
	}
}
