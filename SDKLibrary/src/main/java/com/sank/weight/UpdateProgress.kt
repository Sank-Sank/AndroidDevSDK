package com.sank.weight

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sank.R
import kotlin.math.min


/**
 *   created by sank
 *   on 2020/9/23
 *   描述：带动画的进度条
 */
class UpdateProgress(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private fun Float.dpToPx(): Float =
            this * Resources.getSystem().displayMetrics.density

    private fun Float.pxToDp(): Float =
            this / Resources.getSystem().displayMetrics.density

    private var rectF = RectF()
    private var center: Float = 0F

    var progressValue: Float = 0f
        set(value) {
            field = if (value <= progressMax) value else progressMax
            onProgressChangeListener?.invoke(field)
            invalidate()
        }

    var progressMax: Float = 100F
        set(value) {
            field = if (field >= 0) value else 100F
            invalidate()
        }

    var progressWidth : Float = 3F.dpToPx()
        set(value) {
            field = value.dpToPx()
            backgroundPaint.strokeWidth = field
            requestLayout()
            invalidate()
        }

    var progressColor: Int = Color.BLUE
        set(value) {
            field = value
            foregroundPaint.color = field
            invalidate()
        }

    //背景颜色
    var progressBgColor: Int = Color.GRAY
        set(value) {
            field = value
            backgroundPaint.color = field
            invalidate()
        }

    //背景进度条
    private var backgroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    //圆点
    private var foregroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    var onProgressChangeListener: ((Float) -> Unit)? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet? = null) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.UpdateProgress, 0, 0)
        progressValue = attributes.getFloat(R.styleable.UpdateProgress_progressValue, progressValue)
        progressMax = attributes.getFloat(R.styleable.UpdateProgress_progressMax, progressMax)
        progressColor = attributes.getInt(R.styleable.UpdateProgress_progressColor, progressColor)
        progressBgColor = attributes.getInt(R.styleable.UpdateProgress_progressBgColor, progressBgColor)
        attributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        center = min(width, height).toFloat()
        rectF.set(0F, 0F, center, center)
        setMeasuredDimension(center.toInt(), center.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制进度条背景
        canvas.drawCircle(center,
                center,
                center,
                backgroundPaint)
    }
}