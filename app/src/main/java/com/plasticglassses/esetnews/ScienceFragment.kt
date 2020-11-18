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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.NewsAdapter
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScienceFragment : Fragment() {
    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Firebase.firestore
        var rootView = inflater.inflate(R.layout.fragment_science, container, false)

        generateScienceNews(db)

        //get documents from firestore and populate modles
        populateList(db) { headlineArrayList ->
            Log.d("mylist", headlineArrayList.toString())
            val recyclerHeadlineView =
                rootView.findViewById<View>(R.id.headline_recycler_view) as RecyclerView
            //val headlineArrayList = genNews(recyclerHeadlineView)
            val headlineLayoutManager = LinearLayoutManager(activity)
            recyclerHeadlineView.layoutManager = headlineLayoutManager
            val headlineAdapter = NewsAdapter(headlineArrayList)
            recyclerHeadlineView.adapter = headlineAdapter
        }
        return rootView

    }

    private fun generateScienceNews(db: FirebaseFirestore): ArrayList<newsModel> {
        val headlineArrayList = ArrayList<newsModel>()
        //get general news for homepage
        newsApiRepository.getTopHeadlines(category = Category.SCIENCE, country = Country.GB, q = "", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({ article -> Log.d("getTopHead Science article", article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt)

                //if here since last updated time add to top_headline json

                //read last updated time
                val docRef = db.collection("last_updated").document("general_last_updated")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
//                            upload new articles only
                            val today = Calendar.getInstance()
                            today.add(Calendar.HOUR, -1);
                            val sendDateUAT =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(today.time)
                            Log.d("TIME: Refresh articles from", sendDateUAT)
                            //get only most recent articles
                            if (article.publishedAt > sendDateUAT) {
                                val headline = hashMapOf(
                                    "headline" to article.title,
                                    "image" to article.urlToImage,
                                    "author" to article.author,
                                    "timestamp" to article.publishedAt,
                                    "comments" to arrayListOf<String>()
                                )

                                //add new documents to firebase
                                db.collection("science_headlines").document()
                                    .set(headline)
                                    .addOnSuccessListener {
                                        Log.d(
                                            "Science Fragment",
                                            "DocumentSnapshot successfully written!"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            "Science Fragment",
                                            "Error writing document",
                                            e
                                        )
                                    }
//                              //if here since last updated time add to top_headline json
                            }//else document is too old so don't add
                        } else {
                            Log.d("GENERAL_LAST_UPDATED", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("TAG", "get failed with ", exception)
                    }

            },
                { t -> Log.d("getScienceHeadlines error", t.message!!) })

        return headlineArrayList

    }

    private fun populateList(db: FirebaseFirestore, callback: (ArrayList<newsModel>) -> Unit) {
        val list = ArrayList<newsModel>()
        db.collection("science_headlines").get()
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

}