package com.example.myapplication.ui.login

import android.os.Bundle
import com.example.myapplication.data.model.User
import com.example.myapplication.data.remote.FirebaseHandler

class LoginPresenter(view: LoginContract.View, firebaseHandler: FirebaseHandler) : LoginContract.Presenter{

    private var view: LoginContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler

    override fun handleLoginRequest() {
        view?.startSignIn()
    }

    override fun handleLoginSuccess(user: User) {
        view?.loginSuccess()
        firebaseHandler?.saveUserData(user)
    }

    override fun handleLoginFailure(statusCode: Int, message: String) {
        view?.loginFailure(1, "Sorry, there's problem with login.")
    }

    override fun onStart(extras: Bundle?) {
        // Do nothing on start
    }

    override fun onDestroy() {
        this.view = null
    }


}