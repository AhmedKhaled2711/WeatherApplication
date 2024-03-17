package com.example.weatherapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val splashDuration: Long = 1000 // Duration in milliseconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Post a delayed action to start the next activity after splashDuration milliseconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Start the next activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the splash screen activity
        }, splashDuration)

        //startActivity(Intent(this , MainActivity::class.java))
    }
}