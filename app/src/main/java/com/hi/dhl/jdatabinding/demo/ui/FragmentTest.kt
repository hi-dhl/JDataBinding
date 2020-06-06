package com.hi.dhl.jdatabinding.demo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hi.dhl.jdatabinding.DataBindingFragment
import com.hi.dhl.jdatabinding.demo.R
import com.hi.dhl.jdatabinding.demo.databinding.FragmentTestBinding
import com.hi.dhl.jdatabinding.demo.ui.adapter.TestAdapter

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */

class FragmentTest(val mainViewModel: MainViewModel) : DataBindingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding<FragmentTestBinding>(
            inflater,
            R.layout.fragment_test, container
        ).apply {
            viewModel = mainViewModel
            testAdapter = TestAdapter()
            lifecycleOwner = this@FragmentTest
        }.root
    }

    companion object {
        const val KEY_NAME = "FragmentTest"
    }
}