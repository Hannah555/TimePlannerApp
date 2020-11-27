package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.ui.checklist.PageAdapter
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_home.*
import org.joda.time.DateTime


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), DatePickerListener{

    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout

    companion object{
        var dateSelected: DateTime? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        tabLayout = v.findViewById(R.id.tablayout)
        viewPager = v.findViewById(R.id.viewpager)
        val picker: HorizontalPicker = v.findViewById(R.id.datePicker)

        tabLayout.addTab(tabLayout.newTab().setText("Checklist"))
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL


        picker.setListener(this).init()
        picker.setDate(DateTime())

        return v
    }


    override fun onDateSelected(dateSelected: DateTime?) {
        //2020-11-24T00:00:00.000+08:00
        val dateString = dateSelected?.toString()
        val separate2 = dateString?.split("-", "T")?.map { it.trim() }
        val date = separate2?.get(2) + "/" + separate2?.get(1) + "/" + separate2?.get(0)

        getDate(date)
    }

    fun getDate(date:String){
        val adapter = PageAdapter(childFragmentManager, tabLayout.tabCount, date)
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

    }
}

