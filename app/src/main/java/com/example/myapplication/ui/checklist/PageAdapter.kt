package com.example.myapplication.ui.checklist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.myapplication.ui.schedule.ScheduleFragment
@Suppress("DEPRECATION")
class PageAdapter(fm: FragmentManager, val behavior: Int) : FragmentStatePagerAdapter(fm) {


    override fun getCount(): Int = behavior

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> {
                ChecklistFragment()
            }
            1 -> {
                ScheduleFragment()
            }
            else -> getItem(position)
        }
    }
}