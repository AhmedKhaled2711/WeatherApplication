package com.example.weatherapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapplication.model.StoreLatitudeLongitude
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM favorite_table")
    fun getAllSavedLocations(): Flow<List<StoreLatitudeLongitude>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLocation(location: StoreLatitudeLongitude)

    @Delete
    fun deleteLocation(location: StoreLatitudeLongitude)
}