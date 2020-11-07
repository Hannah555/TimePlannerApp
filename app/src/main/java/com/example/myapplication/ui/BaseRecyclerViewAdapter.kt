package com.example.myapplication.ui

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.checklist.OnItemClickListener

abstract class BaseRecyclerViewAdapter<T>:  RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var list: ArrayList<T>? = ArrayList<T>()
    protected lateinit var itemClickListener: OnItemClickListener

    fun addItems(items: ArrayList<T>){
        this.list?.addAll(items)
        reload()
    }

    fun clear(){
        this.list?.clear()
        reload()
    }

    fun getItem(position: Int): T?{
        return this.list?.get(position)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int = list!!.size

    private fun reload() {
        Handler(Looper.getMainLooper()).post { notifyDataSetChanged() }
    }


}

