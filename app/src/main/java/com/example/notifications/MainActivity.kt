package com.example.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import jd.cdyjy.market.notification.NotificationContent
import jd.cdyjy.market.notification.Notifications

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.button).setOnClickListener {
            Notifications.notify(this, NotificationContent("xxx", "yyy", mapOf()))
        }
    }
}