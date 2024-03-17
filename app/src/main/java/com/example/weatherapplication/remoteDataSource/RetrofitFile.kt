package com.example.weatherapplication.remoteDataSource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val baseURL = "https://openweathermap.org/data/2.5/"
    val retrofitInstance = Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(baseURL)
                            .build()
}
object API {
    val retrofitService : WeatherService by lazy {
        RetrofitHelper.retrofitInstance.create(WeatherService::class.java)
    }
}