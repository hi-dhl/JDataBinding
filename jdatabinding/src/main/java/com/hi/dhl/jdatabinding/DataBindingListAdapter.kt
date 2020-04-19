package com.hi.dhl.jdatabinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/13
 *     desc  :
 * </pre>
 */
abstract class DataBindingListAdapter<T : Any> constructor(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseViewHolder<T>>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val view = inflateView(parent, viewType)
        return viewHolder(viewType, view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        if (position >= currentList.size) {
            return
        }
        val data = getItem(position)
        dowithTry {
            holder.bindData(data)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return layout(position)
    }

    protected abstract fun viewHolder(@LayoutRes layout: Int, view: View): BaseViewHolder<T>
    protected abstract fun layout(position: Int): Int

    private fun inflateView(viewGroup: ViewGroup, @LayoutRes viewType: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(viewType, viewGroup, false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        currentList.clear()
    }
}
