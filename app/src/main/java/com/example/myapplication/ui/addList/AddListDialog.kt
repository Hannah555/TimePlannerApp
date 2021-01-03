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
import org.json.JSONArray
import org.json.JSONObject
import zion830.com.range_picker_dialog.TimeRangePickerDialog
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class AddListDialog : DialogFragment(), AddListContract.View{

    private lateinit var presenter: AddListContract.Presenter
    private val RQ_SPEECH_REC = 102
    private lateinit var task: String
    private var date: String? = null
    private var startTime: String? = null
    private var endTime: String? = null
    private var repeat: Boolean = false
    private lateinit var showDate: TextView
    private lateinit var showTime: TextView
    private lateinit var editActivity: EditText
    private lateinit var timeCheckbox: CheckBox

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(this.requireActivity())

        val inflater = requireActivity().layoutInflater

        val dialogView = inflater.inflate(R.layout.fragment_add_list, null)

        // Reference to each UI widget
        val btn_rec = dialogView.findViewById(R.id.btn_rec) as ImageButton
        val btn_cancle = dialogView.findViewById(R.id.btn_cancle) as ImageButton
        val btn_confirm = dialogView.findViewById(R.id.btn_confirm) as ImageButton
        editActivity = dialogView.findViewById<EditText>(R.id.addActivity)

        val weekSelection = dialogView.findViewById(R.id.weekSelection) as Spinner
        weekSelection.visibility = View.GONE

        val weeks = resources.getStringArray(R.array.week)

        val date_selection = dialogView.findViewById(R.id.date_selection) as LinearLayout
        date_selection.visibility = View.GONE

        val selectDateButton = dialogView.findViewById<ImageButton>(R.id.selectdatebutton)
        showDate = dialogView.findViewById<TextView>(R.id.showdate)

        val time_selection = dialogView.findViewById<LinearLayout>(R.id.time_selection)
        time_selection.visibility = View.GONE

        val selectTimeButton = dialogView.findViewById<ImageButton>(R.id.selecttimebutton)
        showTime = dialogView.findViewById<TextView>(R.id.showtime)

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
        timeCheckbox = dialogView.findViewById<CheckBox>(R.id.timeCheckbox)
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
                            showTime.text = time.toLowerCase()
                            val splitTime = time.toLowerCase().split(" - ")

                            startTime = splitTime[0]
                            endTime = splitTime[1]
                        }
                        .setOnDayMode(true)
                        .build()
                        .show(fragmentManager)
                }
            }
            else{
                time_selection.visibility = View.GONE
                startTime = null
                endTime = null
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
            task = editActivity.text.toString()
            presenter.handleActivitySuccess(Task(task, date, repeat, startTime, endTime))
            dismiss()
        }

        builder.setView(dialogView).setTitle("Add New Checklist/ Schedule...")
        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            presenter.extractActivity(result?.get(0).toString(), this.requireActivity())
        }
    }

//    Voice to Text
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
        Toast.makeText(requireActivity(), jsonData, Toast.LENGTH_LONG).show()
        Log.i("json", jsonData)
        var weekday:String? = null
        var year: String? = null
        var month: String? = null
        var day: String? = null
        var hour: String? = null
        var minute: String? = null

        val json = JSONObject(jsonData)
        val event = json.getString("events")
        val body = json.getString("body")

        val jsonArray = JSONArray(event)
        var i = 0
        var length = jsonArray.length()
        Log.i("Length", jsonArray.length().toString())

        if(length > 1){
            Toast.makeText(this.context, "Sorry, only one event is allowed", Toast.LENGTH_SHORT).show()
        }

        while (i < length){
            val jsonObject = jsonArray.getJSONObject(i)
            val startObject = jsonObject.getString("start")

            val jsonSecond = JSONObject(startObject)
            val fragmentObject = jsonSecond.getString("fragments")

            val jsonThird = JSONObject(fragmentObject)
            weekday = jsonThird.getString("weekday")
            year = jsonThird.getString("year")
            month = jsonThird.getString("month")
            day = jsonThird.getString("day")
            hour = jsonThird.getString("hour")
            minute = jsonThird.getString("minute")

            length--
        }

        val date = day +"/" + month + "/" + year
        val inTimeString = hour + ":" + minute

        val inTimeFormat = SimpleDateFormat("hh:mm")

        val parseTime = inTimeFormat.parse(inTimeString)

        val OutTimeFormat = SimpleDateFormat("a hh:mm")

        val startTime = OutTimeFormat.format(parseTime)

        val addCal = Calendar.getInstance()
        addCal.time = parseTime
        addCal.add(Calendar.HOUR, 1)

//        if number exist in body : put start time & end time
        // check the time box
//        if end time of json is null
        val endTime = OutTimeFormat.format(addCal.time)
        val resultTime = startTime + " - " + endTime

//        if end time of json is not null : just take the end time


        // if checklist: time_in_text
        // else (with time)
        // every day, tomorrow, week, today, custom


        Log.i("TIme!!!!!!!!", resultTime)
        showTime.text = resultTime
        showDate.text = date
        editActivity.setText(body)
    }

    override fun setPresenter(presenter: AddListContract.Presenter) {
        this.presenter = presenter
    }

    fun checkPattern(text: String){
        val am_pattern = Pattern.compile("am")
        val pm_pattern = Pattern.compile("pm")
    }
}