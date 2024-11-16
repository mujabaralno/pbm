package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StopwatchFragment())
                .commit()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_stopwatch -> {
                    // Handle the Stopwatch fragment navigation
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StopwatchFragment())
                        .commit()
                    true
                }
                R.id.navigation_timer -> {
                    // Handle the Timer fragment navigation
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TimerFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
