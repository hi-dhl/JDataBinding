package com.hi.dhl.jdatabinding.demo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/19
 *     desc  :
 * </pre>
 */
class MainViewModel : ViewModel() {
    public val mLiveData: MutableLiveData<MutableList<Model>> by lazy { MutableLiveData<MutableList<Model>>() }
    private val mData: MutableList<Model> by lazy { mutableListOf<Model>() }

    init {
        (1..30).forEach { index ->
            val model = Model(
                index,
                "title" + index,
                System.currentTimeMillis()
            );
            mData.add(model)
        }

        mLiveData.value = mData
    }
}

data class Model(val id: Int, val title: String, val time: Long) {
    companion object {
        val CALLBACK: DiffUtil.ItemCallback<Model> = object : DiffUtil.ItemCallback<Model>() {
            override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean = true
        }
    }
}