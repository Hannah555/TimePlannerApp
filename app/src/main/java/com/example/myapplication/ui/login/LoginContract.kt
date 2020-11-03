package com.example.myapplication.ui.login

import com.example.myapplication.data.model.User
import com.example.myapplication.ui.BasePresenter
import com.example.myapplication.ui.BaseView

interface LoginContract {

    interface View : BaseView<Presenter>{

        fun loginSuccess()

        fun loginFailure(statusCode:Int, message:String)

        fun startSignIn()

        fun navigateToHome()
    }

    interface Presenter : BasePresenter {

        fun handleLoginRequest()

        fun handleLoginSuccess(user: User)

        fun handleLoginFailure(statusCode: Int, message: String)
    }
}


