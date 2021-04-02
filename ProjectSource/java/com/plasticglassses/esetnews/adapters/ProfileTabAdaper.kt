package com.plasticglassses.esetnews.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plasticglassses.esetnews.AlertsFragment
import com.plasticglassses.esetnews.CommentsFragment
import com.plasticglassses.esetnews.MarkersFragment

/**
 * switch the tabs on the profile activity
 */
class ProfileTabAdaper(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    /**
     * populate two tabs
     */
    override fun createFragment(position: Int): Fragment {
        //swipe function inflates fragment in view
        when (position) {
            0 -> return AlertsFragment()
//            1 -> return CommentsFragment()
            1 -> return MarkersFragment()
        }
        return AlertsFragment() //else return first in list
    }
}