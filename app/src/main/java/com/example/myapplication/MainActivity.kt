package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Deklarasi binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan binding untuk setContentView
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan StopwatchFragment saat membuka MainActivity
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StopwatchFragment())
                .commit()
        }

        // Atur item untuk BottomNavigationView menggunakan binding
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_stopwatch -> {
                    // Navigasi ke StopwatchFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StopwatchFragment())
                        .commit()
                    true
                }
                R.id.navigation_timer -> {
                    // Navigasi ke TimerFragment
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
