package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.WeatherResponse
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        apiKey: String
    ) : Response<WeatherResponse>

}