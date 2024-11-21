package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var isPaused = false
    private var totalTimeInMillis: Long = 0
    private var remainingTimeInMillis: Long = 0

    // Menangani inflating layout dan setup awal di Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        // Setup NumberPickers untuk jam, menit, dan detik
        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = 23
        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
        binding.secondPicker.minValue = 0
        binding.secondPicker.maxValue = 59

        // Set listener untuk tombol Start/Stop
        binding.btnStartStop.setOnClickListener {
            if (isRunning) pauseTimer() else if (isPaused) resumeTimer() else startTimer()
        }

        // Set listener untuk tombol Reset
        binding.btnReset.setOnClickListener { resetTimer() }

        // Mengubah warna teks pada NumberPicker untuk jam, menit, dan detik
        val selectedColor = resources.getColor(R.color.white, null)
        val unselectedColor = Color.parseColor("#F5F5F5")

        updateNumberPickerTextColors(binding.hourPicker, selectedColor, unselectedColor)
        updateNumberPickerTextColors(binding.minutePicker, selectedColor, unselectedColor)
        updateNumberPickerTextColors(binding.secondPicker, selectedColor, unselectedColor)

        // Update warna teks ketika nilai NumberPicker berubah
        binding.hourPicker.setOnValueChangedListener { _, _, _ ->
            updateNumberPickerTextColors(binding.hourPicker, selectedColor, unselectedColor)
        }

        binding.minutePicker.setOnValueChangedListener { _, _, _ ->
            updateNumberPickerTextColors(binding.minutePicker, selectedColor, unselectedColor)
        }

        binding.secondPicker.setOnValueChangedListener { _, _, _ ->
            updateNumberPickerTextColors(binding.secondPicker, selectedColor, unselectedColor)
        }

        return binding.root
    }

    // Bersihkan binding saat view dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Fungsi untuk memulai timer
    private fun startTimer() {
        val hours = binding.hourPicker.value
        val minutes = binding.minutePicker.value
        val seconds = binding.secondPicker.value
        totalTimeInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L
        remainingTimeInMillis = totalTimeInMillis

        // Menyembunyikan picker dan menampilkan progress dan waktu yang tersisa
        binding.numberPickerGroup.visibility = View.GONE
        binding.timerProgressView.visibility = View.VISIBLE
        binding.remainingTimeTextView.visibility = View.VISIBLE

        startCountdownTimer(remainingTimeInMillis)  // Mulai menghitung mundur
        isRunning = true
        isPaused = false
        binding.btnStartStop.setImageResource(R.drawable.pause_icon)
    }

    // Fungsi untuk menghentikan timer
    private fun pauseTimer() {
        timer?.cancel()  // Membatalkan countdown
        isRunning = false
        isPaused = true
        binding.btnStartStop.setImageResource(R.drawable.play)
    }

    // Fungsi untuk melanjutkan timer yang dipause
    private fun resumeTimer() {
        startCountdownTimer(remainingTimeInMillis)  // Melanjutkan countdown
        isRunning = true
        isPaused = false
        binding.btnStartStop.setImageResource(R.drawable.pause_icon)
    }

    // Fungsi untuk mereset timer
    private fun resetTimer() {
        timer?.cancel()  // Membatalkan countdown
        isRunning = false
        isPaused = false

        // Reset progress view dan menyembunyikan elemen terkait
        binding.timerProgressView.resetProgress()
        binding.timerProgressView.visibility = View.GONE
        binding.remainingTimeTextView.visibility = View.GONE
        binding.numberPickerGroup.visibility = View.VISIBLE
        binding.btnStartStop.setImageResource(R.drawable.play)
    }

    // Fungsi untuk mengubah warna teks pada NumberPicker
    private fun updateNumberPickerTextColors(
        numberPicker: NumberPicker,
        selectedColor: Int,
        unselectedColor: Int
    ) {
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val child = numberPicker.getChildAt(i)
            if (child is TextView) {
                val currentValue = child.text.toString().toIntOrNull()
                if (currentValue == numberPicker.value) {
                    child.setTextColor(selectedColor)
                    child.setTypeface(null, android.graphics.Typeface.BOLD)  // Set tebal
                } else {
                    child.setTextColor(unselectedColor)
                    child.setTypeface(null, android.graphics.Typeface.NORMAL)
                }
            }
        }
        numberPicker.invalidate()
    }

    // Fungsi untuk memulai countdown timer
    private fun startCountdownTimer(timeInMillis: Long) {
        timer = object : CountDownTimer(timeInMillis, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                val remainingSeconds = millisUntilFinished / 1000
                val hours = remainingSeconds / 3600
                val minutes = (remainingSeconds % 3600) / 60
                val seconds = remainingSeconds % 60
                val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                binding.remainingTimeTextView.text = timeText  // Menampilkan waktu tersisa

                // Mengupdate progress view berdasarkan waktu yang tersisa
                binding.timerProgressView.setProgress(
                    1f - millisUntilFinished.toFloat() / totalTimeInMillis.toFloat()
                )
            }

            // Fungsi yang dijalankan setelah timer selesai
            override fun onFinish() {
                resetTimer()  // Reset timer setelah selesai
            }
        }
        timer?.start()  // Memulai countdown
    }
}
