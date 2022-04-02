package fast.build.util.notification

/**
 * 通知提示规则，包括：功能状态、响铃和振动频率、特定通知状态（免打扰）等
 * Created by Jiantao.Yang on 2021/6/1
 */
open class BaseNotifyRule {

    /**
     * 通知功能状态，默认返回开启通知。
     * 关闭场景：用户手动关掉通知
     */
//    open fun enableAll(): Boolean {
//        return true
//    }

    /**
     * 通知提醒开关
     */
    open fun notifyEnable(): Boolean {
        return true
    }

    /**
     * 通知响铃/提示音 开关，应用内设置，Android （O+）系统已创建的渠道通知无法控制。
     */
    open fun vibrateEnable(): Boolean {
        return true
    }

    /**
     * 通知响铃/提示音 开关，应用内设置，Android （O+）系统已创建的渠道通知无法控制。
     * Tips: 当 {@link #vibrateOrSoundWithoutNotification()}返回true时，若想播放提示音，
     * 则需要提供相应音频资源，重写 {@link BaseNotifier.getCustomSoundUri}方法并返回有效资源。
     */
    open fun soundEnable(): Boolean {
        return true
    }

    /**
     * 在没有通知的情况下，仅振动或播放提示音。
     * 默认为false，场景：振动和提示音由通知一并创建。
     * 为true的场景：IM类消息通知，当在聊天页面或会话列表时，可能仅仅只需要振动或播放声音来提醒用户即可，
     * 这种情况下不需要额外创建通知来打扰用户。
     */
    open fun vibrateOrSoundWithoutNotification(): Boolean {
        return false
    }
}