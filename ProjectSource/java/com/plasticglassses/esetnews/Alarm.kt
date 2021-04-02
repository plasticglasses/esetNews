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
import androidx.core.content.ContextCompat
import com.dfl.newsapi.NewsApiRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

/**
 * This Alarm activates every minute when the service button on the profile page is clicked
 * Example was based on the work in https://stackoverflow.com/questions/4459058/alarm-manager-example
 * @author Stackoverflow user XXX, "XXX" is his actual username
 */
class Alarm : BroadcastReceiver() {

    //    private lateinit var myAlertsArray: ArrayList<String?>
//
//    fun getAlertArray(): ArrayList<String?> {
//            return myAlertsArray
//        }
//    fun setAlertArray(myArray: ArrayList<String?>) {
//        this.myAlertsArray = myArray
//    }

    //setup of external resources
    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")
    private lateinit var auth: FirebaseAuth

    /**
     *When command recived, go to main activity where the function for searching for alert docuemtns is given
     * then print a snackbar
     */
    @SuppressLint("InvalidWakeLockTag", "CheckResult")
    override fun onReceive(context: Context, intent: Intent) {
        FirebaseApp.initializeApp(context)
        val db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }

        //using multiple firestore in application therefore need to enable persistance
        db.firestoreSettings = settings

        val pm =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager

        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Alert")
        wl.acquire()

//        val docRef = db.collection("users")
//        docRef.get()
//            .addOnSuccessListener { documents ->
//                for(doc in documents){
//                    if (doc != null) {
//                        Log.d("Alarm", "DocumentSnapshot data: ${doc.data}")
//                    } else {
//                        Log.d("Alarm", "No such document")
//                    }
//                }
//
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Alarm", "get failed with ", exception)
//            }

        //find user and get alerts
        //look for alerted documets

        //get user alerts from firebase

        // for each alert, run a search for any new articles

        //run check alert function
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)

        //TODO get the new alerts if any and send out a notification

        Toast.makeText(context, "Alarm!!!!!!!!!!!", Toast.LENGTH_LONG).show() // For example
        //update general, science and tech news
        wl.release()
    }


    /**
     * set the timer for repeating the alarm
     * Alarm scheduled every minute for ease of testing
     */
    fun setAlarm(context: Context) {
//        var myArray = arrayListOf(firstString)
//        setAlertArray(myArray)

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

    /**
     * stop the alarm sound
     */
    fun cancelAlarm(context: Context) {
        val intent = Intent(context, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }


    /**
     * Get the id of the document which has the same authUserID sa the current loged in user
     * @param uID - firestore users, user id, unique
     * @param db - firestore database
     */
    private fun getFirestoreID(
        uID: String,
        db: FirebaseFirestore,
        callback: (String) -> Unit
    ): String {
//        val db = Firebase.firestore
        var fID = ""

        //go to users collection
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

    /**
     * get the current logged in user id
     */
    private fun getUserID(): String {
        auth = Firebase.auth
        var user = auth.currentUser

        var thisUid = ""

//        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            thisUid = user.uid
            Log.d("Alarm", ("success we got the UID" + thisUid))

            // User is signed in
        }
        return thisUid
    }

    /**
     *get the current users alert array from firetore
     * @param docID - firestore docuemnt id, unique
     * @param db - firestore database
     */
    private fun getUsersAlerts(
        docID: String?,
        db: FirebaseFirestore,
        callback: (ArrayList<String>) -> Unit
    ): ArrayList<String>? {
        var usersAlerts = arrayListOf<String>()
//        val db = Firebase.firestore
        //get docuement from firebase
        db.collection("users").document(docID!!)
            .get()
            .addOnSuccessListener { result ->
                //get all the alerts
                usersAlerts = (result["alerts"] as ArrayList<String>)
                //populate alerts fragment with chips of alerts
                Log.d("Alarm", ("${result.id} => ${result.data} " + usersAlerts))
                callback.invoke(usersAlerts)
            }
        return usersAlerts
    }

}