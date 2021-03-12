package com.example.im.home.fragment

import Const
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.view.panel.PanelView
import com.example.base.base.BaseFragment
import com.example.base.utli.DensityUtil
import com.example.base.utli.ToastUtil.toast
import com.example.im.R
import com.example.im.SetUpActivity
import com.example.im.databinding.FragmentChatBinding
import com.example.im.entity.EmotionEntity
import com.example.im.entity.PopMenuAction
import com.example.im.home.chat.ChatManagerKit
import com.example.im.home.chat.adapter.MessageListAdapter
import com.example.im.home.chat.entity.PoMessageEntity
import com.example.im.home.chat.entity.QueryEntry
import com.example.im.home.chat.util.MessageUtil
import com.example.im.home.model.MessageViewModel
import com.example.im.util.BackgroundTasks
import com.example.im.util.EmotionManager
import com.example.im.util.FileUtil
import com.example.im.util.SetUpFieldUtil
import com.example.im.view.PanelAddViewPager
import com.example.im.view.PopupList
import kotlinx.android.synthetic.main.fragment_chat.*
import java.io.Serializable
import java.util.*

/**
 * @author 李雄厚
 *
 * @features ***
 */
class ChatFragment : BaseFragment<FragmentChatBinding, MessageViewModel>() {
    private var info: QueryEntry? = null

    private var mHelper: PanelSwitchHelper? = null
    private var unfilledHeight = 0
    private var isAnimation = false
    private var isShowTips = 0
    private var mInputContent = ""

    private var mAdapter: MessageListAdapter? = null
    private var page = 1

    private var mPopActions: MutableList<PopMenuAction> = ArrayList()
    private var mFaceList: MutableList<EmotionEntity> = ArrayList()


    companion object {
        //我的的头像
        const val MY_URL =
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=840492371,1920645159&fm=26&gp=0.jpg"

        fun instance(info: Serializable) = ChatFragment().apply {
            arguments = Bundle().apply {
                putSerializable(Const.INFO, info)
            }
        }
    }

    override fun initView() {
        mBinding.fragment = this
        arguments?.let {
            info = it.getSerializable(Const.INFO) as QueryEntry?
        }
        setMyTitle(info?.title ?: "")
        getIvTitleRight().setImageResource(R.drawable.three_dots)
        getIvTitleRight().setOnClickListener {
            startActivity(Intent(requireContext(), SetUpActivity::class.java))
        }
        if (mBinding.recyclerView.adapter == null) {
            mAdapter = MessageListAdapter()
            mBinding.recyclerView.adapter = mAdapter
            mBinding.recyclerView.setOnEmptySpaceClickListener {
                mHelper?.resetState()
            }
            mBinding.recyclerView.setOnLoadMoreHandler {
                BackgroundTasks.getInstance().postDelayed(Runnable {
                    page++
                    mModel.getMessage(page.toString(), info?.formUser ?: "", false)
                }, SetUpFieldUtil.getField().pullRefreshTime ?: 0L)
            }
            mAdapter?.onMessageLongClick { view, position, messageInfo ->
                showItemPopMenu(position, messageInfo!!, view)
            }
        }
        mBinding.editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mInputContent = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.toString().isNotEmpty()) {
                    if (!isAnimation) {
                        showSend()
                    }
                    if (s.toString().startsWith("对方")) {
                        editTextTips.visibility = View.VISIBLE
                        BackgroundTasks.getInstance().postDelayed(Runnable {
                            mBinding.editText.setText("")
                        }, 350)
                    }
                    if (!TextUtils.equals(mInputContent, mBinding.editText.text.toString())) {
                        EmotionManager.handlerEmotionText(
                            mBinding.editText,
                            mBinding.editText.text.toString(),
                            true
                        )
                    }
                } else {
                    if (isAnimation) {
                        showMore()
                    }
                }
            }
        })
        mBinding.editText.setOnKeyListener { _, keyCode, event ->
            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (editTextTips.visibility == View.VISIBLE && TextUtils.isEmpty(
                        mBinding.editText.text.toString().trim()
                    )
                ) {
                    isShowTips++
                }
                if (editTextTips.visibility == View.VISIBLE && isShowTips == 1) {
                    editTextTips.visibility = View.GONE
                    isShowTips = 0
                }
            }
            false
        }
    }

    override fun initData() {
        mFaceList.addAll(EmotionManager.getEmotionList())
        ChatManagerKit.setProvider()
        BackgroundTasks.getInstance().postDelayed(Runnable {
            mModel.getMessage(page.toString(), info!!.formUser, true)
        }, SetUpFieldUtil.getField().firstChatRefreshTime ?: 0L)


        mModel.loadMessage.observe(viewLifecycleOwner, Observer {
            mAdapter?.setDataSource(it)
        })

        mModel.refreshMessage.observe(viewLifecycleOwner, Observer {
            mBinding.recyclerView.finishRefresh(it.pageNo < it.totalPage)
        })
    }

    private fun showItemPopMenu(index: Int, messageInfo: PoMessageEntity, view: View?) {
        initPopActions(messageInfo)
        if (mPopActions.isEmpty()) {
            return
        }
        val popupList = PopupList(requireContext())
        val mItemList: MutableList<String> = ArrayList()
        for (action in mPopActions) {
            mItemList.add(action.actionName)
        }
        popupList.show(view, mItemList, object : PopupList.PopupListListener {
            override fun onPopupListClick(contextView: View?, contextPosition: Int, position: Int) {
                val action = mPopActions[position]
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener()?.invoke(index, messageInfo)
                }
            }

            override fun showPopupList(
                adapterView: View?,
                contextView: View?,
                contextPosition: Int
            ): Boolean {
                return true
            }

        })
    }

    private fun initPopActions(messageInfo: PoMessageEntity) {
        val actions: MutableList<PopMenuAction> = ArrayList()
        var action = PopMenuAction()
        if (messageInfo.msgType == PoMessageEntity.MSG_TYPE_TEXT) {
            action.actionName = "复制"
            action.setActionClickListener { position, data ->

            }
            actions.add(action)
        }
        action = PopMenuAction()
        action.actionName = "删除"
        action.setActionClickListener { position, data ->
            ChatManagerKit.deleteMessage(position, data as PoMessageEntity)
        }
        actions.add(action)
        if (messageInfo.self) {
            action = PopMenuAction()
            action.actionName = "撤回"
            action.setActionClickListener { position, data ->
                ChatManagerKit.revokeMessage(position, data as PoMessageEntity)
            }
            actions.add(action)
            if (messageInfo.status == PoMessageEntity.MSG_STATUS_SEND_FAIL) {
                action = PopMenuAction()
                action.actionName = "重发"
                action.setActionClickListener { _, _ ->
                    ChatManagerKit.saveMessage(messageInfo, true, info!!.saveLocal)
                }
                actions.add(action)

            }
        }
        mPopActions.clear()
        mPopActions.addAll(actions)
    }

    fun btnVoice() {
        if (isAnimation) {
            showMore()
        }
        if (mBinding.recordingText.visibility == View.GONE) {
            mBinding.recordingText.visibility = View.VISIBLE
            mBinding.editTextLay.visibility = View.GONE
            mBinding.voiceBtn.isSelected = true
            mHelper?.resetState()
        } else {
            mBinding.recordingText.visibility = View.GONE
            mBinding.editTextLay.visibility = View.VISIBLE
            mBinding.voiceBtn.isSelected = false
            mHelper?.toKeyboardState(true)
        }

    }

    fun send() {
        val content = mBinding.editText.text.toString().trim()
        if (TextUtils.isEmpty(content)) {
            "聊天消息不能为空".toast()
            return
        }
        ChatManagerKit.saveMessage(
            MessageUtil.sendMessage(
                info!!.formUser,
                content,
                editTextTips.visibility == View.GONE,
                info!!
            ),
            false,
            info!!.saveLocal
        )
        mBinding.editText.setText("")
    }

    override fun onStart() {
        super.onStart()
        if (mHelper == null) {
            mHelper = PanelSwitchHelper.Builder(this)
                .addEditTextFocusChangeListener {
                    onFocusChange { _, hasFocus ->
                        //可选实现，监听输入框焦点变化
                        if (hasFocus) {
                            recyclerView.scrollToEnd()
                        }
                    }
                }
                .addViewClickListener {
                    onClickBefore {
                        //可选实现，监听触发器的点击
                        when (it?.id) {
                            R.id.editText, R.id.emotionBtn, R.id.addBtn -> {
                                recordingText.visibility = View.GONE
                                editTextLay.visibility = View.VISIBLE
                                voiceBtn.isSelected = false
                                recyclerView.scrollToEnd()
                            }
                        }
                    }
                }
                .addPanelChangeListener {
                    onKeyboard {
                        //可选实现，输入法显示回调 "唤起系统输入法"
                        emotionBtn.isSelected = false
                        recyclerView.scrollToEnd()
                    }
                    onNone {
                        //可选实现，默认状态回调 "隐藏所有面板"
                        emotionBtn.isSelected = false
                    }
                    onPanel {
                        //可选实现，面板显示回调 "唤起面板"
                        if (it is PanelView) {
                            recyclerView.scrollToEnd()
                            emotionBtn.isSelected = it.id == R.id.panel_emotion
                        }
                    }
                    onPanelSizeChange { panelView, _, _, _, _, _ ->
                        //可选实现，输入法动态调整时引起的面板高度变化动态回调
                        if (panelView is PanelView) {
                            when (panelView.id) {
                                R.id.panel_addition -> {
                                    val pagerView =
                                        panelView.findViewById<PanelAddViewPager>(R.id.panelAddViewPager)
                                    pagerView.initViewPager(
                                        this@ChatFragment,
                                        requireActivity(),
                                        panelView.findViewById(R.id.panelAddIndicator)
                                        , 2, 4
                                    )
                                }
                                R.id.panel_emotion -> {
                                    val rv = panelView.findViewById<RecyclerView>(R.id.faceRv)
                                    val deleteFace =
                                        panelView.findViewById<ImageView>(R.id.deleteFace)
                                    EmotionManager.setEmotion(
                                        requireContext(),
                                        rv,
                                        deleteFace,
                                        editText,
                                        mFaceList
                                    )
                                }
                            }
                        }
                    }
                }
                .addContentScrollMeasurer { //可选，滑动模式下， ，定制滑动距离，默认滑动距离为 defaultDistance
                    getScrollDistance { defaultDistance ->
                        defaultDistance - unfilledHeight
                    }
                    getScrollViewId { R.id.recyclerView }
                }
                .build(false) //默认是否打开软键盘
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        val childCount = recyclerView.childCount
                        if (childCount > 0) {
                            val lastChildView: View = recyclerView.getChildAt(childCount - 1)
                            val bottom: Int = lastChildView.bottom
                            val listHeight: Int =
                                recyclerView.height - recyclerView.paddingBottom
                            unfilledHeight = listHeight - bottom
                        }
                    }
                }
            })
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PanelAddViewPager.REQUEST_CODE_PHOTO -> {
                    val videoPath: String = FileUtil.getPathFromUri(data?.data)
                    ChatManagerKit.saveMessage(
                        MessageUtil.buildImageMessage(
                            data!!.data,
                            info!!.formUser,
                            videoPath,
                            editTextTips.visibility == View.GONE,
                            info!!
                        ),
                        false,
                        info!!.saveLocal
                    )
                    Log.e("测试", videoPath)

                }
            }
        }
    }
    private fun showMore() {
        isAnimation = false
        mBinding.addBtn.visibility = View.VISIBLE
        val animator = ValueAnimator.ofInt(
            DensityUtil.dp2px(60F),
            DensityUtil.dp2px(30F)
        )
        animator.addUpdateListener {
            val ps = mBinding.send.layoutParams
            ps.width = (it.animatedValue as Int)
            mBinding.send.layoutParams = ps
        }
        val animator1 = ValueAnimator.ofFloat(
            1F,
            0F
        )
        animator1.addUpdateListener {
            mBinding.send.alpha = (it.animatedValue as Float)
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = 250
        animatorSet.playTogether(animator, animator1)
        animatorSet.start()
        animatorSet.addListener(onEnd = {
            mBinding.send.visibility = View.GONE
        })
    }

    private fun showSend() {
        isAnimation = true
        mBinding.send.visibility = View.VISIBLE
        val animator = ValueAnimator.ofInt(
            DensityUtil.dp2px(30F),
            DensityUtil.dp2px(60F)
        )
        animator.addUpdateListener {
            val ps = mBinding.send.layoutParams
            ps.width = (it.animatedValue as Int)
            mBinding.send.layoutParams = ps
        }
        val animator1 = ValueAnimator.ofFloat(
            0F,
            1F
        )
        animator1.addUpdateListener {
            mBinding.send.alpha = (it.animatedValue as Float)
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = 250
        animatorSet.playTogether(animator, animator1)
        animatorSet.start()
        animatorSet.addListener(onEnd = {
            mBinding.addBtn.visibility = View.GONE
        })
    }
}