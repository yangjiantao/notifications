package jd.cdyjy.market.notification

import android.app.Notification
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 通知渠道配置类
 * Created by Jiantao.Yang on 2021/5/31
 */
@RequiresApi(Build.VERSION_CODES.O)
data class NotificationChannelConfig(
    val id: String,
    val name: String,
    val description: String,
    val importance: Int = NotificationManager.IMPORTANCE_UNSPECIFIED,
    val enableVibration: Boolean = true,
    val lockscreenVisibility: Int = Notification.VISIBILITY_PRIVATE
)
