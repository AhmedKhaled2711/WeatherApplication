package com.example.weatherapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.model.AlertNotification
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM favorite_table")
    fun getAllSavedLocations(): Flow<List<StoreLatitudeLongitude>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocation(location: StoreLatitudeLongitude)

    @Delete
    fun deleteLocation(location: StoreLatitudeLongitude)

    @Query("SELECT * FROM current_table")
    fun getCurrentWeather(): Flow<Model>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCurrentWeather(model: Model)

    @Query("SELECT * FROM alert_table")
    fun getAllSavedAlerts(): Flow<List<AlertNotification>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotification(alert: AlertNotification)

    @Delete
    fun deleteNotification(alert: AlertNotification)

}