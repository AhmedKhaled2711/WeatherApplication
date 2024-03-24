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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.getCurrentTime
import com.example.weatherapplication.home.viewModel.HomeViewModel
import com.example.weatherapplication.home.viewModel.HomeViewModelFactory
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale


class HomeFragment : Fragment() {

    private var lon = 0.0
    private var lat = 0.0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var hourlyAdapter : HourlyAdapter
    private lateinit var dailyAdapter : DailyAdapter
    private lateinit var hourlyLayoutManager: LinearLayoutManager
    private lateinit var daillyLayoutManager: LinearLayoutManager
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("locationKey" , Context.MODE_PRIVATE)
        lon = sharedPreferences.getString("longitude" , "0")!!.toDouble()
        lat = sharedPreferences.getString("latitude" , "0")!!.toDouble()

        initViewModel()
        binding.cityCountry.text = getCityAndCountryFromCoordinates(requireActivity(),lat,lon)



    }

    private fun initViewModel(){
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
        val localDataSource: WeatherLocalDataSource
        val repository: Repository = RepositoryImpl(remoteDataSource)

        val remoteFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, remoteFactory)[HomeViewModel::class.java]

        viewModel.getWeatherDetails(lat , lon)
        viewModel.weatherDetails.observe(viewLifecycleOwner){
               curr ->
            binding.temperature.text = curr.current.temp.toString()
            binding.pressureEdit.text = curr.current.pressure.toString()
            binding.humidityEdit.text = curr.current.humidity.toString()
            binding.windEdit.text = curr.current.wind_speed.toString()
            binding.cloudEdit.text = curr.current.clouds.toString()
            binding.ultravioletEdit.text = curr.current.uvi.toString()
            binding.visibilityEdit.text = curr.current.visibility.toString()
            Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"+ curr.current.weather[0].icon+"@4x.png")
                .into(binding.iv)
            binding.descOfWeather.text  = curr.current.weather[0].description
            binding.today.text = getCurrentTime(curr.current.dt)

            viewModelFactory = HomeViewModelFactory(
                RepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl.getInstance()))
            viewModel = ViewModelProvider(this,viewModelFactory)[HomeViewModel::class.java]

            daillyLayoutManager = LinearLayoutManager(requireContext(),
                RecyclerView.VERTICAL, false)
            dailyAdapter = DailyAdapter(requireContext())
            binding.weeklyRV.apply {
                adapter = dailyAdapter
                layoutManager = daillyLayoutManager
            }
            dailyAdapter.submitList(curr.daily.subList(0,7))

            hourlyLayoutManager = LinearLayoutManager(requireContext(),
                RecyclerView.HORIZONTAL, false)
            hourlyAdapter = HourlyAdapter(requireContext())
            binding.DailyRv.apply {
                adapter = hourlyAdapter
                layoutManager = hourlyLayoutManager
            }
            hourlyAdapter.submitList(curr.hourly.subList(0,24))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        Log.i("geo", "getCityAndCountryFromCoordinates: ${locationText }+ ${latitude}+ ${longitude}")
        return locationText.toString()
    }

}