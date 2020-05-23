package com.hi.dhl.jdatabinding.demo.ui.adapter

import android.view.View
import com.hi.dhl.jdatabinding.DataBindingListAdapter
import com.hi.dhl.jdatabinding.DataBindingViewHolder
import com.hi.dhl.jdatabinding.demo.R
import com.hi.dhl.jdatabinding.demo.ui.Model

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/19
 *     desc  :
 * </pre>
 */
class TestAdapter : DataBindingListAdapter<Model>(
    Model.CALLBACK
) {

    override fun viewHolder(layout: Int, view: View): DataBindingViewHolder<Model> = when (layout) {
        R.layout.recycie_item_header -> HeaderViewHolder(
            view
        )
        else -> MoreViewHolder(view)
    }

    override fun layout(position: Int): Int = when (position) {
        0 -> R.layout.recycie_item_header
        getItemCount() - 1 -> R.layout.recycie_item_footer
        else -> R.layout.recycie_item_test
    }

    override fun getItemCount(): Int = super.getItemCount() + 2
}