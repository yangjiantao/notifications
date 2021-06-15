package jd.cdyjy.market.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


/**
 * 简单的Notifier实现类
 * Created by Jiantao.Yang on 2021/6/1
 */
class SimpleNotifier : BaseNotifier() {

    override fun generateNotifyId(content: NotificationContent): Int {
        // always 1
        return 1
    }

    override fun createNotificationInstance(
        context: Context,
        builder: NotificationCompat.Builder,
        content: NotificationContent
    ): Notification {
        return builder.setContentTitle("测试通知消息")
            .setContentText("消息内容")
            .setNumber(1)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.ic_notification_clear_all)
            .setContentIntent(NotificationUtil.generateOpenAppIntent(context))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createNotificationChannel(
        context: Context,
        content: NotificationContent
    ): String? {
        val config = NotificationChannelConfig(
            "xxx23",
            "聊天消息3",
            "测试",
            NotificationManager.IMPORTANCE_HIGH
        )
        return NotificationUtil.createNotificationChannel(context, config)
    }

    override fun getCustomSoundUri(context: Context): Uri {
        return NotificationUtil.getSoundUri(context, R.raw.dongdong)
    }
}