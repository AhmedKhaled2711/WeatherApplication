package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.Model
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("onecall")
    suspend fun getCurrentWeather(@Query("lat") lat: Double,
                                  @Query("lon") lon: Double,
                                  @Query("units") units: String,
                                  @Query("lang") language: String,
                                  @Query("exclude") exclude:String = "minutely",
                                  @Query("appid") apiKey: String = "30a73a92f374a05cbcd5f6b8caeacab0",

    )
    : Model
}