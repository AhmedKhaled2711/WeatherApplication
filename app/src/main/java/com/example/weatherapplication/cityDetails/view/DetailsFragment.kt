package com.example.weatherapplication.cityDetails.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.cityDetails.viewModel.CityViewModel
import com.example.weatherapplication.cityDetails.viewModel.CityViewModelFactory
import com.example.weatherapplication.databinding.FragmentDetailsBinding
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.getAddressEnglish
import com.example.weatherapplication.getCurrentTime
import com.example.weatherapplication.home.recyclerView.DailyAdapter
import com.example.weatherapplication.home.recyclerView.HourlyAdapter
import com.example.weatherapplication.home.viewModel.HomeViewModel
import com.example.weatherapplication.home.viewModel.HomeViewModelFactory
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl

class DetailsFragment : Fragment() {

    private var lon = 0.0
    private var lat = 0.0
    private var selectedUnit: String? = null
    private lateinit var viewModel: CityViewModel
    private lateinit var viewModelFactory: CityViewModelFactory
    private lateinit var hourlyAdapter : HourlyAdapter
    private lateinit var dailyAdapter : DailyAdapter
    private lateinit var hourlyLayoutManager: LinearLayoutManager
    private lateinit var daillyLayoutManager: LinearLayoutManager
    private lateinit var location : StoreLatitudeLongitude

    private lateinit var binding: FragmentDetailsBinding
    private val args : DetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        location = args.locationDetails
        binding.ivBack.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToFavoriteFragment()
            findNavController().navigate(action)
        }


        setUpDailyRV()
        setUpHourlyRV()
        initViewModel()


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

    private fun initViewModel(){
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()

        val repository: Repository = RepositoryImpl(remoteDataSource , WeatherLocalDataSourceImpl.getInstance(requireContext()))

        val remoteFactory = CityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, remoteFactory)[CityViewModel::class.java]

        viewModel.getWeatherDetails(location.latitude , location.longitude)
        viewModel.weatherDetails.observe(viewLifecycleOwner){
                curr ->
            binding.temperature.text = curr.current.temp.toString()
            binding.pressureEdit.text = curr.current.pressure.toString()
            binding.humidityEdit.text = curr.current.humidity.toString()
            binding.windEdit.text = curr.current.wind_speed.toString()
            binding.cloudEdit.text = curr.current.clouds.toString()
            binding.ultravioletEdit.text = curr.current.uvi.toString()
            binding.visibilityEdit.text = curr.current.visibility.toString()
            binding.cityCountry.text = getAddressEnglish(requireActivity(), lat,lon)
            Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                    + curr.current.weather[0].icon+"@4x.png").into(binding.iv)
            binding.descOfWeather.text  = curr.current.weather[0].description
            binding.today.text = getCurrentTime(curr.current.dt)


            dailyAdapter.submitList(curr.daily.subList(0,7))
            hourlyAdapter.submitList(curr.hourly.subList(0,24))

        }
    }

}