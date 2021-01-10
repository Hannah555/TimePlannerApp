package com.example.myapplication.ui.history

import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.model.History
import com.example.myapplication.data.remote.FirebaseHandler
import com.google.firebase.firestore.QuerySnapshot

class HistoryPresenter(view: HistoryContract.View, firebaseHandler: FirebaseHandler): HistoryContract.Presenter {

    private var view: HistoryContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler
    private var dataList: ArrayList<History>? = ArrayList()

    override fun getHistory() {
        firebaseHandler?.getGraphData(object : FirebaseHandler.GraphDataRetriever{
            override fun onDataFetched(querySnapshot: QuerySnapshot?) {
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val history = document.toObject(History::class.java)

                        dataList?.add(History(history.date, history.title, history.time))
                    }

                    Log.i("history data", dataList.toString())
                    view?.showHistory(dataList!!)
                }
            }

        })
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}