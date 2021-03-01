package com.example.base.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.base.R
import com.example.base.utli.DensityUtil
import com.example.base.utli.ScreenUtil
import java.lang.reflect.ParameterizedType


/**
 * @author 李雄厚
 *
 * @features DialogFragment基类
 */
abstract class BaseFragmentDialog<Binding: ViewDataBinding> : AppCompatDialogFragment() {
    private lateinit var mBinding: Binding

    /**背景昏暗度 */
    private var mDimAmount = 0.5F

    /**左右边距 */
    private var mMargin = 0

    /**进入退出动画 */
    private var mAnimStyle = 0

    private var mGravity = Gravity.CENTER

    /**点击外部取消 */
    private var mOutCancel = true
    private var params: WindowManager.LayoutParams? = null
    private var mContext: Context? = null
    private var mWidth = 0
    private var mHeight = 0
    private var created = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.style_dialog_fuzzy)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBindingWithModel(inflater)
        return mBinding.root
    }
    private fun initBindingWithModel(inflater: LayoutInflater) {
        val type = javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = type.actualTypeArguments
        for (argument in actualTypeArguments) {
            val clazz = argument as Class<*>
            if (clazz.superclass == ViewDataBinding::class.java) {
                val asSubclass = clazz.asSubclass(ViewDataBinding::class.java)
                val declaredMethod =
                    asSubclass.getDeclaredMethod("inflate", LayoutInflater::class.java)
                @Suppress("UNCHECKED_CAST")
                mBinding = declaredMethod.invoke(this, inflater) as Binding
                convertView(mBinding)
            }
        }
        if (!::mBinding.isInitialized) {
            throw IllegalStateException("Binding 必须是ViewDataBinding的子类")
        }
    }

    override fun onStart() {
        super.onStart()
        if (!created) {
            initParams()
            created = true
        }
    }

    open fun initParams() {
        val window = dialog?.window
        if (window != null) {
            params = window.attributes
            params?.dimAmount = mDimAmount
            params?.gravity = mGravity
            //设置dialog宽度
            if (mWidth == 0) {
                params?.width = ScreenUtil.getScreenWidth() - 2 * DensityUtil.dp2px(mMargin.toFloat())
            } else {
                params?.width = DensityUtil.dp2px(mWidth.toFloat())
            }

            //设置dialog高度
            if (mHeight == 0) {
                params?.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                params?.height = DensityUtil.dp2px(mHeight.toFloat())
            }
            //设置dialog动画
            if (mAnimStyle != 0) {
                window.setWindowAnimations(mAnimStyle)
            }
            window.attributes = params
        }
        //是否点击空白处隐藏
        dialog?.setCanceledOnTouchOutside(mOutCancel)
        dialog?.setOnKeyListener { _: DialogInterface?, keyCode: Int, event: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                //是否拦截返回按钮关闭对话框
                return@setOnKeyListener false
            }
            false
        }
    }

    /**
     * 设置背景昏暗度
     *
     * @param dimAmount
     * @return
     */
    open fun setDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float): BaseFragmentDialog<Binding>? {
        mDimAmount = dimAmount
        return this
    }

    /**
     * 弹出框位置
     */
    open fun setGravity(gravity: Int): BaseFragmentDialog<Binding>? {
        mGravity = gravity
        return this
    }

    /**
     * 设置宽高
     *
     * @param width 固定宽度，默认为0，自适应宽度 当设置宽度 >0 {setMargin()} 无效
     * @param height 固定高度，默认为0,自适应高度
     * @return
     */
    open fun setSize(width: Int, height: Int): BaseFragmentDialog<Binding>? {
        mWidth = width
        mHeight = height
        return this
    }

    /**
     * 设置左右margin
     */
    open fun setMargin(margin: Int): BaseFragmentDialog<Binding>? {
        mMargin = margin
        return this
    }

    /**
     * 设置进入退出动画
     */
    open fun setAnimStyle(@StyleRes animStyle: Int): BaseFragmentDialog<Binding>? {
        mAnimStyle = animStyle
        return this
    }

    /**
     * 设置是否点击外部取消
     * @return
     */
    open fun setOutCancel(outCancel: Boolean): BaseFragmentDialog<Binding>? {
        mOutCancel = outCancel
        return this
    }

    open fun show(manager: FragmentManager?): BaseFragmentDialog<Binding>? {
        super.show(manager!!, System.currentTimeMillis().toString())
        return this
    }

    /**
     * 操作dialog布局
     */
    abstract fun convertView(binding: Binding)
}