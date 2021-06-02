package jd.cdyjy.market.notification

/**
 * 通知提示规则，包括：功能状态、响铃和振动频率、特定通知状态（免打扰）等
 * Created by Jiantao.Yang on 2021/6/1
 */
open class BaseNotifyRule {

    /**
     * 通知功能状态，默认返回开启通知。
     * 关闭场景：用户手动关掉通知
     */
    open fun enableAll(): Boolean {
        return true
    }

    /**
     * 是否发送通知。
     */
    open fun notifyEnable(): Boolean {
        return true
    }

    /**
     * 振动 开关
     */
    open fun vibrateEnable(): Boolean {
        return true
    }

    /**
     * 响铃/提示音 开关
     */
    open fun soundEnable(): Boolean {
        return true
    }
}