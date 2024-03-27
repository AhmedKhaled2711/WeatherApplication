package com.example.weatherapplication.cityDetails.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.StateRemote
import com.example.weatherapplication.cityDetails.viewModel.CityViewModel
import com.example.weatherapplication.cityDetails.viewModel.CityViewModelFactory
import com.example.weatherapplication.databinding.FragmentDetailsBinding
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
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
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var lon = 0.0
    private var lat = 0.0
    private lateinit var selectedUnit: String
    private lateinit var selectedLanguage: String
    private lateinit var selectedLocation: String
    private lateinit var selectedWindSpeed: String
    private lateinit var selectedNotification: String
    private lateinit var sharedPreferencesLocation: SharedPreferences
    private lateinit var spSelectedUnit: SharedPreferences
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

        lifecycleScope.launch {
            if (isNetworkConnected(requireContext())) {
                Toast.makeText(requireContext() , "Data from Network", Toast.LENGTH_SHORT).show()
                Log.i("TAG", "initViewModel:$lon ")
                Log.i("TAG", "initViewModel:$lat ")

                viewModel.getWeatherDetails(lat , lon , getMeasurementSystem(selectedUnit),selectedLanguage )
                lifecycleScope.launch {
                    viewModel.weatherDetails.collectLatest { result ->
                        when(result){
                            is StateRemote.Loading ->{
                                binding.progressBar.visibility = View.VISIBLE
                                binding.DailyRv.visibility = View.GONE
                                binding.weeklyRV.visibility = View.GONE
                                binding.homeConstraint.visibility = View.GONE
                                binding.newConstraint.visibility = View.GONE
                            }

                            is StateRemote.Success ->{
                                binding.progressBar.visibility = View.GONE
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
                                Log.i("TAG", "getAddressEnglish:$lon ")
                                Log.i("TAG", "getAddressEnglish:$lat ")
                                binding.cityCountry.text = getAddressEnglish(requireContext(), lat,lon)
                                Glide.with(requireContext()).load("https://openweathermap.org/img/wn/"
                                        + result.data.current.weather[0].icon+"@4x.png").into(binding.iv)
                                binding.descOfWeather.text  = result.data.current.weather[0].description
                                binding.today.text = getCurrentTime(result.data.current.dt)

                                dailyAdapter.submitList(result.data.daily.subList(0,7))
                                hourlyAdapter.submitList(result.data.hourly.subList(0,24))
                            }

                            else ->{
                                binding.progressBar.visibility = View.GONE
                                Log.i("Error", "Error: ")
                            }
                        }
                    }
                }

            }else{
                Toast.makeText(requireContext() , "Check Internet connection", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Check Internet connection")
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = builder.create()
                alertDialog.show()

            }

        }
    }
}


