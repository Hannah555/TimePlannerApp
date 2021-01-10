package com.example.myapplication.ui.analysis

import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

interface AnalysisContract {

    interface View: BaseView<Presenter> {
        fun getDoneData(dataList: ArrayList<String>, barList: ArrayList<BarEntry>, lineList: ArrayList<Entry>)
    }

    interface Presenter: BasePresenter {
        fun showDoneData()
    }
}