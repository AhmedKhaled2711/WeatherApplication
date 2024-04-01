package com.example.weatherapplication.settings

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.weatherapplication.databinding.ActivityMapsSettingsBinding
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModelFactory
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import com.example.weatherapplication.settings.view.SettingsFragment
import com.google.android.gms.maps.model.Marker
import java.io.IOException
import java.util.Locale
import kotlin.math.log

class MapsActivitySettings : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsSettingsBinding
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var selectedPlace: String
    private lateinit var showSelectedLocation: TextView
    private lateinit var spMap: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapSettings) as SupportMapFragment
        mapFragment.getMapAsync(this)

        spMap = this.getSharedPreferences("MapLocation" , Context.MODE_PRIVATE)
        editor = spMap.edit()

        initViewModel()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val currentLocation = LatLng(30.5972, 30.9876)

        val marker = this.googleMap.addMarker(
            MarkerOptions().position(currentLocation).draggable(true)
        )

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(5f))

        this.googleMap.setOnMapClickListener { p0 ->
            if (marker != null) {
                marker.position = p0
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        binding.showSelectedLocationTextView.text = addresses[0].adminArea
                        marker.title = addresses[0].adminArea
                        latitude = marker.position.latitude.toString()
                        longitude = marker.position.longitude.toString()
                        selectedPlace = addresses[0].adminArea
                    }
                } catch (exception: Exception) {
                    Log.e("TAG", "onMapClick:exception--------------------> ${exception.message}")

                }
            }
        }

        this.googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                val geocoder = Geocoder(this@MapsActivitySettings, Locale.getDefault())
                val addresses: List<Address>?
                try {
                    addresses = geocoder.getFromLocation(
                        marker.position.latitude,
                        marker.position.longitude,
                        1
                    )
                    if (!addresses.isNullOrEmpty()) {
                        showSelectedLocation.text = addresses[0].adminArea
                        latitude = marker.position.latitude.toString()
                        longitude = marker.position.longitude.toString()
                        selectedPlace = addresses[0].adminArea
                        marker.title = selectedPlace
                    }
                } catch (exception: IOException) {
                    Log.e("TAG", "onMarkerDragEnd: exception-----------------> ${exception.message} ")

                    finish()
                    Log.i("Tag", "onMarkerDragEnd: $latitude $longitude ")
                }
            }
        })

    }

    private fun initViewModel(){

        binding.btnAddLocation.setOnClickListener {

            Log.i("map", "Map: ${latitude.toDouble()} ")
            Log.i("map", "Map: ${longitude.toDouble()} ")
            Log.i("map", "Map: ${selectedPlace} ")

            editor.putString("latitudeSettings", latitude.toDouble().toString())
            editor.putString("longitudeSettings", longitude.toDouble().toString())
            editor.apply()

            finish()
        }
    }
}