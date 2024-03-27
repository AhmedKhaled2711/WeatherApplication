package com.example.weatherapplication.home.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.R
import com.example.weatherapplication.cityDetails.view.DetailsFragmentArgs
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.favorite.recyclerView.AdapterFav
import com.example.weatherapplication.getAddressEnglish
import com.example.weatherapplication.getCurrentTime
import com.example.weatherapplication.getMeasurementSystem
import com.example.weatherapplication.home.recyclerView.DailyAdapter
import com.example.weatherapplication.home.recyclerView.HourlyAdapter
import com.example.weatherapplication.home.viewModel.HomeViewModel
import com.example.weatherapplication.home.viewModel.HomeViewModelFactory
import com.example.weatherapplication.isNetworkConnected
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import kotlin.math.log


class HomeFragment : Fragment() {

    private var lon = 0.0
    private var lat = 0.0
    private lateinit var selectedUnit: String
    private lateinit var selectedLanguage: String
    private lateinit var selectedLocation: String
    private lateinit var selectedWindSpeed: String
    private lateinit var selectedNotification: String
    private lateinit var sharedPreferencesLocation: SharedPreferences
    private lateinit var spSelectedUnit: SharedPreferences
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var hourlyAdapter : HourlyAdapter
    private lateinit var dailyAdapter : DailyAdapter
    private lateinit var hourlyLayoutManager: LinearLayoutManager
    private lateinit var daillyLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesLocation = requireActivity().getSharedPreferences("locationKey" , Context.MODE_PRIVATE)
        lon = sharedPreferencesLocation.getString("longitude" , "0")!!.toDouble()
        lat = sharedPreferencesLocation.getString("latitude" , "0")!!.toDouble()

        spSelectedUnit= requireActivity().getSharedPreferences("settings" , Context.MODE_PRIVATE)
        selectedUnit = spSelectedUnit.getString("selectedUnit" , "" ).toString()
        Log.i("sp", "$selectedUnit ")
        selectedLanguage = spSelectedUnit.getString("selectedLanguage" , "" ).toString()
        Log.i("sp", "$selectedLanguage ")
        selectedLocation = spSelectedUnit.getString("selectedLocation" , "" ).toString()
        Log.i("sp", "$selectedLocation ")
        selectedWindSpeed = spSelectedUnit.getString("selectedWindSpeed" , "" ).toString()
        Log.i("sp", "$selectedWindSpeed ")
        selectedNotification = spSelectedUnit.getString("selectedNotification" , "" ).toString()
        Log.i("sp", "$selectedNotification ")

        Log.i("TAG", "onViewCreated: $lon")
        Log.i("TAG", "onViewCreated: $lat")
        setUpDailyRV()
        setUpHourlyRV()
        initViewModel()





    }

    private fun initViewModel(){
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()

        val repository: Repository = RepositoryImpl(remoteDataSource , WeatherLocalDataSourceImpl.getInstance(requireContext()))

        val remoteFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, remoteFactory)[HomeViewModel::class.java]


        lifecycleScope.launch {
            if (isNetworkConnected(requireContext())) {
                Toast.makeText(requireContext() , "Data from Network", Toast.LENGTH_SHORT).show()
                Log.i("TAG", "initViewModel:$lon ")
                Log.i("TAG", "initViewModel:$lat ")

                viewModel.getWeatherDetails(lat , lon , getMeasurementSystem(selectedUnit),selectedLanguage )
                viewModel.weatherDetails.observe(viewLifecycleOwner){
                        curr ->
                    binding.temperature.text = curr.current.temp.toString()
                    binding.pressureEdit.text = curr.current.pressure.toString()
                    binding.humidityEdit.text = curr.current.humidity.toString()
                    binding.windEdit.text = curr.current.wind_speed.toString()
                    binding.cloudEdit.text = curr.current.clouds.toString()
                    binding.ultravioletEdit.text = curr.current.uvi.toString()
                    binding.visibilityEdit.text = curr.current.visibility.toString()
                    Log.i("TAG", "getAddressEnglish:$lon ")
                    Log.i("TAG", "getAddressEnglish:$lat ")
                    binding.cityCountry.text = getAddressEnglish(requireContext(), lat,lon)
                    Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                            + curr.current.weather[0].icon+"@4x.png").into(binding.iv)
                    binding.descOfWeather.text  = curr.current.weather[0].description
                    binding.today.text = getCurrentTime(curr.current.dt)

                    viewModel.insertCurrentWeather(curr)
                    dailyAdapter.submitList(curr.daily.subList(0,7))
                    hourlyAdapter.submitList(curr.hourly.subList(0,24))

                }
            } else {
                Toast.makeText(requireContext() , "Data from DB", Toast.LENGTH_SHORT).show()
                viewModel.getCurrentWeather()
                viewModel.weatherDetails.observe(viewLifecycleOwner){
                        curr ->
                    binding.temperature.text = curr.current.temp.toString()
                    binding.pressureEdit.text = curr.current.pressure.toString()
                    binding.humidityEdit.text = curr.current.humidity.toString()
                    binding.windEdit.text = curr.current.wind_speed.toString()
                    binding.cloudEdit.text = curr.current.clouds.toString()
                    binding.ultravioletEdit.text = curr.current.uvi.toString()
                    binding.visibilityEdit.text = curr.current.visibility.toString()
                    binding.cityCountry.text = getAddressEnglish(requireContext(), lat,lon)
                    Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                            + curr.current.weather[0].icon+"@4x.png").into(binding.iv)
                    binding.descOfWeather.text  = curr.current.weather[0].description
                    binding.today.text = getCurrentTime(curr.current.dt)
                    dailyAdapter.submitList(curr.daily.subList(0,7))
                    hourlyAdapter.submitList(curr.hourly.subList(0,24))

                }
            }
        }

    }

    private fun setUpDailyRV(){
        daillyLayoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL, false)
        dailyAdapter = DailyAdapter(requireContext())
        binding.weeklyRV.apply {
            adapter = dailyAdapter
            layoutManager = daillyLayoutManager
        }
    }

    private fun setUpHourlyRV(){
        hourlyLayoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.HORIZONTAL, false)
        hourlyAdapter = HourlyAdapter(requireContext())
        binding.DailyRv.apply {
            adapter = hourlyAdapter
            layoutManager = hourlyLayoutManager
        }
    }


    fun getCityAndCountryFromCoordinates(context: Context, latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var city: String? = null
        var country: String? = null

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                city = address.locality
                country = address.countryName
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val locationText = StringBuilder()
        if (!city.isNullOrEmpty()) {
            locationText.append(city)
        }
        if (!country.isNullOrEmpty()) {
            if (locationText.isNotEmpty()) {
                locationText.append(", ")
            }
            locationText.append(country)
        }

        return locationText.toString()
    }




}