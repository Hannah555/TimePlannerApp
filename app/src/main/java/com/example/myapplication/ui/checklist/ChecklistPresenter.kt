package com.example.myapplication.ui.checklist

import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.model.History
import com.example.myapplication.data.remote.FirebaseHandler
import com.google.firebase.firestore.QuerySnapshot

class ChecklistPresenter(view:ChecklistContract.View, firebaseHandler: FirebaseHandler): ChecklistContract.Presenter {

    private var view: ChecklistContract.View = view
    private var firebaseHandler: FirebaseHandler = firebaseHandler

    override fun handleDoneTask(history: History) {
        var isExist = false

        firebaseHandler.getHistoryData(object: FirebaseHandler.HistoryRetriever{
            override fun onDataFetched(querySnapshot: QuerySnapshot?) {
                if (querySnapshot != null){
                    for (document in querySnapshot){
                        val getHistory = document.toObject(History::class.java)

                        if (getHistory.title == history.title && getHistory.date == history.date && getHistory.time == history.time){
                            isExist = true
                        }
                    }

                }
                if (isExist){
                    Log.i("exist", "yes")
                }else{
                    firebaseHandler.saveHistoryData(history)
                }
//                Log.i("exist", isExist.toString())
            }

        },history)

    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}