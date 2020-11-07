package com.example.myapplication.ui.checklist

import android.view.View

interface OnItemClickListener {
    abstract fun onItemClick(position:Int, view: View?)

    abstract fun onChecklistClick(position: Int, view: View?)
}