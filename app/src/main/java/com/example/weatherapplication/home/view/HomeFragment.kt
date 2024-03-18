package com.example.weatherapplication.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentHomeBinding
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.home.viewModel.HomeViewModel
import com.example.weatherapplication.home.viewModel.HomeViewModelFactory
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        Log.i("Tag", " Tesssssssssssssssssssst ")
    }

    private fun initViewModel(){
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
        val localDataSource: WeatherLocalDataSource
        val repository: Repository = RepositoryImpl(remoteDataSource)

        val remoteFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, remoteFactory).get(HomeViewModel::class.java)

        viewModel.weatherDetails.observe(viewLifecycleOwner){
               curr ->
            Log.i("Tag", "initViewModel: ${curr.city.name} ")
            Log.i("Tag", "initViewModel: ${curr.city.country} ")
            Log.i("Tag", "initViewModel: ${curr.message} ")
            Log.i("Tag", "initViewModel: ${curr.list.get(0).weather.get(0).description} ")


        }
    }

}