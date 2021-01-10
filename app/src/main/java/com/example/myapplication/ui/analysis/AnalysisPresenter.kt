package com.example.myapplication.ui.analysis

import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.model.History
import com.example.myapplication.data.remote.FirebaseHandler
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.google.firebase.firestore.QuerySnapshot
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat


class AnalysisPresenter(view: AnalysisContract.View, firebaseHandler: FirebaseHandler): AnalysisContract.Presenter {

    private var view: AnalysisContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler
    private lateinit var label: ArrayList<String>
    private var mapDate: MutableMap<String, Int> = mutableMapOf()
    private var validDateList = ArrayList<String>()

    override fun showDoneData() {

        var i = 0
        var completed = 0

        firebaseHandler?.getGraphData(object : FirebaseHandler.GraphDataRetriever {
            private var dateList: ArrayList<String>? = ArrayList()
            private var barList: ArrayList<BarEntry>? = ArrayList()
            private var lineList: ArrayList<Entry>? = ArrayList()
            private var label: ArrayList<String>? = ArrayList()
            var i = 0
            override fun onDataFetched(querySnapshot: QuerySnapshot?) {
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val history = document.toObject(History::class.java)

                        dateList?.add(history.title!!)

                    }
                    val map = dateList?.groupingBy { it }?.eachCount()
                    Log.i("map", map.toString())

                    val endDate = DateTime.now().minusDays(1)
                    val startDate = endDate.minusDays(7)
//                    val endDate = Date()

                    iterateDates(startDate, endDate)

                    for (i in validDateList){
                        val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
                        val input = SimpleDateFormat("yyyy-MM-dd")
                        val startFormat = input.parse(i.toString())
                        val sdfStart = format.format(startFormat)

                        mapDate.put(sdfStart, 0)
                    }

                    val result = (map?.asSequence()?.plus(mapDate.asSequence()))?.distinct()
                        ?.groupBy ( { it.key }, {it.value})?.mapValues {
                            it.value.sum() }

                    Log.i("result map", result?.toSortedMap().toString())

                    if (result != null) {
                        var count = 0
                        for (i in result.toSortedMap()) {
                            Log.i("key", i.key)
                            Log.i("value", i.value.toString())
                            barList?.add(BarEntry(count.toFloat(), i.value.toFloat()))
                            lineList?.add(Entry(count.toFloat(), i.value.toFloat()))
                            label?.add(i.key.substring(0,5))
                            count++
                        }
                        view?.getDoneData(label!!,barList!!,lineList!!)
                    }

                }
            }

        })
    }

    fun iterateDates(start: DateTime, end: DateTime){

        val format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val inputStart = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val startFormat = inputStart.parse(start.toString())
        val endFormat = inputStart.parse(end.toString())
        val sdfStart = format.format(startFormat)
        val sdfEnd = format.format(endFormat)

        val formatter: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

        val begin:LocalDate = formatter.parseLocalDate(sdfStart.toString())
        val last:LocalDate = formatter.parseLocalDate(sdfEnd.toString())

        var date = begin
        while (date.isBefore(last))
        {
            date = date.plusDays(1)
            validDateList.add(date.toString())
        }
        Log.i("valid date", validDateList.toString())
    }



    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }


}
