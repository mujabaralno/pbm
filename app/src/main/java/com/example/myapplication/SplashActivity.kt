package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySplashBinding

/*
Splash screen akan muncul pertama kali.
Pastikan untuk mengatur SplashActivity sebagai aktivitas pertama
di file AndroidManifest dengan menambahkan:
android:name="android.intent.category.LAUNCHER"
android:name="android.intent.action.MAIN"
atau ubah replace MainActivity dengan SplashActivity
*/

class SplashActivity : AppCompatActivity() {

    // Deklarasi binding
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menggunakan binding untuk setContentView
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menampilkan splash screen selama 3 detik sebelum pindah ke OnboardingActivity
        Handler(Looper.getMainLooper()).postDelayed({
            goToOnboarding()
        }, 3000L)
    }

    // Fungsi untuk berpindah ke OnboardingActivity setelah splash screen
    private fun goToOnboarding() {
        Intent(this, OnboardingActivity::class.java).also {
            startActivity(it)
            finish() // Mengakhiri SplashActivity agar tidak kembali saat back pressed
        }
    }
}