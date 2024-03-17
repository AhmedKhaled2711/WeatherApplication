package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("forecast")
    suspend fun getCurrentWeather(@Query("lat") lat: Double,
                                  @Query("lon") lon: Double,
                                  @Query("appid") apiKey: String
    )
    : WeatherResponse
}