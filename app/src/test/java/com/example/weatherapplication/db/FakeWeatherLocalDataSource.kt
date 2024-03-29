package com.example.weatherapplication.db

import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import kotlinx.coroutines.flow.Flow

class FakeWeatherLocalDataSource : WeatherLocalDataSource {

    val fakeFavLocationsList = mutableListOf<StoreLatitudeLongitude>()

    override suspend fun getAllStoredLocations(): Flow<List<StoreLatitudeLongitude>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertLocationInRoom(location: StoreLatitudeLongitude) {
        fakeFavLocationsList.add(location)
    }

    override suspend fun deleteLocationInRoom(location: StoreLatitudeLongitude) {
        fakeFavLocationsList.removeAll{
            it == location
        }
    }

    override suspend fun getCurrentWeather(): Flow<Model> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCurrentWeather(model: Model) {
        TODO("Not yet implemented")
    }
}