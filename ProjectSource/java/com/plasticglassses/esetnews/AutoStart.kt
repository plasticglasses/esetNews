
package com.plasticglassses.esetnews

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Not fully utilised
 * starts the alarm function at boot so that alerts are searched for everyday
 * calls the alarm
 * @auther XXX
 */
class AutoStart : BroadcastReceiver() {
    var alarm = Alarm()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            alarm.setAlarm(context)
        }
    }
}