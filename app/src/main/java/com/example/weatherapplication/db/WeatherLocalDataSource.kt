package com.example.weatherapplication.db

import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.model.AlertNotification
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun getAllStoredLocations(): Flow<List<StoreLatitudeLongitude>>

    suspend fun insertLocationInRoom(location: StoreLatitudeLongitude)

    suspend fun deleteLocationInRoom(location: StoreLatitudeLongitude)

    suspend fun getCurrentWeather(): Flow<Model>

    suspend fun insertCurrentWeather(model: Model)

    suspend fun getAllSavedAlerts(): Flow<List<AlertNotification>>
    suspend fun insertNotification(alert: AlertNotification)
    suspend fun deleteNotification(alert: AlertNotification)
}