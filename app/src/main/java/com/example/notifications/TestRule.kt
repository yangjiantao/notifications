package com.example.notifications

import jd.cdyjy.market.notification.BaseNotifyRule

/**
 * Created by Jiantao.Yang on 2021/6/15
 */
class TestRule : BaseNotifyRule(){
    override fun notifyEnable(): Boolean {
        return SettingsActivity.preferenceConfig.notifyAll
    }

    override fun notifyVibrateEnable(): Boolean {
        return SettingsActivity.preferenceConfig.vibrateEnable
    }

    override fun notifySoundEnable(): Boolean {
        return SettingsActivity.preferenceConfig.soundEnable
    }
}