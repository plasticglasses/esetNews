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
import com.plasticglassses.esetnews.adapters.NewsAdapter
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class TechFragment : Fragment() {
    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_tech, container, false)


        //generateTechNews()


        //setContentView(R.layout.fragment_home)
        var headlineArrayList = ArrayList<newsModel>()
        //get news

        headlineArrayList = populateList()
        //get text box on science fragment and add in a new headline

        Log.d("mylist", headlineArrayList.toString())

        //home fragment recycler view
        //val headlineArrayList = populateList(myHeadlinelist, myImglist, myAuthorlist, myTimestamplist)

        val recyclerHeadlineView = rootView.findViewById<View>(R.id.headline_recycler_view_tech) as RecyclerView
        //val headlineArrayList = genNews(recyclerHeadlineView)
        val headlineLayoutManager = LinearLayoutManager(activity)
        recyclerHeadlineView.layoutManager = headlineLayoutManager
        val headlineAdapter = NewsAdapter(headlineArrayList)
        recyclerHeadlineView.adapter = headlineAdapter

        return rootView
    }

    private fun generateTechNews(): ArrayList<newsModel> {
        val headlineArrayList = ArrayList<newsModel>()
        //get general news for homepage
        newsApiRepository.getTopHeadlines(category = Category.TECHNOLOGY, country = Country.GB, q = "", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({ article -> Log.d("getTopHead Technology article", article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt)

                //if here since last updated time add to top_headline json

            },
                { t -> Log.d("getTopHeadlines error", t.message!!) })

        return headlineArrayList

    }
    private fun populateList(): ArrayList<newsModel> {
        val list = ArrayList<newsModel>()
        val myHeadlineList = arrayOf("Liz3232", "name13232", "example22332")
        val myHeadlineImgList = arrayOf("Liz", "name", "example")
        val myPublishersList = arrayOf("ET", "ETt", "ETs")
        val myTimestampList = arrayOf("12:30", "12:30:59", "12:31")

        for (i in 0..2){
            val thisModel = newsModel()
            thisModel.setHeadline(myHeadlineList[i].toString())
            thisModel.setHeadlineImg(myHeadlineImgList[i].toString())
            thisModel.setTimestamp(myTimestampList[i].toString())
            thisModel.setPublisher(myPublishersList[i].toString())
            list.add(thisModel)
        }
        return list
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