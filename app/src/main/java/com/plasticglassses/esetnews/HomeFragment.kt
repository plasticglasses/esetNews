package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dfl.newsapi.NewsApiRepository

//import com.dfl.newsapi.NewsApiRepository
//import com.dfl.newsapi.enums.Category
//import com.dfl.newsapi.enums.Country
//import com.koushikdutta.ion.Ion
//import io.reactivex.schedulers.Schedulers
//import org.json.JSONObject


class HomeFragment : Fragment() {

    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }




//    fun getHeadlines(view: View){
//        val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")
//
//        newsApiRepository.getTopHeadlines(category = Category.GENERAL, country = Country.US, q = "trump", pageSize = 20, page = 1)
//            .subscribeOn(Schedulers.io())
//            .toFlowable()
//            .flatMapIterable { articles -> articles.articles }
//            .subscribe({ article -> Log.d("getTopHead CC article", article.title) },
//                { t -> Log.d("getTopHeadlines error", t.message.toString()) })
//    }


}