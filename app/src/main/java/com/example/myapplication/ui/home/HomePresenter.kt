package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.remote.FirebaseHandler

class HomePresenter(view: HomeContract.View, firebaseHandler: FirebaseHandler): HomeContract.Presenter {

    private var view: HomeContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler

    override fun handleUserInfo(currentUser: String) {
        val name = firebaseHandler?.getName(currentUser)
        val photoUri = firebaseHandler?.getPhotoUri(currentUser)
//        if (name != null && photoUri != null) {
//            view?.userInfo(name, photoUri)
//        }

        Log.i("home_name", name.toString())
        Log.i("home_photoUri", photoUri.toString())
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        this.view = null
    }


}