package jd.cdyjy.market.notification

import android.app.Notification
import android.content.Context
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
            Log.d(TAG, "notifyRule.enable is false.")
            return false
        }

        if (notifyRule.notifyEnable()) {
            // 允许发送通知
            generateNotification(context, content)
        }

        if (notifyRule.soundEnable()) {
            // 允许播放提示音
        }

        if (notifyRule.vibrateEnable()) {
            // 允许振动
        }

        return true
    }

    private fun generateNotification(context: Context, content: NotificationContent) {
        // 1. Create/Retrieve Notification Channel for O and beyond devices (26+).
        // NotificationChannels are required for Notifications on O (API 26) and above.
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, content)
            } else null

        // 2. build a notification
        val builder: NotificationCompat.Builder =
            if (channelId != null) {
                NotificationCompat.Builder(context, channelId)
            } else NotificationCompat.Builder(context)

        val notification = createNotificationInstance(context, builder, content)

        // 3. notify
        notificationManager!!.notify(generateNotifyId(content), notification)
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
    abstract fun createNotificationChannel(context: Context, content: NotificationContent): String?

    fun setRule(rule: BaseNotifyRule) {
        notifyRule = rule
    }

    companion object {
        private const val TAG = "BaseNotifier"
    }
}