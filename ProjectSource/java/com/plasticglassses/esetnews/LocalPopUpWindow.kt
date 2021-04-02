package com.plasticglassses.esetnews

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_local_pop_up_window.*

/**
 * pop up window for adding markers to map
 */
class LocalPopUpWindow : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /**
     * open a dialog window
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_local_pop_up_window)

        // Initialize Firebase Auth
        auth = Firebase.auth
        var user = Firebase.auth.currentUser

        val editText = findViewById<View>(R.id.markerText) as EditText


        popup_add_button.setOnClickListener {

            var latitude = ""
            var longitude = ""

            val extras = intent.extras
            if (extras != null) {
                latitude = extras.getString("lat").toString()
                longitude = extras.getString("lng").toString()
            } else {

            }


            val db = Firebase.firestore
            val user = auth.currentUser

            val marker = hashMapOf(
                "lat" to latitude,
                "lng" to longitude,
                "text" to editText.text.toString(),
                "user" to user!!.uid.toString()
            )

            val newMarkerRef = db.collection("markers").document()
            newMarkerRef.set(marker)
                .addOnSuccessListener { Log.d("Local", "Marker successfully written!") }
                .addOnFailureListener { e -> Log.w("Local", "Error writing marker", e) }

            finish()

        }


    }

}
