package com.plasticglassses.esetnews

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder
import android.widget.Toast

class MyService: Service() {

    var alarm = Alarm()

    private lateinit var ringtone: Ringtone

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int{

        alarm.setAlarm(this);

        val firstString = intent.getStringExtra("firstString")

        Toast.makeText(this, firstString.toString(), Toast.LENGTH_LONG).show()
        playAlert()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone.stop()
        Toast.makeText(this, "mooleymoo", Toast.LENGTH_LONG).show()
    }

    fun playAlert(){
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ringtone = RingtoneManager.getRingtone(this, alarmSound)

        if(!ringtone.isPlaying){
            ringtone.play()
        }
    }

}
