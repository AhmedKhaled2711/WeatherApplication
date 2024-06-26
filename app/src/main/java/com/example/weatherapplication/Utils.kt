package com.example.weatherapplication

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.weatherapplication.model.AlertNotification
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.StoreLatitudeLongitude
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Constants for notification
const val notificationID = 121
const val channelID = "channel1"


fun getCurrentTime(dt : Long) : String{
    val data = Date(dt * 1000)
    val dateFormat = SimpleDateFormat("EEE , d/M/yyyy", Locale.US)
    return dateFormat.format(data)
}

fun getHourTime(dt : Long) : String{
    val data = Date(dt * 1000)
    val dateFormat = SimpleDateFormat("k a", Locale.US)
    return dateFormat.format(data)
}


fun getDay(dt: Long): String {
    val data = Date(dt * 1000)
    val dateFormat = SimpleDateFormat("E", Locale.US)
    return dateFormat.format(data)
}

sealed class StateDB{
    data class Success(val data: List<StoreLatitudeLongitude>):StateDB()
    data class Failure(val msg:Throwable):StateDB()
    object Loading:StateDB()

}

sealed class StateNotification{
    data class Success(val data: List<AlertNotification>):StateNotification()
    data class Failure(val msg:Throwable):StateNotification()
    object Loading:StateNotification()

}

sealed class StateRemote{
    data class Success(val data: Model):StateRemote()
    data class Failure(val msg:Throwable):StateRemote()
    object Loading:StateRemote()

}

fun getAddressEnglish(context: Context, lat: Double?, lon: Double?): String {
    val geocoder = Geocoder(context)
    val addressList: MutableList<Address>? = geocoder.getFromLocation(lat ?: 0.0, lon ?: 0.0, 1)

    if (addressList != null) {
        if (addressList.isEmpty()) {
            return "Unknown location"
        }
    }

    val address = addressList?.get(0)
    val city = address?.locality ?: "Unknown City"
    val country = address?.countryName ?: "Unknown Country"

    return "$city, $country"
}

fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

fun getMeasurementSystem(input: String): String {
    return when (input) {
        "Fahrenheit" -> "imperial"
        "Celsius" -> "metric"
        else -> ""
    }
}



