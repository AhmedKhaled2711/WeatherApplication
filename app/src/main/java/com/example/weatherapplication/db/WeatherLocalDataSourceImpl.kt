package com.example.weatherapplication.db

import android.content.Context
import com.example.dayone.Five.WeatherDataBase
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.model.AlertNotification
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(context: Context) : WeatherLocalDataSource{

    private var weatherDao : WeatherDao?
    init {
        val dataBase = WeatherDataBase.getInstance(context)
        weatherDao = dataBase.getWeatherDao()
    }

    companion object {
        @Volatile
        private var instance: WeatherLocalDataSourceImpl? = null
        fun getInstance(context: Context): WeatherLocalDataSourceImpl {
            if(instance == null)
                instance = WeatherLocalDataSourceImpl(context)
            return instance as WeatherLocalDataSourceImpl
        }
    }
    override suspend fun getAllStoredLocations(): Flow<List<StoreLatitudeLongitude>> {
        return weatherDao!!.getAllSavedLocations()
    }

    override suspend fun insertLocationInRoom(location: StoreLatitudeLongitude) {
        return weatherDao!!.insertLocation(location)
    }

    override suspend fun deleteLocationInRoom(location: StoreLatitudeLongitude) {
        return weatherDao!!.deleteLocation(location)
    }

    override suspend fun getCurrentWeather(): Flow<Model> {
        return weatherDao!!.getCurrentWeather()
    }

    override suspend fun insertCurrentWeather(model: Model) {
        return weatherDao!!.insertCurrentWeather(model)
    }

    override suspend fun getAllSavedAlerts(): Flow<List<AlertNotification>> {
        return weatherDao!!.getAllSavedAlerts()
    }

    override suspend fun insertNotification(alert: AlertNotification) {
        return weatherDao!!.insertNotification(alert)
    }

    override suspend fun deleteNotification(alert: AlertNotification) {
        return weatherDao!!.deleteNotification(alert)
    }
}