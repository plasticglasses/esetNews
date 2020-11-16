package com.plasticglassses.esetnews

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
        TabLayoutMediator(
            tabLayout,
            viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = tabProfileTitles[0] //alerts
                    1 -> tab.text = tabProfileTitles[1] //comments
                }
            }).attach()

        auth = Firebase.auth
        val user = auth.currentUser
        val db = Firebase.firestore

        getAlerts()


    }

    private fun getAlerts() {

        val db = Firebase.firestore

//        val user = FirebaseAuth.getInstance().currentUser

//        Log.d(TAG, user.toString())
//
//        user?.let {
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            val uid = user.uid
//            Log.d(TAG, uid)
//        }
//        Log.d(TAG, FirebaseAuth.getInstance().currentUser?.uid.toString())
//        FirebaseAuth.getInstance().currentUser?.uid


//.getCurrentUser()!!.getUid()
//        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser()!!.getUid())
//            .get()
//            .addOnSuccessListener { result ->
//                Log.d(TAG, "${result.id} => ${result.data}")
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }

//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
            }
    }
}




