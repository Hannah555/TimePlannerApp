package com.example.myapplication.ui.history

import com.example.myapplication.data.model.History
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface HistoryContract {
    interface View: BaseView<Presenter> {
        fun showHistory(historyList: ArrayList<History>)
    }

    interface Presenter: BasePresenter {
        fun getHistory()
    }
}