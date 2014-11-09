
package com.sunsun.framework.component.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.util.List;

/**
 * 描述: app 资源，尺寸大小基本属性操作类
 * 
 */
public class AppCommonUtil {

    public static String Version_Url = "";

    /**
     * 获取当前版本号
     * 
     * @param context
     * @return versionCode
     */
    public static int getCurVersion(Context context) {
        try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = pinfo.versionCode;

            return versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取当前版本号 备注：返回格式[for Android V 1.0.0]
     * 
     * @param context
     * @return versionCode
     */
    public static String getCurVersionName(Context context) {
        try {
            PackageInfo pinfo;
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            String versionCode = pinfo.versionName;
            return "for Android V " + versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return "for Android V 1.0.0";
    }

    /**
     * 新版本下载地址
     * 
     * @return
     */
    public static String getAppUrl() {
        return Version_Url;
    }

    /**
     * 获取屏幕尺寸 备注:返回的是数组 int[0] 代表 宽度,int[1]代表高度
     * 
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return new int[] {
                display.getWidth(), display.getHeight()
        };
    }

    /**
     * 屏幕类型
     * 
     * @param context
     * @return
     */
    public static ScreenType getScreenType(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (dm.densityDpi >= DisplayMetrics.DENSITY_HIGH) {
            return ScreenType.HSCREEN;
        } else if (dm.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            return ScreenType.MSCREEN;
        } else if (dm.densityDpi == DisplayMetrics.DENSITY_LOW) {
            return ScreenType.LSCREEN;
        } else {
            return ScreenType.MSCREEN;
        }
    }

    /**
     * 应用是否被隐藏<在后台运行>
     * 
     * @param context
     * @return
     */
    public static boolean IsRunOnBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (context.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName())) {
            return false;
        }
        return true;
    }

    /**
     * 创建快捷方式
     * 
     * @param context
     * @param cls
     * @param icon 快捷方式图标
     * @param app_name 快捷方式名称
     */
    public static void createShortcut(Context context, Class cls, int icon, int app_name) {
        final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
        // 是否可以有多个快捷方式的副本
        final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

        Intent createIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        // 快捷方式的名称
        createIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(app_name));
        // 不允许重复创建
        createIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);

        // 指定快捷方式的启动对象
        Intent target = new Intent(Intent.ACTION_MAIN);
        target.addCategory(Intent.CATEGORY_LAUNCHER);
        target.setComponent(new ComponentName(context.getPackageName(), cls.getName()));
        target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        createIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, target);

        // 快捷方式的图标
        createIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, icon));

        context.sendBroadcast(createIntent);
    }

    /**
     * . * 删除当前应用的桌面快捷方式 . * . * @param cx .
     */
    public static void delShortcut(Context cx) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA))
                    .toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = cx.getPackageManager().getLaunchIntentForPackage(
                cx.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        cx.sendBroadcast(shortcut);
    }

    // /**
    // * 判断桌面是否已添加快捷方式
    // *
    // * @param cx
    // * @param titleName 快捷方式名称
    // * @return
    // */
    // public static boolean hasShortcut(Context cx, String title) {
    // boolean result = false;
    // final String uriStr;
    // if (android.os.Build.VERSION.SDK_INT < 8) {
    // uriStr = "content://com.android.launcher.settings/favorites?notify=true";
    // } else {
    // uriStr =
    // "content://com.android.launcher2.settings/favorites?notify=true";
    // }
    // final Uri CONTENT_URI = Uri.parse(uriStr);
    // final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,
    // "title=?", new String[] {
    // title
    // }, null);
    // if (c != null && c.getCount() > 0) {
    // result = true;
    // }
    // return result;
    // }

    /**
     * 获取手机内部存储
     * 
     * @param context
     * @return
     */
    public static long getInternalAvailableBlocks(Context context) {
        // String mTotalSize = "内部总容量：";
        // String mAvailSize = "内部剩余容量：";
        StatFs statInternal = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statInternal.getBlockSize();
        long totalBlocks = statInternal.getBlockCount() * blockSize;
        long availableBlocks = statInternal.getAvailableBlocks() * blockSize;
        // mTotalSize += Formatter.formatFileSize(context, totalBlocks *
        // blockSize);
        // mAvailSize += Formatter.formatFileSize(context, availableBlocks *
        // blockSize);
        return availableBlocks;
    }

    /**
     * 获取状态栏高度
     * 
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.top;
    }

    /**
     * 根据表情类型获取资源ID
     * 
     * @param context
     * @param type
     * @return
     */
    public static int getFacialIdentifier(Context context, String type) {
        if (TextUtils.isEmpty(type)) {
            return -1;
        }
        Resources res = context.getResources();
        int resId = res.getIdentifier(String.format("like_%s", type), "drawable",
                context.getPackageName());
        return resId;
    }

    /**
     * dip转px
     * 
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context
                .getResources().getDisplayMetrics());
    }

    /**
     * dip转px
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
