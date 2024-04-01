package com.example.weatherapplication

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

class MainActivity : AppCompatActivity() {

    val locationRequestID = 1000
    private lateinit var selectedLanguage: String
    private lateinit var spSettings: SharedPreferences
    private lateinit var locationCallback : LocationCallback
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        spSettings= this.getSharedPreferences("settings" , Context.MODE_PRIVATE)
        selectedLanguage = spSettings.getString("selectedLanguage" , "" ).toString()
        Log.i("sp", "$selectedLanguage ")

        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        bottomNavigationView = findViewById(R.id.bottomNav)

        sharedPreferences = this.getSharedPreferences("locationKey" , Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION),locationRequestID
            )

            return
        }




        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest:LocationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult)
            {
                Log.i("TAG", locationResult.toString())
                // Toast.makeText(this@MainActivity ,locationResult.toString() , Toast.LENGTH_LONG ).show()
                editor.putString("longitude" , locationResult.lastLocation?.longitude.toString())
                editor.putString("latitude",locationResult.lastLocation?.latitude.toString())
                editor.apply()

            }
        }

        locationProviderClient.requestLocationUpdates(locationRequest , locationCallback , Looper.myLooper())


    }


    fun restart() {
        val intent = intent
        finish()
        startActivity(intent)
    }

}