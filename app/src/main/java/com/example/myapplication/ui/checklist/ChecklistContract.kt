package com.example.myapplication.ui.checklist

import com.example.myapplication.data.model.History
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface ChecklistContract {
    interface View:BaseView<Presenter>{
        fun addDoneTask(history: History)
    }

    interface Presenter: BasePresenter {
        fun handleDoneTask(history: History)
    }
}