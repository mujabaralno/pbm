package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

// Custom View untuk menampilkan progress timer dalam bentuk lingkaran
class TimerProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var angle = 360f  // Nilai sudut untuk menggambar progress
    private val backgroundPaint = Paint().apply {
        color = 0xFF1F1E1E.toInt()  // Warna latar belakang
        style = Paint.Style.STROKE
        strokeWidth = 45f  // Ketebalan stroke
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        color = 0xFF3E11B9.toInt()  // Warna progress
        style = Paint.Style.STROKE
        strokeWidth = 45f  // Ketebalan stroke
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND  // Bentuk ujung garis melengkung
    }

    private val rectF = RectF()  // Untuk mendefinisikan area lingkaran

    // Fungsi untuk menggambar progress pada canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 45f
        rectF.set(padding, padding, width - padding, height - padding)

        // Gambar lingkaran latar belakang
        canvas.drawArc(rectF, 0f, 360f, false, backgroundPaint)

        // Gambar progress berdasarkan nilai sudut
        canvas.drawArc(rectF, -90f, angle, false, progressPaint)
    }

    // Fungsi untuk mengubah nilai progress
    fun setProgress(progress: Float) {
        angle = 360f * (1 - progress)  // Menghitung sudut berdasarkan nilai progress
        invalidate()  // Memanggil ulang drawing
    }

    // Fungsi untuk mereset progress ke kondisi awal
    fun resetProgress() {
        angle = 360f  // Set sudut ke 360 (lingkaran penuh)
        invalidate()  // Memanggil ulang drawing
    }
}
