package fast.build.util.notification

/**
 * Created by Jiantao.Yang on 2021/5/31
 */
data class NotificationContent(
    val title: String,
    val text: String,
    val extra: Map<String, Any>? = null,
)
