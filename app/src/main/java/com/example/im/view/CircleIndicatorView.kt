package com.example.im.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import com.example.im.R

/**
 *@author: 雄厚
 *Date: 2020/9/4
 *Time: 9:25
 */
class CircleIndicatorView : View{
    private var mPaint: Paint? = null
    private var mRadius = 20f
    private var mGap = 0f
    private var mCount = 0
    private var mIndex = 0
    private var selectColor = -0x99999a
    private var unSelectColor = -0x333334
    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs){
        init()
    }
    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicatorView)
        selectColor = ta.getColor(R.styleable.CircleIndicatorView_cir_selectColor, selectColor)
        unSelectColor = ta.getColor(R.styleable.CircleIndicatorView_cir_unSelectColor, unSelectColor)
        ta.recycle()
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint?.isAntiAlias = true
    }

    fun setRadius(radius: Float) {
        mRadius = radius
        invalidate()
    }

    fun setGap(gap: Float) {
        mGap = gap
        invalidate()
    }

    fun setCount(count: Int) {
        mCount = count
        invalidate()
    }

    fun setSelectedIndex(index: Int) {
        mIndex = index
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mCount < 1) {
            return
        }
        val width = width
        val height = height
        val w = mCount * mRadius * 2 + mGap * (mCount - 1)
        val x = (width - w) / 2 + mRadius
        val y = (height - mRadius * 2) / 2 + mRadius
        var startX = x
        for (i in 0 until mCount) {
            if (mIndex == i) {
                mPaint?.color = selectColor
            } else {
                mPaint?.color = unSelectColor
            }
            canvas.drawCircle(startX, y, mRadius, mPaint!!)
            startX += mRadius * 2 + mGap
        }
    }
}