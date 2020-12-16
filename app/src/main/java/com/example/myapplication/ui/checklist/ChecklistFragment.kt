package com.example.myapplication.ui.checklist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Task
import com.example.myapplication.ui.schedule.ScheduleFragment
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.row_layout.*
import kotlinx.android.synthetic.main.row_layout.view.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [ChecklistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChecklistFragment() : Fragment(), DatePickerListener {

    protected lateinit var rootview: View
    lateinit var recyclerView: RecyclerView
    lateinit var mdatabase: FirebaseFirestore
    private var adapter: RecyclerViewAdapter? = null
    private lateinit var tquery:Query
//    private lateinit var scheduleFragment: ScheduleFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SEQUENCE", "ON CREATE")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_checklist, container, false)
        val picker: HorizontalPicker = rootview.findViewById(R.id.datePicker)
        val to_scheduleButton: Button = rootview.findViewById(R.id.to_schedule)

        picker.setListener(this).init()
        picker.setDate(DateTime())

//        to_scheduleButton.setOnClickListener {
//            scheduleFragment = ScheduleFragment()
//            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, checklistFragment).setTransition(
//                FragmentTransaction.TRANSIT_FRAGMENT_OPEN
//            ).commit()
//        }

        initView()
        return rootview
    }

    private fun initializerRecyclerView(thisdate:String) {
        Log.i("date in recyclerview", thisdate)
        recyclerView = rootview.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        mdatabase = FirebaseFirestore.getInstance()
        tquery = mdatabase.collection(FirebaseAuth.getInstance().currentUser!!.uid).document("profile").collection(
            "task"
        )
            .whereEqualTo("startTime", null)
            .whereEqualTo("date", thisdate)
        val options= FirestoreRecyclerOptions.Builder<Task>()
            .setQuery(tquery, Task::class.java)
            .build()
        adapter = RecyclerViewAdapter(options)
        recyclerView.adapter = adapter

        val mIth = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT.or(
                ItemTouchHelper.RIGHT
            )
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter?.deleteItem(viewHolder.adapterPosition)
                Toast.makeText(requireActivity(), "Task deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(recyclerView)

        adapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                documentSnapshot: DocumentSnapshot,
                position: Int,
                isDone: Boolean
            ) {
                val taskid: String = documentSnapshot.id
                Log.i("------", "------------------")
                Log.i("Position", position.toString())
                Log.i("Path", documentSnapshot.reference.path)
                Log.i("Snapshot done", documentSnapshot.get("done").toString())
                Log.i("isDone", isDone.toString())
                documentSnapshot.reference.update("done", !isDone)
            }
        })
        adapter!!.startListening()
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter!!.stopListening()
    }

    fun initView() {
        val c: Date = Calendar.getInstance().getTime()

        val df = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)
        initializerRecyclerView(formattedDate)
    }

    override fun onDateSelected(dateSelected: DateTime?) {
        //2020-11-24T00:00:00.000+08:00
        val dateString = dateSelected?.toString()
        val separate2 = dateString?.split("-", "T")?.map { it.trim() }
        val date = separate2?.get(2) + "/" + separate2?.get(1) + "/" + separate2?.get(0)
        Log.i("date", date)
        initializerRecyclerView(date)
    }

}
