package com.example.myapplication.ui.schedule

import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.github.tlaabs.timetableview.Schedule

interface ScheduleContract {

    interface View: BaseView<Presenter>{
        fun showSchedule(title: String?, startHour: Int?, startMinute: Int?, endHour: Int?, endMinute: Int?): ArrayList<Schedule>
        fun getSchedule(schedule: ArrayList<Schedule>?)
    }

    interface Presenter: BasePresenter{
        fun scheduleHandler(date: String): ArrayList<Schedule>?
    }
}