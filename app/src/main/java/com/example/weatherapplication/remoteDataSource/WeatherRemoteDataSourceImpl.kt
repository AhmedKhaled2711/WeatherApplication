package com.example.weatherapplication.remoteDataSource

import android.util.Log
import com.example.weatherapplication.model.Model
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRemoteDataSourceImpl : WeatherRemoteDataSource {

    private val API_URL = "https://api.openweathermap.org/data/3.0/"
    private val weatherService : WeatherService

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
        weatherService = retrofit.create(WeatherService:: class.java)
    }

//    companion object {
//        @Volatile
//        private var instance: WeatherRemoteDataSourceImpl? = null
//        fun getInstance(): WeatherRemoteDataSourceImpl {
//            if(instance == null)
//                instance = WeatherRemoteDataSourceImpl()
//            return instance as WeatherRemoteDataSourceImpl
//        }
//    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,

    ): Response<Model> {
        Log.i("Tag", "Test : WeatherRemoteDataSourceImpl")
        Log.i("Tag", "Test : WeatherRemoteDataSourceImpl final" +
                " ${weatherService.getCurrentWeather(lat=lat , lon=lon  ).body()}")
        return weatherService.getCurrentWeather(lat=lat , lon=lon )
    }
}