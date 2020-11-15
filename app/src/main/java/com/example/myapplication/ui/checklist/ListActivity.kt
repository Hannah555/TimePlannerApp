package com.example.myapplication.ui.checklist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R


class ListActivity : AppCompatActivity(), ListContract.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val fab: View = findViewById(R.id.micfab)
        val back: ImageButton = findViewById(R.id.backbutton)
        val item: EditText = findViewById(R.id.addlist)
        val dateDisplay: TextView = findViewById(R.id.showdate)
        val selectDate: ImageButton = findViewById(R.id.selectdatebutton)
        val timeDisplay: TextView = findViewById(R.id.showtime)
        val selectTime: ImageButton = findViewById(R.id.selecttiembutton)

        // Radio button

        back.setOnClickListener{
            back()
        }

    }

    fun back(){
        val intent: Intent = Intent()
        setResult(2, intent)
        finish()
    }

    override fun onConfirmAddList() {

    }

    override fun onCancelList() {
        TODO("Not yet implemented")
    }

    override fun setPresenter(presenter: ListContract.Presenter) {
        TODO("Not yet implemented")
    }

}