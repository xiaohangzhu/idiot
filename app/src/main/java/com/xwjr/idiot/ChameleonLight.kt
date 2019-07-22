package com.xwjr.idiot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.graphics.RectF
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix
import android.graphics.Bitmap


class ChameleonLight : View {

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mContext: Context? = null
    private var percent = 0f
    private var path = Path()
    private var bitmap: Bitmap? = null
    private var rectF = RectF()
    private val cMatrix = ColorMatrix()

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.light)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())

        if (bitmap != null)
            canvas.drawBitmap(bitmap!!, null, rectF, mPaint)

    }

    fun setPercent(curPercent: Float) {
        percent = curPercent
        cMatrix.set(
            floatArrayOf(
                0.4f + percent * 0.6f, 0f, 0f, 0f, 0f,
                0f, 0.4f + percent * 0.6f, 0f, 0f, 0f,
                0f, 0f, 0.4f + percent * 0.6f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        mPaint.colorFilter = ColorMatrixColorFilter(cMatrix)
        postInvalidate()
    }
}
