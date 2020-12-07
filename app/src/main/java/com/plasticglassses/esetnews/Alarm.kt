package com.plasticglassses.esetnews

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import com.dfl.newsapi.NewsApiRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.schedulers.Schedulers
import org.intellij.lang.annotations.Language
import java.util.*
import kotlin.collections.ArrayList


class Alarm : BroadcastReceiver() {

    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")

    @SuppressLint("InvalidWakeLockTag", "CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        val pm =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager

        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alert")
        wl.acquire()

        val db = Firebase.firestore

        val uID = getUserID()

        getFirestoreID(uID) { fID ->
            //get last updated
            Log.d("Alarm", ("fID returned " + fID))
            getUsersAlerts(fID) { alertArray ->
                for(alert in alertArray){
                    newsApiRepository.getEverything(q = alert, domains = null, from =  "2020-11-01", to = "2020-12-01",  pageSize = 20, page = 1)
                        .subscribeOn(Schedulers.io())
                        .toFlowable()
                        .flatMapIterable { articles -> articles.articles }
                        .subscribe({ article -> Log.d("getEverything article", article.title) },
                            { t -> Log.d("getEverything error", t.message.toString()) })
                    Toast.makeText(context, alert.toString(), Toast.LENGTH_LONG).show() // For example
                }
            }
        }


        //find user and get alerts
        //look for alerted documets


        //get user alerts from firebase

        // for each alert, run a search for any new articles

        //add to general collection


        //update general, science and tech news
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


    /*
   Get the id of the document which has the same authUserID sa the surrent loged in user
    */
    private fun getFirestoreID(
        uID: String,
        callback: (String) -> Unit
    ): String {
        val db = Firebase.firestore
        var fID = ""

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                //for each user in firestore
                for (document in result) {
                    //get userid of the firestore document
                    val docID = document.getString("authUserID")
                    //check that its correct user
                    if (docID == uID) {
                        fID = document.id
                        callback.invoke(fID)
                    }
                }
            }

        return fID
    }

    /*
    get the current logged in user id
     */
    private fun getUserID(): String {
        var thisUid = ""

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            thisUid = user.uid
            Log.d("Alarm", ("success we got the UID" + thisUid))

            // User is signed in
        }
        return thisUid
    }

    /*
   get the current users alert array from firetore
    */
    private fun getUsersAlerts(
        docID: String?, callback: (ArrayList<String>) -> Unit
    ): ArrayList<String>? {
        var usersAlerts = arrayListOf<String>()
        val db = Firebase.firestore
        db.collection("users").document(docID!!)
            .get()
            .addOnSuccessListener { result ->
                //val username = result.getString("username")
                usersAlerts = (result["alerts"] as ArrayList<String>)
                //populate alerts fragment with chips of alerts
                Log.d("Alarm", ("${result.id} => ${result.data} " + usersAlerts))
                callback.invoke(usersAlerts)
            }
        return usersAlerts
    }
///*
//    update firebase last updated counter
//    */
//    fun updateLastUpdated(db: FirebaseFirestore, callback: (Timestamp) -> Unit) {
//        val docRef = db.collection("users").document(getUserID())
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    val today = Calendar.getInstance()
//                    Log.d("TIME: update last updated to: ", today.time.toString())
//
//                    //set data from firestore
//                    val data = hashMapOf("last_updated" to today.time)
//
//                    //upload
//                    db.collection("users").document(getUserID())
//                        .set(data, SetOptions.merge())
//
//                } else {
//                    Log.d("Updating general headlines last updated", "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Updating general headlines last updated", "get failed with ", exception)
//            }
//    }

}

