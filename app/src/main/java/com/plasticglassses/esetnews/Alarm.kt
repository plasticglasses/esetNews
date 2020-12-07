package com.plasticglassses.esetnews

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.widget.Toast


class Alarm : BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context, intent: Intent) {
        val pm =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager

        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alert")
        wl.acquire()

        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show() // For example
        wl.release()
    }

    fun setAlarm(context: Context) {
        val am =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, Alarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            100 * 60 * 10.toLong(),
            pi
        ) // Millisec * Second * Minute
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}

