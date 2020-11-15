package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.ui.checklist.PageAdapter
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.google.android.material.tabs.TabLayout
import org.joda.time.DateTime


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), DatePickerListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        val tabLayout:TabLayout = v.findViewById(R.id.tablayout)
        val viewPager: ViewPager = v.findViewById(R.id.viewpager)
        val picker: HorizontalPicker = v.findViewById(R.id.datePicker)

        tabLayout.addTab(tabLayout.newTab().setText("Checklist"))
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = PageAdapter(childFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
        })

        picker.setListener(this).init()



        return v
    }



    override fun onDateSelected(dateSelected: DateTime?) {
        Toast.makeText(context, "selected date: " + dateSelected.toString(), Toast.LENGTH_SHORT).show()
    }

}