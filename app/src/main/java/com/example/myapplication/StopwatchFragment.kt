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

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!

    private var isRunning = false
    private var startTime = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val lapTimes = mutableListOf<String>()
    private lateinit var lapAdapter: LapTimesAdapter


    private val updateTimer = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
            if (isRunning) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTime

                // Calculate seconds and milliseconds
                val seconds = (elapsedTime / 1000) % 60
                val milliseconds = (elapsedTime % 1000) / 10


                binding.tvStopwatch.text = String.format("%02d:%02d", seconds, milliseconds)

                val progress = (elapsedTime % 15000) / 15000f
                binding.circularProgressView.setProgress(progress)

                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)

        // Set up start/stop button listener
        binding.btnStartStop.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }

        // Set up lap button listener
        binding.btnLap.setOnClickListener {
            addLapTime()
        }

        // Set up reset button listener
        binding.btnReset.setOnClickListener {
            resetTimer()
        }

        binding.clipBoard.setOnClickListener {
            val allLapTimes = lapTimes.joinToString("\n")
            copyToClipboard(allLapTimes)
        }

        // Set up RecyclerView for lap times
        lapAdapter = LapTimesAdapter(lapTimes, requireContext())
        binding.recyclerViewLapTimes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewLapTimes.adapter = lapAdapter

        return binding.root
    }



    @SuppressLint("SetTextI18n")
    private fun startTimer() {
        isRunning = true
        startTime = System.currentTimeMillis()
        binding.btnStartStop.setImageResource(R.drawable.pause_icon)
        handler.post(updateTimer)
    }

    @SuppressLint("SetTextI18n")
    private fun stopTimer() {
        isRunning = false
        binding.btnStartStop.setImageResource(R.drawable.play)
        handler.removeCallbacks(updateTimer)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun resetTimer() {
        stopTimer()
        binding.tvStopwatch.text = "00:00.00"
        lapTimes.clear()
        lapAdapter.notifyDataSetChanged() // Clear lap times in RecyclerView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addLapTime() {
        val lapNumber = lapTimes.size + 1
        val lapTime = "LAP $lapNumber: ${binding.tvStopwatch.text}"
        lapTimes.add(0, lapTime)
        lapAdapter.notifyDataSetChanged()
        binding.recyclerViewLapTimes.scrollToPosition(0)
    }

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
