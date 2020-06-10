package com.hi.dhl.jdatabinding.demo.ui.adapter

import android.view.View
import com.hi.dhl.jdatabinding.DataBindingViewHolder
import com.hi.dhl.jdatabinding.demo.databinding.RecycieItemTestBinding
import com.hi.dhl.jdatabinding.demo.ui.Model

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/19
 *     desc  :
 * </pre>
 */
class TestViewHolder(view: View) : DataBindingViewHolder<Model>(view) {

    val binding: RecycieItemTestBinding by viewHolderBinding(view)

    override fun bindData(data: Model, position: Int) {
        binding.apply {
            model = data
            executePendingBindings()
        }
    }
}