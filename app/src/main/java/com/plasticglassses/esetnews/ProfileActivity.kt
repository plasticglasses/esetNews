package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.plasticglassses.esetnews.adapters.ProfileTabAdaper

class ProfileActivity : AppCompatActivity() {

    private val TAG = "Profile Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(findViewById(R.id.toolbar))

        //back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //profile tabs
        val tabLayout = findViewById<TabLayout>(R.id.profile_tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.profile_pager)
        val tabProfileTitles = resources.getStringArray(R.array.tabProfileTitles) //in strings file

        //bind adapter and fragments to activity
        viewPager.adapter = ProfileTabAdaper(this) //populate the activity
        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy{tab, position ->
            when(position){
                0 -> tab.text = tabProfileTitles[0] //alerts
                1 -> tab.text = tabProfileTitles[1] //comments
            }
        }).attach()

        //firestore
        val db = Firebase.firestore

        // Create a new user with a first and last name
//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )

// Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }

    }
}