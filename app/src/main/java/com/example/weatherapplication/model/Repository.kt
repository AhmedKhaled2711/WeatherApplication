package com.example.weatherapplication.model

import kotlinx.coroutines.flow.Flow

interface Repository {

     suspend fun getWeather( lat: Double,
                            lon: Double,
                            units:String,
                            language:String
    ) : Model

    suspend fun getFavoriteLocations(): Flow<List<StoreLatitudeLongitude>>
    suspend fun insertToFavorite(location: StoreLatitudeLongitude)
    suspend fun removeFromFavorite(favoritePlaces: StoreLatitudeLongitude)

    suspend fun getCurrentWeather(): Flow<Model>

    suspend fun insertCurrentWeather(model: Model)

    suspend fun getAllSavedAlerts(): Flow<List<AlertNotification>>
    suspend fun insertNotification(alert: AlertNotification)
    suspend fun deleteNotification(alert: AlertNotification)

}