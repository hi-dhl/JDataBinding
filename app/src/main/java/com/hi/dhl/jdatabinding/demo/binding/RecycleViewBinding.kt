package com.hi.dhl.jdatabinding.demo.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hi.dhl.jdatabinding.demo.ui.Model
import com.hi.dhl.jdatabinding.demo.ui.adapter.TestAdapter

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/3/23
 *     desc  :
 * </pre>
 */

@BindingAdapter("adapter")
fun bindAdapter(recyclerView: RecyclerView, adapter: TestAdapter) {
    recyclerView.adapter = adapter
}

@BindingAdapter("adapterList")
fun bindAdapterList(recyclerView: RecyclerView, data: List<Model>?) {
    val adapter = recyclerView.adapter as? TestAdapter
        ?: throw RuntimeException(" adapter is null")
    data?.let { adapter.submitList(it, SubmitListCallback(recyclerView)) }
    recyclerView.requestFocus()
    recyclerView.scrollToPosition(0)
}

class SubmitListCallback(val recyclerView: RecyclerView) : Runnable {
    override fun run() {
        recyclerView.scrollToPosition(0)
        recyclerView.requestFocus()
    }
}