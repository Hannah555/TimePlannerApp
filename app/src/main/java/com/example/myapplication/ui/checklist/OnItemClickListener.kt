package com.example.myapplication.ui.checklist

import com.google.firebase.firestore.DocumentSnapshot

interface OnItemClickListener {
    abstract fun onItemClick(documentSnapshot: DocumentSnapshot, position:Int, isDone: Boolean)

}