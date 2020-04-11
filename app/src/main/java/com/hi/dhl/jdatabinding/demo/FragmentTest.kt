package com.hi.dhl.jdatabinding.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hi.dhl.jdatabinding.DataBindingFragment
import com.hi.dhl.jdatabinding.demo.databinding.TestFragmentBinding

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */
class FragmentTest : DataBindingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding<TestFragmentBinding>(inflater, R.layout.test_fragment, container).apply {
            display.text = getString(R.string.app_name)
        }.root
    }
}