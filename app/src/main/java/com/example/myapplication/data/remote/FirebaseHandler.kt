package com.example.myapplication.data.remote

import android.net.Uri
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.model.User
import com.google.firebase.firestore.QuerySnapshot

interface FirebaseHandler {

    fun saveUserData(user: User)

    fun getName(tunnel: FirebaseTunnel,currentUser: String)

    fun saveTask(task: Task)

    fun getScheduleTask(retriever: ScheduleRetriever, date: String)

    fun deleteScheduleTask(docID: String)

    interface FirebaseTunnel{
        fun onDataFetched(name: String, photo: Uri)
    }

    interface ScheduleRetriever{
        fun onDateFetched(querySnapshot: QuerySnapshot?, date: String)
    }
}