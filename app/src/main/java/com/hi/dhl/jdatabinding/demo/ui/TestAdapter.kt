package com.hi.dhl.jdatabinding.demo.ui

import android.view.View
import com.hi.dhl.jdatabinding.BaseViewHolder
import com.hi.dhl.jdatabinding.DataBindingListAdapter
import com.hi.dhl.jdatabinding.demo.R

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
    override fun viewHolder(layout: Int, view: View): BaseViewHolder<Model> =
        TestViewHolder(view)

    override fun layout(position: Int): Int =
        R.layout.recycie_item_test
}