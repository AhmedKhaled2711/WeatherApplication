package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRemoteDataSourceImpl : WeatherRemoteDataSource {

    private val API_URL = "https://openweathermap.org/data/2.5/"
    private val weatherService : WeatherService

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
        weatherService = retrofit.create(WeatherService:: class.java)
    }

    companion object {
        @Volatile
        private var instance: WeatherRemoteDataSourceImpl? = null
        fun getInstance(): WeatherRemoteDataSourceImpl {
            if(instance == null)
                instance = WeatherRemoteDataSourceImpl()
            return instance as WeatherRemoteDataSourceImpl
        }
    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Response<WeatherResponse> {
        return weatherService.getCurrentWeather(lat , lon , apiKey)
    }
}