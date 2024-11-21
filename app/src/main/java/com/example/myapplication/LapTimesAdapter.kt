package com.example.myapplication

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemLapTimeBinding

/**
 * Adapter untuk menampilkan daftar waktu putaran (lap times) dalam RecyclerView.
 *
 * @param lapTimes List yang berisi string waktu putaran.
 * @param context Konteks dari aktivitas atau fragment yang menggunakan adapter ini.
 */

class LapTimesAdapter(
    private val lapTimes: List<String>,
    private val context: Context
) : RecyclerView.Adapter<LapTimesAdapter.LapViewHolder>() {

    /**
     * ViewHolder untuk menyimpan referensi ke elemen UI di setiap item lap time menggunakan View Binding.
     *
     * @param binding Binding untuk layout item lap time.
     */
    inner class LapViewHolder(val binding: ItemLapTimeBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Dipanggil ketika RecyclerView membutuhkan ViewHolder baru untuk sebuah item.
     *
     * @param parent ViewGroup yang berisi item-item RecyclerView.
     * @param viewType Tipe tampilan item (tidak digunakan di sini).
     * @return Sebuah LapViewHolder baru.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val binding = ItemLapTimeBinding.inflate(LayoutInflater.from(context), parent, false)
        return LapViewHolder(binding)
    }

    /**
     * Dipanggil untuk mengikat data ke ViewHolder tertentu.
     *
     * @param holder LapViewHolder yang akan menampilkan data.
     * @param position Posisi item dalam daftar.
     */
    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        // Mengatur teks waktu putaran menggunakan binding
        holder.binding.tvLapTime.text = lapTimes[position]

        // Menambahkan klik untuk menyalin waktu putaran ke clipboard
        holder.binding.tvLapTime.setOnClickListener {
            copyToClipboard(lapTimes[position])
        }
    }

    /**
     * Mengembalikan jumlah item dalam daftar.
     *
     * @return Jumlah item dalam `lapTimes`.
     */
    override fun getItemCount(): Int {
        return lapTimes.size
    }

    /**
     * Menyalin teks ke clipboard dan menampilkan Toast sebagai konfirmasi.
     *
     * @param text Teks yang akan disalin ke clipboard.
     */
    private fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Lap Time", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Lap time copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
