package com.plasticglassses.esetnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.plasticglassses.esetnews.adapters.NewsAdapter
//import com.koushikdutta.ion.Ion
import com.plasticglassses.esetnews.adapters.TabAdapter
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.card_headline.*


class MainActivity : AppCompatActivity() {

    val newsApiRepository = NewsApiRepository("8abf9b3bbc4c4e86b186100f1c3f4e6d")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        appbar
        val mNewsToolbar = findViewById<Toolbar>(R.id.news_toolbar)
        setSupportActionBar(mNewsToolbar)

        //news tab bar
        val tabLayout = findViewById<TabLayout>(R.id.news_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.news_pager)
        val tabSections = resources.getStringArray(R.array.tabSections)

        viewPager.adapter = TabAdapter(this)
        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy{tab, position ->
            when(position){
                0 -> tab.text = tabSections[0]
                1 -> tab.text = tabSections[1]
                2 -> tab.text = tabSections[2]
                3 -> tab.text = tabSections[3]

            }
        }).attach()


        setContentView(R.layout.fragment_home)
        //home fragment recycler view
        val headlineArrayList = populateList()

        val recyclerHeadlineView = findViewById<View>(R.id.headline_recycler_view) as RecyclerView
        //val headlineArrayList = genNews(recyclerHeadlineView)
        val headlineLayoutManager = LinearLayoutManager(this)
        recyclerHeadlineView.layoutManager = headlineLayoutManager
        val headlineAdapter = NewsAdapter(headlineArrayList)
        recyclerHeadlineView.adapter = headlineAdapter


    }

    private fun populateList(): MutableList<newsModel> {
        val list = ArrayList<newsModel>()
        val myHeadlineImgList = arrayOf("Liz", "name", "example")
        val myHeadlineList = arrayOf("Liz", "name1", "example2")
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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //appbar
        menuInflater.inflate((R.menu.news_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //appbar
        val myView = findViewById<View>(R.id.news_toolbar)
        when(item!!.itemId){
            R.id.profile -> {
                val snackbar = Snackbar.make(myView, getString(R.string.profile), Snackbar.LENGTH_SHORT)
                snackbar.show()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_link -> {
                val snackbar = Snackbar.make(myView, getString(R.string.linked), Snackbar.LENGTH_SHORT)
                snackbar.show()
                return true
            }
            R.id.action_logout -> {
                val snackbar = Snackbar.make(myView, getString(R.string.log_out), Snackbar.LENGTH_SHORT)
                snackbar.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun logout(item: MenuItem) {

        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /*
    on science frgament
     */
//    fun genNews(view: View): MutableList<newsModel> {
//        val list: MutableList<newsModel> = ArrayList()
//
//        val headlineTextBox = findViewById<TextView>(R.id.scienceHeadlineTextBox)
//
//        //get text box on science fragment and add in a new headline
//        newsApiRepository.getTopHeadlines(category = Category.GENERAL, country = Country.US, q = "trump", pageSize = 20, page = 1)
//            .subscribeOn(Schedulers.io())
//            .toFlowable()
//            .flatMapIterable { articles -> articles.articles }
//            .subscribe({ article -> Log.d("getTopHead CC article", article.title + " " + article.urlToImage + " " + article.author + " " + article.publishedAt)
//
//                val newHeadline = newsModel()
//                list.add(newHeadline)
//
//                headlineTextBox.text = article.title +  article.urlToImage + article.author + article.publishedAt
//
//            },
//                { t -> Log.d("getTopHeadlines error", t.message!!) })
//        val newHeadline = newsModel("test", "test","test", "test")
//        list.add(newHeadline)
//        Log.d("list", list.toString());
//        Log.d("listing eadline", "headline" + list[0].getHeadline());
//
//        return list
//    }
}