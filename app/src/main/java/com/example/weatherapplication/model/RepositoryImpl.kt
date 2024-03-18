package com.example.weatherapplication.model

import android.util.Log
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import retrofit2.Response

class RepositoryImpl(private val remoteDataSource: WeatherRemoteDataSource,
                    // private val localDataSource: WeatherRemoteDataSource
    )
    : Repository {
    override suspend fun getWeather(lat: Double,
                                    lon: Double
    ): Response<Model> {
        Log.i("Tag", "Test : RepositoryImpl")
        return remoteDataSource.getWeatherOverNetwork(lat = lat , lon = lon  )

    }
}