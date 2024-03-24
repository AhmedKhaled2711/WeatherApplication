package com.example.weatherapplication.favorite.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.MapsActivity
import com.example.weatherapplication.StateDatabase
import com.example.weatherapplication.databinding.FragmentFavoriteBinding
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.favorite.recyclerView.AdapterFav
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModelFactory
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() , OnRemoveClickListener{

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: AdapterFav
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddLocation.setOnClickListener{
            startActivity(Intent(requireContext(), MapsActivity::class.java))
        }
        setUpRecyclerView()
        initViewModel()
    }

    override fun onRemoveClick(location: StoreLatitudeLongitude) {
        viewModel.deleteLocation(location)
        Toast.makeText(requireContext(), "Product removed from Favourites", Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView(){
        var manager = LinearLayoutManager(requireContext())
        manager.orientation = RecyclerView.VERTICAL
        binding.favoriteRV.layoutManager = manager

        adapter = AdapterFav(this, requireActivity())
        adapter.submitList(emptyList())
        binding.favoriteRV.adapter = adapter
    }

      fun initViewModel(){
//        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
//
//        val repository: Repository = RepositoryImpl(remoteDataSource , WeatherLocalDataSourceImpl.getInstance(this))
//
//        val remoteFactory = FavoriteViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, remoteFactory)[FavoriteViewModel::class.java]

        val factory = FavoriteViewModelFactory(
            RepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                WeatherLocalDataSourceImpl.getInstance(requireContext())

            )
        )

        viewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        lifecycleScope.launch {
            viewModel._favoriteLocation.collectLatest {result ->
                when(result){
                    is StateDatabase.Loading ->{
                        //binding.progressBar.visibility = View.VISIBLE
                        //binding.rv.visibility = View.GONE
                    }

                    is StateDatabase.Success ->{
                       // binding.progressBar.visibility = View.GONE
                        //binding.rv.visibility = View.VISIBLE
                        adapter.submitList(result.data)
                    }

                    else ->{
                        //binding.progressBar.visibility = View.GONE
                        //Log.i("Error", "Error: ")
                    }
                }

            }
        }

    }

}