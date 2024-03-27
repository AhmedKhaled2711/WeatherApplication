package com.example.weatherapplication.home.viewModel

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

class HomeViewModel(private var repository: Repository)
    : ViewModel() {

    private var _weatherDetails: MutableStateFlow<StateRemote> = MutableStateFlow(StateRemote.Loading)
    val weatherDetails = _weatherDetails.asStateFlow()

    private var _weatherDetailsDB: MutableStateFlow<StateRemote> = MutableStateFlow(StateRemote.Loading)
    val weatherDetailsDB = _weatherDetailsDB.asStateFlow()


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

     fun getCurrentWeather(){
         viewModelScope.launch(Dispatchers.IO) {
             repository.getCurrentWeather().collect { weather ->
                 _weatherDetailsDB.value = StateRemote.Success(weather)
             }
         }
     }

     fun insertCurrentWeather(model: Model){
         viewModelScope.launch(Dispatchers.IO){
             repository.insertCurrentWeather(model)

         }
     }

    override fun onCleared() {
        super.onCleared()
    }
}