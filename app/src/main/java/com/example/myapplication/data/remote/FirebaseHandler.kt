package com.example.myapplication.data.remote

import android.net.Uri
import com.example.myapplication.data.model.User

interface FirebaseHandler {

    fun saveUserData(user: User)

    fun getName(tunnel: FirebaseTunnel,currentUser: String)

    interface FirebaseTunnel{
        fun onDataFetched(name: String, photo: Uri)
    }

}