package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2


// Data untuk halaman onboarding
data class OnboardingPage(
    val imageRes: Int, // ID resource gambar
    val title: String, // Judul halaman
    val description: String // Deskripsi halaman
)

class OnboardingActivity : AppCompatActivity() {

    private var isLastSlide = false // Menandai apakah pengguna berada di slide terakhir

    // Data onboarding
    private val onboardingData = listOf(
        OnboardingPage(R.drawable.jam1, "Welcome to Timely!", "Keep track of time effortlessly. Our app provides a precise and easy-to-use stopwatch and timer for all your timing needs."),
        OnboardingPage(R.drawable.jam2, "Stay Focused", "Use the timer to manage tasks and stay focused. Set time limits and stay productive, whether for work, study, or workouts."),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Inisialisasi ViewPager2 untuk menampilkan halaman onboarding
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val buttonNext = findViewById<Button>(R.id.btnNext)
        val buttonSkip = findViewById<Button>(R.id.btnSkip)
        buttonSkip.background = null

        // Mengatur adapter ViewPager2
        viewPager.adapter = SimpleOnboardingAdapter(onboardingData)

        // Tombol Next untuk berpindah ke halaman berikutnya
        buttonNext.setOnClickListener {
            if (viewPager.currentItem < onboardingData.size - 1) {
                viewPager.currentItem += 1
            } else {
                // Jika sudah di slide terakhir, buka MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        // Tombol Skip untuk langsung ke MainActivity
        buttonSkip.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Callback untuk mendeteksi perubahan halaman
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                isLastSlide = position == onboardingData.size - 1
                buttonNext.text = if (isLastSlide) "Start" else "Next"
            }
        })
    }


    // Adapter untuk ViewPager2
    inner class SimpleOnboardingAdapter(
        private val onboardingPages: List<OnboardingPage>
    ) : RecyclerView.Adapter<SimpleOnboardingAdapter.OnboardingViewHolder>() {

        // ViewHolder untuk setiap halaman onboarding
        inner class OnboardingViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
            private val imageView: ImageView = view.findViewById(R.id.imageView)
            private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
            private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

            // Mengisi data untuk halaman onboarding
            fun bind(onboardingPage: OnboardingPage) {
                imageView.setImageResource(onboardingPage.imageRes)
                titleTextView.text = onboardingPage.title
                descriptionTextView.text = onboardingPage.description
            }
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): OnboardingViewHolder {
            // Menggunakan layout item untuk setiap halaman
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding, parent, false)
            return OnboardingViewHolder(view)
        }

        override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
            holder.bind(onboardingPages[position])
        }

        override fun getItemCount(): Int = onboardingPages.size // Jumlah halaman onboarding
    }
}