package com.example.im

import com.example.base.base.BaseActivity
import com.example.im.databinding.ActivitySetUpBinding
import com.example.im.dialog.TimeDialog
import com.example.im.entity.SetUpField
import com.example.im.util.SetUpFieldUtil

/**
 * @author 李雄厚
 *
 * @features ***
 */
class SetUpActivity : BaseActivity<ActivitySetUpBinding>() {
    override fun initLayout(): Int = R.layout.activity_set_up

    override fun initView() {
        setMyTitle(getString(R.string.set_up))
        initContent()
        initClick()
    }

    private fun initContent() {
        mBinding.firstChatRefreshTime.setContent("${SetUpFieldUtil.getField().firstChatRefreshTime.toString()}毫秒")
        mBinding.pullRefreshTime.setContent("${SetUpFieldUtil.getField().pullRefreshTime.toString()}毫秒")
        mBinding.msgSuccessOrFailure.setChecked(SetUpFieldUtil.getField().msgSuccessOrFailure!!)
        mBinding.localMsg.setChecked(SetUpFieldUtil.getField().localMsg!!)
        mBinding.deleteMessage.setChecked(SetUpFieldUtil.getField().deleteMessage!!)
        mBinding.revokeMessageTime.setContent("${SetUpFieldUtil.getField().revokeMessageTime.toString()}分钟")
        mBinding.revokeMessage.setChecked(SetUpFieldUtil.getField().revokeMessage!!)
        mBinding.revokeMessageLocal.setChecked(SetUpFieldUtil.getField().revokeMessageLocal!!)
    }

    private fun initClick() {
        mBinding.firstChatRefreshTime.setOnCommonClick {
            TimeDialog.instance(SetUpFieldUtil.getField().firstChatRefreshTime.toString())
                .setOnClick {
                    mBinding.firstChatRefreshTime.setContent("${it}毫秒")
                    val field = SetUpField()
                    field.firstChatRefreshTime = it.toLong()
                    SetUpFieldUtil.saveField(field)

                }.show(supportFragmentManager)
        }

        mBinding.pullRefreshTime.setOnCommonClick {
            TimeDialog.instance(SetUpFieldUtil.getField().pullRefreshTime.toString())
                .setOnClick {
                    mBinding.pullRefreshTime.setContent("${it}毫秒")
                    val field = SetUpField()
                    field.pullRefreshTime = it.toLong()
                    SetUpFieldUtil.saveField(field)

                }.show(supportFragmentManager)
        }
        mBinding.revokeMessageTime.setOnCommonClick {
            TimeDialog.instance(SetUpFieldUtil.getField().revokeMessageTime.toString())
                .setOnClick {
                    mBinding.revokeMessageTime.setContent("${it}分钟")
                    val field = SetUpField()
                    field.revokeMessageTime = it.toInt()
                    SetUpFieldUtil.saveField(field)

                }.show(supportFragmentManager)
        }
        mBinding.msgSuccessOrFailure.setOnSwitchClick { _, b ->
            val field = SetUpField()
            field.msgSuccessOrFailure = b
            SetUpFieldUtil.saveField(field)
        }

        mBinding.localMsg.setOnSwitchClick { _, b ->
            val field = SetUpField()
            field.localMsg = b
            SetUpFieldUtil.saveField(field)
        }
        mBinding.deleteMessage.setOnSwitchClick { _, b ->
            val field = SetUpField()
            field.deleteMessage = b
            SetUpFieldUtil.saveField(field)
        }

        mBinding.revokeMessage.setOnSwitchClick { _, b ->
            val field = SetUpField()
            field.revokeMessage = b
            SetUpFieldUtil.saveField(field)
        }
        mBinding.revokeMessageLocal.setOnSwitchClick { _, b ->
            val field = SetUpField()
            field.revokeMessageLocal = b
            SetUpFieldUtil.saveField(field)
        }

    }


}