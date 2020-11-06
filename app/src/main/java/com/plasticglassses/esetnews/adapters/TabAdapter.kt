package com.plasticglassses.esetnews.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plasticglassses.esetnews.HomeFragment
import com.plasticglassses.esetnews.LocalFragment
import com.plasticglassses.esetnews.ScienceFragment
import com.plasticglassses.esetnews.TechFragment

class TabAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity){
    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return HomeFragment()
            1 -> return TechFragment()
            2 -> return ScienceFragment()
            3 -> return LocalFragment()
        }
        return HomeFragment() //return first on default
    }

    override fun getItemCount(): Int {
        return 4//there are 4 tab sections
    }
}