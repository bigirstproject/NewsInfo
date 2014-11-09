package com.sunsun.framework.component.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import com.sunsun.framework.component.debug.KGLog;

public class EditTextUtil {

	/** 可输入最大字符数 */
	public static final int MAX_LENGTH = 16;

	public static class AdnNameLengthFilter implements InputFilter {
		// 输入的字符串长度
		private int nMax;

		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			Log.w("Android_12", "source(" + start + "," + end + ")=" + source
					+ ",dest(" + dstart + "," + dend + ")=" + dest);
			nMax = 0;
			char[] dests = dest.toString().toCharArray();
			for (int j = 0; j < dest.length(); j++) {
				if (isChinese(dests[j])) {
					nMax += 2;
				} else {
					nMax += 1;
				}
			}
			char[] cs = source.toString().toCharArray();
			for (int i = 0; i < cs.length; i++) {
				if (isChinese(cs[i])) {
					nMax += 2;
				} else {
					nMax += 1;
				}
				KGLog.d("xhc", "max length is " + nMax);
				if (nMax < MAX_LENGTH) {
					continue;
				} else if (nMax == MAX_LENGTH) {
					return source.subSequence(0, i + 1);
				} else {
					if (i > 1) {
						return source.subSequence(0, i);
					} else {
						return "";
					}

				}
			}
			return source;
		}
	}

	/**
	 * 判断是否是中文字符
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
}
