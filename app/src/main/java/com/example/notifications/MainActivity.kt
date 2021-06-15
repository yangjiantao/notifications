package com.example.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import jd.cdyjy.market.notification.NotificationContent
import jd.cdyjy.market.notification.Notifications

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Notifications.setNotifier(SimpleNotifier(), TestRule())

        findViewById<View>(R.id.button).setOnClickListener {
            Notifications.notify(this, NotificationContent("xxx", "yyy", mapOf()))
        }

        findViewById<View>(R.id.settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}