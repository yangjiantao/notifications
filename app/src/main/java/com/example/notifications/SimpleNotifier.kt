package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import jd.cdyjy.market.notification.BaseNotifier
import jd.cdyjy.market.notification.NotificationChannelConfig
import jd.cdyjy.market.notification.NotificationContent
import jd.cdyjy.market.notification.NotificationUtil


/**
 * 普通应用通知，简单的Notifier实现类
 * 1. 自定义通知内容及样式，用法请参考官方API
 * 2. Android O以下可配置Rule来控制提示音和振动开关，Android O及其更高系统，则在通知渠道创建后交由用户控制。
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
    ): NotificationChannel? {
        val config = NotificationChannelConfig(
            "xxx23",
            "聊天消息3",
            "测试",
            NotificationManager.IMPORTANCE_HIGH
        )

        // NotificationChannels are required for Notifications on O (API 26) and above.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Initializes NotificationChannel.
            val notificationChannel = NotificationChannel(config.id, config.name, config.importance)
            notificationChannel.description = config.description
            notificationChannel.enableVibration(config.enableVibration)
            notificationChannel.lockscreenVisibility = config.lockscreenVisibility
            notificationChannel.setSound(getCustomSoundUri(context), Notification.AUDIO_ATTRIBUTES_DEFAULT)

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            notificationChannel
        } else null
    }

    override fun getCustomSoundUri(context: Context): Uri {
        return NotificationUtil.getSoundUri(context, R.raw.dongdong)
    }
}