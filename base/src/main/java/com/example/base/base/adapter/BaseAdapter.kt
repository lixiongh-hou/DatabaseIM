package com.example.base.base.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.base.utli.NonDoubleClick.clickWithTrigger

/**
 * @author 李雄厚
 *
 * @features ***
 */
abstract class BaseAdapter<T, V : ViewDataBinding> : RecyclerView.Adapter<BaseViewHolder<V>>() {

    var data: ArrayList<T> = ArrayList()
    open var clickEvent: ((T,V,Int) -> Unit)? = null


    /**
     * 刷新数据
     */
    fun refreshData(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    /**
     * 清楚数据
     */
    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    /**
     * 添加数据
     */
    fun addAllData(list: Collection<T>) {
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<V> {
        return BaseViewHolder(createBinding(parent, viewType))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        bind(holder.binding, data[position], position)
        holder.binding.executePendingBindings()
        holder.binding.root.clickWithTrigger {
            clickEvent?.invoke(data[position], holder.binding, position)

        }
        holder.setIsRecyclable(false)
    }


    abstract fun createBinding(parent: ViewGroup, viewType: Int): V
    abstract fun bind(binding: V, data: T, position: Int)

    /**
     * 解决全局刷新时图片闪烁
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}