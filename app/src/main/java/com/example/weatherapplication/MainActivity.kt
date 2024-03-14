package com.example.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.weatherapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*binding.bottomNav.setOnClickListener{ menuItem ->
            when(menuItem.id){
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.favorite -> {
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false

            }

        }*/
        
        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.home -> HomeFragment()
                R.id.search -> SearchFragment()
                R.id.favorite -> FavoriteFragment()
                R.id.settings -> SettingsFragment()
                else -> return@setOnNavigationItemSelectedListener false
            }
            replaceFragment(fragment)
            true
        }
        replaceFragment(FavoriteFragment())

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container , fragment)
            .commit()
    }
}