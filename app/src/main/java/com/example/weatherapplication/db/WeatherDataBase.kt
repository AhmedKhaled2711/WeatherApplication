package com.example.dayone.Five

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapplication.db.WeatherDao
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.ModelConverter
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.model.AlertNotification

@Database(entities = arrayOf(StoreLatitudeLongitude::class , Model::class , AlertNotification::class) , version =  5 , exportSchema = false)
@TypeConverters(ModelConverter::class)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun  getWeatherDao() : WeatherDao

    companion object{
        private var INSTANCE  : WeatherDataBase? = null

        fun getInstance (context: Context) : WeatherDataBase{
            return INSTANCE ?: synchronized(this){
                val  instance = Room.databaseBuilder(
                    context.applicationContext , WeatherDataBase::class.java , "weather_dataBase"

                ).fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance

            }
        }
    }
}