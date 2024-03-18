package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.Model
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double
    ) : Response<Model>

}