package com.example.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.base.R
import com.example.base.annotation.BindEventBus
import com.example.base.bridge.EventObserver
import com.example.base.utli.EventBusUtil
import com.example.base.utli.ToastUtil.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zy.multistatepage.MultiStateContainer
import com.zy.multistatepage.OnRetryEventListener
import com.zy.multistatepage.bindMultiState
import com.zy.multistatepage.state.EmptyState
import com.zy.multistatepage.state.ErrorState
import com.zy.multistatepage.state.LoadingState
import com.zy.multistatepage.state.SuccessState
import kotlinx.android.synthetic.main.title_view.*
import java.lang.reflect.ParameterizedType

/**
 * @author 李雄厚
 *
 * @features Fragment基类
 */
abstract class BaseFragment<Binding : ViewDataBinding, VM : BaseViewModel> : Fragment() {
    lateinit var mBinding: Binding
    lateinit var mModel: VM
    var activity: AppCompatActivity? = null
    var isFirst = true
    var isSuccess = false
    var multiStateContainer: MultiStateContainer? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
        holderBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBindingWithModel(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mModel.toastLiveData.observe(viewLifecycleOwner, EventObserver<String> {
            toast(it)
        })
        //错误信息回调
        mModel.errorLiveData.observe(viewLifecycleOwner, EventObserver<String> {
            toast(it)
            failureAfter()
            multiStateContainer?.run { show<ErrorState>() }

        })
        // EventBus
        if (this.javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBusUtil.register(this)
        }
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (isFirst) {
            initData()
            isFirst = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // EventBus
        if (this.javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBusUtil.unregister(this)
        }
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
            } else if (clazz.superclass == BaseViewModel::class.java || clazz == BaseViewModel::class.java) {
                val asSubclass = clazz.asSubclass(BaseViewModel::class.java)
                @Suppress("UNCHECKED_CAST")
                mModel = ViewModelProvider(this).get(asSubclass) as VM
            }
        }
        if (!::mBinding.isInitialized) {
            throw IllegalStateException("Binding 必须是ViewDataBinding的子类")
        }
        if (!::mModel.isInitialized) {
            throw IllegalStateException("Model 必须是BaseViewModel的子类")
        }
    }

    abstract fun initView()
    abstract fun initData()

    fun toast(message: String?) {
        message ?: return
        if (message.isNullOrEmpty()) {
            return
        }
        message.toast()
    }

    private var callback: OnBackPressedCallback? = null

    private fun callback(): OnBackPressedCallback {
        if (callback == null) {
            callback = object : OnBackPressedCallback(true) {
                @Override
                override fun handleOnBackPressed() {
                    onBackClickListener()
                }
            }
        }
        return callback!!
    }

    open fun holderBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, callback())
    }

    protected fun findNavController(): NavController? {
        return Navigation.findNavController(mBinding.root)
    }

    open fun onBackClickListener() {
        findNavController()?.navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback = null
    }

    /*--------------------------------标题布局-------------------------------------------*/
    /**
     * 设置标题默认格式
     */
    fun setMyTitle(title: CharSequence) {
        if (tvTitle == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        } else {
            tvTitle.text = title
            ivTitleBack.setOnClickListener {
                onBackClickListener()
            }
        }
    }

    fun getLiBar(): LinearLayout {
        if (liBar == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        return liBar
    }

    /**
     * 获取标题右边按钮图标
     */
    fun getIvTitleRight(): ImageView {
        if (ivTitleRight == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        ivTitleRight.visibility = View.VISIBLE
        return ivTitleRight
    }

    /**
     * 获取标题左边按钮图标
     */
    fun getIvTitleLift(): ImageView {
        if (ivTitleLeft == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        ivTitleLeft.visibility = View.VISIBLE
        return ivTitleLeft
    }

    /**
     * 获取标题左边文字
     */
    fun getTvTitleText(): TextView {
        if (tvTitleText == null) {
            throw NullPointerException("请在对应的布局中加入”<include layout=\"@layout/title_view\"/>标题布局")
        }
        tvTitleText.visibility = View.VISIBLE
        return tvTitleText
    }

    /**
     * 获取Tv或Ev文本
     */
    fun getTvEvString(view: View): String {
        if (view is TextView) {
            return view.text.toString().trim()
        }
        if (view is EditText) {
            return view.text.toString().trim()
        }
        return ""
    }

    /*--------------------------------刷新控件-------------------------------------------*/
    /**刷新,加载 */
    companion object {
        private const val REFRESH = 1
        private const val LOADING = 2
    }

    /**一页的数据数 */
    var pageSize = 20

    /**页数 */
    private var page = 0

    /**数据长度 */
    private var dataLength = 0

    /**记录当前操作,刷新还是加载 */
    private var what: Int = REFRESH

    /**刷新布局*/
    private var mRefreshLayout: SmartRefreshLayout? = null

    open fun getPage(): String {
        return page.toString()
    }

    open fun isRefresh(): Boolean {
        return what == REFRESH
    }

    /**
     * 开始刷新
     */
    open fun startRefresh() {
        what = REFRESH
        page = 0
        this.refresh()
    }

    /**
     * 开始加载
     */
    open fun startLoadMore() {
        what = LOADING
        page++
        this.loadMore()
    }

    open fun refresh() {}

    open fun loadMore() {}

    fun initRefresh() {
        mRefreshLayout = mBinding.root.findViewById(R.id.mPullRefreshLayout)
        mRefreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                // 开始加载
                startLoadMore()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                // 开始刷新
                startRefresh()
            }
        })
    }

    /**
     * 使用这个方法一定要在你需要在外层包含一个在“mPullRefreshView”id布局
     */
    fun initRefreshView() {
        val view = mBinding.root.findViewById<View>(R.id.mPullRefreshView)
        //或者
        multiStateContainer = view.bindMultiState(OnRetryEventListener {
                isSuccess = false
                multiStateContainer?.run { show<LoadingState>() }
                this.initData()
        })
        multiStateContainer?.run { show<LoadingState>() }
    }

    /**
     * 数据请求成功后执行
     *
     * @param newLength 响应后数据源的长度
     */
    open fun successAfter(newLength: Int) {
        successAfter(newLength, true)
    }

    open fun successAfter(newLength: Int, isFullscreen: Boolean) {
        if (null != mRefreshLayout) {
            if (what == LOADING) {
                if (newLength - dataLength < pageSize || newLength < pageSize) {
                    // 加载成功吗没有有更多数据
                    mRefreshLayout?.finishLoadMore(0, true, true)
                } else {
                    // 加载成功有更多数据
                    mRefreshLayout?.finishLoadMore(0, true, false)
                }
            } else {
                // 刷新成功、本来老版本后面参数有个0
                mRefreshLayout?.finishRefresh(true)
                if (newLength < pageSize) {
                    //刷新成功没有更多数据
                    mRefreshLayout?.setNoMoreData(true)
                } else {
                    //刷新成功还有更多数据
                    mRefreshLayout?.setNoMoreData(false)
                }
            }
        }
        dataLength = newLength
        if (isFullscreen) {
            setEmpty()
        }

    }

    /**
     * 数据请求失败后执行
     *
     */
    open fun failureAfter() {

        if (null != mRefreshLayout) {
            if (what == LOADING) {
                // 加载失败
                mRefreshLayout?.finishLoadMore(0, false, false)
                page--
            } else {
                // 刷新失败、少个0
                mRefreshLayout?.finishRefresh(false)
            }
        }
    }

    /**
     * 判断数据源长度,决定是否显示布局
     */
    private fun setEmpty() {
        if (!isSuccess) {
            if (dataLength > 0) {
                multiStateContainer?.run { show<SuccessState>() }
            } else if (dataLength == 0) {
                multiStateContainer?.run { show<EmptyState>() }
            }
            isSuccess = true
        }
    }

    /**
     * 启用刷新
     */
    protected open fun setEnableRefresh(enable: Boolean) {
        if (null != mRefreshLayout) {
            mRefreshLayout!!.setEnableRefresh(enable)
            mRefreshLayout!!.setEnableOverScrollDrag(true)
        }
    }

    /**
     * 启用加载
     */
    protected open fun setEnableLoadMore(enable: Boolean) {
        if (null != mRefreshLayout) {
            mRefreshLayout!!.setEnableLoadMore(enable)
            mRefreshLayout!!.setEnableOverScrollDrag(true)
        }
    }
}