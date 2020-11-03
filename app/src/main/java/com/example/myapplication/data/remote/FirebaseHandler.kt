package com.example.myapplication.data.remote

import com.example.myapplication.data.model.User

interface FirebaseHandler {

    fun saveUserData(user: User)

    fun getName(currentUser: String)

    fun getPhotoUri(currentUser: String)
}