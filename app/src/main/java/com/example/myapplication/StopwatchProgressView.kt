package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class StopwatchProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Sudut progres (0 - 360)
    private var angle = 0f

    // Paint untuk background lingkaran
    private val backgroundPaint = Paint().apply {
        color = 0xFF1F1E1E.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 45f
        isAntiAlias = true
    }

    // Paint untuk progres lingkaran
    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 45f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Gradien warna untuk progres
        val gradient = LinearGradient(
            width / 3f, 0f,
            width / 3f, height.toFloat(),
            0xFF1E1E1E.toInt(),
            0xFF3E11B9.toInt(),
            android.graphics.Shader.TileMode.CLAMP
        )
        progressPaint.shader = gradient
    }

    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 45f
        rectF.set(padding, padding, width - padding, height - padding)

        // Gambar background lingkaran
        canvas.drawArc(rectF, 0f, 360f, false, backgroundPaint)

        // Gambar progres
        canvas.drawArc(rectF, -90f, angle, false, progressPaint)
    }

    /**
     * Mengatur progres dan memperbarui tampilan.
     */
    fun setProgress(progress: Float) {
        angle = progress * 360f
        invalidate()
    }

    /**
     * Mereset progres ke 0.
     */
    fun resetProgress() {
        angle = 0f
        invalidate()
    }

}
