package com.plasticglassses.esetnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dfl.newsapi.NewsApiRepository
import com.dfl.newsapi.enums.Category
import com.dfl.newsapi.enums.Country
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
//import com.koushikdutta.ion.Ion
import com.plasticglassses.esetnews.adapters.TabAdapter
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject


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
    fun genNews(view: View) {

       val headlineTextBox = findViewById<TextView>(R.id.scienceHeadlineTextBox)

//        // Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(this)
        val url = "http://newsapi.org/v2/everything?q=bitcoin&from=2020-10-12&sortBy=publishedAt&apiKey=8abf9b3bbc4c4e86b186100f1c3f4e6d"
//
//        // Request a string response from the provided URL.
//        val stringRequest = StringRequest(Request.Method.GET, url,
//            Response.Listener<String> { response ->
//                // Display the first 500 characters of the response string.
//                textView.text = "Response is: ${response.substring(0, 500)}"
//            },
//            Response.ErrorListener { textView.text = "That didn't work!" })
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest)
//
//        val url2 = "http://newsapi.org/v2/everything?q=bitcoin&from=2020-10-15&sortBy=publishedAt&apiKey=8abf9b3bbc4c4e86b186100f1c3f4e6d"
//
//        val stringRequest2 = StringRequest(Request.Method.GET, url2,
//            Response.Listener<String> { response ->
//                // Display the first 500 characters of the response string.
//                textView.text = "Response is: ${response.substring(0, 500)}"
//            },
//            Response.ErrorListener { textView.text = "That didn't work!" })
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest2)
//
//        val jsonObjectRequest = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            Response.Listener { response ->
//                textView.text = "Response: %s".format(response.toString())
//            },
//            Response.ErrorListener { error ->
//                // TODO: Handle error
//            }
//        )


        //get text box on science fragment and add in a new headline
        newsApiRepository.getTopHeadlines(category = Category.GENERAL, country = Country.US, q = "trump", pageSize = 20, page = 1)
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .flatMapIterable { articles -> articles.articles }
            .subscribe({ article -> Log.d("getTopHead CC article", article.title)
                headlineTextBox.text = article.title
            },
                { t -> Log.d("getTopHeadlines error", t.message!!) })



    }

//    fun getHeadline(view: View){
//        Ion.with(this)
//            .load("GET", "https://newsapi.org/v2/top-headlines?country=us&apiKey=8abf9b3bbc4c4e86b186100f1c3f4e6d")
//            .asString()
//            .setCallback{ex, result -> processHeadlines(result)}
//    }


//    fun processHeadlines(headlineData: String) {
//        val myJson = JSONObject(headlineData)
//        val myHeadline = myJson.getString("status")
//
//        val myTxtVew = findViewById<TextView>(R.id.txtHeadline)
//        myTxtVew.text = myHeadline
//    }
}