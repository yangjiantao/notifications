package jd.cdyjy.market.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Created by Jiantao.Yang on 2021/6/1
 */
abstract class BaseNotifier {

    private var notifyRule: BaseNotifyRule = BaseNotifyRule()
    private var notificationManager: NotificationManagerCompat? = null

    /**
     * 根据通知数据，返回通知id
     */
    protected abstract fun generateNotifyId(content: NotificationContent): Int

    /**
     * 创建并发送通知。
     * 模板方法，创建和发送的逻辑确认，不允许之类覆盖。
     * @return true 发送成功，false 失败（用户关闭、校验不过等）
     */
    fun notify(context: Context, content: NotificationContent): Boolean {
        if (notificationManager == null) {
            // init first
            notificationManager = NotificationManagerCompat.from(context)
        }
        if (!notificationManager!!.areNotificationsEnabled()) {
            Log.w(TAG, "You need to enable notifications for this app.")
            return false
        }
        if (!notifyRule.enableAll()) {
            Log.i(TAG, "notifyRule.enable is false.")
            return false
        }

        if (notifyRule.notifyEnable()) {
            // 允许发送通知
            generateNotification(context, content)
        }

        return true
    }

    @SuppressLint("NewApi")
    private fun generateNotification(context: Context, content: NotificationContent) {
        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        // NotificationChannels are required for Notifications on O (API 26) and above.
        val channel =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, content)
            } else null

        // 2. build a notification
        val builder: NotificationCompat.Builder =
            if (channel != null) {
                NotificationCompat.Builder(context, channel.id)
            } else NotificationCompat.Builder(context)

        val notification = createNotificationInstance(context, builder, content)

        // 检查提示音和振动配置
        checkSoundAndVibrate(context, notification)

        // 创建通知前回调
        beforeNotify(notification, content)

        // 3. notify
        notificationManager!!.notify(generateNotifyId(content), notification)
    }

    /**
     * 创建通知前回调
     * 应用场景：小米系统设置应用角标-未读数、其它
     */
    open fun beforeNotify(notification: Notification, content: NotificationContent) {
    }

    /**
     * Android 8.0以下系统可以动态控制通知的声音和振动，8.0及以上，创建通知渠道后，就无法更改通知行为，此时用户拥有完全控制权。
     */
    private fun checkSoundAndVibrate(
        context: Context,
        notification: Notification
    ) {
        // check sound uri
        if (notifyRule.soundEnable()) {
            val customSoundUri = getCustomSoundUri(context)
            if (customSoundUri != null) {
                notification.sound = customSoundUri
                notification.defaults =
                    notification.defaults and NotificationCompat.DEFAULT_SOUND.inv()
            } else {
                // 防止配置错误导致没有提示音
                notification.defaults =
                    notification.defaults or NotificationCompat.DEFAULT_SOUND
            }
        } else {
            notification.sound = null
            notification.defaults =
                notification.defaults and NotificationCompat.DEFAULT_SOUND.inv()
        }

        // check vibrate
        if (notifyRule.vibrateEnable()) {
            notification.defaults =
                notification.defaults or NotificationCompat.DEFAULT_VIBRATE
        } else {
            notification.defaults =
                notification.defaults and NotificationCompat.DEFAULT_VIBRATE.inv()
        }
    }

    /**
     * 提示音资源
     */
    open fun getCustomSoundUri(context: Context): Uri? {
        return null
    }

    /**
     * 创建通知实例对象
     */
    abstract fun createNotificationInstance(
        context: Context,
        builder: NotificationCompat.Builder,
        content: NotificationContent
    ): Notification

    /**
     * 根据通知数据data，创建/获取通知渠道ID
     */
    @RequiresApi(Build.VERSION_CODES.O)
    abstract fun createNotificationChannel(
        context: Context,
        content: NotificationContent
    ): NotificationChannel?


    internal fun setRule(rule: BaseNotifyRule) {
        notifyRule = rule
    }

    companion object {
        private const val TAG = "BaseNotifier"
    }
}