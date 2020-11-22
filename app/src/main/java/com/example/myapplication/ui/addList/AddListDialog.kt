package com.example.myapplication.ui.addList

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.remote.FirebaseHandlerImpl
import zion830.com.range_picker_dialog.TimeRangePickerDialog
import java.text.SimpleDateFormat
import java.util.*


class AddListDialog : DialogFragment(), AddListContract.View{

    private lateinit var presenter: AddListContract.Presenter
    private val RQ_SPEECH_REC = 102
    private lateinit var task: String
    private var date: String? = null
    private var startTime: String? = null
    private var endTime: String? = null
    private var repeat: Boolean = false


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(this.requireActivity())

        val inflater = requireActivity().layoutInflater

        val dialogView = inflater.inflate(R.layout.fragment_add_list, null)

        // Reference to each UI widget
        val btn_rec = dialogView.findViewById(R.id.btn_rec) as ImageButton
        val btn_cancle = dialogView.findViewById(R.id.btn_cancle) as ImageButton
        val btn_confirm = dialogView.findViewById(R.id.btn_confirm) as ImageButton

        val weekSelection = dialogView.findViewById(R.id.weekSelection) as Spinner
        weekSelection.visibility = View.GONE

        val weeks = resources.getStringArray(R.array.week)

        val date_selection = dialogView.findViewById(R.id.date_selection) as LinearLayout
        date_selection.visibility = View.GONE

        val selectDateButton = dialogView.findViewById<ImageButton>(R.id.selectdatebutton)
        val showDate = dialogView.findViewById<TextView>(R.id.showdate)

        val time_selection = dialogView.findViewById<LinearLayout>(R.id.time_selection)
        time_selection.visibility = View.GONE

        val selectTimeButton = dialogView.findViewById<ImageButton>(R.id.selecttimebutton)
        val showTime = dialogView.findViewById<TextView>(R.id.showtime)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Radio Button Activity
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.date)
        val everyWeekButton = dialogView.findViewById<RadioButton>(R.id.everyweek)
        everyWeekButton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    weekSelection.visibility = View.VISIBLE
                    // Set Week In Spinner
                    if (weekSelection != null) {
                        val adapter = ArrayAdapter(
                            requireActivity(),
                            android.R.layout.simple_spinner_item,
                            weeks
                        )
                        weekSelection.adapter = adapter

                        weekSelection.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                //Toast.makeText(requireActivity(), "Selected Item: " + weeks[position], Toast.LENGTH_SHORT).show()
                                date = weeks[position]
                                repeat = true
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }
                    }
                } else {
                    weekSelection.visibility = View.GONE
                }
            }
        })

        val customDateButton = dialogView.findViewById<RadioButton>(R.id.customdate)
        customDateButton.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    date_selection.visibility = View.VISIBLE
                    date = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    repeat = false
                    // Access date
                    showDate.setText(SimpleDateFormat("dd/MM/yyyy").format(Date()))

                    selectDateButton.setOnClickListener {
                        val dpd = DatePickerDialog(
                            requireContext(),
                            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                val getDate = "" + dayOfMonth + "/" + month + "/" + year
                                showDate.setText(getDate)
                                date = getDate
                            }, year, month, day
                        )
                        dpd.show()
                    }
                } else
                    date_selection.visibility = View.GONE
            }

        })
        val everydayDateButton = dialogView.findViewById<RadioButton>(R.id.everyday)
        everydayDateButton.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                repeat = isChecked
                date = "everyday"
            }
        })

        radioGroup.setOnCheckedChangeListener{ group, checkedID ->
            when(checkedID){
                R.id.tomorrow -> {
//                    Toast.makeText(this.context, "tomorrow", Toast.LENGTH_SHORT).show()
                    c.add(Calendar.DAY_OF_MONTH, 1)
                    val tomorrow = c.time
                    date = SimpleDateFormat("dd/MM/yyyy").format(tomorrow)
                    repeat = false
                }
                R.id.today -> {
//                    Toast.makeText(this.context, "today", Toast.LENGTH_SHORT).show()
                    date = SimpleDateFormat("dd/MM/yyyy").format(Date())
                    repeat = false
                }
            }
        }

        // Checkbox Time activity
        val timeCheckbox = dialogView.findViewById<CheckBox>(R.id.timeCheckbox)
        timeCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                time_selection.visibility = View.VISIBLE
                // Set Time
                val sdf = SimpleDateFormat("a h:mm")
                startTime = sdf.format(Date())

                val date = sdf.parse(startTime)
                val cal: Calendar = Calendar.getInstance()
                cal.time = date
                cal.add(Calendar.HOUR, 1)
                val sdfEnd = cal.time
                endTime = sdf.format(sdfEnd)

                showTime.text = startTime + " - " + endTime

                selectTimeButton.setOnClickListener {
                    val fragmentManager = requireActivity().supportFragmentManager
                    TimeRangePickerDialog.Builder()
                        .setOnTimeRangeSelectedListener { timeRange ->
                            val time = timeRange.readableTimeRange
                            showTime.text = time
                        }
                        .setOnDayMode(true)
                        .build()
                        .show(fragmentManager)
                }
            }
            else{
                time_selection.visibility = View.GONE
                startTime = null.toString()
                endTime = null.toString()
            }
        }

        // Set Presenter
        setPresenter(AddListPresenter(this, FirebaseHandlerImpl()))


        // Upper Click button
        btn_rec.setOnClickListener{
            startRecord()
        }

        btn_cancle.setOnClickListener {
            dismiss()
        }

        btn_confirm.setOnClickListener {
            val editActivity = dialogView.findViewById<EditText>(R.id.addActivity)
            task = editActivity.text.toString()
            presenter.handleActivitySuccess(Task(task, date, repeat, startTime, endTime))
        }

        builder.setView(dialogView).setTitle("Add New Activity")
        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            presenter.extractActivity(result?.get(0).toString(), this.requireActivity())
        }
    }

    private fun startRecord() {
        if(!SpeechRecognizer.isRecognitionAvailable(requireActivity())){
            Toast.makeText(
                requireActivity(),
                "Speech recognition is not available...",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: Exercise everyday")
            startActivityForResult(intent, RQ_SPEECH_REC)
        }
    }

    override fun getJsonData(jsonData: String) {
        Toast.makeText(requireActivity(), jsonData, Toast.LENGTH_SHORT).show()
        Log.i("json", jsonData)
//        val json = JSONObject(jsonData)
//        val event = json.getString("events")
//
//        val jsonArray = JSONArray(event)
//        var i = 0
//        while (i < jsonArray.length()){
//            val jsonObject = jsonArray.getJSONObject(i)
//            val startObject = jsonObject.getString("start")
//
//            val jsonSecond = JSONObject(startObject)
//            val fragmentObject = jsonSecond.getString("fragment")
//
//            val jsonThird = JSONObject(fragmentObject)
//            val weekday = jsonThird.getString("weekday")
//        }
    }

    override fun setPresenter(presenter: AddListContract.Presenter) {
        this.presenter = presenter
    }



}