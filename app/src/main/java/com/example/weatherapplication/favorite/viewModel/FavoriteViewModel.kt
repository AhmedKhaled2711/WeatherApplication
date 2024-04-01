package com.example.weatherapplication.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.StoreLatitudeLongitude
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private var repository: Repository)
    : ViewModel() {

    var favoriteLocation : MutableStateFlow<StateDB> = MutableStateFlow(StateDB.Loading)

    init {
        getLocationsFromDB()
    }

    fun insertFavorite(storeLatitudeLongitude: StoreLatitudeLongitude){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertToFavorite(storeLatitudeLongitude)
            getLocationsFromDB()
        }
    }

    fun getLocationsFromDB(){
        viewModelScope.launch (Dispatchers.IO){
            repository.getFavoriteLocations().collect{
                favoriteLocation.value = StateDB.Success(it)
            }
        }
    }

    fun deleteLocation(storeLatitudeLongitude: StoreLatitudeLongitude){
        viewModelScope.launch(Dispatchers.IO){
            repository.removeFromFavorite(storeLatitudeLongitude)
            getLocationsFromDB()
        }
    }


}