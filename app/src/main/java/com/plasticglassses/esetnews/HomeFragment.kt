package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.NewsAdapter
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Firebase.firestore
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)

        //get all new news from newsAPI
        generateGeneralNews(db)

        //get documents from firestore and populate modles
        populateList(db) { headlineArrayList ->
            Log.d("mylist", headlineArrayList.toString())
            val recyclerHeadlineView =
                rootView.findViewById<View>(R.id.headline_recycler_view) as RecyclerView
            val headlineLayoutManager = LinearLayoutManager(activity)
            recyclerHeadlineView.layoutManager = headlineLayoutManager
            val headlineAdapter = NewsAdapter(headlineArrayList)
            recyclerHeadlineView.adapter = headlineAdapter
        }

        updateLastUpdated(db)

        return rootView
    }

    private fun generateGeneralNews(db: FirebaseFirestore) {
        //get general news for homepage
        newsApiRepository.getTopHeadlines(
            category = Category.GENERAL,
            country = Country.GB,
            q = "",
            pageSize = 20,
            page = 1
        )
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({ article ->

                //print out new articles
                Log.d(
                    "getTopHead General article",
                    article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt
                )

                //read last updated time
                val docRef = db.collection("last_updated").document("general_last_updated")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
//                            upload new articles only
                            //get last updated date from firebase, format it into ISO format and compare to the pblished at time
                            getLastUpdated(db) { result ->
                                var javaDate = result.toDate() as Date
                                val formattedDate =
                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(javaDate)

                                //only add new articles to firebase
                                if (article.publishedAt > formattedDate) {
                                    val headline = hashMapOf(
                                        "headline" to article.title,
                                        "image" to article.urlToImage,
                                        "author" to article.author,
                                        "timestamp" to article.publishedAt,
                                        "comments" to arrayListOf<String>()
                                    )

                                    //add new documents to firebase
                                    db.collection("general_headlines").document()
                                        .set(headline)
                                        .addOnSuccessListener {
                                            Log.d(
                                                "home Fragment",
                                                "DocumentSnapshot successfully written!"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "home Fragment",
                                                "Error writing document",
                                                e
                                            )
                                        }
                                }//else document is too old so don't add
                            }
                        } else {
                            Log.d("GENERAL_LAST_UPDATED", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("TAG", "get failed with ", exception)
                    }
            },
                { t -> Log.d("getTopHeadlines error", t.message!!) })
    }

    /*
    take firebase docuemnts and add them to list for processing by recycler view
     */
    private fun populateList(db: FirebaseFirestore, callback: (ArrayList<newsModel>) -> Unit) {
        val list = ArrayList<newsModel>()
        db.collection("general_headlines").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {

                        //make model with data from firestore
                        val thisModel = newsModel()
                        thisModel.setHeadline(document.get("headline").toString())
                        thisModel.setHeadlineImg(document.get("image").toString())
                        thisModel.setTimestamp(document.get("timestamp").toString())
                        thisModel.setPublisher(document.get("author").toString())

                        //add to list so that recycler view can take data
                        list.add(thisModel)
                    } else {
                        Log.d("POPULATE LIST", "No such document")
                    }
                    callback.invoke(list)

                }
            }
            .addOnFailureListener { exception ->
                Log.d("POPULATE LIST", "get failed with ", exception)
            }
    }

    /*
    update firebase last updated counter
     */
    fun updateLastUpdated(db: FirebaseFirestore) {
        val docRef = db.collection("last_updated").document("general_last_updated")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val today = Calendar.getInstance()
                    Log.d("TIME: update last updated to: ", today.time.toString())

                    //set data from firestore
                    val data = hashMapOf("last_updated" to today.time)

                    //upload
                    db.collection("last_updated").document("general_last_updated")
                        .set(data, SetOptions.merge())

                } else {
                    Log.d("Updating general headlines last updated", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Updating general headlines last updated", "get failed with ", exception)
            }
    }

    /*
    callback the fiestore counter of date last checked
     */
    fun getLastUpdated(db: FirebaseFirestore, callback: (Timestamp) -> Unit) {
        //get the general-headline documents
        val docRef = db.collection("last_updated").document("general_last_updated")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    //add it to timestamp field in firestore
                    var lastUpdated = (document["last_updated"] as Timestamp)

                    callback.invoke(lastUpdated)
                    Log.d("GET LAST UPDATED: ", lastUpdated.toString())

                } else {
                    Log.d("GET LAST UPDATED", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("GET LAST UPDATED", "get failed with ", exception)
            }
    }
}
