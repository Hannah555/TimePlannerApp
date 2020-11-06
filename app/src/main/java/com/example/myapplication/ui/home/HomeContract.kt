package com.example.myapplication.ui.home

import android.net.Uri
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface HomeContract {

    interface View: BaseView<Presenter>{
        fun signOut()
        fun userInfo(name: String, photoUri: Uri)
    }

    interface Presenter: BasePresenter{
        fun handleUserInfo(currentUser: String)
        fun passName(name: String, photoUri: Uri)
    }
}