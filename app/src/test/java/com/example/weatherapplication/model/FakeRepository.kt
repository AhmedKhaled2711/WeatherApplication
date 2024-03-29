package com.example.weatherapplication.model

import kotlinx.coroutines.flow.Flow

class FakeRepository : Repository {

    val fakeFavLocationsList = mutableListOf<StoreLatitudeLongitude>()
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Model {
        //return Model()
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteLocations(): Flow<List<StoreLatitudeLongitude>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertToFavorite(location: StoreLatitudeLongitude) {
        fakeFavLocationsList.add(location)
    }

    override suspend fun removeFromFavorite(favoritePlaces: StoreLatitudeLongitude) {
       fakeFavLocationsList.removeAll{
           it == favoritePlaces
       }
    }

    override suspend fun getCurrentWeather(): Flow<Model> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCurrentWeather(model: Model) {
        TODO("Not yet implemented")
    }
}