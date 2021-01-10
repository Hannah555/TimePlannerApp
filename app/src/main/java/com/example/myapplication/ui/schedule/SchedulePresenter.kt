package com.example.myapplication.ui.schedule

import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.remote.FirebaseHandler
import com.github.tlaabs.timetableview.Schedule
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat

class SchedulePresenter(view: ScheduleContract.View, firebaseHandler: FirebaseHandler): ScheduleContract.Presenter {

    private var view: ScheduleContract.View = view
    private var firebaseHandler: FirebaseHandler = firebaseHandler
    var startHour: Int? = 0
    var startMinute: Int? = 0
    var endHour: Int? = 0
    var endMinute: Int? = 0

    override fun scheduleHandler(date: String): ArrayList<Schedule>? {
        var list: ArrayList<Schedule>? = null
        val documentIDList: ArrayList<String>? = ArrayList()
//        Log.i("schedule presenter date", date)
        firebaseHandler.getScheduleTask(object : FirebaseHandler.ScheduleRetriever{
            override fun onDateFetched(querySnapshot: QuerySnapshot?, date: String) {

                Log.i("onFetch", date)
                val format = SimpleDateFormat("dd/MM/yyyy")
                val sdf = format.parse(date)
                val weekFormat = SimpleDateFormat("EEEE")
                val week = weekFormat.format(sdf)

                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val task = document.toObject(Task::class.java)
//                        Log.i("task info", currentDocument.task.toString())
//                        Log.i("date info", currentDocument.date.toString())
//                        Log.i("repeat info", currentDocument.repeat.toString())
//                        Log.i("done info", currentDocument.done.toString())
//                        Log.i("startTime info", currentDocument.startTime.toString())
//                        Log.i("endTime info", currentDocument.endTime.toString())
                        if(task.date == date || task.date == "everyday" || task.date == week){
                            val startTime = task.startTime
                            val endTime = task.endTime
                            val title = task.task

                            val startPart = startTime?.split(" ", ":")
                            val endPart = endTime?.split(" ", ":")

                            // For end hour
                            endHour = if(endPart?.get(0) == "pm"  && endHour!! >= 1 && endHour!! <= 11){
                                Log.i("test", "run time")
                                endPart.get(1).toInt() + 12
                            }else{
                                endPart?.get(1)?.toInt()
                            }

                            startHour = startPart?.get(1)?.toInt()
                            if(startPart?.get(0) == "pm" && startHour!! < 12){
                                startHour = startPart?.get(1)?.toInt() + 12
                            }

                            startMinute = startPart?.get(2)?.toInt()

                            endMinute = endPart?.get(2)?.toInt()

                            Log.i("start part", startPart?.get(0).toString())
                            Log.i("end part", endPart?.get(0).toString())
                            Log.i("start hour", startHour.toString())

                            list = view.showSchedule(title, startHour, startMinute, endHour, endMinute)
                            documentIDList!!.add(document.id)
                        }
                    }
                    view.getSchedule(list, documentIDList)
                }
            }
        }, date)
        return list
    }

    override fun passDocumentID(docID: String) {
        firebaseHandler.deleteScheduleTask(docID)
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }


}