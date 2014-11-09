package com.sunsun.framework.component.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

import com.sunsun.framework.component.debug.KGLog;

/**
 * 描述:字符操作
 * 
 */
public class StringUtil {

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 字符串转长整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static long toLong(String str, long defValue) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return TextUtils.isEmpty(s) || "null".equals(s);
	}

	/**
	 * 截取文件名称
	 * 
	 * @param filePath
	 * @return
	 */
	public static String spiltFileName(String savePath) {
		if (TextUtils.isEmpty(savePath)) {
			return null;
		}
		int end = savePath.lastIndexOf("/");
		if (end == -1) {
			return null;
		}
		return savePath.substring(end + 1, savePath.length());

	}

	private static String appVersionKey;

	/**
	 * 应用版本app版本key
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionKey(Context context) {
		if (appVersionKey == null) {
			int code = AppCommonUtil.getCurVersion(context);
			String codeStirng = "";
			if (code < 10000) {
				codeStirng = "0" + code;
			} else {
				codeStirng = String.valueOf(code);
			}
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < codeStirng.length(); i++) {
				char c = codeStirng.charAt(i);
				buffer.append(c).append(".");
			}
			String s = buffer.substring(0, buffer.length() - 1);
			if (KGLog.isDebug()) {
				appVersionKey = "Android/" + s + "/debug";
			} else {
				appVersionKey = "Android/" + s + "/release";
			}
		}

		return appVersionKey;
	}

	/**
	 * 将字符串转成MD5值
	 * 
	 * @param string
	 * @return
	 */
	public static String stringToMD5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}

	// 判断输入是否是合法字符
	public static boolean isValidChar(String strInput) {
		String strPattern = "^[0-9a-zA-Z]*$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strInput);
		return m.matches();
	}

	// 判断是否为邮件地址
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	// 判断是否为电话号码
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * 可接受的电话格式有: ^\\(? : 可以使用 "(" 作为开头 (\\d{3}): 紧接着三个数字 \\)? : 可以使用")"接续
		 * [- ]? : 在上述格式后可以使用具选择性的 "-". (\\d{3}) : 再紧接着三个数字 [- ]? : 可以使用具选择性的
		 * "-" 接续. (\\d{5})$: 以五个数字结束. 可以比较下列数字格式: (123)456-7890, 123-456-7890,
		 * 1234567890, (123)-456-7890
		 */

		// String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";

		String expression = "^(13|15|18)\\d{9}$";
		/*
		 * 可接受的电话格式有: ^\\(? : 可以使用 "(" 作为开头 (\\d{3}): 紧接着三个数字 \\)? : 可以使用")"接续
		 * [- ]? : 在上述格式后可以使用具选择性的 "-". (\\d{4}) : 再紧接着四个数字 [- ]? : 可以使用具选择性的
		 * "-" 接续. (\\d{4})$: 以四个数字结束. 可以比较下列数字格式: (02)3456-7890, 02-3456-7890,
		 * 0234567890, (02)-3456-7890
		 */
		// String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber; /* 创建Pattern */
		Pattern pattern = Pattern.compile(expression);
		/* 将Pattern 以参数传入Matcher作Regular expression */
		Matcher matcher = pattern.matcher(inputStr);
		/* 创建Pattern2 */
		// Pattern pattern2 = Pattern.compile(expression2);
		/* 将Pattern2 以参数传入Matcher2作Regular expression */
		// Matcher matcher2 = pattern2.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

}
