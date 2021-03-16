package com.example.im.dialog

import android.os.Bundle
import android.view.View
import com.example.base.base.BaseFragmentDialog
import com.example.im.R
import com.example.im.databinding.DialogSelectBinding

/**
 * @author 李雄厚
 *
 * @features ***
 */
class SelectDialog : BaseFragmentDialog<DialogSelectBinding>() {
    private var title = ""
    private var onClick: ((View?) -> Unit)? = null

    companion object {
        private const val TITLE = "title"
        fun instance(title: String) = SelectDialog().apply {
            setMargin(40)
            setAnimStyle(R.style.DialogCentreAnim)
            arguments = Bundle().apply {
                putString(TITLE, title)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE).toString()
        }
    }

    override fun convertView(binding: DialogSelectBinding) {
        binding.title = title
        binding.confirm.setOnClickListener {
            onClick?.invoke(it)
            dismiss()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }

    fun setOnclick(onClick: ((View?) -> Unit)?): SelectDialog {
        this.onClick = onClick
        return this
    }
}