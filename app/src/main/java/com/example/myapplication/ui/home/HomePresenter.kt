package com.example.myapplication.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.remote.FirebaseHandler

class HomePresenter(view: HomeContract.View, firebaseHandler: FirebaseHandler): HomeContract.Presenter {

    private var view: HomeContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler

    override fun handleUserInfo(currentUser: String) {
        firebaseHandler?.getName(object: FirebaseHandler.FirebaseTunnel{
            override fun onDataFetched(name: String, photo: Uri) {
                view?.userInfo(name, photo)
            }
        }, currentUser)
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        this.view = null
    }

    override fun passName(name: String, photo: Uri) {
        Log.i("This real name",name)
        Log.i("This is photo",photo.toString())
    }

}