package com.example.weatherapplication.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.databinding.FragmentSettingsBinding
import com.example.weatherapplication.favorite.MapsActivity
import com.example.weatherapplication.favorite.view.FavoriteFragmentDirections
import com.example.weatherapplication.settings.MapsActivitySettings
import java.util.Locale

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("settings" , Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.rgTemperature.setOnCheckedChangeListener { group, checkedId ->
            Log.i("SettingsFragment", "Checked ID: $checkedId")
            val selectedUnit = when (checkedId) {
                R.id.btn_celsius -> {
                    getString(R.string.celsius)
                }
                R.id.btn_kelvin -> getString(R.string.kelvin)
                R.id.btn_fahrenheit -> getString(R.string.fahrenheit)
                else -> ""
            }
            editor.putString("selectedUnit" ,selectedUnit )
            editor.apply()
        }

        binding.rgLang.setOnCheckedChangeListener { group, checkedId ->
            val selectedLanguage = when (checkedId) {
                R.id.btn_english -> "en"
                R.id.btn_arabic -> "ar"
                else -> ""
            }

            val locale = Locale(selectedLanguage)
            Locale.setDefault(locale)
            val configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
            (requireActivity() as MainActivity).restart()

            editor.putString("selectedLanguage" ,selectedLanguage )
            editor.apply()
        }

        binding.rgLocation.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_gps -> {
                    // Handle GPS option selected
                    val selectedLocation = "GPS"
                    editor.putString("selectedLocation" ,selectedLocation )
                    editor.apply()
                    // Save selectedLocation to SharedPreferences or perform other actions
                }
                R.id.btn_map -> {
                    // Handle Map option selected
                    startActivity(Intent(requireContext(), MapsActivitySettings::class.java))
                    binding.rgLocation.clearCheck()
                }
            }

        }


        binding.rgWindSpeed.setOnCheckedChangeListener { group, checkedId ->
            val selectedWindSpeed = when (checkedId) {
                R.id.btn_ms -> "M / S"
                R.id.btn_mh -> "M / h"
                else -> ""
            }
            editor.putString("selectedWindSpeed" ,selectedWindSpeed )
            editor.apply()
        }

        binding.rgNotification.setOnCheckedChangeListener { group, checkedId ->
            val selectedNotification = when (checkedId) {
                R.id.btn_enable -> "enable"
                R.id.btn_disable -> "disable"
                else -> ""
            }
            editor.putString("selectedNotification" ,selectedNotification )
            editor.apply()
        }
    }


}