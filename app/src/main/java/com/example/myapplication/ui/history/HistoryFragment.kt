package com.example.myapplication.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.History
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import kotlinx.android.synthetic.main.fragment_checklist.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment(), HistoryContract.View {
    // TODO: Rename and change types of parameters
    private lateinit var mAdapter: TimeLineAdapter
    private val mDataList = ArrayList<History>()
    private lateinit var mLayoutManager:LinearLayoutManager
    lateinit var recyclerView: RecyclerView
    private lateinit var presenter: HistoryContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        setPresenter(HistoryPresenter(this, FirebaseHandlerImpl()))

//        setDataListItems()
//        initRecyclerView()

        presenter.getHistory()

        return view
    }

    private fun setDataListItems(){
        mDataList.add(History("2017-02-11 9:30am", "complete the task",""))
        mDataList.add(History("2018-02-11 9:30am", "task",""))
    }

    private fun initRecyclerView(){
        mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = TimeLineAdapter(mDataList)
        recyclerView.adapter = mAdapter
    }

    override fun showHistory(historyList: ArrayList<History>) {
        mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = TimeLineAdapter(historyList)
        recyclerView.adapter = mAdapter
    }

    override fun setPresenter(presenter: HistoryContract.Presenter) {
        this.presenter = presenter
    }
}