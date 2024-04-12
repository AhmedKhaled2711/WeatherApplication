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
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.StateRemote
import com.example.weatherapplication.cityDetails.view.DetailsFragmentArgs
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.db.WeatherLocalDataSource
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import kotlin.math.log


class HomeFragment : Fragment() {

    private var lon = 0.0
    private var lat = 0.0
    private var lonMap = 0.0
    private var latMap = 0.0
    private lateinit var selectedUnit: String
    private lateinit var selectedLanguage: String
    private lateinit var selectedLocation: String
    private lateinit var selectedWindSpeed: String
    private lateinit var selectedNotification: String
    private lateinit var sharedPreferencesLocation: SharedPreferences
    private lateinit var spSettings: SharedPreferences
    private lateinit var spMapLocation: SharedPreferences
    private lateinit var viewModel: HomeViewModel
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
        //Map
        spMapLocation = requireActivity().getSharedPreferences("MapLocation" , Context.MODE_PRIVATE)
        lonMap = spMapLocation.getString("longitudeSettings" , "0")!!.toDouble()
        latMap = spMapLocation.getString("latitudeSettings" , "0")!!.toDouble()

        //GPS
        sharedPreferencesLocation = requireActivity().getSharedPreferences("locationKey" , Context.MODE_PRIVATE)
        lon = sharedPreferencesLocation.getString("longitude" , "30.9782")!!.toDouble()
        lat = sharedPreferencesLocation.getString("latitude" , "30.2945")!!.toDouble()

        spSettings= requireActivity().getSharedPreferences("settings" , Context.MODE_PRIVATE)
        selectedUnit = spSettings.getString("selectedUnit" , "" ).toString()
        Log.i("sp", "$selectedUnit ")
        selectedLanguage = spSettings.getString("selectedLanguage" , "" ).toString()
        Log.i("sp", "$selectedLanguage ")
        selectedLocation = spSettings.getString("selectedLocation" , "" ).toString()
        Log.i("sp", "$selectedLocation ")
        selectedWindSpeed = spSettings.getString("selectedWindSpeed" , "" ).toString()
        Log.i("sp", "$selectedWindSpeed ")
        selectedNotification = spSettings.getString("selectedNotification" , "" ).toString()
        Log.i("sp", "$selectedNotification ")


        setUpDailyRV()
        setUpHourlyRV()
        initViewModel()



    }

    private fun initViewModel(){
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
        val localDataSource : WeatherLocalDataSource =  WeatherLocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource ,localDataSource)
        val remoteFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, remoteFactory)[HomeViewModel::class.java]

        if (selectedLocation == "GPS"){
            lifecycleScope.launch {
                if (isNetworkConnected(requireContext())) {
                    Toast.makeText(requireContext() , "Data from Network", Toast.LENGTH_SHORT).show()

                    viewModel.getWeatherDetails(lat , lon , getMeasurementSystem(selectedUnit),selectedLanguage )
                    lifecycleScope.launch {
                        viewModel.weatherDetails.collectLatest { result ->
                            when(result){
                                is StateRemote.Loading ->{
                                    binding.progressBarHome.visibility = View.VISIBLE
                                    binding.DailyRv.visibility = View.GONE
                                    binding.weeklyRV.visibility = View.GONE
                                    binding.homeConstraint.visibility = View.GONE
                                    binding.newConstraint.visibility = View.GONE
                                }

                                is StateRemote.Success ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    binding.DailyRv.visibility = View.VISIBLE
                                    binding.weeklyRV.visibility = View.VISIBLE
                                    binding.homeConstraint.visibility = View.VISIBLE
                                    binding.newConstraint.visibility =View.VISIBLE
                                    TemperatureDegree(selectedUnit)
                                    binding.temperature.text = result.data.current.temp.toString()
                                    binding.pressureEdit.text = result.data.current.pressure.toString()
                                    binding.humidityEdit.text = result.data.current.humidity.toString()
                                    binding.windEdit.text = result.data.current.wind_speed.toString()
                                    binding.cloudEdit.text = result.data.current.clouds.toString()
                                    binding.ultravioletEdit.text = result.data.current.uvi.toString()
                                    binding.visibilityEdit.text = result.data.current.visibility.toString()
                                    binding.cityCountry.text =  result.data.timezone.split("/")[1]
                                    Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                                            + result.data.current.weather[0].icon+"@4x.png").into(binding.iv)
                                    binding.descOfWeather.text  = result.data.current.weather[0].description
                                    binding.today.text = getCurrentTime(result.data.current.dt)

                                    viewModel.insertCurrentWeather(result.data)
                                    dailyAdapter.submitList(result.data.daily.subList(0,7))
                                    hourlyAdapter.submitList(result.data.hourly.subList(0,24))
                                }

                                else ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    Log.i("Error", "Error: ")
                                }
                            }
                        }
                    }

                } else {
                    viewModel.getCurrentWeather()
                    lifecycleScope.launch {
                        viewModel.weatherDetailsDB.collectLatest { result ->
                            when(result){
                                is StateRemote.Loading ->{
                                    binding.progressBarHome.visibility = View.VISIBLE
                                    binding.DailyRv.visibility = View.GONE
                                    binding.weeklyRV.visibility = View.GONE
                                    binding.homeConstraint.visibility = View.GONE
                                    binding.newConstraint.visibility = View.GONE
                                }

                                is StateRemote.Success ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    binding.DailyRv.visibility = View.VISIBLE
                                    binding.weeklyRV.visibility = View.VISIBLE
                                    binding.homeConstraint.visibility = View.VISIBLE
                                    binding.newConstraint.visibility =View.VISIBLE
                                    binding.temperature.text = result.data.current.temp.toString()
                                    binding.pressureEdit.text = result.data.current.pressure.toString()
                                    binding.humidityEdit.text = result.data.current.humidity.toString()
                                    binding.windEdit.text = result.data.current.wind_speed.toString()
                                    binding.cloudEdit.text = result.data.current.clouds.toString()
                                    binding.ultravioletEdit.text = result.data.current.uvi.toString()
                                    binding.visibilityEdit.text = result.data.current.visibility.toString()

                                    binding.cityCountry.text =  result.data.timezone.split("/")[1]
                                    Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                                            + result.data.current.weather[0].icon+"@4x.png").into(binding.iv)
                                    binding.descOfWeather.text  = result.data.current.weather[0].description
                                    binding.today.text = getCurrentTime(result.data.current.dt)

                                    //viewModel.insertCurrentWeather(result.data)
                                    dailyAdapter.submitList(result.data.daily.subList(0,7))
                                    hourlyAdapter.submitList(result.data.hourly.subList(0,24))
                                }

                                else ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    Log.i("Error", "Error: ")
                                }
                            }
                        }
                    }

                }
            }
        }
        else{
            //Map
            lifecycleScope.launch {
                if (isNetworkConnected(requireContext())) {
                    Toast.makeText(requireContext() , "Data from Network", Toast.LENGTH_SHORT).show()

                    viewModel.getWeatherDetails(latMap , lonMap , getMeasurementSystem(selectedUnit),selectedLanguage )
                    lifecycleScope.launch {
                        viewModel.weatherDetails.collectLatest { result ->
                            when(result){
                                is StateRemote.Loading ->{
                                    binding.progressBarHome.visibility = View.VISIBLE
                                    binding.DailyRv.visibility = View.GONE
                                    binding.weeklyRV.visibility = View.GONE
                                    binding.homeConstraint.visibility = View.GONE
                                    binding.newConstraint.visibility = View.GONE
                                }

                                is StateRemote.Success ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    binding.DailyRv.visibility = View.VISIBLE
                                    binding.weeklyRV.visibility = View.VISIBLE
                                    binding.homeConstraint.visibility = View.VISIBLE
                                    binding.newConstraint.visibility =View.VISIBLE
                                    TemperatureDegree(selectedUnit)
                                    binding.temperature.text = result.data.current.temp.toString()
                                    binding.pressureEdit.text = result.data.current.pressure.toString()
                                    binding.humidityEdit.text = result.data.current.humidity.toString()
                                    binding.windEdit.text = result.data.current.wind_speed.toString()
                                    binding.cloudEdit.text = result.data.current.clouds.toString()
                                    binding.ultravioletEdit.text = result.data.current.uvi.toString()
                                    binding.visibilityEdit.text = result.data.current.visibility.toString()
                                    binding.cityCountry.text =  result.data.timezone.split("/")[1]
                                    Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                                            + result.data.current.weather[0].icon+"@4x.png").into(binding.iv)
                                    binding.descOfWeather.text  = result.data.current.weather[0].description
                                    binding.today.text = getCurrentTime(result.data.current.dt)

                                    viewModel.insertCurrentWeather(result.data)
                                    dailyAdapter.submitList(result.data.daily.subList(0,7))
                                    hourlyAdapter.submitList(result.data.hourly.subList(0,24))
                                }

                                else ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    Log.i("Error", "Error: ")
                                }
                            }
                        }
                    }

                } else {
                    viewModel.getCurrentWeather()
                    lifecycleScope.launch {
                        viewModel.weatherDetailsDB.collectLatest { result ->
                            when(result){
                                is StateRemote.Loading ->{
                                    binding.progressBarHome.visibility = View.VISIBLE
                                    binding.DailyRv.visibility = View.GONE
                                    binding.weeklyRV.visibility = View.GONE
                                    binding.homeConstraint.visibility = View.GONE
                                    binding.newConstraint.visibility = View.GONE
                                }

                                is StateRemote.Success ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    binding.DailyRv.visibility = View.VISIBLE
                                    binding.weeklyRV.visibility = View.VISIBLE
                                    binding.homeConstraint.visibility = View.VISIBLE
                                    binding.newConstraint.visibility =View.VISIBLE
                                    TemperatureDegree(selectedUnit)
                                    binding.temperature.text = result.data.current.temp.toString()
                                    binding.pressureEdit.text = result.data.current.pressure.toString()
                                    binding.humidityEdit.text = result.data.current.humidity.toString()
                                    binding.windEdit.text = result.data.current.wind_speed.toString()
                                    binding.cloudEdit.text = result.data.current.clouds.toString()
                                    binding.ultravioletEdit.text = result.data.current.uvi.toString()
                                    binding.visibilityEdit.text = result.data.current.visibility.toString()

                                    binding.cityCountry.text =  result.data.timezone.split("/")[1]
                                    Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                                            + result.data.current.weather[0].icon+"@4x.png").into(binding.iv)
                                    binding.descOfWeather.text  = result.data.current.weather[0].description
                                    binding.today.text = getCurrentTime(result.data.current.dt)

                                    dailyAdapter.submitList(result.data.daily.subList(0,7))
                                    hourlyAdapter.submitList(result.data.hourly.subList(0,24))
                                }

                                else ->{
                                    binding.progressBarHome.visibility = View.GONE
                                    Log.i("Error", "Error: ")
                                }
                            }
                        }
                    }

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

    fun TemperatureDegree( unit : String) : String{
        when(unit){
            "Fahrenheit" ->  binding.MeasurementTemperatureDegree.text = "F"
            "Celsius" -> binding.MeasurementTemperatureDegree.text = "C"
            else -> binding.MeasurementTemperatureDegree.text = "K"
        }
        return unit
    }




}