package com.example.weatherapplication.settings

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TickHandler(
    private val externalScope: CoroutineScope,
    private val tickIntervalMs: Long = 5000,
    context: Context

) {
    private val _tickFlow = MutableSharedFlow<String>(replay = 1)
    val tickFlow: SharedFlow<String> = _tickFlow

    var spSettings= context.getSharedPreferences("settings" , Context.MODE_PRIVATE)
    var selectedLanguage = spSettings.getString("selectedLanguage" , "" ).toString()

    init {

        externalScope.launch {
            while(true) {
                _tickFlow.emit(selectedLanguage)
                delay(tickIntervalMs)
            }
        }
    }
}