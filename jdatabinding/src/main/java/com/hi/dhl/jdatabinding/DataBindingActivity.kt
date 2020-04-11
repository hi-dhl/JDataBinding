package com.hi.dhl.jdatabinding

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 * </pre>
 */
abstract class DataBindingActivity : FragmentActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }

}