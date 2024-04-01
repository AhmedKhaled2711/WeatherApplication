package com.example.weatherapplication.favorite.view

import android.app.AlertDialog
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.favorite.MapsActivity
import com.example.weatherapplication.StateDB
import com.example.weatherapplication.databinding.FragmentFavoriteBinding
import com.example.weatherapplication.db.WeatherLocalDataSource
import com.example.weatherapplication.db.WeatherLocalDataSourceImpl
import com.example.weatherapplication.favorite.recyclerView.AdapterFav
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModel
import com.example.weatherapplication.favorite.viewModel.FavoriteViewModelFactory
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.RepositoryImpl
import com.example.weatherapplication.model.StoreLatitudeLongitude
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSource
import com.example.weatherapplication.remoteDataSource.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoriteFragment : Fragment() , OnRemoveClickListener , AdapterFav.OnItemClickListener{

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
        adapter.setOnItemClickListener(this)
    }

    override fun onRemoveClick(location: StoreLatitudeLongitude) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to remove this city?")
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteLocation(location)
            Toast.makeText(requireContext(), "City removed", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            Toast.makeText(requireContext(), "Deletion cancelled", Toast.LENGTH_LONG).show()
        }
        builder.show()
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
        val remoteDataSource : WeatherRemoteDataSource = WeatherRemoteDataSourceImpl.getInstance()
        val localDataSource : WeatherLocalDataSource =  WeatherLocalDataSourceImpl.getInstance(requireContext())
        val repository: Repository = RepositoryImpl(remoteDataSource ,localDataSource)

        val remoteFactory = FavoriteViewModelFactory(repository)

        viewModel = ViewModelProvider(this, remoteFactory).get(FavoriteViewModel::class.java)

        lifecycleScope.launch {
            viewModel.favoriteLocation.collectLatest {result ->
                when(result){
                    is StateDB.Loading ->{
                        binding.animationView.visibility = View.VISIBLE
                        binding.favoriteRV.visibility = View.GONE
                    }

                    is StateDB.Success ->{
                        if (result.data.isNotEmpty()) {
                            binding.animationView.visibility = View.GONE
                            binding.tvAnimation.visibility = View.GONE
                            binding.favoriteRV.visibility = View.VISIBLE
                            adapter.submitList(result.data)
                            binding.animationView.cancelAnimation()
                        } else {
                            binding.animationView.visibility = View.VISIBLE
                            binding.tvAnimation.visibility = View.VISIBLE
                            binding.favoriteRV.visibility = View.GONE
                        }
                    }

                    else ->{
                        binding.animationView.visibility = View.GONE
                        Log.i("Error", "Error: ")
                    }
                }

            }
        }

    }

    override fun onItemClick(item: StoreLatitudeLongitude) {
        Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(item)
        findNavController().navigate(action)
    }

}