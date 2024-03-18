package com.example.weatherapplication.model

import retrofit2.Response

interface Repository {

    suspend fun getWeather( lat: Double,
                            lon: Double,
                            apiKey: String
    ) : Response<WeatherResponse>
}