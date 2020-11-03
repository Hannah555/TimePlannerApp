package com.example.myapplication.ui

import android.app.Activity
import android.app.AlertDialog
import com.example.myapplication.R

class LoadingDialog {

    private lateinit var activity: Activity
    private lateinit var dialog: AlertDialog

    constructor(myActivity: Activity){
        activity = myActivity
    }

    fun startLoadingDialog(){
        val builder = AlertDialog.Builder(activity);
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_loading, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}

