package com.example.weatherapplication.cityDetails.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.StateRemote
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityViewModel(private var repository: Repository)
    : ViewModel() {

    private var _weatherDetails: MutableStateFlow<StateRemote> = MutableStateFlow(StateRemote.Loading)
    val weatherDetails = _weatherDetails.asStateFlow()


    fun getWeatherDetails(lat: Double,
                          lon: Double,
                          units:String,
                          language:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val weather = repository.getWeather(lat, lon, units, language)
            _weatherDetails.value = StateRemote.Success(weather)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}