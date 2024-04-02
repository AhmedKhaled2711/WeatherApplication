package com.example.weatherapplication.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.channelID
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.model.Model
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.notificationID
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Notification : BroadcastReceiver() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var sharedPreferencesLocation: SharedPreferences
    private lateinit var spNotification: SharedPreferences
    private var lon = 0.0
    private var lat = 0.0
    private lateinit var selectedNotification: String
    override fun onReceive(context: Context, intent: Intent) {
        spNotification = context.getSharedPreferences("settings" , Context.MODE_PRIVATE)
        selectedNotification = spNotification.getString("selectedNotification" , "" ).toString()
        Log.i("sp", "$selectedNotification ")
        if (selectedNotification == "enable")
        {
            playSong(context)
            CoroutineScope(Dispatchers.IO).launch {
                val weather = getAlertNotification(context)
                val i = Intent(context, MainActivity::class.java)

                val newIntent = i.apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, newIntent,
                    PendingIntent.FLAG_IMMUTABLE)

                val bigTextStyle = NotificationCompat.BigTextStyle()
                bigTextStyle.bigText(weather.alerts?.get(0)?.description)
                bigTextStyle.setBigContentTitle(weather.timezone.split("/")[1])

                val notification = NotificationCompat.Builder(context, channelID)
                    .setSmallIcon(R.drawable.ic_alert)
                    .setContentTitle(weather.timezone.split("/")[1])
                    .setContentText(weather.alerts?.get(0)?.description)
                    .setStyle(bigTextStyle)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                manager.notify(notificationID, notification)
            }
        }
        else
        {
            // Notification off
        }



    }

    private fun playSong(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.relax)
        mediaPlayer?.start()
    }

    private suspend fun getAlertNotification(context: Context): Model {
        sharedPreferencesLocation =
            context.getSharedPreferences("locationKey", Context.MODE_PRIVATE)
        lon = sharedPreferencesLocation.getString("longitude", "0")!!.toDouble()
        lat = sharedPreferencesLocation.getString("latitude", "0")!!.toDouble()

        Log.i("not", "onReceive: $lon  $lat ")

        val remoteDataSource: WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
        val localDataSource: WeatherLocalDataSource =
            WeatherLocalDataSourceImpl.getInstance(context)
        val repository: Repository = RepositoryImpl(remoteDataSource, localDataSource)

        //Log.i("not", "onReceive: ${weather.alerts?.get(0)} ")
        return repository.getWeather(lat, lon, "metric", "en")
    }
}

