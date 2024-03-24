package com.example.weatherapplication.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private var repository: Repository)
    : ViewModel() {

    private var _weatherDetails = MutableLiveData<Model>()
    var weatherDetails: LiveData<Model> = _weatherDetails

//    init {
//        getWeatherDetails(30.0444 , 31.2357 )
//        Log.i("Tag", "Test : HomeViewModel")
//    }
     fun getWeatherDetails(lat: Double,
                           lon: Double
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getWeather(lat , lon )
            if (response.isSuccessful){
                _weatherDetails.postValue(response.body())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}