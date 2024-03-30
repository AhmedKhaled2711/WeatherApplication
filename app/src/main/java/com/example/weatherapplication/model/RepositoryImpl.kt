package com.example.weatherapplication.model

import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val remoteDataSource: WeatherRemoteDataSource,
                     private val localDataSource: WeatherLocalDataSource
    )
    : Repository {
    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units:String,
        language:String
    ): Model {
        return remoteDataSource.getWeatherOverNetwork(lat = lat, lon = lon , language=language , units = units)

    }

    override suspend fun getFavoriteLocations(): Flow<List<StoreLatitudeLongitude>> {
        return localDataSource.getAllStoredLocations()
    }

    override suspend fun insertToFavorite(location: StoreLatitudeLongitude) {
         localDataSource.insertLocationInRoom(location)
    }

    override suspend fun removeFromFavorite(location: StoreLatitudeLongitude) {
         localDataSource.deleteLocationInRoom(location)
    }

    override suspend fun getCurrentWeather(): Flow<Model> {
        return localDataSource.getCurrentWeather()
    }

    override suspend fun insertCurrentWeather(model: Model) {
        return localDataSource.insertCurrentWeather(model)
    }

    override suspend fun getAllSavedAlerts(): Flow<List<AlertNotification>> {
        return localDataSource.getAllSavedAlerts()
    }

    override suspend fun insertNotification(alert: AlertNotification) {
        return localDataSource.insertNotification(alert)
    }

    override suspend fun deleteNotification(alert: AlertNotification) {
        return localDataSource.deleteNotification(alert)
    }

    companion object {
        private var instance: RepositoryImpl? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource ,
            localDataSource: WeatherLocalDataSource
        ): RepositoryImpl {
            return instance ?: synchronized(this) {
                val temp = RepositoryImpl(
                    remoteDataSource , localDataSource
                )
                instance = temp
                temp
            }

        }
    }

}