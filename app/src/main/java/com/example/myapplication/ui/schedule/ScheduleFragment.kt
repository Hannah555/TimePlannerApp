package com.example.myapplication.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.remote.FirebaseHandlerImpl
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
class ScheduleFragment(date: String) : Fragment(), ScheduleContract.View {

    private lateinit var presenter: ScheduleContract.Presenter
    var thisdate = date
    private lateinit var timetable: TimetableView
    val schedules = ArrayList<Schedule>()

//    companion object{
//        private lateinit var timetable: TimetableView
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_schedule, container, false)

        timetable = v.findViewById(R.id.timetableview)
        Log.i("SEQUENCE", "1")

//        Set Presenter
//        setPresenter(SchedulePresenter(ScheduleFragment(thisdate), FirebaseHandlerImpl()))
        setPresenter(SchedulePresenter(this, FirebaseHandlerImpl()))
        Log.i("SEQUENCE", "2")
//        call presenter
//        timetable.removeAll()
        presenter.scheduleHandler(thisdate)

        timetable.setOnStickerSelectEventListener { idx, schedules ->
            Log.i("idx", idx.toString())
        }



//        val slist = testing()
//        timetable.add(slist)

        return v
    }


    override fun showSchedule(
        title: String?,
        startHour: Int?,
        startMinute: Int?,
        endHour: Int?,
        endMinute: Int?
    ): ArrayList<Schedule> {

        val schedule = Schedule()
        schedule.classTitle = title

        schedule.startTime = Time(startHour!!, startMinute!!)

        schedule.endTime = Time(endHour!!, endMinute!!)

        schedules.add(schedule)

        Log.i("show schdule", "runnign here!!!")
        Log.i("hour", startHour.toString() + " " + startMinute.toString())
        Log.i("minute", endHour.toString() + " " + endMinute.toString())
        Log.i("schedule", schedules.toString())

        return schedules
    }

    override fun getSchedule(schedule: ArrayList<Schedule>?) {
        Log.i("SEQUENCE", "5")
        if(schedule != null) {
            Log.i("schedule in method", schedule.toString())
            timetable.add(schedule)
        }
    }


    override fun setPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }


    fun test(){
        val schedule = Schedule()
        schedule.classTitle = "this"

        schedule.startTime = Time(23, 23)

        schedule.endTime = Time(25, 25)

        schedules.add(schedule)

        Log.i("sample schedule", schedules.toString())
    }

    fun testing(): ArrayList<Schedule>?{
        val list = presenter.scheduleHandler(thisdate)
            //test()
        //timetable.add(schedules)
        return list
    }



}


