package com.example.myapplication.data.model

import android.net.Uri

data class User(var uid: String) {

    var email:String = ""
    var displayName: String = ""
    var photoUri: Uri? = null

    constructor(uid: String, email: String, displayName: String, photoUri: Uri): this(uid){
        this.email = email
        this.displayName = displayName
        this.photoUri = photoUri
    }
}