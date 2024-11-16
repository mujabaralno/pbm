package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class StopwatchProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var angle = 0f
    private val paint = Paint().apply {
        color = 0xFF4285F4.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }
    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 20f
        rectF.set(padding, padding, width - padding, height - padding)
        canvas.drawArc(rectF, -90f, angle, false, paint)
    }

    fun setProgress(progress: Float) {
        angle = progress * 360f
        invalidate()
    }
}
