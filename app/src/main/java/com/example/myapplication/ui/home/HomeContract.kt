package com.example.myapplication.ui.home

import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface HomeContract {

    interface View: BaseView<Presenter>{
        fun signOut()
        fun userInfo(name: String, photoUri: String)
    }

    interface Presenter: BasePresenter{
        fun handleUserInfo(currentUser: String)
    }
}