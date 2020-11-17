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

class ScienceFragment : Fragment() {
    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_science, container, false)


        generateScienceNews()


        //setContentView(R.layout.fragment_home)
        var headlineArrayList = ArrayList<newsModel>()
        //get news

        headlineArrayList = populateList()
        //get text box on science fragment and add in a new headline

        Log.d("mylist", headlineArrayList.toString())

        //home fragment recycler view
        //val headlineArrayList = populateList(myHeadlinelist, myImglist, myAuthorlist, myTimestamplist)

        val recyclerHeadlineView = rootView.findViewById<View>(R.id.headline_recycler_view_science) as RecyclerView
        //val headlineArrayList = genNews(recyclerHeadlineView)
        val headlineLayoutManager = LinearLayoutManager(activity)
        recyclerHeadlineView.layoutManager = headlineLayoutManager
        val headlineAdapter = NewsAdapter(headlineArrayList)
        recyclerHeadlineView.adapter = headlineAdapter

        return rootView

    }

    private fun generateScienceNews(): ArrayList<newsModel> {
        val headlineArrayList = ArrayList<newsModel>()
        //get general news for homepage
        newsApiRepository.getTopHeadlines(category = Category.SCIENCE, country = Country.GB, q = "", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({ article -> Log.d("getTopHead Science article", article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt)

                //if here since last updated time add to top_headline json

            },
                { t -> Log.d("getTopHeadlines error", t.message!!) })

        return headlineArrayList

    }

    private fun populateList(): ArrayList<newsModel> {
        val list = ArrayList<newsModel>()
        val myHeadlineList = arrayOf("Liz324", "name1234324", "example234")
        val myHeadlineImgList = arrayOf("Liz", "name234", "example234")
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

}