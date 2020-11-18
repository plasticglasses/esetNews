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
import com.plasticglassses.esetnews.adapters.NewsAdapter
import io.reactivex.schedulers.Schedulers
import java.io.File

//import com.dfl.newsapi.NewsApiRepository
//import com.dfl.newsapi.enums.Category
//import com.dfl.newsapi.enums.Country
//import com.koushikdutta.ion.Ion
//import io.reactivex.schedulers.Schedulers
//import org.json.JSONObject


class HomeFragment : Fragment() {

    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var rootView = inflater.inflate(R.layout.fragment_home, container, false)

        //generateGeneralNews()
//instead of using json ue firestore and create a collection, try this out first using alerts
//        val fileName = "top-headlines.json"
//        val path = main.filesDir.absolutePath
//        var lines = File("C:\\Users\\lizks\\AndroidStudioProjects\\esetNews2\\app\\src\\main\\java\\com\\plasticglassses\\esetnews\\top-headlines.json").readLines()
////        lines.forEach{line -> Log.d("top-headlines json file", line)}


        //setContentView(R.layout.fragment_home)
        var headlineArrayList = ArrayList<newsModel>()
        //get news

        headlineArrayList = populateList()
        //get text box on science fragment and add in a new headline

        Log.d("mylist", headlineArrayList.toString())

        //home fragment recycler view
        //val headlineArrayList = populateList(myHeadlinelist, myImglist, myAuthorlist, myTimestamplist)

        val recyclerHeadlineView = rootView.findViewById<View>(R.id.headline_recycler_view) as RecyclerView
        //val headlineArrayList = genNews(recyclerHeadlineView)
        val headlineLayoutManager = LinearLayoutManager(activity)
        recyclerHeadlineView.layoutManager = headlineLayoutManager
        val headlineAdapter = NewsAdapter(headlineArrayList)
        recyclerHeadlineView.adapter = headlineAdapter

        return rootView
    }

    private fun generateGeneralNews(): ArrayList<newsModel> {
        val headlineArrayList = ArrayList<newsModel>()
        //get general news for homepage
        newsApiRepository.getTopHeadlines(category = Category.GENERAL, country = Country.GB, q = "", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({ article -> Log.d("getTopHead General article", article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt)

                //if here since last updated time add to top_headline json

            },
                { t -> Log.d("getTopHeadlines error", t.message!!) })

        return headlineArrayList

    }

    private fun populateList(): ArrayList<newsModel> {
        val list = ArrayList<newsModel>()
        val myHeadlineList = arrayOf("Liz", "name1", "example2")
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



//    private fun populateList(myHeadlineList1: MutableList<String>, myHeadlineImgList1: MutableList<String>, myPublishersList1: MutableList<String>, myTimestampList1: MutableList<String>): MutableList<newsModel> {
//        val list = ArrayList<newsModel>()
//        val myHeadlineImgList = myHeadlineImgList1
//        val myHeadlineList = myHeadlineList1
//        val myPublishersList = myPublishersList1
//        val myTimestampList = myTimestampList1
//        var count = 0
//        for (headline in myHeadlineList){
//            count = count +1
//        }
//
//        for (i in 0..count-1){
//            val thisModel = newsModel()
//            thisModel.setHeadline(myHeadlineList[i].toString())
//            thisModel.setHeadlineImg(myHeadlineImgList[i].toString())
//            thisModel.setTimestamp(myTimestampList[i].toString())
//            thisModel.setPublisher(myPublishersList[i].toString())
//            list.add(thisModel)
//        }
//        return list
//    }

}