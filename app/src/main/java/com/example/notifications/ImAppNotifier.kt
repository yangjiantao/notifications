package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import jd.cdyjy.market.notification.*

/**
 * 即时通讯类App通知实现
 * Created by Jiantao.Yang on 2021/6/17
 */
class ImAppNotifier : BaseNotifier() {

    override fun generateNotifyId(content: NotificationContent): Int {
        // 可以会话为单位生成不同的通知ID
        return content.hashCode() % 2
    }

    override fun createNotificationInstance(
        context: Context,
        builder: NotificationCompat.Builder,
        content: NotificationContent
    ): Notification {
        return builder.setContentTitle("年会抽奖")
            .setContentText("小红：好大的红包啊")
            .setNumber(1)
            .setAutoCancel(true)
            .setSmallIcon(android.R.drawable.ic_notification_clear_all)
//            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
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
            "id:im",
            "聊天消息",
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


    /**
     * 同步小米系统桌面未读数角标
     */
    override fun beforeNotify(notification: Notification, content: NotificationContent) {
        BadgeUtils.setBadgeOfMIUI(notification, 1)
    }

    override fun getCustomSoundUri(context: Context): Uri {
        return NotificationUtil.getSoundUri(context, R.raw.dongdong)
    }
}