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
     * 通知提醒开关
     */
    open fun notifyEnable(): Boolean {
        return true
    }

    /**
     * 通知振动 开关，仅能控制Android O以下系统
     */
    open fun notifyVibrateEnable(): Boolean {
        return true
    }

    /**
     * 通知响铃/提示音 开关，仅能控制Android O以下系统
     */
    open fun notifySoundEnable(): Boolean {
        return true
    }
}