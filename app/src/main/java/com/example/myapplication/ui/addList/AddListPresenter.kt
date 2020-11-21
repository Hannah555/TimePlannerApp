package com.example.myapplication.ui.addList

import android.os.Bundle
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


    override fun extractActivity(result: String) {
        CoroutineScope(IO).launch {
            val response = apiRequest(result)

            withContext(Main){
                view?.getJsonData(response)
            }
        }
    }

    private fun apiRequest(result: String): String {
        val client = OkHttpClient()

        var body: String = "timezone=Asia%2FKuala_Lumpur&query="

        body += URLEncoder.encode(result, "UTF-8")

        val request: Request = Request.Builder()
            .url("https://reminders-and-events-nlp1.p.rapidapi.com/api/v1/reminder")
            .addHeader("x-rapidapi-host", "reminders-and-events-nlp1.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "8b5813f977msh42df36b93b5eb8bp1576edjsn5269c25654d1")
            .addHeader("content-type", "application/x-www-form-urlencoded")
            .post(body.toRequestBody(MEDIA_TYPE_URL))
            .build()

        val response: Response = client.newCall(request).execute()

        return response.body.toString()
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