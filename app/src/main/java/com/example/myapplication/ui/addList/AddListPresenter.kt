package com.example.myapplication.ui.addList

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.myapplication.data.model.Task
import com.example.myapplication.data.remote.FirebaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.net.URLEncoder

class AddListPresenter(view: AddListContract.View, firebaseHandler: FirebaseHandler): AddListContract.Presenter {

    private var view: AddListContract.View? = view
    private var firebaseHandler: FirebaseHandler? = firebaseHandler
//    private lateinit var storage:SharedPreferences


    override fun extractActivity(result: String, context: Context) {
//        storage = context.getSharedPreferences("storage", Context.MODE_PRIVATE)
        CoroutineScope(IO).launch {
            Log.i("FUCK", "AFA")
            val response = apiRequest(result)
            Log.i("LOG", "test")
            withContext(Main){
                view?.getJsonData(response)
            }
        }
    }

    override fun handleActivitySuccess(task: Task) {
        firebaseHandler?.saveTask(task)
    }

    private fun apiRequest(result: String): String {
        val client = OkHttpClient()

        var body: String = "timezone=Asia%2FKuala_Lumpur&query="

        body += URLEncoder.encode(result, "UTF-8")

        val request: Request = Request.Builder()
            .url("https://reminders-and-events-nlp1.p.rapidapi.com/api/v1/reminder")
            .addHeader("x-rapidapi-host", "reminders-and-events-nlp1.p.rapidapi.com")
//            .addHeader("x-rapidapi-key", "8b5813f977msh42df36b93b5eb8bp1576edjsn5269c25654d1")
            .addHeader("x-rapidapi-key", "bdc15c3867msh83c4476b53afeebp188ebfjsna36760a6cd51")
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .post(body.toRequestBody(MEDIA_TYPE_URL))
            .build()

        val response: Response = client.newCall(request).execute()

//        storage.edit().putString("a", response.body!!.string()).apply()
//        val a:String = storage.getString("a", "")!!
        return response.body!!.string()
//        return a
    }

    override fun onStart(extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        this.view = null
    }

    companion object {
        val MEDIA_TYPE_URL = "application/x-www-form-urlencoded".toMediaType()
    }
}