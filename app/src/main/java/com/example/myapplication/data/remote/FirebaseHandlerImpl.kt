package com.example.myapplication.data.remote

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.example.myapplication.data.model.User
import com.example.myapplication.ui.home.HomeContract
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseHandlerImpl: FirebaseHandler {

    private val db = Firebase.firestore
//    private lateinit var presenter: HomeContract.Presenter

    open fun FirebaseHandlerImpl(presenter: HomeContract.Presenter) {
//        this.presenter = presenter
    }

    override fun saveUserData(user: User) {
        val docRef = db.collection(user.uid.toString()).document("profile")
        //Log.i("user",user.uid.toString())
        docRef.get().addOnSuccessListener { document ->
            //Log.i("collection",document.toString())
            if (document.data != null){
                //Log.i("status","exists")
                docRef.update(
                    mapOf(
                        "Email" to user.email,
                        "Name" to user.displayName,
                        "PhotoUri" to user.photoUri.toString()
                    )
                )
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

    @SuppressLint("LongLogTag")
    override fun getName(tunnel: FirebaseHandler.FirebaseTunnel, currentUser: String) {
        Log.i("Current User", currentUser)
        val docRef = db.collection(currentUser).document("profile")

        docRef.get().addOnSuccessListener { document ->
            if (document.data != null){
                var name = document.getString("Name")!!
                var photo = document.getString("PhotoUri")
//                Log.i("This is the real real name", name)
//                Log.i("This is the real photo", photo)
                tunnel.onDataFetched(name, Uri.parse(photo))
               // presenter.passName(name, Uri.parse(photo))
            }else{
                Log.i("Error", "Exception")
            }
        }
    }


}

