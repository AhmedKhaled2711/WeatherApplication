package com.example.weatherapplication.model

import com.example.weatherapplication.db.FakeWeatherLocalDataSource
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.remoteDataSource.FakeWeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import org.junit.Before


class RepositoryImplTest{

    lateinit var weatherRemoteDataSource: WeatherRemoteDataSource
    lateinit var weatherLocalDataSource: WeatherLocalDataSource
    lateinit var repository: RepositoryImpl

    @Before
    fun setup(){
        weatherRemoteDataSource = FakeWeatherRemoteDataSource()
        weatherLocalDataSource = FakeWeatherLocalDataSource()
        repository = RepositoryImpl(
            weatherRemoteDataSource,
            weatherLocalDataSource
        )
    }



}