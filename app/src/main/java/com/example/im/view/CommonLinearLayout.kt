package com.example.im.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.im.R

/**
 * @author 李雄厚
 *
 * @features ***
 */
class CommonLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var tvTitle: TextView? = null
    private var tvContent: TextView? = null
    private var ivBack: ImageView? = null
    private var switch: SwitchCompat? = null
    private var layCommon: RelativeLayout? = null

    private var title: String = ""
    private var content: String = ""
    private var isBack = true
    private var isSwitch = false
    private var onCommonClick: ((View?) -> Unit)? = null
    private var onSwitchClick: ((View?, Boolean) -> Unit)? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CommonLinearLayout)
        title = a.getString(R.styleable.CommonLinearLayout_common_title).toString()
        content = a.getString(R.styleable.CommonLinearLayout_common_content).toString()
        isBack = a.getBoolean(R.styleable.CommonLinearLayout_common_isBack, isBack)
        isSwitch = a.getBoolean(R.styleable.CommonLinearLayout_common_isSwitch, isSwitch)
        a.recycle()
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.common_linear_layout, this)
        tvTitle = findViewById(R.id.title)
        tvContent = findViewById(R.id.content)
        ivBack = findViewById(R.id.back)
        switch = findViewById(R.id.switchCompat)
        layCommon = findViewById(R.id.commonLay)

        tvTitle?.text = title
        tvContent?.text = content
        switch?.visibility = if (isSwitch) View.VISIBLE else View.GONE
        tvContent?.visibility = if (isSwitch) View.GONE else View.VISIBLE
        layCommon?.setOnClickListener {
            onCommonClick?.invoke(it)
        }
        switch?.setOnCheckedChangeListener { view, b ->
            onSwitchClick?.invoke(view, b)
        }
    }


    fun getContent(): String {
        return tvContent?.text.toString().trim()
    }

    fun setContent(content: String?) {
        tvContent?.text = content
    }

        fun setChecked(checked: Boolean) {
        switch?.isChecked = checked
    }

    fun setOnCommonClick(onCommonClick: ((View?) -> Unit)) {
        this.onCommonClick = onCommonClick
    }

    fun setOnSwitchClick(onSwitchClick: ((View?, Boolean) -> Unit)) {
        this.onSwitchClick = onSwitchClick
    }

}