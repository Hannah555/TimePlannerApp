package com.example.myapplication.ui.checklist

import android.os.Bundle
import com.example.myapplication.data.model.Item
import com.example.myapplication.data.remote.FirebaseHandler

class ListPresenter(view: ListContract.View, firebaseHandler: FirebaseHandler): ListContract.Presenter {

    private var view: ListContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler

    override fun getUserList(item: Item) {
        TODO("Not yet implemented")
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}