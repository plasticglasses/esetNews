package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HeadlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headline)
        var fireDocID = intent.getStringExtra("id")

        val headlineText = findViewById<TextView>(R.id.contextHeadline)
        val timestampText = findViewById<TextView>(R.id.contextTimestamp)
        val headlineImage = findViewById<ImageView>(R.id.contextHeadineImage)



        val db = Firebase.firestore

//        timestampText.text= info.get(1).toString()
        val generalHeadlineDocRef = db.collection("general_headlines").document(fireDocID.toString())
        generalHeadlineDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    headlineText.text= fireDocID.toString()

//if document is a general headline, populate activity with information
                    val headlineText = findViewById<TextView>(R.id.contextHeadline)
                    val timestampText = findViewById<TextView>(R.id.contextTimestamp)
                    val headlineImage = findViewById<ImageView>(R.id.contextHeadineImage)

                    headlineText.text = document.get("headline").toString()
                    timestampText.text = document.get("timestamp").toString()


                    Log.d("Full screen article", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("Full screen article", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Full screen article", "get failed with ", exception)
            }






    }
}