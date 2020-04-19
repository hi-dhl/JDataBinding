package com.hi.dhl.jdatabinding

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/3/17
 *     desc  :
 * </pre>
 */
abstract class BaseViewHolder<T>(val view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener,
    View.OnLongClickListener {

    @Throws(Exception::class)
    abstract fun bindData(data: T)

    fun view() = view

    fun context(): Context {
        return view.context
    }

    inline fun <reified T : ViewDataBinding> viewHolderBinding(view: View): Lazy<T> =
        lazy {
            requireNotNull(DataBindingUtil.bind<T>(view)) { "cannot find the matched layout." }
        }

}