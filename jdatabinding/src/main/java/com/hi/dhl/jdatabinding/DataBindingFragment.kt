package com.hi.dhl.jdatabinding

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
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
abstract class DataBindingFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    inline fun <reified T : ViewBinding> DataBindingFragment.binding() =
        FragmentDataBindingDelegate(T::class.java, this)

    class FragmentDataBindingDelegate<T : ViewBinding>(
        classes: Class<T>,
        fragment: Fragment
    ) : ReadOnlyProperty<Fragment, T> {

        init {
            fragment.lifecycle.addObserver(LifeCalcyObserver())
        }

        private val bindView = classes.getMethod("bind", View::class.java)
        private var viewBinding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {

            viewBinding?.also {
                return it
            }

            val bind = bindView.invoke(null, thisRef.view) as T
            return bind.also { viewBinding = it }
        }

        inner class LifeCalcyObserver : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                val state = source.lifecycle.currentState
                if (state == Lifecycle.State.DESTROYED) {
                    viewBinding = null
                }
            }

        }
    }

}
