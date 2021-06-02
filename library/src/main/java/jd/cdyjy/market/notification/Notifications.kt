package jd.cdyjy.market.notification

import android.content.Context

/**
 * Created by Jiantao.Yang on 2021/6/2
 */
object Notifications {

    private var notifier: BaseNotifier = SimpleNotifier()

    fun setNotifier(notifier: BaseNotifier) {
        this.notifier = notifier
    }

    fun notify(context: Context, content: NotificationContent): Boolean {
        return notifier.notify(context, content)
    }
}