package com.example.myapplication.ui.checklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Task
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class RecyclerViewAdapter constructor(options: FirestoreRecyclerOptions<Task>) : FirestoreRecyclerAdapter<Task, RecyclerViewAdapter.TaskViewHolder>(options) {

    private lateinit var listener: OnItemClickListener

    override fun onBindViewHolder(tasktViewHolder: TaskViewHolder, position: Int, task: Task) {
        //Log.i("ONBIND", position.toString())


        tasktViewHolder.textView.text = task.task
        val document = snapshots.getSnapshot(position)
        var isDone = document.getBoolean("done")

        if(isDone!!)
            tasktViewHolder.statusButton.setImageResource(R.drawable.ic_baseline_check_circle_24)
        else
            tasktViewHolder.statusButton.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)

        tasktViewHolder.statusButton.setOnClickListener {
            Log.i("status button", isDone.toString())
            listener.onItemClick(document, position, isDone)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return TaskViewHolder(view)
    }

    fun deleteItem(position: Int){
        snapshots.getSnapshot(position).reference.delete()
    }

    class TaskViewHolder constructor(private val view: View): RecyclerView.ViewHolder(view){

        val textView = view.findViewById<TextView>(R.id.contentTextView)
        val statusButton = view.findViewById<ImageView>(R.id.statusButton)

    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

}



