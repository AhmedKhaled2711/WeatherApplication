package com.example.weatherapplication

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun getCurrentTime(dt : Long) : String{
    val data = Date(dt * 1000)
    val dateFormat = SimpleDateFormat("EEE , d/M/yyyy", Locale.US)
    return dateFormat.format(data)
}

fun getHourTime(dt : Long) : String{
    val data = Date(dt * 1000)
    val dateFormat = SimpleDateFormat("k a", Locale.US)
    return dateFormat.format(data)
}


fun getDay(dt : Long) : String{
    val data = Date(dt * 1000)
    val dateFormat = SimpleDateFormat("E, d/M", Locale.US)
    return dateFormat.format(data)
}

