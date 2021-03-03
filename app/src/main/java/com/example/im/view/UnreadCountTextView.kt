package com.example.im.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.base.utli.DensityUtil
import com.example.im.R

/**
 * @author 李雄厚
 *
 * @features ***
 */
class UnreadCountTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mNormalSize = DensityUtil.dp2px(18.4F)

    private var mPaint: Paint? = null

    init {
        mPaint = Paint()
        mPaint?.color = ContextCompat.getColor(getContext(), R.color.read_dot_bg)
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.6f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (text.isEmpty()) {
            // 没有字符，就在本View中心画一个小圆点
            val l: Int = (measuredWidth - DensityUtil.dp2px(10F)) / 2
            val r = measuredWidth - l
            canvas!!.drawOval(RectF(l.toFloat(), l.toFloat(), r.toFloat(), r.toFloat()), mPaint!!)
        } else if (text.length == 1) {
            canvas!!.drawOval(RectF(0F, 0F, mNormalSize.toFloat(), mNormalSize.toFloat()), mPaint!!)
        } else if (text.length > 1) {
            canvas!!.drawRoundRect(RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat()), measuredHeight / 2.toFloat(), measuredHeight / 2.toFloat(), mPaint!!)
        }
        super.onDraw(canvas)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = mNormalSize
        val height = mNormalSize
        if (text.length > 1) {
            width = mNormalSize + DensityUtil.dp2px((text.length - 1) * 10F)
        }
        setMeasuredDimension(width, height)
    }
}