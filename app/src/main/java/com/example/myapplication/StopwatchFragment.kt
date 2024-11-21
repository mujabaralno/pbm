package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentStopwatchBinding

/*
untuk menggunakan binding masuk ke build.gradle.kts lalu tambahkan
buildFeatures{
        viewBinding = true
}
dibawah defaultConfig
 */

class StopwatchFragment : Fragment() {

    // Binding untuk layout FragmentStopwatch
    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!

    // Variabel untuk melacak waktu yang telah berlalu sebelum stopwatch dijeda
    private var elapsedBeforePause = 0L

    // Status apakah stopwatch sedang berjalan
    private var isRunning = false

    // Waktu mulai stopwatch
    private var startTime = 0L

    // Handler untuk menjalankan update timer pada UI
    private val handler = Handler(Looper.getMainLooper())

    // List untuk menyimpan waktu putaran (lap times)
    private val lapTimes = mutableListOf<String>()

    // Adapter untuk RecyclerView yang menampilkan lap times
    private lateinit var lapAdapter: LapTimesAdapter

    // Runnable untuk memperbarui stopwatch setiap 10ms
    private val updateTimer = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
            if (isRunning) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTime

                // Hitung detik dan milidetik
                val seconds = (elapsedTime / 1000) % 60
                val milliseconds = (elapsedTime % 1000) / 10

                // Perbarui tampilan stopwatch
                binding.tvStopwatch.text = String.format("%02d:%02d", seconds, milliseconds)

                // Perbarui progress bar (progress melingkar) setiap 5 detik
                val progress = (elapsedTime % 5000) / 5000f
                binding.circularProgressView.setProgress(progress)

                // Jalankan lagi setelah 10ms
                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)

        // Tombol Start/Stop untuk memulai atau menjeda stopwatch
        binding.btnStartStop.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }

        // Tombol Lap untuk menambahkan waktu lap
        binding.btnLap.setOnClickListener {
            addLapTime()
        }

        // Tombol Reset untuk mereset stopwatch dan lap times
        binding.btnReset.setOnClickListener {
            resetTimer()
        }

        // Tombol Clipboard untuk menyalin semua lap times ke clipboard
        binding.clipBoard.setOnClickListener {
            val allLapTimes = lapTimes.joinToString("\n")
            copyToClipboard(allLapTimes)
        }

        // Inisialisasi RecyclerView dan adapter
        lapAdapter = LapTimesAdapter(lapTimes, requireContext())
        binding.recyclerViewLapTimes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLapTimes.adapter = lapAdapter

        return binding.root
    }

    /**
     * Memulai stopwatch. Mengubah status menjadi berjalan, mengatur waktu mulai
     */
    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            startTime = System.currentTimeMillis() - elapsedBeforePause
            binding.btnStartStop.setImageResource(R.drawable.pause_icon)
            handler.post(updateTimer)
            binding.btnLap.isEnabled = true
            binding.btnReset.isEnabled = false
            binding.btnLap.alpha = 1.0f
            binding.btnReset.alpha = 0.5f

        } else {
            stopTimer()
        }
    }

    /**
     * Menjeda stopwatch. Menghentikan handler dan suara, serta menyimpan waktu yang telah berlalu.
     */
    @SuppressLint("SetTextI18n")
    private fun stopTimer() {
        isRunning = false
        binding.btnStartStop.setImageResource(R.drawable.play)
        handler.removeCallbacks(updateTimer)
        elapsedBeforePause = System.currentTimeMillis() - System.currentTimeMillis()
        binding.btnLap.isEnabled = false
        binding.btnReset.isEnabled = true
        binding.btnLap.alpha = 0.5f
        binding.btnReset.alpha = 1.0f
    }

    /**
     * Mereset stopwatch, lap times, dan progress bar
     */
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun resetTimer() {
        stopTimer()
        binding.tvStopwatch.text = "00:00.00"
        lapTimes.clear()
        lapAdapter.notifyDataSetChanged()
        binding.btnReset.isEnabled = false
        binding.btnReset.alpha = 0.5f
        binding.circularProgressView.resetProgress()
    }


    /**
     * Menambahkan waktu putaran ke dalam list dan memperbarui RecyclerView.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun addLapTime() {
        val lapNumber = lapTimes.size + 1
        val lapTime = "LAP $lapNumber: ${binding.tvStopwatch.text}"
        lapTimes.add(0, lapTime)
        lapAdapter.notifyDataSetChanged()
        binding.recyclerViewLapTimes.scrollToPosition(0)
        if (isRunning) {
            binding.btnLap.isEnabled
        }
        binding.btnReset.alpha = if (!binding.btnReset.isEnabled) 0.5f else 1.0f
    }

    /**
     * Menyalin teks ke clipboard dan menampilkan toast.
     */
    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Lap Times", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "All lap times copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}
