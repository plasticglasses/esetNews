package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HeadlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headline)
        var fireDocID = intent.getStringExtra("id")

        val postButton = findViewById<Button>(R.id.postButton)
//        val timestampText = findViewById<TextView>(R.id.contextTimestamp)
//        val headlineImage = findViewById<ImageView>(R.id.contextHeadineImage)

        //back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = Firebase.firestore

//        timestampText.text= info.get(1).toString()
        val generalHeadlineDocRef = db.collection("general_headlines").document(fireDocID.toString())
        generalHeadlineDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val url = document.getString("article")
                    val myWebView =  findViewById<WebView>(R.id.contentWebview);
                    myWebView.loadUrl(url!!);

                    Log.d("Full screen article", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("Full screen article", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Full screen article", "get failed with ", exception)
            }


        postButton.setOnClickListener(){

            val commentText = findViewById<TextView>(R.id.addCommentText).text.toString()


            // Update one field, creating the document if it does not already exist.
            val data = hashMapOf("comments[1][0]" to commentText)

            db.collection("general_headlines").document(fireDocID.toString())
                .set(data, SetOptions.merge())
        }

    }
}