package com.example.weatherapplication.model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    suspend fun getWeather( lat: Double,
                            lon: Double,
                            units:String,
                            language:String
    ) : Response<Model>

    suspend fun getFavoriteLocations(): Flow<List<StoreLatitudeLongitude>>
    suspend fun insertToFavorite(location: StoreLatitudeLongitude)
    suspend fun removeFromFavorite(favoritePlaces: StoreLatitudeLongitude)

    suspend fun getCurrentWeather(): Model

    suspend fun insertCurrentWeather(model: Model)

}