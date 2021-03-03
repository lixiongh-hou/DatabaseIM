package com.example.im.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.base.utli.DensityUtil
import com.example.im.R


/**
 * @author 李雄厚
 *
 * @features 绘制一个三角形
 */
class TriangleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val TOP = 0
        private const val BOTTOM = 1
        private const val RIGHT = 2
        private const val LEFT = 3
        private const val DEFAULT_WIDTH = 10F
        private const val DEFAULT_HEIGHT = 6F
    }

    private var mPaint: Paint? = null
    private var mColor = 0
    private var mWidth = 0F
    private var mHeight = 0F
    private var mDirection = 0
    private var mPath: Path? = null

    init {
        mPaint = Paint()
        mPaint?.isAntiAlias = true
        mPaint?.style = Paint.Style.FILL
        mPath = Path()
        mDirection = TOP

        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.TriangleView, 0, 0)
        mColor = typedArray.getColor(
            R.styleable.TriangleView_trv_color,
            ContextCompat.getColor(getContext(), R.color.pop_color)
        )
        mDirection = typedArray.getInt(R.styleable.TriangleView_trv_direction, mDirection)
        typedArray.recycle()
        mPaint?.color = mColor


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (mWidth == 0F || widthMode != MeasureSpec.EXACTLY) {
            mWidth = DensityUtil.dp2px(DEFAULT_WIDTH).toFloat()
        }
        if (mHeight == 0F || heightMode != MeasureSpec.EXACTLY) {
            mHeight = DensityUtil.dp2px(DEFAULT_HEIGHT).toFloat()
        }
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (mDirection) {
            TOP -> {
                mPath?.moveTo(0F, mHeight)
                mPath?.lineTo(mWidth, mHeight)
                mPath?.lineTo(mWidth / 2, 0F)
            }
            BOTTOM -> {
                mPath?.moveTo(0F, 0F)
                mPath?.lineTo(mWidth / 2, mHeight)
                mPath?.lineTo(mWidth, 0F)
            }
            RIGHT -> {
                mPath?.moveTo(0F, 0F)
                mPath?.lineTo(0F, mHeight)
                mPath?.lineTo(mWidth, mHeight / 2)
            }
            LEFT -> {
                mPath?.moveTo(0F, mHeight / 2)
                mPath?.lineTo(mWidth, mHeight)
                mPath?.lineTo(mWidth, 0F)
            }
        }
        mPath?.close()
        canvas?.drawPath(mPath!!, mPaint!!)
    }
}