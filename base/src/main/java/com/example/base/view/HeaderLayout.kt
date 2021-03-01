package com.example.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import com.example.base.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle


/**
 * @author 李雄厚
 *
 * @features 刷新头部
 */
class HeaderLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), RefreshHeader {

    private var mTaoBaoView: TaoBaoView
    private var mTextView: TextView
    private var refreshingAnimation: RotateAnimation? = null

    init {
        val view = View.inflate(context, R.layout.custom_refresh_header_view, this)
        mTaoBaoView = view.findViewById(R.id.taoBaoView)
        mTextView = view.findViewById(R.id.reTextView)
        refreshingAnimation = AnimationUtils.loadAnimation(context, R.anim.rotating) as RotateAnimation
        val lir = LinearInterpolator()
        refreshingAnimation?.interpolator = lir
        mTaoBaoView.setProgress(90)
    }

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        if (success){
            mTextView.text = "刷新完成"
        }else{
            mTextView.text = "刷新失败"
        }
        mTaoBaoView.clearAnimation()
        return 0
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View = this

    override fun setPrimaryColors(vararg colors: Int) {
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when(newState){
            RefreshState.PullDownToRefresh -> {
                mTextView.text = "下拉刷新"
                mTaoBaoView.setIsShowIcon(true)
            }
            RefreshState.RefreshReleased -> {
                mTextView.text = "正在刷新..."
                mTaoBaoView.setIsShowIcon(false)
                mTaoBaoView.startAnimation(refreshingAnimation)
            }
            RefreshState.ReleaseToRefresh -> {
                mTextView.text = "释放刷新"
                mTaoBaoView.setIsShowIcon(false)
            }
            else ->{}
        }
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
        if (isDragging) {
            val progress = (percent.coerceAtMost(1.0f) * 100)
            mTaoBaoView.setProgress(if (progress > 90) 90 else progress.toInt())
        }
    }

    override fun isSupportHorizontalDrag(): Boolean = false
}