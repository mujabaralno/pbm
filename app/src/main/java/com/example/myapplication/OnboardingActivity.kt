package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

class OnboardingActivity : AppCompatActivity() {

    private var isLastSlide = false

    private val onboardingData = listOf(
        OnboardingPage(R.drawable.onboarding1, "Welcome to TIMEAPP!", "Keep track of time effortlessly. Our app provides a precise and easy-to-use stopwatch and timer for all your timing needs."),
        OnboardingPage(R.drawable.onboarding2, "Stay Focused", "Use the timer to manage tasks and stay focused. Set time limits and stay productive, whether for work, study, or workouts."),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val buttonNext = findViewById<Button>(R.id.btnNext)
        val buttonSkip = findViewById<Button>(R.id.btnSkip)
        buttonSkip.background = null
        viewPager.adapter = SimpleOnboardingAdapter(onboardingData)

        buttonNext.setOnClickListener {
            if (viewPager.currentItem < onboardingData.size - 1) {
                viewPager.currentItem += 1
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        buttonSkip.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                isLastSlide = position == onboardingData.size - 1
                buttonNext.text = if (isLastSlide) "Start" else "Next"
            }
        })
    }


    inner class SimpleOnboardingAdapter(
        private val onboardingPages: List<OnboardingPage>
    ) : RecyclerView.Adapter<SimpleOnboardingAdapter.OnboardingViewHolder>() {

        inner class OnboardingViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
            private val imageView: ImageView = view.findViewById(R.id.imageView)
            private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
            private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

            fun bind(onboardingPage: OnboardingPage) {
                imageView.setImageResource(onboardingPage.imageRes)
                titleTextView.text = onboardingPage.title
                descriptionTextView.text = onboardingPage.description
            }
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): OnboardingViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding, parent, false)
            return OnboardingViewHolder(view)
        }

        override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
            holder.bind(onboardingPages[position])
        }

        override fun getItemCount(): Int = onboardingPages.size
    }
}