package com.hi.dhl.jdatabinding.demo.ui.adapter

import android.view.View
import com.hi.dhl.jdatabinding.DataBindingViewHolder
import com.hi.dhl.jdatabinding.demo.databinding.RecycieItemHeaderBinding
import com.hi.dhl.jdatabinding.demo.ui.Model

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/19
 *     desc  :
 * </pre>
 */
class HeaderViewHolder(view: View) : DataBindingViewHolder<Model>(view) {

    val binding: RecycieItemHeaderBinding by viewHolderBinding(view)

    override fun bindData(data: Model, position: Int) {
    }

}