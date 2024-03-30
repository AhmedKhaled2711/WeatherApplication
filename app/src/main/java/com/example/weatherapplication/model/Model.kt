package com.example.weatherapplication.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.Nullable

@Entity(tableName = "current_table")
data class Model(
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0,
    @Nullable
    @TypeConverters(ModelConverter::class)
    val alerts: List<Alert>?,
    @Embedded(prefix = "current_")
    val current: Current,
    @TypeConverters(ModelConverter::class)
    val daily: List<Daily>,
    @TypeConverters(ModelConverter::class)
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)
@TypeConverters(ModelConverter::class)
data class Alert(
    val description: String,
    val end: Int,
    val event: String,
    val sender_name: String,
    val start: Int,
    val tags: List<String>
)

@TypeConverters(ModelConverter::class)
data class Current(
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_speed: Double
)
@TypeConverters(ModelConverter::class)
data class Daily(
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
    val feels_like: FeelsLike,
    val humidity: Int,
    val pop: Double, // Change type to Double to handle decimal values
    val pressure: Int,
    val rain: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val uvi: Double,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)

@TypeConverters(ModelConverter::class)
data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)

@TypeConverters(ModelConverter::class)
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)
@Parcelize
@Entity(tableName = "favorite_table")
data class StoreLatitudeLongitude(
    @PrimaryKey
    var longitude:Double,
    var latitude: Double,
    var name: String?
) : Parcelable

@Parcelize
@Entity(tableName = "alert_table")
data class AlertNotification(

    @PrimaryKey(autoGenerate = false)
    var time : Long

): Parcelable
