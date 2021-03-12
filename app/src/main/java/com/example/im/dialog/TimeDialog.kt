package com.example.im.dialog

import android.os.Bundle
import androidx.databinding.ObservableField
import com.example.base.base.BaseFragmentDialog
import com.example.base.utli.DecimalsLengthFilter
import com.example.im.R
import com.example.im.databinding.DialogTimeBinding

/**
 * @author 李雄厚
 *
 * @features ***
 */
class TimeDialog : BaseFragmentDialog<DialogTimeBinding>() {
    private var time = ""
    var timeField = ObservableField<String>()
    private var onClick: ((String) -> Unit)? = null

    companion object {
        private const val TIME = "time"
        fun instance(time: String) = TimeDialog().apply {
            setMargin(40)
            setAnimStyle(R.style.DialogCentreAnim)
            arguments = Bundle().apply {
                putString(TIME, time)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            time = it.getString(TIME).toString()
        }
    }

    override fun convertView(binding: DialogTimeBinding) {
        binding.dialog = this
        timeField.set(time)
    }

    fun setOnClick() {
        onClick?.invoke(timeField.get()!!)
        dismiss()
    }

    fun setOnClick(onClick: ((String) -> Unit)): TimeDialog {
        this.onClick = onClick
        return this
    }

}