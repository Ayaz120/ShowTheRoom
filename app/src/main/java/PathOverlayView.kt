package ca.unb.mobiledev.show

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class PathOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val pathPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private var pathCoordinates: List<Pair<Float, Float>> = emptyList()

    fun setPath(coordinates: List<Pair<Float, Float>>) {
        pathCoordinates = coordinates
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (pathCoordinates.isNotEmpty()) {
            val path = Path()
            path.moveTo(pathCoordinates[0].first, pathCoordinates[0].second)
            for (i in 1 until pathCoordinates.size) {
                path.lineTo(pathCoordinates[i].first, pathCoordinates[i].second)
            }
            canvas.drawPath(path, pathPaint)
        }
    }
}