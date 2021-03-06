/*
 * Copyright (C) 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fast.build.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import java.lang.ref.SoftReference

/**
 * Simplifies common [Notification] tasks.
 */
object NotificationUtil {

    private val ringtoneMap: MutableMap<String, SoftReference<Ringtone>> = mutableMapOf()

    /**
     * create NotificationChannel and return channelId.
     */
    fun createNotificationChannel(
        context: Context, channelConfig: NotificationChannelConfig
    ): NotificationChannel? {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            val channelId = channelConfig.id

            // The user-visible name of the channel.
            val channelName: CharSequence = channelConfig.name
            // The user-visible description of the channel.
            val channelDescription = channelConfig.description
            val channelImportance = channelConfig.importance
            val channelEnableVibrate = channelConfig.enableVibration
            val channelLockscreenVisibility = channelConfig.lockscreenVisibility

            // Initializes NotificationChannel.
            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
            notificationChannel.description = channelDescription
            notificationChannel.enableVibration(channelEnableVibrate)
            notificationChannel.lockscreenVisibility = channelLockscreenVisibility

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            notificationChannel
        } else {
            // Returns null for pre-O (26) devices.
            null
        }
    }

    fun openNotificationSettingsForApp(context: Context) {
        // Links to this app's notification settings.
        val intent = Intent()
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", context.packageName)
        intent.putExtra("app_uid", context.applicationInfo.uid)

        // for Android 8 and above
        intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        context.startActivity(intent)
    }

    /**
     * @return ?????????App???PendingIntent
     */
    fun generateOpenAppIntent(context: Context): PendingIntent? {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * @param rawResId ????????????id
     */
    fun getSoundUri(context: Context, rawResId: Int): Uri {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + rawResId)
    }

    /**
     * ??????
     * @param pattern an array of longs of times for which to turn the vibrator on or off.
     * @param repeat  the index into pattern at which to repeat, or -1 if
     *                you don't want to repeat.
     */
    fun vibrate(context: Context, pattern: LongArray, repeat: Int = -1) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibrator != null) {
            //?????????10???????????? ??????10?????????????????????????????? ????????? VibratorService: Ignoring incoming vibration as process with?????????
            //?????? ???????????? https://stackoverflow.com/questions/57893054/vibration-on-widget-click-not-working-since-api-29
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM) //key
                    .build()
                val ve = VibrationEffect.createWaveform(pattern, repeat)
                vibrator.vibrate(ve, audioAttributes)
            } else {
                vibrator.vibrate(pattern, repeat)
            }
        }
    }

    /**
     * ???????????????
     */
    fun playSound(context: Context, rawResId: Int) {
        playSound(context, getSoundUri(context, rawResId))
    }

    /**
     * ???????????????
     */
    fun playSound(context: Context, soundUri: Uri) {
        val ringtone = ringtoneMap[soundUri.toString()]?.get()
        if (ringtone != null) {
            if (ringtone.isPlaying) {
                return
            }
            ringtone.play()
        } else {
            val rt = RingtoneManager.getRingtone(context, soundUri)
            rt?.play()
            ringtoneMap[soundUri.toString()] = SoftReference(rt)
        }
    }
}