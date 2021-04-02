package com.plasticglassses.esetnews//package com.plasticglassses.esetnews
/**
 * not part of final solutoins
 * attemp to make a pop up notification
 */
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.ContextWrapper
//import android.graphics.Color
//import androidx.core.app.NotificationCompat
//
//internal class NotificationHelper(base: AlertsFragment) : ContextWrapper(base){
//
//    private var notifManager: NotificationManager? = null
//
//    private val manager: NotificationManager?
//    get(){
//        if (notifManager == null){
//            notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        }
//        return notifManager
//    }
//    init{
//        createChannels()
//    }
//
//    fun createChannels(){
//        val notificationChannel =
//            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//        notificationChannel.enableLights(true)
//        notificationChannel.lightColor = Color.RED
//        notificationChannel.setShowBadge(true)
//        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        manager!!.createNotificationChannel(notificationChannel)
//    }
//
//    fun getNotification(title: String, body: String): NotificationCompat.Builder{
//        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setSmallIcon(R.drawable.places_ic_search)
//            .setNumber(3)
//            .setAutoCancel(true)
//
//    }
//
//    fun notify(id: Int, notification: NotificationCompat.Builder){
//        manager!!.notify(id, notification.build())
//
//    }
//
//    companion object {
//        const val CHANNEL_ID = "com.plasticglassses.esetnews.CHANNEL_ONE"
//        const val CHANNEL_NAME = "This Channel"
//    }
//
//}