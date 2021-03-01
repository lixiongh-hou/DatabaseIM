package com.example.base.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.base.R
import com.example.base.base.BaseApp
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @author 李雄厚
 *
 * @features ***
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TabView constructor(context: Context, @Nullable attrs: AttributeSet) :
    FrameLayout(context, attrs) {

    /**
     * 未选中视图
     */
    private lateinit var mNormalImageView: ImageView

    /**
     * 选中视图
     */
    private lateinit var mSelectedImageView: ImageView

    /**
     * 文本视图
     */
    private lateinit var mTitleView: TextView

    /**
     * 文本内容
     */
    private var mTitle: String = ""

    /**
     * 未选中图片
     */
    private var mNormalDrawable: Drawable? = null

    /**
     * 选中图片
     */
    private var mSelectedDrawable: Drawable? = null

    private var mTargetColor = 0
    private var mTargetColorUn = 0

    companion object {
        /**
         * 标题和轮廓图默认的着色
         */
        private var DEFAULT_TAB_COLOR = ContextCompat.getColor(BaseApp.instance, R.color.black)

        /**
         * 标题和轮廓图最终的着色
         */
        private const val DEFAULT_TAB_TARGET_COLOR = -0xFFFB7299
    }

    init {
        //加载布局
        inflate(context, R.layout.tab_layout, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.TabView)
        for (i in 0 until a.indexCount) {
            when (val attr = a.getIndex(i)) {
                // 获取标题和轮廓最终的着色
                R.styleable.TabView_tabColor -> mTargetColor =
                    a.getColor(attr, DEFAULT_TAB_TARGET_COLOR.toInt())

                R.styleable.TabView_tabColor_un -> mTargetColorUn =
                    a.getColor(attr, DEFAULT_TAB_COLOR)

                //获取未选中图
                R.styleable.TabView_tabImage -> mNormalDrawable = a.getDrawable(attr)
                //获取选中图
                R.styleable.TabView_tabSelectedImage -> mSelectedDrawable = a.getDrawable(attr)
                // 获取标题
                R.styleable.TabView_tabTitle -> mTitle = a.getString(attr).toString()
            }
        }
        a.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()

        //设置标题，默认着色为灰色
        mTitleView = findViewById(R.id.tab_title)
        mTitleView.setTextColor(mTargetColorUn)
        mTitleView.text = mTitle

        //设置未选中图片，不透明，默认着色为灰色
        mNormalImageView = findViewById(R.id.tab_image)
        mNormalDrawable?.setTint(mTargetColorUn)
        mNormalDrawable?.alpha = 255
        mNormalImageView.setImageDrawable(mNormalDrawable)


        // 设置选中图片，透明， 默认着色为灰色
        mSelectedImageView = findViewById(R.id.tab_selected_image)
        mSelectedDrawable?.alpha = 0
        mSelectedImageView.setImageDrawable(mSelectedDrawable)
    }

    /**
     * 根据进度值进行变色和透明度处理
     * @param percentage 进度值，取值[0, 1]。
     */
    fun setXPercentage(percentage: Float) {
        if (percentage < 0 || percentage > 1) {
            Log.e("测试", "拦截")
            return
        }

        // 1. 颜色变换
        val finalColor = evaluate(percentage, mTargetColorUn, mTargetColor)
        mTitleView.setTextColor(finalColor)
        mNormalDrawable?.setTint(finalColor)

        // 2. 透明度变换
        if (percentage in 0.5..1.0) {
            // 原理如下
            // 进度值: 0.5 ~ 1
            // 透明度: 0 ~ 1
            // 公式: percentage - 1 = (alpha - 1) * 0.5
            val alpha = ceil((255 * ((percentage - 1) * 2 + 1)).toDouble())
            mNormalDrawable?.alpha = (255 - alpha).toInt()
            mSelectedDrawable?.alpha = alpha.toInt()
        } else {
            mNormalDrawable?.alpha = 255
            mSelectedDrawable?.alpha = 0
        }

        // 3. 更新UI
        invalidateUI()
    }

    /**
     * 根据不同线程更新UI。
     */
    private fun invalidateUI() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // 主线程
            invalidate()
        } else {
            // 工作线程
            postInvalidate()
        }
    }

    /**
     * 计算不同进度值对应的颜色值，这个方法取自 ArgbEvaluator.java 类。
     *
     * @param percentage 进度值，范围[0, 1]。
     * @param startValue 起始颜色值。
     * @param endValue 最终颜色值。
     * @return 返回与进度值相应的颜色值。
     */
    private fun evaluate(percentage: Float, startValue: Int, endValue: Int): Int {
        val startA = (startValue shr 24 and 0xff) / 255.0f
        var startR = (startValue shr 16 and 0xff) / 255.0f
        var startG = (startValue shr 8 and 0xff) / 255.0f
        var startB = (startValue and 0xff) / 255.0f
        val endA = (endValue shr 24 and 0xff) / 255.0f
        var endR = (endValue shr 16 and 0xff) / 255.0f
        var endG = (endValue shr 8 and 0xff) / 255.0f
        var endB = (endValue and 0xff) / 255.0f

        // convert from sRGB to linear
        startR = startR.toDouble().pow(2.2).toFloat()
        startG = startG.toDouble().pow(2.2).toFloat()
        startB = startB.toDouble().pow(2.2).toFloat()
        endR = endR.toDouble().pow(2.2).toFloat()
        endG = endG.toDouble().pow(2.2).toFloat()
        endB = endB.toDouble().pow(2.2).toFloat()

        // compute the interpolated color in linear space
        var a = startA + percentage * (endA - startA)
        var r = startR + percentage * (endR - startR)
        var g = startG + percentage * (endG - startG)
        var b = startB + percentage * (endB - startB)

        // convert back to sRGB in the [0..255] range
        a *= 255.0f
        r = r.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        g = g.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        b = b.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        return a.roundToInt() shl 24 or (r.roundToInt() shl 16) or (g.roundToInt() shl 8) or b.roundToInt()
    }

}