package com.example.myapplication.ui.checklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Task
import com.example.myapplication.ui.BaseRecyclerViewAdapter
import kotlinx.android.synthetic.main.row_layout.view.*

class RecyclerViewAdapter: BaseRecyclerViewAdapter<Task>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.row_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var myHolder = holder as? MyViewHolder
        myHolder?.setUpView(item = getItem(position))
    }



    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener{

        private val textView: TextView = view.contentTextView
        private val statusButton: ImageButton = view.statusButton

        init {
            view.setOnClickListener(this)
            statusButton.setOnClickListener(View.OnClickListener { v ->
                itemClickListener.onChecklistClick(adapterPosition, v)
            })
        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(adapterPosition, v)
        }

        fun setUpView(item: Task?) {
            textView.text = item?.task
        }

    }
}
