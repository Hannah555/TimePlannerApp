package com.example.myapplication.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.model.Task
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_schedule, container, false)

        val timetable = v.findViewById<TimetableView>(R.id.timetableview)

        val schedules = ArrayList<Schedule>()
        val schedule = Schedule()
        schedule.classTitle = "Data Structure" // sets subject

        schedule.classPlace = "IT-601" // sets place

        schedule.professorName = "Won Kim" // sets professor

        schedule.startTime = Time(10, 0) // sets the beginning of class time (hour,minute)

        schedule.endTime = Time(13, 30) // sets the end of class time (hour,minute)

        schedules.add(schedule)

        val taskList = ArrayList<Task>()
        val user = Task()
//        user.endTime = Time(10, 0)
//        taskList.add()
//.. add one or more schedules
//.. add one or more schedules
        timetable.add(schedules)


        return v
    }




}


