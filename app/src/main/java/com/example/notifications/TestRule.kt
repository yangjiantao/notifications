package com.example.notifications

import fast.build.util.notification.BaseNotifyRule

/**
 * Created by Jiantao.Yang on 2021/6/15
 */
class TestRule : BaseNotifyRule() {
    override fun notifyEnable(): Boolean {
        return SettingsActivity.preferenceConfig.notifyAll
    }

    override fun vibrateEnable(): Boolean {
        return SettingsActivity.preferenceConfig.vibrateEnable
    }

    override fun soundEnable(): Boolean {
        return SettingsActivity.preferenceConfig.soundEnable
    }

    override fun vibrateOrSoundWithoutNotification(): Boolean {
        return true
    }
}