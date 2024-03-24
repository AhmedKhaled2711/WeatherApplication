package com.example.dayone.Five

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapplication.db.WeatherDao
import com.example.weatherapplication.model.StoreLatitudeLongitude

@Database(entities = arrayOf(StoreLatitudeLongitude::class) , version =  1)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun  getWeatherDao() : WeatherDao

    companion object{
        private var INSTANCE  : WeatherDataBase? = null

        fun getInstance (context: Context) : WeatherDataBase{
            return INSTANCE ?: synchronized(this){
                val  instance = Room.databaseBuilder(
                    context.applicationContext , WeatherDataBase::class.java , "weather_dataBase"

                ).build()
                INSTANCE = instance
                instance

            }
        }
    }
}