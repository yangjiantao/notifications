package jd.cdyjy.market.notification;

import android.app.Notification;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 非原生系统机型角标适配
 */
public class BadgeUtils {

    private static final String TAG = "BadgeUtils";

    /**
     * 使用场景：在未读消息数变更时调用该方法来更新桌面应用角标未读数
     * 设置Badge 目前支持Launcher:
     * EXUI MIUI Sony Samsung LG HTC Nova
     *
     * @param context  context
     * @param msgCount count
     */
    public static void setBadgeCount(Context context, int msgCount) {
        if (msgCount <= 0) {
            msgCount = 0;
        } else {
            msgCount = Math.max(0, Math.min(msgCount, 99));
        }
        if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
            setBadgeOfEXUI(context, msgCount);
        } else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
            setBadgeOfNova(context, msgCount);
        } else if (Build.MANUFACTURER.toLowerCase().contains("zuk")) {
            setBadgeOfZuk(context, msgCount);
        } else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
            setBadgeOfSony(context, msgCount);
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung") ||
                Build.MANUFACTURER.toLowerCase().contains("lg")) {
            setBadgeOfSumsung(context, msgCount);
        } else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            setBadgeOfHTC(context, msgCount);
        }
    }

    /**
     * 设置MIUI的Badge 小米需要和通知栏进行绑定
     * @param count 该通知实例「notification」对象，对应的未读消息数，系统会进行累加。
     */
    public static void setBadgeOfMIUI(Notification notification, int count) {

        try {
            if (Build.MANUFACTURER.toLowerCase().contains("xiaomi")) {
                Field field = notification.getClass().getDeclaredField("extraNotification");
                Object extraNotification = field.get(notification);
                Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
                method.invoke(extraNotification, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置华为Badge
     * 需要添加权限：<uses-permission android:name="com.huawei.android.launcher.permission.CHANGE_BADGE" />
     *
     * @param context
     * @param count
     */
    private static void setBadgeOfEXUI(Context context, int count) {
        try {
            Bundle badgeBundle = new Bundle();
            badgeBundle.putString("package", context.getPackageName());
            badgeBundle.putString("class", getLauncherClassName(context));
            badgeBundle.putInt("badgenumber", count);
            context.getContentResolver().call(
                    Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                    "change_badge", null, badgeBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Nova的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfNova(Context context, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tag", context.getPackageName() + "/" +
                getLauncherClassName(context));
        contentValues.put("count", count);
        context.getContentResolver().insert(
                Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                contentValues);
    }

    /**
     * 设置联想ZUK的Badge
     * 需要添加权限：<uses-permission android:name="android.permission.READ_APP_BADGE" />
     *
     * @param context
     * @param count
     */
    private static void setBadgeOfZuk(Context context, int count) {
        Bundle extra = new Bundle();
        extra.putInt("app_badge_count", count);
        context.getContentResolver().call(
                Uri.parse("content://com.android.badge/badge"),
                "setAppBadgeCount", null, extra);
    }


    /**
     * 设置索尼的Badge
     * 需添加权限：<uses-permission android:name="com.sonymobile.home.permission.PROVIDER_INSERT_BADGE" />
     * <uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" />
     * <uses-permission android:name="com.sonyericsson.home.action.UPDATE_BADGE" />
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfSony(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow); // 是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName); // 启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count)); // 数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName()); // 包名
        context.sendBroadcast(localIntent);
    }

    /**
     * 设置三星的Badge设置LG的Badge
     * 需添加权限：<uses-permission android:name="com.sec.android.provider.badge.permission.READ" />
     * <uses-permission android:name="com.sec.android.provider.badge.permission.WRITE" />
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfSumsung(Context context, int count) {
        // 获取你当前的应用
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    /**
     * 设置HTC的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfHTC(Context context, int count) {
        Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
        ComponentName localComponentName = new ComponentName(context.getPackageName(),
                getLauncherClassName(context));
        intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
        intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
        context.sendBroadcast(intentNotification);
        Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
        intentShortcut.putExtra("packagename", context.getPackageName());
        intentShortcut.putExtra("count", count);
        context.sendBroadcast(intentShortcut);
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     * "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // All Application must have 1 Activity at least.Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        // 另一种实现方式
        // ComponentName componentName = context.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName()).getComponent();
        // return componentName.getClassName();
        return info.activityInfo.name;
    }
}