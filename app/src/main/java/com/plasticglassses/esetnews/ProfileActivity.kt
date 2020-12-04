package com.plasticglassses.esetnews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.ProfileTabAdaper

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "Profile Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))

        //back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //profile tabs
        val tabLayout = findViewById<TabLayout>(R.id.profile_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.profile_pager)
        val tabProfileTitles = resources.getStringArray(R.array.tabProfileTitles) //in strings file

        //bind adapter and fragments to activity
        viewPager.adapter = ProfileTabAdaper(this) //populate the activity
        TabLayoutMediator(
            tabLayout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = tabProfileTitles[0] //alerts
                    1 -> tab.text = tabProfileTitles[1] //comments
                }
            }).attach()

        //setup to use firebase
        auth = Firebase.auth
        val user = auth.currentUser
        val db = Firebase.firestore

        //get the users specific alerts from firesore and populate the view
        getAlerts()

        //sample service
        val mTextView = findViewById<TextView>(R.id.textView1)
        mTextView.text="Example of service"

    }

    //alert service
    fun startService(view: View){
        val serviceIntent = Intent(this, MyService::class.java)
        serviceIntent.putExtra("firstString", "Hello")
        serviceIntent.putExtra("secondWord", "world")
        startService(serviceIntent)
    }

    fun stopService(view:View){
        val serviceIntent = Intent(this, MyService::class.java)
        stopService(serviceIntent)
    }

    //return up the navigation tree when back button pressed
    override fun onSupportNavigateUp(): Boolean {
        finish()
        // or call onBackPressed()
        return true
    }

    private fun getAlerts() {

        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }

            }
    }

}




