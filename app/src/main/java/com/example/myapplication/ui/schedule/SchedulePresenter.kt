package com.example.myapplication.ui.schedule

import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.remote.FirebaseHandler
import com.github.tlaabs.timetableview.Schedule
import com.google.firebase.firestore.QuerySnapshot

class SchedulePresenter(view: ScheduleContract.View, firebaseHandler: FirebaseHandler): ScheduleContract.Presenter {

    private var view: ScheduleContract.View = view
    private var firebaseHandler: FirebaseHandler = firebaseHandler
    var startHour: Int? = 0
    var startMinute: Int? = 0
    var endHour: Int? = 0
    var endMinute: Int? = 0

    override fun scheduleHandler(date: String): ArrayList<Schedule>? {
        var list: ArrayList<Schedule>? = null
        Log.i("schedule presenter date", date)
        Log.i("SEQUENCE", "3")
        firebaseHandler.getScheduleTask(object : FirebaseHandler.ScheduleRetriever{
            override fun onDateFetched(querySnapshot: QuerySnapshot?, date: String) {
                Log.i("SEQUENCE", "4")
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val task = document.toObject(Task::class.java)
//                        Log.i("task info", currentDocument.task.toString())
//                        Log.i("date info", currentDocument.date.toString())
//                        Log.i("repeat info", currentDocument.repeat.toString())
//                        Log.i("done info", currentDocument.done.toString())
//                        Log.i("startTime info", currentDocument.startTime.toString())
//                        Log.i("endTime info", currentDocument.endTime.toString())
                        if(task.date == date){
                            val startTime = task.startTime
                            val endTime = task.endTime
                            val title = task.task

                            val startPart = startTime?.split(" ", ":")
                            val endPart = endTime?.split(" ", ":")

                            // For start hour
                            if(startPart?.get(0) == "am"){
                                startHour = startPart.get(1).toInt() + 12
                                if(startHour!! == 24){
                                    startHour = 0
                                }
                            }else{
                                startHour = startPart?.get(1)?.toInt()
                            }

                            startMinute = startPart?.get(2)?.toInt()

                            // For end hour
                            if(endPart?.get(0) == "am"){
                                endHour = endPart.get(1).toInt() + 12
                                if(endHour!! == 24){
                                    endHour = 0
                                }
                            }else{
                                endHour = endPart?.get(1)?.toInt()
                            }

                            endMinute = endPart?.get(2)?.toInt()

                            list = view.showSchedule(title, startHour, startMinute, endHour, endMinute)
                        }
                    }
                    view.getSchedule(list)
                }
            }
        }, date)
        Log.i("schedule in presenter", list.toString())
        return list
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }


}