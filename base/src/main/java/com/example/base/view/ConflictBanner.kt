package com.example.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.stx.xhb.xbanner.XBanner


/**
 * @author 李雄厚
 *
 * @features ***
 */
class ConflictBanner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    XBanner(context, attrs, defStyleAttr) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //设置不拦截
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}