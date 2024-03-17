package com.example.weatherapplication.model

import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import retrofit2.Response

class RepositoryImpl(private val remoteDataSource: WeatherRemoteDataSource,
                    // private val localDataSource: WeatherRemoteDataSource
    )
    : Repository {
    override suspend fun getWeather(lat: Double,
                                    lon: Double,
                                    apiKey: String
    ): Response<WeatherResponse> {
        return remoteDataSource.getWeatherOverNetwork(lat , lon , apiKey)
    }
}