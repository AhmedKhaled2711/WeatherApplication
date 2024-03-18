package com.example.weatherapplication.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private var repository: Repository)
    : ViewModel() {

    private var _weatherDetails = MutableLiveData<WeatherResponse>()
    var weatherDetails: LiveData<WeatherResponse> = _weatherDetails

    init {
        getWeatherDetails(30.0444 , 31.2357 , "30a73a92f374a05cbcd5f6b8caeacab0")
        Log.i("Tag", "Test : HomeViewModel")
    }
     fun getWeatherDetails(lat: Double,
                           lon: Double,
                           apiKey: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getWeather(lat , lon , apiKey)
            if (response.isSuccessful){
                _weatherDetails.postValue(response.body())
                Log.i("Tag", "getWeatherDetails: ${response.body()?.list?.get(0)?.main?.humidity}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}