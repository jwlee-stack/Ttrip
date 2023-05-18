package org.sfy.ttrip.presentation.landmark

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import org.sfy.ttrip.R

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private val pathList = mutableListOf<Pair<Path, Paint>>()
    private var currentPath: Pair<Path, Paint>? = null

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 11f
    }

    override fun onDraw(canvas: Canvas?) {
        pathList.forEach { (path, paint) ->
            canvas?.drawPath(path, paint)
        }
        currentPath?.let { (path, paint) ->
            canvas?.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Pair(Path(), Paint(paint).apply { style = Paint.Style.STROKE })
                currentPath?.first?.moveTo(x!!, y!!)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath?.first?.lineTo(x!!, y!!)
            }
            MotionEvent.ACTION_UP -> {
                currentPath?.first?.lineTo(x!!, y!!)
                currentPath?.let {
                    pathList.add(it)
                }
                currentPath = null
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setColorBlack() {
        paint.color = ContextCompat.getColor(context, R.color.black)
    }

    fun setColorNeonBlue() {
        paint.color = ContextCompat.getColor(context, R.color.neon_blue)
    }

    fun setColorPear() {
        paint.color = ContextCompat.getColor(context, R.color.pear)
    }

    fun setColorOldRose() {
        paint.color = ContextCompat.getColor(context, R.color.old_rose)
    }

    // 그림판 옵션 초기화
    fun reset() {
        pathList.clear()
        currentPath = null
        invalidate()
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
    }

    // 그림을 비트맵으로 저장하는 메소드
    fun getBitmap(): Bitmap? {
        // 뷰의 크기를 비트맵의 크기로 설정합니다.
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 모든 경로를 그립니다.
        pathList.forEach { (path, paint) ->
            canvas.drawPath(path, paint)
        }
        currentPath?.let { (path, paint) ->
            canvas.drawPath(path, paint)
        }

        return bitmap
    }
}