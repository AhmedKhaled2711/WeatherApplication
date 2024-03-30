package com.example.weatherapplication.alarm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.StateNotification
import com.example.weatherapplication.model.AlertNotification
import com.example.weatherapplication.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(private var repository: Repository)
    : ViewModel() {

    var alertNotifications : MutableStateFlow<StateNotification> = MutableStateFlow(StateNotification.Loading)

    init {
        getAlerts()
    }
    fun getAlerts(){
        viewModelScope.launch (Dispatchers.IO){
            repository.getAllSavedAlerts().collect{
                alertNotifications.value = StateNotification.Success(it)
            }
        }
    }

    fun insertAlerts(alertNotification: AlertNotification){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertNotification(alertNotification)
            getAlerts()
        }
    }

    fun deleteAlert(alertNotification: AlertNotification){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteNotification(alertNotification)
            getAlerts()
        }
    }

}