package com.example.myapplication.data.remote

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.model.User
import com.google.firebase.auth.FirebaseAuth
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
                tunnel.onDataFetched(name, Uri.parse(photo))
            }else{
                Log.i("Error", "Exception")
            }
        }
    }

    override fun saveTask(task: Task) {

        db.collection(FirebaseAuth.getInstance().currentUser!!.uid).document("profile").collection("task")
            .document().set(task)
            .addOnCompleteListener {dbTask->
                if(dbTask.isSuccessful)
                    Log.i("DB TAS", "Suceces")
                else
                    Log.i("DB TAS", "Failed")
            }
/*
        db.collection(FirebaseAuth.getInstance().currentUser!!.uid).document("profile").collection("task")
            .get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val result = task.result

                    if (result != null) {
                        for(document in result){
                            val currentDocument = document.toObject(Task::class.java)
                            Log.i("task info", currentDocument.task.toString())
                            Log.i("date info",currentDocument.date.toString())
                            Log.i("repeat info", currentDocument.repeat.toString())
                            Log.i("done info", currentDocument.done.toString())
                            Log.i("startTime info", currentDocument.startTime.toString())
                            Log.i("endTime info", currentDocument.endTime.toString())
                        }
                    }
                }
            }

 */
    }

    override fun getScheduleTask(retriever: FirebaseHandler.ScheduleRetriever, date: String) {
        val query = db.collection(FirebaseAuth.getInstance().currentUser!!.uid).document("profile").collection("task")
            .whereNotEqualTo("startTime", null)

        query.get().addOnCompleteListener { task->
            if(task.isSuccessful){
//                val result = task.result
                retriever.onDateFetched(task.result, date)



//                    if (result != null) {
//                        for (document in result) {
//                            val currentDocument = document.toObject(Task::class.java)
//                            Log.i("task info", currentDocument.task.toString())
//                            Log.i("date info", currentDocument.date.toString())
//                            Log.i("repeat info", currentDocument.repeat.toString())
//                            Log.i("done info", currentDocument.done.toString())
//                            Log.i("startTime info", currentDocument.startTime.toString())
//                            Log.i("endTime info", currentDocument.endTime.toString())
//                            if(currentDocument.date == date){
//                                retriever.onDateFetched(currentDocument)
//
//                            }
//                        }
//                    }

            }
        }
    }

    override fun deleteScheduleTask(docID: String) {
        val query = db.collection(FirebaseAuth.getInstance().currentUser!!.uid).document("profile")
            .collection("task").document(docID)
        query.delete()
            .addOnSuccessListener {
                Log.i("info", docID + " Deleted")
            }
            .addOnFailureListener{ e ->
                Log.i("Error", e.toString())
            }

    }
}

