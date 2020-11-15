package com.example.myapplication.ui.checklist

import com.example.myapplication.data.model.Item
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface ListContract {

    interface View: BaseView<Presenter> {
        fun onConfirmAddList()
        fun onCancelList()
    }

    interface Presenter: BasePresenter {
        fun getUserList(item: Item)
    }
}