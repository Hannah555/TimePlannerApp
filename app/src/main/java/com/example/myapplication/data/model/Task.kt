package com.example.myapplication.data.model

import java.sql.Time
import java.util.*

class Task{
    var task: String? = null
    var done: Boolean = false
    var dueDate: Date? = null
    var dueTime: Time? = null

    constructor(task: String?, done: Boolean, dueDate: Date?, dueTime: Time?) {
        this.task = task
        this.done = done
        this.dueDate = dueDate
        this.dueTime = dueTime
    }
}