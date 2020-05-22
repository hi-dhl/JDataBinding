package com.hi.dhl.jdatabinding.demo.di

import com.hi.dhl.jdatabinding.demo.ui.FragmentTest
import com.hi.dhl.jdatabinding.demo.ui.MainViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/19
 *     desc  :
 * </pre>
 */
val viewModelsModule = module {
    viewModel { MainViewModel() }
}

val fragmentModules = module {
    fragment { FragmentTest(get()) }
}

val appModules = listOf(fragmentModules, viewModelsModule)