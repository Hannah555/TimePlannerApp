package com.example.myapplication.ui.addList

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import java.util.*

class AddListDialog() : DialogFragment(), AddListContract.View{

    private lateinit var presenter: AddListContract.Presenter
    private val RQ_SPEECH_REC = 102


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(this.requireActivity())

        val inflater = requireActivity().layoutInflater

        val dialogView = inflater.inflate(R.layout.fragment_add_list, null)

        // Reference to each UI widget
        val btn_rec = dialogView.findViewById(R.id.btn_rec) as ImageButton
        val btn_cancle = dialogView.findViewById(R.id.btn_cancle) as ImageButton
        val btn_confirm = dialogView.findViewById(R.id.btn_confirm) as ImageButton
        val addActivity = dialogView.findViewById(R.id.addActivity) as EditText
        val weekSelection = dialogView.findViewById(R.id.weekSelection) as Spinner
        val weeks = resources.getStringArray(R.array.week)

        // Access week in spinner
        if(weekSelection != null){
            val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, weeks)
            weekSelection.adapter = adapter

            weekSelection.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Toast.makeText(requireActivity(), "Selected Item: "+ weeks[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

        btn_rec.setOnClickListener{
            startRecord()
        }

        btn_cancle.setOnClickListener {
            dismiss()
        }

        builder.setView(dialogView).setTitle("Add New Activity")


        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            presenter.extractActivity(result?.get(0).toString())
        }
    }

    private fun startRecord() {
        if(!SpeechRecognizer.isRecognitionAvailable(requireActivity())){
            Toast.makeText(requireActivity(), "Speech recognition is not available...", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: Exercise everyday")
            startActivityForResult(intent, RQ_SPEECH_REC)
        }
    }

    override fun getJsonData(jsonData: String) {
        Toast.makeText(requireActivity(), jsonData, Toast.LENGTH_SHORT).show()
    }

    override fun setPresenter(presenter: AddListContract.Presenter) {
        this.presenter = presenter
    }


}