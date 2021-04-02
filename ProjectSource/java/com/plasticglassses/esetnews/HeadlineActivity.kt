package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.CommentsAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * view full screen article
 */
class HeadlineActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    /**
     * check which type of article , either general, tech or science and find the docuemnt and display the full document in a web view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headline)
        //intent from news adapter. once articel selected from the adapter...
        var fireDocID = intent.getStringExtra("id")

        //add comments to firebase fro artile
        val postButton = findViewById<Button>(R.id.postButton)
        val db = Firebase.firestore

        //recycler view to view comments
//        val commentArrayList = populateCommentList()
//
//        val recyclerView = findViewById<View>(R.id.commentRecyclerView) as RecyclerView
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        val commentAdapter = CommentsAdapter(commentArrayList)
//        recyclerView.adapter = commentAdapter

        //check if the articel is in the general cheadline colleciton in firebase
        db.collection("general_headlines").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    for (document in task.result!!) {
                        list.add(document.id)
                        if (document.id.toString().equals(fireDocID.toString())) {
                            //setup full article view
                            val generalHeadlineDocRef =
                                db.collection("general_headlines").document(fireDocID.toString())
                            generalHeadlineDocRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {

                                        val url = document.getString("article")
                                        val myWebView = findViewById<WebView>(R.id.contentWebview);
                                        myWebView.loadUrl(url!!);

                                        Log.d(
                                            "Full screen article",
                                            "DocumentSnapshot data: ${document.data}"
                                        )
                                    } else {
                                        Log.d("Full screen article", "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->

                                    Log.d("Full screen article", "get failed with ", exception)
                                }

                        }
                    }
                }
            })

        //check if the articel is in the science cheadline colleciton in firebase
        db.collection("science_headlines").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    for (document in task.result!!) {
                        list.add(document.id)
                        if (document.id.toString().equals(fireDocID.toString())) {
                            val generalHeadlineDocRef =
                                db.collection("science_headlines").document(fireDocID.toString())
                            generalHeadlineDocRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {

                                        val url = document.getString("article")
                                        val myWebView = findViewById<WebView>(R.id.contentWebview);
                                        myWebView.loadUrl(url!!);

                                        Log.d(
                                            "Full screen article",
                                            "DocumentSnapshot data: ${document.data}"
                                        )
                                    } else {
                                        Log.d("Full screen article", "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("Full screen article", "get failed with ", exception)
                                }
                        }
                    }
                }
            })

        //check if the articel is in the tech cheadline colleciton in firebase
        db.collection("tech_headlines").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    for (document in task.result!!) {
                        list.add(document.id)
                        if (document.id.toString().equals(fireDocID.toString())) {
                            Log.d("NOOOWAY tech", list.toString())
                            val generalHeadlineDocRef =
                                db.collection("tech_headlines").document(fireDocID.toString())
                            generalHeadlineDocRef.get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {

                                        val url = document.getString("article")
                                        val myWebView = findViewById<WebView>(R.id.contentWebview);
                                        myWebView.loadUrl(url!!);

                                        Log.d(
                                            "Full screen article",
                                            "DocumentSnapshot data: ${document.data}"
                                        )
                                    } else {
                                        Log.d("Full screen article", "No such document")
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("Full screen article", "get failed with ", exception)
                                }
                        }
                    }
                }
            })


        //add comments to firebase
        postButton.setOnClickListener() {

            auth = Firebase.auth
            val user = auth.currentUser?.uid
            val commentText = findViewById<TextView>(R.id.addCommentText).text.toString()

            val df: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'") // Quoted "Z" to indicate UTC, no timezone offset
            val timestamp = df.format(Date()).toString()

            // var myArray = arrayListOf(user, commentText, timestamp)
            var list = (user.toString() + "!" + commentText.toString() + "!" + timestamp.toString())

            //add to article
            db.collection("general_headlines").document(fireDocID.toString())
                .update("comments", FieldValue.arrayUnion(list))

            //add reference to comments
            db.collection("users").document(user!!)
                .update("comments", FieldValue.arrayUnion(fireDocID))
        }


        //back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    //for comment adapter, no longer implemented, ran into trouble finding if docuemnt was tech, science or general
//    private fun populateCommentList( fireDocID :String,  collectionPath: String): ArrayList<commentsModel> {
//        val db = Firebase.firestore
//
//        val list = ArrayList<commentsModel>()
//        var commentRef =
//            db.collection(collectionPath)
//        commentRef.get().addOnSuccessListener { documents ->
//            for (document in documents) {
//                val any = if (document != null) {
//
//                    //make model with data from firestore
//                    val thisModel = commentsModel()
//                    thisModel.setLongCommentPackage(document.get("comments").toString())
//
//                    //add to list so that recycler view can take data
//                    list.add(thisModel)
//                } else {
//                    Log.d("POPULATE LIST", "No such document")
//                }
//                callback.invoke(list)
//
//            }
//        }
//            .addOnFailureListener { exception ->
//                Log.d("POPULATE LIST", "get failed with ", exception)
//            }
//    }

}