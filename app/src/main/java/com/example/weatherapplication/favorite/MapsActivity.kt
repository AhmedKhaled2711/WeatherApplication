package com.example.weatherapplication.favorite

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
import com.example.weatherapplication.databinding.ActivityMapsBinding
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModelFactory
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import com.google.android.gms.maps.model.Marker
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding:ActivityMapsBinding
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var selectedPlace: String
    private lateinit var showSelectedLocation: TextView
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapSettings) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
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
                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
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
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()

        val repository: Repository = RepositoryImpl(remoteDataSource , WeatherLocalDataSourceImpl.getInstance(this))

        val remoteFactory = FavoriteViewModelFactory(repository)

        viewModel = ViewModelProvider(this, remoteFactory)[FavoriteViewModel::class.java]

        binding.btnAddLocation.setOnClickListener {
            viewModel.insertFavorite(
                StoreLatitudeLongitude(
                    latitude.toDouble(),
                    longitude.toDouble(),
                    selectedPlace,
                )

            )
            finish()
        }
    }
}