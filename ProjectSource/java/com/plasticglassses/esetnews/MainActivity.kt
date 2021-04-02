package com.plasticglassses.esetnews

//import com.koushikdutta.ion.Ion
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.dfl.newsapi.NewsApiRepository
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.TabAdapter

/**
 * hosts headline fragments, home, science, tech
 */
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
        TabLayoutMediator(
            tabLayout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = tabSections[0]
                    1 -> tab.text = tabSections[1]
                    2 -> tab.text = tabSections[2]
                    3 -> tab.text = tabSections[3]

                }
            }).attach()

//        val alertArray = intent.getStringExtra("newAlertArray")
//        Log.d("Alarm request successful", alertArray.toString())
    }

    override fun onResume() {
        super.onResume()
        alarmFunctions()
    }

    /**
     * check for articles with alarm key words
     */
    fun alarmFunctions() {
        val db = Firebase.firestore

//                val fm: FragmentManager = supportFragmentManager
//
//                val fragment: HomeFragment = fm.findFragmentById(R.id.homeFragment) as HomeFragment
        val fragment = HomeFragment()
        (fragment as HomeFragment).generateAlertNews(db)
//                fragment.generateAlertNews(db)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //appbar
        menuInflater.inflate((R.menu.news_toolbar_layout), menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //appbar
        val myView = findViewById<View>(R.id.news_toolbar)
        when (item!!.itemId) {
            R.id.profile -> {
                val snackbar =
                    Snackbar.make(myView, getString(R.string.profile), Snackbar.LENGTH_SHORT)
                snackbar.show()
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                val snackbar =
                    Snackbar.make(myView, getString(R.string.log_out), Snackbar.LENGTH_SHORT)
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
}
