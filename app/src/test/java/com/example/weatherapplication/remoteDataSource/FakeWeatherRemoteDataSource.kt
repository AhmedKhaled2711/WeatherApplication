package com.example.weatherapplication.remoteDataSource

import com.example.weatherapplication.model.Model

class FakeWeatherRemoteDataSource : WeatherRemoteDataSource {
    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units: String,
        language: String
    ): Model {
        TODO("Not yet implemented")
    }
}