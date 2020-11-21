package com.example.myapplication.ui.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


/**
 * A simple [Fragment] subclass.
 * Use the [ChecklistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChecklistFragment : Fragment() {

    protected lateinit var rootview: View
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    private fun onCreateComponent() {
        adapter = RecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_checklist, container, false)
        initView()
        return rootview
    }

    private fun initView() {
        setUpAdapter()
        initializerRecyclerView()
//        setDummyData()
    }

    private fun setDummyData() {
//        var list: ArrayList<Task> = ArrayList()
//        list.add(Task("Item 1", true,))
//        list.add(Task("Item 2", false))
//        adapter.addItems(list)
    }

    private fun initializerRecyclerView() {
        recyclerView = rootview.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun setUpAdapter() {
        adapter.setOnItemClickListener(onItemClickListener = object : OnItemClickListener{
            override fun onItemClick(position: Int, view: View?) {
                var item = adapter.getItem(position)
                Toast.makeText(context, "Success" + item, Toast.LENGTH_SHORT).show()
            }

            override fun onChecklistClick(position: Int, view: View?) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        var TAG = ChecklistFragment::class.java.simpleName
        const val ARG_POSITION: String = "positioin"

        fun newInstance(): ChecklistFragment {
            var fragment = ChecklistFragment();
            val args = Bundle()
            args.putInt(ARG_POSITION, 1)
            fragment.arguments = args
            return fragment
        }
    }


}