package com.example.myapplication.data.model

class Task{
    var task: String? = null
    var done: Boolean = false
    var date: String? = null
    var repeat: Boolean = false
    var startTime: String? = null
    var endTime: String? = null

    constructor(
        task: String?,
        done: Boolean,
        date: String?,
        repeat: Boolean,
        startTime: String?,
        endTime: String?
    ) {
        this.task = task
        this.done = done
        this.date = date
        this.repeat = repeat
        this.startTime = startTime
        this.endTime = endTime
    }

    constructor(task: String?,
                date: String?,
                repeat: Boolean,
                startTime: String?,
                endTime: String?){
        this.task = task
        this.date = date
        this.repeat = repeat
        this.startTime = startTime
        this.endTime = endTime
    }

    constructor()


}