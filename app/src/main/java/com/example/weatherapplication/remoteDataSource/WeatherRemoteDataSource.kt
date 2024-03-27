package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.Model
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRemoteDataSource {
    suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units:String,
        language:String
    ) : Model

}