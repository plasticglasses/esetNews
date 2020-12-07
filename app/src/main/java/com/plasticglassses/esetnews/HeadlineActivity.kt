package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class HeadlineActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headline)
        //intent from news adapter. once articel selected from the adapter...
        var fireDocID = intent.getStringExtra("id")

        var fireDocPath = intent.getStringExtra("filePath")

        Log.d("YOU LOVE HIM", fireDocPath.toString())

        val postButton = findViewById<Button>(R.id.postButton)
//        val timestampText = findViewById<TextView>(R.id.contextTimestamp)
//        val headlineImage = findViewById<ImageView>(R.id.contextHeadineImage)

        //back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = Firebase.firestore

        //setup full article view
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

//
//        val scienceHeadlineDocRef = db.collection("science_headlines").document(fireDocID.toString())
//        scienceHeadlineDocRef.get()
//            .addOnSuccessListener { documentScience ->
//                if (documentScience != null) {
//
//                    val url = documentScience.getString("article")
//                    val myWebView =  findViewById<WebView>(R.id.contentWebview);
//                    myWebView.loadUrl(url!!);
//
//                    Log.d("Full screen article", "DocumentSnapshot data: ${documentScience.data}")
//                } else {
//                    Log.d("Full screen article", "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Full screen article", "get failed with ", exception)
//            }
//
//        val techHeadlineDocRef = db.collection("science_headlines").document(fireDocID.toString())
//        techHeadlineDocRef.get()
//            .addOnSuccessListener { documentTech ->
//                if (documentTech != null) {
//
//                    val url = documentTech.getString("article")
//                    val myWebView =  findViewById<WebView>(R.id.contentWebview);
//                    myWebView.loadUrl(url!!);
//
//                    Log.d("Full screen article", "DocumentSnapshot data: ${documentTech.data}")
//                } else {
//                    Log.d("Full screen article", "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("Full screen article", "get failed with ", exception)
//            }

        //add comments to firebase
        postButton.setOnClickListener(){

            auth = Firebase.auth
            val user = auth.currentUser?.uid
            val commentText = findViewById<TextView>(R.id.addCommentText).text.toString()

            val df: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
            val timestamp = df.format(Date()).toString()

             // var myArray = arrayListOf(user, commentText, timestamp)
            var list = (user.toString() + "!" + commentText.toString() + "!" +  timestamp.toString())

            //add to article
            db.collection("general_headlines").document(fireDocID.toString()).update("comments", FieldValue.arrayUnion(list))

            //add reference to comments
            db.collection("users").document(user!!).update("comments", FieldValue.arrayUnion(fireDocID))
        }

        //populate recycler view tih current comments
//
//        val commentArrayList = populateCommentList()
//
//        val recyclerView = findViewById<View>(R.id.commentRecyclerView) as RecyclerView
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        val commentAdapter = CommentAdapter(commentArrayList)
//        recyclerView.adapter = commentAdapter
    }

//    private fun populateCommentList(): ArrayList<CommentModel> {
//        val list = ArrayList<CommentModel>()
//
//
//        return list
//    }

}