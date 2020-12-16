package com.example.myapplication.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment() : Fragment(), ScheduleContract.View, DatePickerListener {

    private lateinit var presenter: ScheduleContract.Presenter
    private lateinit var timetable: TimetableView
    val schedules = ArrayList<Schedule>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_schedule, container, false)
        val picker: HorizontalPicker = v.findViewById(R.id.datePicker)

        picker.setListener(this).init()
        picker.setDate(DateTime())

        timetable = v.findViewById(R.id.timetableview)
        Log.i("SEQUENCE", "1")

//        Set Presenter
//        setPresenter(SchedulePresenter(ScheduleFragment(thisdate), FirebaseHandlerImpl()))
        setPresenter(SchedulePresenter(this, FirebaseHandlerImpl()))
        Log.i("SEQUENCE", "2")
//        call presenter
//        timetable.removeAll()
        val c: Date = Calendar.getInstance().getTime()

        val df = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)
        presenter.scheduleHandler(formattedDate)

        timetable.setOnStickerSelectEventListener { idx, schedules ->
            Log.i("idx", idx.toString())
            Log.i("Timetable sche", schedules.toString())
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


    override fun onDateSelected(dateSelected: DateTime?) {
        //2020-11-24T00:00:00.000+08:00
        val dateString = dateSelected?.toString()
        val separate2 = dateString?.split("-", "T")?.map { it.trim() }
        val date = separate2?.get(2) + "/" + separate2?.get(1) + "/" + separate2?.get(0)
        Log.i("date", date)
        timetable.removeAll()
        schedules.clear()
        presenter.scheduleHandler(date)
    }

}


