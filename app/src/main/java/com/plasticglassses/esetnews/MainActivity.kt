package com.plasticglassses.esetnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
//import com.koushikdutta.ion.Ion
import com.plasticglassses.esetnews.adapters.TabAdapter
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
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
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

//    fun getHeadline(view: View){
//        Ion.with(this)
//            .load("GET", "https://newsapi.org/v2/top-headlines?country=us&apiKey=8abf9b3bbc4c4e86b186100f1c3f4e6d")
//            .asString()
//            .setCallback{ex, result -> processHeadlines(result)}
//    }
//
//
//    fun processHeadlines(headlineData: String) {
//        val myJson = JSONObject(headlineData)
//        val myHeadline = myJson.getString("status")
//
//        val myTxtVew = findViewById<TextView>(R.id.txtHeadline)
//        myTxtVew.text = myHeadline
//    }
}