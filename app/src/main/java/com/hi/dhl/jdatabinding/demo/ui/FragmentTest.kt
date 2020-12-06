package com.hi.dhl.jdatabinding.demo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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

class FragmentTest(val mainViewModel: MainViewModel) : DataBindingFragment(R.layout.fragment_test) {

    val bind: FragmentTestBinding by binding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            viewModel = mainViewModel
            testAdapter = TestAdapter()
            lifecycleOwner = this@FragmentTest
        }
    }

    companion object {
        const val KEY_NAME = "FragmentTest"
    }
}