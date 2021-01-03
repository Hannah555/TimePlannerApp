package com.example.myapplication.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.History
import com.example.myapplication.ui.history.TimeLineAdapter.TimeLineViewHolder
import com.github.vipulasri.timelineview.TimelineView
import kotlinx.android.synthetic.main.item_timeline.view.*

class TimeLineAdapter(private val mFeedList: List<History>) : RecyclerView.Adapter<TimeLineViewHolder>(){

    private lateinit var mLayoutInflater: LayoutInflater

    class TimeLineViewHolder(itemView: View, viewType: Int): RecyclerView.ViewHolder(itemView){
        val date = itemView.text_date
        val title = itemView.text_title
        val timeline = itemView.timeline
        init {
            timeline.initLine(viewType)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        if(!::mLayoutInflater.isInitialized){
            mLayoutInflater = LayoutInflater.from(parent.context)
        }
        return TimeLineViewHolder(mLayoutInflater.inflate(R.layout.item_timeline,parent, false), viewType)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val timeLineModel = mFeedList[position]

        if(timeLineModel.date.isNotEmpty()){
            holder.date.visibility = View.VISIBLE
            holder.date.text = timeLineModel.date
        } else{
            holder.date.visibility = View.GONE
        }

        holder.title.text = timeLineModel.message
    }


    override fun getItemCount() = mFeedList.size
}