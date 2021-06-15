package jd.cdyjy.market.notification

import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import java.lang.IllegalStateException

/**
 * 非法实现类，需要自定义实现并调用  {@link Notifications.setNotifier} 配置
 * Created by Jiantao.Yang on 2021/6/15
 */
class IllegalNotifier : BaseNotifier() {
    override fun generateNotifyId(content: NotificationContent): Int {
        return 0
    }

    override fun createNotificationInstance(
        context: Context,
        builder: NotificationCompat.Builder,
        content: NotificationContent
    ): Notification {
        throw IllegalStateException("请自定义实现BaseNotifier子类并调用  {@link Notifications.setNotifier} 配置")
    }

    override fun createNotificationChannel(
        context: Context,
        content: NotificationContent
    ): NotificationChannel? {
        throw IllegalStateException("请自定义实现BaseNotifier子类并调用  {@link Notifications.setNotifier} 配置")
    }
}