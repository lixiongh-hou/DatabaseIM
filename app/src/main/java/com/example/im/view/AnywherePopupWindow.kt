package com.example.im.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.XmlResourceParser
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Xml
import android.view.Gravity
import android.view.InflateException
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.base.utli.DensityUtil
import com.example.im.R
import com.example.im.entity.MenuItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

/** Menu tag name in XML. */
private const val XML_MENU = "menu"

/** Group tag name in XML. */
private const val XML_GROUP = "group"

/** Item tag name in XML. */
private const val XML_ITEM = "item"

private const val ANCHORED_GRAVITY = Gravity.TOP or Gravity.START

/** 默认菜单宽度 */
private var DEFAULT_MENU_WIDTH = DensityUtil.dp2px(180F)
private var VERTICAL_OFFSET = DensityUtil.dp2px(10F)

/**
 * @author 李雄厚
 *
 * @features ***
 */
class AnywherePopupWindow constructor(var context: Context, var view: View) : PopupWindow(context) {

    constructor(activity: Activity) : this(activity, activity.findViewById(android.R.id.content))

    init {
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.setOnTouchListener(MenuTouchListener())
    }

    private var menuItemList: MutableList<MenuItem> = ArrayList()
    private var screenPoint: Point = getScreenMetrics(context)
    private var clickX = 0
    private var clickY = 0
    private var menuWidth = 0
    private var menuHeight = 0
    private lateinit var menuLayout: LinearLayout
    var onPopupItemClickListener: ((View?, Int) -> Unit)? = null

    fun inflate(menuRes: Int) {
        inflate(menuRes, DEFAULT_MENU_WIDTH)
    }

    fun inflate(menuRes: Int, itemWidth: Int) {
        var parser: XmlResourceParser? = null
        try {
            parser = context.resources.getLayout(menuRes)
            val attrs = Xml.asAttributeSet(parser)
            parseMenu(parser, attrs)
        } catch (e: XmlPullParserException) {
            throw InflateException("Error inflating menu XML", e)
        } catch (e: IOException) {
            throw InflateException("Error inflating menu XML", e)
        } finally {
            parser?.close()
        }
        generateLayout(DensityUtil.dp2px(itemWidth.toFloat()))
    }

    fun items(vararg items: String) {
        items(DEFAULT_MENU_WIDTH, *items)
    }

    fun items(itemWidth: Int, vararg items: String) {
        menuItemList.clear()
        for (i in items.indices) {
            val menuModel = MenuItem(items[i])
            menuItemList.add(menuModel)
        }
        generateLayout(DensityUtil.dp2px(itemWidth.toFloat()))
    }

    fun items(itemList: MutableList<MenuItem>) {
        menuItemList.clear()
        menuItemList.addAll(itemList)
        generateLayout(DEFAULT_MENU_WIDTH)
    }

    fun items(itemList: MutableList<MenuItem>, itemWidth: Int) {
        menuItemList.clear()
        menuItemList.addAll(itemList)
        generateLayout(DensityUtil.dp2px(itemWidth.toFloat()))
    }

    private fun generateLayout(itemWidth: Int) {
        menuLayout = LinearLayout(context)
        menuLayout.background = ContextCompat.getDrawable(context, R.drawable.bg_shadow)
        menuLayout.orientation = LinearLayout.VERTICAL
        val padding = DensityUtil.dp2px(12F)
        for (i in menuItemList.indices) {
            val textView = TextView(context)
            textView.isClickable = true
            textView.background = ContextCompat.getDrawable(context, R.drawable.selector_item)
            textView.setPadding(padding, padding, padding, padding)
            textView.width = itemWidth
            textView.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            textView.textSize = 15F
            textView.setTextColor(Color.BLACK)
            val menuModel = menuItemList[i]
            if (menuModel.itemResId != View.NO_ID) {
                //如果在左边添加了图标
                val drawable = ContextCompat.getDrawable(context, menuModel.itemResId)
                drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                textView.compoundDrawablePadding = DensityUtil.dp2px(12F)
                textView.setCompoundDrawables(drawable, null, null, null)
            }
            textView.text = menuModel.item
            if (onPopupItemClickListener != null) {
                textView.setOnClickListener(ItemOnClickListener(i))
            }
            menuLayout.addView(textView)
        }
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        menuLayout.measure(width, height)
        menuWidth = menuLayout.measuredWidth
        menuHeight = menuLayout.measuredHeight
        contentView = menuLayout
        this.width = menuWidth
        this.height = menuHeight
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseMenu(parser: XmlPullParser, attrs: AttributeSet) {
        var eventType = parser.eventType
        var tagName: String
        var lookingForEndOfUnknownTag = false
        var unknownTagName: String? = null

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.name
                if (tagName == XML_MENU) {
                    // Go to next tag
                    eventType = parser.next()
                    break
                }
                throw RuntimeException("Expecting menu, got $tagName")
            }
            eventType = parser.next()
        } while (eventType != XmlPullParser.END_DOCUMENT)

        var reachedEndOfMenu = false

        while (!reachedEndOfMenu) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (lookingForEndOfUnknownTag) {
                        return
                    }
                    tagName = parser.name
                    if (tagName == XML_GROUP) {
                        //	parser group
                        parser.next()
                    } else if (tagName == XML_ITEM) {
                        readItem(attrs)
                    } else if (tagName == XML_MENU) {
                        // A menu start tag denotes a submenu for an item
                        //pares subMenu
                        parser.next()
                    } else {
                        lookingForEndOfUnknownTag = true
                        unknownTagName = tagName
                    }
                }
                XmlPullParser.END_TAG -> {
                    tagName = parser.name
                    if (lookingForEndOfUnknownTag && tagName == unknownTagName) {
                        lookingForEndOfUnknownTag = false
                        unknownTagName = null
                    } else if (tagName == XML_MENU) {
                        reachedEndOfMenu = true
                    }
                }
                XmlPullParser.END_DOCUMENT -> throw java.lang.RuntimeException("Unexpected end of document")
            }
            eventType = parser.next()
        }
    }

    private fun readItem(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MenuItem)
        val itemTitle = a.getText(R.styleable.MenuItem_menu_title)
        val itemIconResId = a.getResourceId(R.styleable.MenuItem_icon, View.NO_ID)
        val menu = MenuItem(itemTitle.toString(), if (itemIconResId != View.NO_ID) itemIconResId else View.NO_ID)
        menuItemList.add(menu)
        a.recycle()
    }

    fun show(point: Point) {
        clickX = point.x
        clickY = point.y
        show()
    }

    fun show() {
        if (isShowing) {
            return
        }

        //it is must ,other wise 'setOutsideTouchable' will not work under Android5.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        if (clickX <= screenPoint.x / 2) {
            if (clickY + menuHeight < screenPoint.y) {
                animationStyle = R.style.Animation_top_left
                showAtLocation(view, ANCHORED_GRAVITY, clickX, clickY + VERTICAL_OFFSET)
            } else {
                animationStyle = R.style.Animation_bottom_left
                showAtLocation(view, ANCHORED_GRAVITY, clickX, clickY - menuHeight - VERTICAL_OFFSET)
            }
        } else {
            if (clickY + menuHeight < screenPoint.y) {
                animationStyle = R.style.Animation_top_right
                showAtLocation(view, ANCHORED_GRAVITY, clickX - menuWidth, clickY + VERTICAL_OFFSET)
            } else {
                animationStyle = R.style.Animation_bottom_right
                showAtLocation(view, ANCHORED_GRAVITY, clickX - menuWidth, clickY - menuHeight - VERTICAL_OFFSET)
            }
        }
    }

    fun setOnItemClickListener(onPopupItemClickListener: ((View?, Int) -> Unit)?) {
        this.onPopupItemClickListener = onPopupItemClickListener
        if (onPopupItemClickListener != null) {
            for (i in 0 until menuLayout.childCount) {
                val view = menuLayout.getChildAt(i)
                view.setOnClickListener(ItemOnClickListener(i))
            }
        }
    }


    inner class MenuTouchListener : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                clickX = event.rawX.toInt()
                clickY = event.rawY.toInt()
            }
            return false
        }

    }

    inner class ItemOnClickListener(var position: Int = 0) : View.OnClickListener {
        override fun onClick(v: View?) {
            dismiss()
            if (onPopupItemClickListener != null) {
                onPopupItemClickListener?.invoke(v, position)
            }
        }

    }

    private fun getScreenMetrics(context: Context): Point {
        val dm = context.resources.displayMetrics
        val wScreen = dm.widthPixels
        val hScreen = dm.heightPixels
        return Point(wScreen, hScreen)
    }

}