package com.example.base.base

import android.app.Application
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.example.base.R
import com.example.base.utli.DensityUtil
import com.example.base.view.HeaderLayout
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.zy.multistatepage.MultiStateConfig
import com.zy.multistatepage.MultiStatePage
import net.mikaelzero.mojito.Mojito
import net.mikaelzero.mojito.loader.glide.GlideImageLoader
import net.mikaelzero.mojito.view.sketch.SketchImageLoadFactory

/**
 * @author 李雄厚
 *
 * @features ***
 */
class BaseApp: Application() {


    override fun onCreate() {
        super.onCreate()
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        instance = this
        val config = MultiStateConfig.Builder()
            .alphaDuration(300)
            .errorIcon(com.zy.multistatepage.R.mipmap.state_error)
            .emptyIcon(com.zy.multistatepage.R.mipmap.state_empty)
            .emptyMsg("这里什么都没有")
            .loadingMsg("加载中...")
            .errorMsg("哎呀，出错了")
            .build()
        MultiStatePage.config = config

        //查看大图框架
        Mojito.initialize(
            GlideImageLoader.with(this),
            SketchImageLoadFactory()
        )
    }

    companion object{
        lateinit var instance: Application private set

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> // 设置头部和尾部的高度
                layout.setHeaderHeight(50F).setFooterHeight(50F)
                    // 设置主题颜色
                    .setPrimaryColorsId(R.color.white, R.color.bg_black_90)
                    // 在内容不满一页的时候，是否可以上拉加载更多
                    .setEnableLoadMoreWhenContentNotFull(true)
                    // 是否在加载更多完成之后滚动内容显示新数据
                    .setEnableScrollContentWhenLoaded(true)
                    // 是否启用越界回弹
                    .setEnableOverScrollBounce(false)
                    // 设置回弹动画时长
                    .setReboundDuration(250)
                    // 设置回弹显示插值器
                    .setReboundInterpolator(LinearInterpolator())
                HeaderLayout(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
                ClassicsFooter(context)
                    .setSpinnerStyle(SpinnerStyle.Translate)
                    .setDrawableArrowSize(16F)
                    .setDrawableMarginRight(10F)
                    .setDrawableProgressSize(16F)
                    .setFinishDuration(400)
                    .setTextSizeTitle(14F)
                    .setAccentColor(ContextCompat.getColor(context, R.color.bg_black_90))
                    .setPrimaryColor(ContextCompat.getColor(context, R.color.bg_F4))
            }
        }
    }
}