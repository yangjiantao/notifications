## Android Notifications

- 封装可复用的代码逻辑，方便快速创建通知
- 支持自定义扩展通知样式
- 支持自定义通知提示规则（比如：1s内只提示一次，会话开启免打扰）
- 提供桌面应用角标（未读数）适配方法
- 支持振动和提示音开关配置及资源自定义
- 其它


### Usage

- 根据业务实现自己的Notifier

```
// 示例代码

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
        
        // NotificationChannels are required for Notifications on O (API 26) and above.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Initializes NotificationChannel.
            val notificationChannel = NotificationChannel(config.id, config.name, config.importance)
            notificationChannel.description = config.description
            notificationChannel.enableVibration(config.enableVibration)
            notificationChannel.lockscreenVisibility = config.lockscreenVisibility
            notificationChannel.setSound(getCustomSoundUri(context), Notification.AUDIO_ATTRIBUTES_DEFAULT)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            notificationChannel
        } else null
    }

}
```

- 初始化
```
Notifications.setNotifier(SimpleNotifier())
```
- 通知提醒
```
Notifications.notify(this, NotificationContent("标题", "内容", mapOf()))
```


### 参考
- https://developer.android.com/guide/topics/ui/notifiers/notifications
- https://www.jianshu.com/p/2566fdebcae4