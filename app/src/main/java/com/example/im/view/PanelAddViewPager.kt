package com.example.im.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.base.base.adapter.BaseAdapter
import com.example.im.R
import com.example.im.databinding.ItemPanelAddBinding
import com.example.im.entity.PopMenuAction
import com.example.im.home.activity.CameraActivity


/**
 * @author 李雄厚
 *
 * @features ***
 */
class PanelAddViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private val mActions: MutableList<PopMenuAction> = ArrayList()
    private var mActivity: Activity? = null
    private var mFragment: Fragment? = null

    fun initViewPager(
        fragment: Fragment,
        activity: Activity,
        indicatorView: CircleIndicatorView,
        rowNum: Int,
        spanNum: Int
    ) {
        mActivity = activity
        mFragment = fragment
        if (mActions.isEmpty()) {
            return
        }
        //1.根据数据的多少来分页，每页的数据为rw
        //每个单页包含的数据量：2*4=8；
        val singlePageDataNum = rowNum * spanNum
        //算出有几页菜单：20%8 = 3;
        var pageNum = mActions.size / singlePageDataNum
        if (mActions.size % singlePageDataNum > 0) {
            pageNum += 1
        }
        val mList = ArrayList<RecyclerView>()
        for (i in 0 until pageNum) {
            val recyclerView = RecyclerView(context)
            val gridLayoutManager = GridLayoutManager(context, spanNum)
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
            val fromIndex = i * singlePageDataNum
            var toIndex = (i + 1) * singlePageDataNum
            if (toIndex > mActions.size) toIndex = mActions.size
            //a.截取每个页面包含数据
            val menuItems = ArrayList(mActions.subList(fromIndex, toIndex))
            val mAdapter = object : BaseAdapter<PopMenuAction, ItemPanelAddBinding>() {
                override fun createBinding(
                    parent: ViewGroup,
                    viewType: Int
                ): ItemPanelAddBinding = ItemPanelAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                override fun bind(
                    binding: ItemPanelAddBinding,
                    data: PopMenuAction,
                    position: Int
                ) {
                    binding.itemInputMoreImage.setImageResource(data.iconResId)
                    binding.itemInputMoreText.text = data.actionName
                }

            }
            recyclerView.adapter = mAdapter
            mAdapter.refreshData(menuItems)
            mAdapter.clickEvent = { data, _, position ->
                val action = menuItems[position]
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener()?.invoke(position, data)
                }
            }
            mList.add(recyclerView)

            //2.ViewPager的适配器
            val menuViewPagerAdapter = MenuViewPagerAdapter(mList)
            adapter = menuViewPagerAdapter
            offscreenPageLimit = pageNum - 1
            //指示器
            indicatorView.setCount(pageNum)
            indicatorView.setGap(30F)
            indicatorView.setRadius(8F)
            addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    indicatorView.setSelectedIndex(position)
                }

            })
        }
    }

    inner class MenuViewPagerAdapter(mList: ArrayList<RecyclerView>) : PagerAdapter() {
        private var mList: ArrayList<RecyclerView> = ArrayList()

        init {
            this.mList = mList
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return if (mList.isEmpty()) 0 else mList.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(mList[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(mList[position])
            return mList[position]
        }
    }

    init {
        initViewPagerActions()
    }

    private fun initViewPagerActions() {

        val actions: MutableList<PopMenuAction> = java.util.ArrayList()
        var action = PopMenuAction()
        action.actionName = "相册"
        action.iconResId = R.drawable.create_c2c
        action.setActionClickListener { _, _ ->
            //动态申请权限
            if (isAccessPermissions()){
                //执行启动相册的方法
                openAlbum()
            }
        }
        actions.add(action)

        action = PopMenuAction()
        action.actionName = "拍摄"
        action.iconResId = R.drawable.create_c2c
        action.setActionClickListener { position, data ->
            //动态申请权限
            if (isAccessPermissions()) {
                //执行启动相册的方法
                openCamera()
            }
        }
        actions.add(action)

        mActions.clear()
        mActions.addAll(actions)
    }

    private fun openCamera() {
        mFragment?.startActivityForResult(Intent(mActivity, CameraActivity::class.java), REQUEST_CODE_VIDEO)
    }

    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        val mimeTypes = arrayOf("image/*", "video/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        mFragment?.startActivityForResult(intent, REQUEST_CODE_PHOTO)

    }

    companion object {
        const val REQUEST_CODE_PHOTO = 0x1012
        const val REQUEST_CODE_VIDEO = 0x1011
    }

    private fun isAccessPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(mActivity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(mActivity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(mActivity!!, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(mActivity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                1
            )
            return false
        }
        return true
    }
}
