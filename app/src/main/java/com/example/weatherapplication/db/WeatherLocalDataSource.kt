package com.example.weatherapplication.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun getAllStoredLocations(): Flow<List<StoreLatitudeLongitude>>

    suspend fun insertLocationInRoom(location: StoreLatitudeLongitude)

    suspend fun deleteLocationInRoom(location: StoreLatitudeLongitude)

    suspend fun getCurrentWeather(): Flow<Model>

    suspend fun insertCurrentWeather(model: Model)





}