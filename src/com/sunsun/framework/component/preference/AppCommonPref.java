package com.sunsun.framework.component.preference;

import android.content.Context;

import com.sunsun.framework.component.base.BaseApplication;

/**
 * 描述:
 * 
 */
public class AppCommonPref extends PreferenceOpenHelper {

	private volatile static AppCommonPref mInstance = null;

	private AppCommonPref(Context context, String prefname) {
		super(context, prefname);
	}

	public static AppCommonPref getInstance() {
		if (mInstance == null) {
			synchronized (AppCommonPref.class) {
				if (mInstance == null) {
					Context context = BaseApplication.getInstance();
					String name = context.getPackageName() + "appinfo";
					mInstance = new AppCommonPref(context, name);
				}
			}
		}

		return mInstance;
	}

	public int getBacupVersion() {
		return getInt(Keys.BACKUP_VERSION, -1);
	}

	public void putCurVersion(int versionCode) {
		putInt(Keys.BACKUP_VERSION, versionCode);

	}

	public String getNewVersionName() {
		return getString(Keys.NEW_VERSION_NAME, "");
	}

	public void putNewVersionName(String versionName) {
		putString(Keys.NEW_VERSION_NAME, versionName);
	}

	public String getNewVersionTitle() {
		return getString(Keys.NEW_VERSION_TITLE, "");
	}

	public void putNewVersionTitle(String versionTitle) {
		putString(Keys.NEW_VERSION_TITLE, versionTitle);
	}

	public String getNewVersionContent() {
		return getString(Keys.NEW_VERSION_CONTENT, "");
	}

	public void putNewVersionContent(String versionContent) {
		putString(Keys.NEW_VERSION_CONTENT, versionContent);
	}

	public String getNewVersionUrl() {
		return getString(Keys.NEW_VERSION_URL, "");
	}

	public void putNewVersionUrl(String url) {
		putString(Keys.NEW_VERSION_URL, url);
	}

	// public boolean getHasNewVersion() {
	// return getBoolean(Keys.HAVE_NEW_VERSION, false);
	// }
	//
	// public void putHasNewVersion(boolean value) {
	// putBoolean(Keys.HAVE_NEW_VERSION, value);
	// }

	public int getVersionMustUpdate() {
		return getInt(Keys.VERSION_MUST_UPDATE, 0);
	}

	public void putVersionMustUpdate(int code) {
		putInt(Keys.VERSION_MUST_UPDATE, code);
	}

	public int getNewVersionCode() {
		return getInt(Keys.NEW_VERSION_CODE, -1);
	}

	public void putNewVersionCode(int code) {
		putInt(Keys.NEW_VERSION_CODE, code);
	}

	public boolean getCheckUpdateLaunch() {
		return getBoolean(Keys.CHECK_UPDATE_LAUNCH, true);
	}

	public void putCheckUpdateLaunch(boolean value) {
		putBoolean(Keys.CHECK_UPDATE_LAUNCH, value);
	}

	public void putLastPage(int position) {
		putInt(Keys.LAST_PAGE, position);
	}

	public int getLastPage() {
		return getInt(Keys.LAST_PAGE, 0);
	}

	/**
	 * 是否创建快捷方式
	 * 
	 * @return
	 */
	public boolean getCreateShortcut() {
		return getBoolean(Keys.CREATE_SHORTCUT, false);
	}

	/**
	 * 是否创建快捷方式
	 */
	public void putCreateShortcut(boolean value) {
		putBoolean(Keys.CREATE_SHORTCUT, value);
	}

	public static interface Keys {
		/**
		 * 备份版本号
		 */
		public static String BACKUP_VERSION = "backup_version";

		/**
		 * 是否强制更新
		 */
		public static String VERSION_MUST_UPDATE = "version_must_update";

		/**
		 * 新版本号
		 */
		public static String NEW_VERSION_CODE = "new_version_code";

		/**
		 * 新版本名
		 */
		public static String NEW_VERSION_NAME = "new_version_name";

		/**
		 * 新版本标题
		 */
		public static String NEW_VERSION_TITLE = "new_version_title";

		/**
		 * 新版本内容
		 */
		public static String NEW_VERSION_CONTENT = "new_version_content";

		/**
		 * 新版本下载地址
		 */
		public static String NEW_VERSION_URL = "new_version_url";

		/**
		 * 是否启动自动检测新版本
		 */
		public static String CHECK_UPDATE_LAUNCH = "check_update_launch";

		/**
		 * 最后一次退出时的页面
		 */
		public static String LAST_PAGE = "last_page";

		/**
		 * 是否已经创建快捷方式
		 */
		public static String CREATE_SHORTCUT = "create_shortcut";

	}
}
