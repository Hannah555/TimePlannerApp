package com.example.myapplication.ui.addList

import android.content.Context
import com.example.myapplication.data.model.Task
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface AddListContract {

    interface View: BaseView<Presenter>{
        fun getJsonData(jsonData: String)
    }

    interface Presenter: BasePresenter{
        fun extractActivity(result: String, context: Context)

        fun handleActivitySuccess(task: Task)
    }

}