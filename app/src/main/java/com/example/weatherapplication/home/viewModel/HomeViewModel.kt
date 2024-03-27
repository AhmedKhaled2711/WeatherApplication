package com.example.weatherapplication.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private var repository: Repository)
    : ViewModel() {

    private var _weatherDetails = MutableLiveData<Model>()
    var weatherDetails: LiveData<Model> = _weatherDetails

    var favoriteLocation : MutableStateFlow<StateDB> = MutableStateFlow(StateDB.Loading)


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

     fun getCurrentWeather(){
         viewModelScope.launch(Dispatchers.IO) {
             val response = repository.getCurrentWeather()
             _weatherDetails.postValue(response)

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