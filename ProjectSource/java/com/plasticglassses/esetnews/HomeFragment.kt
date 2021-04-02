package com.plasticglassses.esetnews

import android.annotation.SuppressLint
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.NewsAdapter
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * populate general news, on activity main under home tab
 */
class HomeFragment : Fragment() {

    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth
        var user = auth.currentUser

        val db = Firebase.firestore
        var rootView = inflater.inflate(R.layout.fragment_home, container, false)


        //check alert articles before resetting last updated
        generateAlertNews(db)

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



        return rootView
    }

    /**
     * get the current users alerts and find news api articles about them
     */
    public fun generateAlertNews(db: FirebaseFirestore) {

        getFirestoreID(getUserID(), db) { result ->
            getUsersAlerts(result, db) { alertArray ->
                getLastUpdated(db) { date ->
                    var javaDate = date.toDate()
                    val formattedDate =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(javaDate)

                    val now = Date()
                    val formattedNow =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(now)
                    for (alert in alertArray) {
                        Log.d("Finding user specific alerts", alertArray.toString())
                        newsApiRepository.getTopHeadlines(category = Category.GENERAL, country = Country.GB, q = alert, pageSize = 20, page = 1)
                            .subscribeOn(Schedulers.io())
                            .toFlowable()
                            .flatMapIterable { articles -> articles.articles }
                            .subscribe({ article -> Log.d("ALERTS", article.title)

//                            upload new articles only
                                if (article.publishedAt > formattedDate) {
//
                                    //print out new articles
//                                    Log.d(
//                                        "ALERTS",
//                                        article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt
//                                    )
                                    val headline = hashMapOf(
                                        "headline" to article.title,
                                        "image" to article.urlToImage,
                                        "author" to article.author,
                                        "timestamp" to article.publishedAt,
                                        "article" to article.url,
                                        "comments" to ""
                                    )

                                    //sound a notification
                                    Log.d(
                                        "PLAYING ALERT",
                                        "starting"
                                    )
                                    MyService().playAlert()

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
                                                e)
                                        }
                                }


                            },
                                { t -> Log.d("getEverything error", t.message.toString()) })

                    }
                }
            }
        }
    }

    /**
     *
     * get teh latest  news api article headlines
     */
    @SuppressLint("CheckResult")
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

                //read last updated time
                val docRef = db.collection("last_updated").document("general_last_updated")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
//                            upload new articles only
                            //get last updated date from firebase, format it into ISO format and compare to the pblished at time
                            getLastUpdated(db) { result ->
                                val javaDate = result.toDate()
                                val formattedDate =
                                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(javaDate)

                                if (article.publishedAt > formattedDate) {
//
                                    //print out new articles
                                    Log.d(
                                        "getTopHead General article",
                                        article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt
                                    )
                                    //add to headline model array so recycler view can be populated
                                    val headline = hashMapOf(
                                        "headline" to article.title,
                                        "image" to article.urlToImage,
                                        "author" to article.author,
                                        "timestamp" to article.publishedAt,
                                        "article" to article.url,
                                        "comments" to arrayListOf<String>(
                                            arrayListOf<String>("user").toString(),
                                            arrayListOf<String>("comment").toString(),
                                            arrayListOf<String>("timestamp").toString()
                                        )
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
                            updateLastUpdated(db) {}
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


    /**
    take firebase docuemnts and add them to list for processing by recycler view
     */
    private fun populateList(db: FirebaseFirestore, callback: (ArrayList<newsModel>) -> Unit) {
        val list = ArrayList<newsModel>()
        var headlineRef =
            db.collection("general_headlines").orderBy("timestamp", Query.Direction.DESCENDING)
        headlineRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                if (document != null) {

                    //make model with data from firestore
                    val thisModel = newsModel()
                    thisModel.setHeadline(document.get("headline").toString())
                    thisModel.setHeadlineImg(document.get("image").toString())
                    thisModel.setTimestamp(document.get("timestamp").toString())
                    thisModel.setPublisher(document.get("author").toString())
                    thisModel.setFirebaseDocID(document.id)

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

    /**
    update firebase last updated counter
     */
    fun updateLastUpdated(db: FirebaseFirestore, callback: (Timestamp) -> Unit) {
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

    /**
    callback the fiestore counter of date last checked
     */
    fun getLastUpdated(db: FirebaseFirestore, callback: (Timestamp) -> Unit) {
        //get the general-headline docuemnts
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

    /**
     * get the current user that is logged in
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
        return thisUid.toString()
    }

    /**
  Get the id of the document which has the same authUserID sa the surrent loged in user
   */
    private fun getFirestoreID(
        uID: String,
        db: FirebaseFirestore,
        callback: (String) -> Unit
    ): String {
//        val db = Firebase.firestore
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

    /**
     get the current users alert array from firetore
    */
    private fun getUsersAlerts(
        docID: String?,
        db: FirebaseFirestore,
        callback: (ArrayList<String>) -> Unit
    ): ArrayList<String>? {
        var usersAlerts = arrayListOf<String>()
//        val db = Firebase.firestore
        db.collection("users").document(docID!!)
            .get()
            .addOnSuccessListener { result ->
                //val username = result.getString("username")
                usersAlerts = (result["alerts"] as ArrayList<String>)
                //populate alerts fragment with chips of alerts
                Log.d("Home Frag", ("${result.id} => ${result.data} " + usersAlerts))
                callback.invoke(usersAlerts)
            }
        return usersAlerts
    }


}
