package com.example.myapplication.data.model

class History {

    var date: String? = null
    var title: String? = null
    var time: String? = null

    constructor(date: String?,
                title: String?,
                time: String?){
        this.date = date
        this.title = title
        this.time = time
    }

    constructor()
}