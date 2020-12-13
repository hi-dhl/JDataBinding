package com.hi.dhl.jdatabinding

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.hi.dhl.jdatabinding.ext.addObserver
import com.hi.dhl.jdatabinding.ext.bindMethod
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/4/9
 *     desc  :
 *
 *     jdatabinding >=1.0.4 和  jdatabinding < 1.0.4 用法不一样，
 *
 *     1.0.4 基于 FragmentDataBindingDelegate 实现的，处于 Lifecycle.State.DESTROYED 将会销毁数据，
 *     1.0.4 用法更加简单，请查看 README.md
 */

inline fun <reified T : ViewBinding> Fragment.binding() =
    FragmentBindingDelegate(T::class.java, this)

class FragmentBindingDelegate<T : ViewBinding>(
    classes: Class<T>,
    fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    var viewBinding: T? = null
    val bindView = classes.bindMethod()

    init {
        fragment.lifecycle.addObserver { destroyed() }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return viewBinding?.run {
            return this

        } ?: let {

            val bind = bindView.invoke(null, thisRef.view) as T
            return bind.apply { viewBinding = this }
        }
    }

    private fun destroyed() {
        viewBinding = null
    }
}