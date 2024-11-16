package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class LapTimesAdapter(
    private val lapTimes: List<String>,
    private val context: Context
) : RecyclerView.Adapter<LapTimesAdapter.LapViewHolder>() {

    inner class LapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lapTimeTextView: TextView = itemView.findViewById(R.id.tvLapTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_lap_time, parent, false)
        return LapViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.lapTimeTextView.text = lapTimes[position]
    }

    override fun getItemCount(): Int {
        return lapTimes.size
    }

    private fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Lap Time", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Lap time copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
