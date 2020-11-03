package com.example.myapplication.data.remote

import android.util.Log
import com.example.myapplication.data.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseHandlerImpl: FirebaseHandler {

    private val db = Firebase.firestore

    override fun saveUserData(user: User) {
        val docRef = db.collection(user.uid.toString()).document("profile")
        //Log.i("user",user.uid.toString())
        docRef.get().addOnSuccessListener { document ->
            //Log.i("collection",document.toString())
            if (document.data != null){
                //Log.i("status","exists")
                docRef.update(mapOf("Email" to user.email, "Name" to user.displayName, "PhotoUri" to user.photoUri.toString()))
            }else{
                //Log.i("status","not exists")
                val userData = hashMapOf(
                    "Email" to user.email,
                    "Name" to user.displayName,
                    "PhotoUri" to user.photoUri.toString()
                )
                docRef.set(userData)
            }
        }
    }

    override fun getName(currentUser: String) {
        Log.i("Current User",currentUser)
        val docRef = db.collection(currentUser).document("profile")
        var name: String? = null
        docRef.get().addOnSuccessListener { document ->
            if (document.data != null){
                 name = document.getString("Name")
                passName(name)
            }else{
                Log.i("Error", "Exception")
            }
        }
    }

    fun passName(name: String?):String{
        Log.i("name",name!!)
        return name
    }

    override fun getPhotoUri(currentUser: String) {
        Log.i("Current User",currentUser)
        val docRef = db.collection(currentUser).document("profile")
        var photoUri: String? = null
        docRef.get().addOnSuccessListener { document ->
            if (document.data != null){
//                Log.i("name", document.getString("Name"))
                photoUri = document.getString("PhotoUri")
            }else{
                Log.i("Error", "Exception")
            }
        }

    }


}

